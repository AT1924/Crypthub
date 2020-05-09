package edu.brown.cs.term.gui;

import java.util.HashSet;
import java.util.Set;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.TimeInForce;
import com.binance.api.client.domain.account.NewOrder;
import com.binance.api.client.exception.BinanceApiException;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.term.database.CrypthubUser;
import edu.brown.cs.term.general.Tuple;
import edu.brown.cs.term.thread.BuyThread;
import edu.brown.cs.term.thread.SellThread;
import edu.brown.cs.term.thread.TradingAlgorithmThread;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

public class TradingHandler {
  private static final Gson GSON = new Gson();

  public static class FullTradeSellHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String symbol = qm.value("symbol");
      String username = qm.value("username");
      String sellPrice = qm.value("sell_price");
      String buyPrice = qm.value("buy_price");
      String stopPrice = qm.value("stop_price");
      String quantity = qm.value("quantity");

      StringBuilder builder = new StringBuilder();
      builder.append(username);
      builder.append(" ");
      builder.append(symbol);
      builder.append(" SELL");

      SellThread st = new SellThread(username, builder.toString(), symbol,
          sellPrice, buyPrice, stopPrice, quantity);
      return GSON.toJson(ImmutableMap.of("success", true));
    }
  }

  public static class FullTradeBuyHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String symbol = qm.value("symbol");
      String username = qm.value("username");
      String sellPrice = qm.value("sell_price");
      String buyPrice = qm.value("buy_price");
      String stopPrice = qm.value("stop_price");
      String quantity = qm.value("quantity");

      StringBuilder builder = new StringBuilder();
      builder.append(username);
      builder.append(" ");
      builder.append(symbol);
      builder.append(" BUY");
      BuyThread bt = new BuyThread(username, builder.toString(), symbol,
          buyPrice, sellPrice, stopPrice, quantity);
      return GSON.toJson(ImmutableMap.of("success", true));
    }
  }

  public static class ValidOrders implements Route {
    @Override
    public String handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String symbol = qm.value("symbol");
      String sellPrice = qm.value("sell_price");
      String buyPrice = qm.value("buy_price");
      String quantity = qm.value("quantity");
      String username = qm.value("username");
      CrypthubUser user = new CrypthubUser();
      // TODO: make sure public private keys are valid
      Tuple<String, String> keys = user.getPublicPrivateKey(username);
      String publicKey = keys.getFirstEntry();
      String privateKey = keys.getSecondEntry();
      BinanceApiClientFactory fact
          = BinanceApiClientFactory.newInstance(publicKey, privateKey);
      BinanceApiRestClient client = fact.newRestClient();

      try {
        client.newOrderTest(NewOrder.limitSell(symbol, TimeInForce.GTC,
            quantity, sellPrice));
        client.newOrderTest(NewOrder.limitBuy(symbol, TimeInForce.GTC,
            quantity, buyPrice));
        return GSON.toJson(ImmutableMap.of("success", true));
      } catch (BinanceApiException e) {
        return GSON.toJson(
            ImmutableMap.of("success", false, "error", e.toString()));
      }
    }
  }

  public static class AlgorithmicTrader implements Route {
    @Override
    public String handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String username = qm.value("username");
      System.out.println(username);
      String upper_limit = qm.value("upper_limit");
      String lower_limit = qm.value("lower_limit");
      String multiplier = qm.value("multiplier");
      String fraction = qm.value("fraction");
      String[] coins = qm.value("coins").split(" ");
      Set<String> coins_set = new HashSet<>();
      for (String coin : coins) {
        coins_set.add(coin.trim());
      }
      System.out.println(coins_set);
      TradingAlgorithmThread td
          = new TradingAlgorithmThread(username, "sample_thread",
              coins_set, upper_limit, lower_limit, multiplier, fraction);
      return GSON.toJson(ImmutableMap.of("success", true));
    }
  }
}
