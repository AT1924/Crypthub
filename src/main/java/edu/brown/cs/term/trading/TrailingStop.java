package edu.brown.cs.term.trading;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.binance.api.client.BinanceApiAsyncRestClient;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.binance.api.client.domain.market.TickerStatistics;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.term.database.CrypthubUser;
import edu.brown.cs.term.general.Tuple;
import edu.brown.cs.term.thread.SellThread;

public class TrailingStop {
  private String APIkey = 
      "qW9xUO8kNpHHnxb0dJjStRrzE7tN0oTlPCbah4IWdxkgyAa5vqJXzSME6IYlDhrp";
  private String secretKey = 
      "PXEisYYJhKd6T36nC8mH05AoZl4gcyytcz1Gd5Ybin1iv6GeS27rXySsaAn3Z5Mp";
  private Map<String, Double> priceMap = new HashMap<String, Double>();
  private Map<String, Boolean> executedMap
      = new HashMap<String, Boolean>();
  private double stopPercent = 0.10;
  private String username;
  private Set<String> symbols;
  BinanceApiClientFactory fact2;
  BinanceApiRestClient client2;
  String message = "";
  private Gson GSON;

  public TrailingStop(String user,
      Set<String> symb, double percent) {
    username = user;
    symbols = symb;
    stopPercent = percent;
    setPublicPrivateKey(username);
    initializeMaps(symbols);
  }
  
  public String setPublicPrivateKey(String username) {
    CrypthubUser user = new CrypthubUser();
    this.username = username;
    Tuple<String, String> keys = user.getPublicPrivateKey(username);
    if (keys.equals(new Tuple<>("", ""))) {
      return GSON.toJson(ImmutableMap.of("success", false));
    }
    this.APIkey = keys.getFirstEntry();
    this.secretKey = keys.getSecondEntry();
    fact2 = BinanceApiClientFactory.newInstance(APIkey, secretKey);
    client2 = fact2.newRestClient();
    return GSON.toJson(ImmutableMap.of("success", true));
  }
  
  public void calculate() {
    while(true) {
      monitorPrice();
    }
  }

  public void monitorPrice() {
    BinanceApiClientFactory fact
        = BinanceApiClientFactory.newInstance(APIkey, secretKey);
    BinanceApiAsyncRestClient client = fact.newAsyncRestClient();
    StringBuilder sb = new StringBuilder();
    sb.append("Checking for trailing stop for these assets: ");
    for (String symbol : symbols) {
      sb.append(symbol + ", ");
    }
    sb.substring(0, sb.length()-2);
    sb.append(". Time is " + java.time.LocalTime.now());
    message = sb.toString();
    System.out.println(message);
    for (String symbol : symbols) {
      CompletableFuture<TickerStatistics> ticker
          = new CompletableFuture<TickerStatistics>();
      client.get24HrPriceStatistics(symbol,
          (TickerStatistics response) -> {
            ticker.complete(response);
          });
      TickerStatistics ts = null;
      try {
        ts = ticker.get();
      } catch (Exception e) {
        System.out.println("Error getting ticker");
      }
      String price = ts.getLastPrice();
      Double priceVal = Double.parseDouble(price);
      if (priceVal >= priceMap.get(symbol)) {
        priceMap.replace(symbol, priceVal);
      } else {
        double decrease = priceMap.get(symbol) - priceVal;
        double percentLoss = decrease / priceMap.get(symbol);
        if (percentLoss > stopPercent && !executedMap.get(symbol)) {
          // execute stop loss order with fulltrade function;
          message = "Stop loss condition found for " + symbol
              + ". Executing stop order. Time is: "
              + java.time.LocalTime.now();
          System.out.println(message);
          String[] splits = price.split("\\.");
          int decimalLength = splits[1].length();
          double valBuy = priceVal - (priceVal * stopPercent);
          double valStop = priceVal * (1.0 + stopPercent/2.0);
          BigDecimal bdStop = new BigDecimal(valStop)
              .setScale(decimalLength, RoundingMode.HALF_UP);
          BigDecimal bdBuy = new BigDecimal(valBuy)
              .setScale(decimalLength, RoundingMode.HALF_UP);
          String quantity = client2.getAccount().getAssetBalance(symbol.substring(0, 3)).getFree();
          SellThread st = new SellThread("TsSellThread", username, symbol,
              price, bdBuy.toPlainString(),
              bdStop.toPlainString(), quantity);
        }
      }
    }
    try {
      Thread.sleep(30000);
    } catch (InterruptedException e) {
      System.out.println("Thread interrupted");
    }
  }

  private void initializeMaps(Set<String> symbols) {
    for (String symbol : symbols) {
      priceMap.put(symbol, -1.0);
      executedMap.put(symbol, false);
    }
  }

}
