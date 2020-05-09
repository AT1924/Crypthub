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

public class FullTradeSell {
  // private String APIkey =
  // "qW9xUO8kNpHHnxb0dJjStRrzE7tN0oTlPCbah4IWdxkgyAa5vqJXzSME6IYlDhrp";
  // private String secretKey =
  // "PXEisYYJhKd6T36nC8mH05AoZl4gcyytcz1Gd5Ybin1iv6GeS27rXySsaAn3Z5Mp";
  private String APIkey = "";
  private String secretKey = "";
  private String username = "js";
  private BinanceApiClientFactory fact;
  private BinanceApiRestClient client;
  private Trades trades;
  private Set<String> symbols;
  private String message = "";
  private Gson GSON;

  // TODO: figure out how this works
  public FullTradeSell(String user) {
    GSON = new Gson();
    username = user;
    setPublicPrivateKey(username);
    trades = new Trades(); // Intializes Trades DB
  }

  /**
   * Sets the public and private key for use in trading.
   * @param username
   *          username of the user
   * @return String representing JSON object
   */
  public String setPublicPrivateKey(String username) {
    CrypthubUser user = new CrypthubUser();
    this.username = username;
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

  public void fullTrade(String symbol, String sellPrice, String buyPrice,
      String stopPrice, String quantity) {
    NewOrderResponse sell = placeSellOrder(symbol, sellPrice, quantity);
    NewOrderResponse buy = checkAndBuy(sell, symbol, buyPrice, false);
    monitorForStop(buy, sell, symbol, stopPrice, false);
  }

  public NewOrderResponse placeSellOrder(String symbol, String sellPrice,
      String quantity) {
    double lastPrice = Double
        .valueOf(client.get24HrPriceStatistics(symbol).getLastPrice());
    double sell = Double.valueOf(sellPrice);
    if (!username.equals("")) {
      // double amountFree =
      // Double.valueOf(client.getAccount().getAssetBalance(symbol.substring(0,3)).getFree());
      // double amountToSell = amountFree / frac;
      NewOrderResponse res = client.newOrder(NewOrder.limitSell(symbol,
          TimeInForce.GTC, quantity, sellPrice));
      trades.addTrade(username, client.getOrderStatus(
          new OrderStatusRequest(symbol, res.getOrderId())));
      message
          = "Sell posted for " + quantity + " " + symbol.substring(0, 3)
              + ". Time is " + java.time.LocalTime.now();
      RsiWebSocket.sendCustomTradingUpdateMessage(username, res.getOrderId(), message);
      System.out.println(message);

      return res;
      // } else if (!username.equals("")) {
      // System.out.println("ERROR: Current price is higher than sell
      // price");
      // return null;
    } else {
      System.out.println("ERROR: Please login before making a trade.");
      return null;
    }
  }

  public NewOrderResponse checkAndBuy(NewOrderResponse sell, String symbol,
      String buyPrice, boolean sellFilled) {
    NewOrderResponse res = null;
    while (!sellFilled) {
      Order order = client.getOrderStatus(
          new OrderStatusRequest(symbol, sell.getOrderId()));
      if (order.getStatus().toString().equals("FILLED")) {
        message = "Sell has been filled. Posting buy. Time is: "
            + java.time.LocalTime.now();
        RsiWebSocket.sendCustomTradingUpdateMessage(username,
            sell.getOrderId(), message);
        sellFilled = true;
        double assetAmount = Double.valueOf(sell.getPrice())
            * Double.valueOf(sell.getOrigQty());
        double buyQuant = assetAmount / Double.valueOf(buyPrice);
        BigDecimal bd
            = new BigDecimal(buyQuant).setScale(2, RoundingMode.HALF_DOWN);
        trades.updateTrade(username,
            client.getOrderStatus(
                new OrderStatusRequest(symbol, sell.getOrderId())),
            message);
        res = client.newOrder(NewOrder.limitBuy(symbol, TimeInForce.GTC,
            bd.toPlainString(), buyPrice));
        trades.addTrade(username, client.getOrderStatus(
            new OrderStatusRequest(symbol, res.getOrderId())));
      } else {
        message = "Sell not complete. Time is: "
            + java.time.LocalTime.now();
        System.out.println(message);
        RsiWebSocket.sendCustomTradingUpdateMessage(username, sell.getOrderId(),
            message);
        try {
          Thread.sleep(30000);
        } catch (Exception e) {
          System.out.println("Thread interrupted");
        }
      }
    }
    return res;
  }

  public void monitorForStop(NewOrderResponse buy, NewOrderResponse sell,
      String symbol, String stopPrice, boolean buyFilled) {
    while (!buyFilled) {
      Order order = client.getOrderStatus(
          new OrderStatusRequest(symbol, buy.getOrderId()));
      if (order.getStatus().toString().equals("FILLED")) {
        message = "Buy complete. Good trade. Time is: "
            + java.time.LocalTime.now();
        RsiWebSocket.sendCustomTradingUpdateMessage(username, buy.getOrderId(), message);
        System.out.println(message);
        trades.updateTrade(username,
            client.getOrderStatus(
                new OrderStatusRequest(symbol, buy.getOrderId())),
            message);
        buyFilled = true;
      } else {
        double currentPrice = Double
            .valueOf(client.get24HrPriceStatistics(symbol).getLastPrice());
        double stop = Double.valueOf(stopPrice);
        if (currentPrice > stop) {
          try {
            Thread.sleep(60000);
          } catch (Exception e) {
            System.out.println("Thread interrupted");
          }

          double newCurrent = Double.valueOf(
              client.get24HrPriceStatistics(symbol).getLastPrice());
          if (newCurrent <= stop) {
            continue;
          } else {
            message = "Stop price exceeded. Cancelling previous"
                + " buy and generating stop buy order. Time is: "
                + java.time.LocalTime.now();
            RsiWebSocket.sendCustomTradingUpdateMessage(username, buy.getOrderId(), message);
            System.out.println(message);
            client.cancelOrder(
                new CancelOrderRequest(symbol, buy.getOrderId()));
            trades.updateTrade(username,
                client.getOrderStatus(
                    new OrderStatusRequest(symbol, buy.getOrderId())),
                message);

            String price
                = client.get24HrPriceStatistics(symbol).getLastPrice();
            String[] splits = price.split("\\.");
            int decimalLength = splits[1].length();
            double valPrice = Double.valueOf(price) * 1.0025;
            BigDecimal bd = new BigDecimal(valPrice)
                .setScale(decimalLength, RoundingMode.HALF_DOWN);
            double quant = Double.valueOf(sell.getOrigQty())
                * Double.valueOf(sell.getPrice());
            double stopQuant = quant / bd.doubleValue();
            BigDecimal sd = new BigDecimal(stopQuant).setScale(2,
                RoundingMode.HALF_DOWN);
            NewOrderResponse res = client
                .newOrder(NewOrder.limitBuy(symbol, TimeInForce.GTC,
                    sd.toPlainString(), bd.toPlainString()));
            message = "Stop buy posted. Price of " + price
                + ". Awaiting completion. Bad trade. Time is "
                + java.time.LocalTime.now();
            RsiWebSocket.sendCustomTradingUpdateMessage(username, buy.getOrderId(), message);
            trades.addTrade(username, client.getOrderStatus(
                new OrderStatusRequest(symbol, res.getOrderId())));
            System.out.println(message);
            buyFilled = true;
          }
        } else {
          message = "Monitoring for stop loss or buy completion. Time is: "
              + java.time.LocalTime.now();
          System.out.println(message);
          RsiWebSocket.sendCustomTradingUpdateMessage(username, buy.getOrderId(),
              message);

          try {
            Thread.sleep(30000);
          } catch (Exception e) {
            System.out.println("Thread interrupted");
          }
        }
      }
    }
  }
}
