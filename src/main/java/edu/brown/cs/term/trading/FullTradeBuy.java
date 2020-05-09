package edu.brown.cs.term.trading;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.TimeInForce;
import com.binance.api.client.domain.account.NewOrder;
import com.binance.api.client.domain.account.NewOrderResponse;
import com.binance.api.client.domain.account.Order;
import com.binance.api.client.domain.account.request.CancelOrderRequest;
import com.binance.api.client.domain.account.request.OrderStatusRequest;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.term.database.CrypthubUser;
import edu.brown.cs.term.database.Trades;
import edu.brown.cs.term.general.Tuple;
import websockets.RsiWebSocket;

public class FullTradeBuy {
  private String APIkey
      = "aXGg0J5kFv08YOytKZrCav9rWnjgzdFx3BvjIjY5M2gQQCaYSEdznhAGG4kspVr2";
  private String secretKey
      = "pgbthhPJBg8FS29FCeyYvlF5ay1CV0we4xxilxL9pytc5djb7RxDCs7k4kChthMI";
  private String username = "js";
  private BinanceApiClientFactory fact;
  private BinanceApiRestClient client;
  private Trades trades;
  private Set<String> symbols;
  private Gson GSON;
  private String message = "";
  private int orderId;

  public FullTradeBuy(String user) {
    GSON = new Gson();
    username = user;
    setPublicPrivateKey(username);
    trades = new Trades();
  }

  /**
   * Sets the public and private key for use in trading.
   * @param username
   *          username of the user
   * @return String representing JSON object
   */
  public String setPublicPrivateKey(String username) {
    CrypthubUser user = new CrypthubUser();
    Tuple<String, String> keys = user.getPublicPrivateKey(username);
    if (keys.equals(new Tuple<>("", ""))) {
      return GSON.toJson(ImmutableMap.of("success", false));
    }
    APIkey = keys.getFirstEntry();
    secretKey = keys.getSecondEntry();
    fact = BinanceApiClientFactory.newInstance(APIkey, secretKey);
    client = fact.newRestClient();
    return GSON.toJson(ImmutableMap.of("success", true));
  }

  public void fullTrade(String symbol, String buyPrice, String sellPrice,
      String stopPrice, String quantity) {
    NewOrderResponse buy = placeBuyOrder(symbol, buyPrice, quantity);
    NewOrderResponse sell = checkAndSell(buy, symbol, sellPrice, false);
    monitorForStop(sell, symbol, stopPrice, false);
  }

  public NewOrderResponse placeBuyOrder(String symbol, String buyPrice,
      String quantity) {
    double lastPrice = Double
        .valueOf(client.get24HrPriceStatistics(symbol).getLastPrice());
    double priceBuy = Double.valueOf(buyPrice);
    if (!username.equals("")) {
      NewOrderResponse res = client.newOrder(
          NewOrder.limitBuy(symbol, TimeInForce.GTC, quantity, buyPrice));
      trades.addTrade(username, client.getOrderStatus(
          new OrderStatusRequest(symbol, res.getOrderId())));
      return res;
    } else {
      System.out.println("ERROR: Please login before making a trade.");
      return null;
    }
  }

  public NewOrderResponse checkAndSell(NewOrderResponse buy, String symbol,
      String sellPrice, boolean buyFilled) {
    NewOrderResponse res = null;
    while (!buyFilled) {
      Order order = client.getOrderStatus(
          new OrderStatusRequest(symbol, buy.getOrderId()));
      if (order.getStatus().toString().equals("FILLED")) {
        buyFilled = true;
        double assetAmount = Double.valueOf(buy.getOrigQty());
        res = client.newOrder(NewOrder.limitSell(symbol, TimeInForce.GTC,
            buy.getOrigQty(), sellPrice));
        trades.updateTrade(username,
            client.getOrderStatus(
                new OrderStatusRequest(symbol, buy.getOrderId())),
            message);
        trades.addTrade(username, client.getOrderStatus(
            new OrderStatusRequest(symbol, res.getOrderId())));
      } else {
        message
            = "Buy not complete. Time is: " + java.time.LocalTime.now();
        System.out.println(message);
        RsiWebSocket.sendCustomTradingUpdateMessage(username,
            buy.getOrderId(), message);
        try {
          Thread.sleep(30000);
        } catch (Exception e) {
          System.out.println("Thread interrupted.");
        }
      }
    }
    return res;
  }

  public void monitorForStop(NewOrderResponse sell, String symbol,
      String stopPrice, boolean sellFilled) {
    while (!sellFilled) {
      Order order = client.getOrderStatus(
          new OrderStatusRequest(symbol, sell.getOrderId()));
      if (order.getStatus().toString().equals("FILLED")) {
        message = "Sell complete. Good trade.";
        RsiWebSocket.sendCustomTradingUpdateMessage(username,
            sell.getOrderId(), message);
        System.out.println(message);

        sellFilled = true;
        trades.updateTrade(username,
            client.getOrderStatus(
                new OrderStatusRequest(symbol, sell.getOrderId())),
            message);
        break;
      } else {
        double currentPrice = Double
            .valueOf(client.get24HrPriceStatistics(symbol).getLastPrice());
        double stop = Double.valueOf(stopPrice);
        if (currentPrice < stop) {
          try {
            Thread.sleep(60000);
          } catch (Exception e) {
            System.out.println("Thread interrupted");
          }

          String newCurrent
              = client.get24HrPriceStatistics(symbol).getLastPrice();
          if (Double.valueOf(newCurrent) >= stop) {
            continue;
          } else {
            message = "Current price still below stop price. Cancelling "
                + "previous sell and generating stop sell. Time is: "
                + java.time.LocalTime.now();
            RsiWebSocket.sendCustomTradingUpdateMessage(username,
                sell.getOrderId(), message);
            System.out.println(message);
            client.cancelOrder(
                new CancelOrderRequest(symbol, sell.getOrderId()));
            trades.updateTrade(username,
                client.getOrderStatus(
                    new OrderStatusRequest(symbol, sell.getOrderId())),
                message);

            double asset = Double.valueOf(sell.getOrigQty());
            String[] splits = newCurrent.split("\\.");
            int decimalLength = splits[1].length();
            double sp = Double.valueOf(newCurrent)
                - (Double.valueOf(newCurrent) * 0.0025);
            BigDecimal bd = new BigDecimal(sp).setScale(decimalLength,
                RoundingMode.HALF_DOWN);
            NewOrderResponse res = client
                .newOrder(NewOrder.limitSell(symbol, TimeInForce.GTC,
                    sell.getOrigQty(), bd.toPlainString()));
            message = "Stop buy posted. Price of " + newCurrent
                + ". Awaiting completion. Bad trade. Time is "
                + java.time.LocalTime.now();
            trades.addTrade(username, client.getOrderStatus(
                new OrderStatusRequest(symbol, res.getOrderId())));
            System.out.println(message);
          }
        } else {
          message
              = "Monitoring for stop loss or sell completion. Time is: "
                  + java.time.LocalTime.now();
          System.out.println(message);
          RsiWebSocket.sendCustomTradingUpdateMessage(username,
              sell.getOrderId(), message);
          try {
            Thread.sleep(10000);
          } catch (Exception e) {
            System.out.println("Thread interrupted.");
          }
        }
      }
    }
  }
}
