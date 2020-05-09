package edu.brown.cs.term.trading;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.term.database.CrypthubUser;
import edu.brown.cs.term.general.Tuple;
import edu.brown.cs.term.thread.BuyThread;
import edu.brown.cs.term.thread.MaThread;
import edu.brown.cs.term.thread.RsiThread;
import edu.brown.cs.term.thread.SellThread;
import websockets.RsiWebSocket;

public class TradingAlgorithm {

  RsiThread rsi2;
  MaThread ma2;
  private String APIkey
      = "qW9xUO8kNpHHnxb0dJjStRrzE7tN0oTlPCbah4IWdxkgyAa5vqJXzSME6IYlDhrp";
  private String secretKey
      = "PXEisYYJhKd6T36nC8mH05AoZl4gcyytcz1Gd5Ybin1iv6GeS27rXySsaAn3Z5Mp";
  private boolean tradeNotFound = true;
  Map<String, Boolean> foundMap = new HashMap<String, Boolean>();
  BinanceApiClientFactory fact3;
  BinanceApiRestClient client3;
  Set<String> symbols = new HashSet<String>();
  double rsiLimitHigh = 85.0;
  double rsiLimitLow = 15.0;
  double multiplier = 2.0;
  double percentGain = 10.0;
  double fraction = 4.0;
  String username = "js";
  String message = "";
  CandlestickInterval interval = CandlestickInterval.HALF_HOURLY;
  FullTradeSell fts = null;
  FullTradeBuy ftb = null;
  private Gson GSON;

  public TradingAlgorithm(CandlestickInterval i, String user,
      Set<String> symb, String upper, String lower, String mult,
      String frac) {
    interval = i;
    username = user;
    GSON = new Gson();
    setPublicPrivateKey(username);
    symbols = symb;
    rsiLimitHigh = Double.valueOf(upper);
    rsiLimitLow = Double.valueOf(lower);
    multiplier = Double.valueOf(mult);
    fraction = Double.valueOf(frac);
    fts = new FullTradeSell(username);
    ftb = new FullTradeBuy(username);
    rsi2 = new RsiThread(username, "TaRsiThread", symbols);
    ma2 = new MaThread(username, "TaMaThread", symbols, mult);
    initializeMaps(symbols);

    try {
      Thread.sleep(20000);
    } catch (InterruptedException e) {

    }
    while (true) {
      System.out.println(rsi2.getRSIMap());
      if (rsi2 != null && ma2 != null && rsi2.getRSIMap() != null
          && ma2.getMAMap() != null) {
        System.out.println("true");
        findTrade();
      } else {
        System.out.println("false");
      }
    }

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
    fact3 = BinanceApiClientFactory.newInstance(APIkey, secretKey);
    client3 = fact3.newRestClient();
    return GSON.toJson(ImmutableMap.of("success", true));
  }

  public void findTrade() {
    StringBuilder sb = new StringBuilder();
    sb.append("Looking for trade in these markets: ");
    for (String symbol : symbols) {
      sb.append(symbol + ", ");
    }
    sb.deleteCharAt(sb.length() - 2);
    sb.append(". Time is: " + java.time.LocalTime.now());
    message = sb.toString();
    System.out.println(message);
    this.updateFrontEnd(message);

    for (String symbol : symbols) {
      if (!foundMap.get(symbol)) {
        long endTime = client3.getServerTime();
        long startTime = endTime - (3600 * 1000);
        List<Candlestick> candlesticks = client3.getCandlestickBars(symbol,
            CandlestickInterval.HALF_HOURLY, 2, startTime, endTime);
        double upperBand = ma2.getMAMap().get(symbol)
            + (multiplier * ma2.getSDMap().get(symbol));
        double lowerBand = ma2.getMAMap().get(symbol)
            - (multiplier * ma2.getSDMap().get(symbol));
        double currentRSI = rsi2.getRSIMap().get(symbol);
        if (currentRSI > rsiLimitHigh && Double.parseDouble(candlesticks
            .get(candlesticks.size() - 1).getClose()) > upperBand) {
          message = "RSI and MA upper limit hit. Making a trade on "
              + symbol.substring(0, 3) + ". Time is: "
              + java.time.LocalTime.now();
          System.out.println(message);
          this.updateFrontEnd(message);

          String lastPrice
              = client3.get24HrPriceStatistics(symbol).getLastPrice();
          double sellPrice = Double.valueOf(lastPrice);
          double buyPrice = sellPrice - (sellPrice * percentGain);
          String finalBuyPrice = sellToBuyPrice(lastPrice, percentGain);
          String finalStopPrice = sellToStopPrice(lastPrice, percentGain);
          String quantity = client3.getAccount()
              .getAssetBalance(symbol.substring(0, 3)).getFree();
          double quant = Double.parseDouble(quantity);
          double quantToTrade = quant / fraction;
          BigDecimal quantBD = new BigDecimal(quantToTrade).setScale(2,
              RoundingMode.HALF_DOWN);
          SellThread st
              = new SellThread("TaSellThread", username, symbol, lastPrice,
                  finalBuyPrice, finalStopPrice, quantBD.toPlainString());
          foundMap.replace(symbol, true);
        } else if (currentRSI < rsiLimitLow
            && Double.parseDouble(candlesticks.get(candlesticks.size() - 1)
                .getClose()) < lowerBand) {
          message = "RSI and MA lower limits hit. Making a trade on "
              + symbol.substring(0, 3) + ". Time is: "
              + java.time.LocalTime.now();
          System.out.println(message);
          this.updateFrontEnd(message);
          String lastPrice
              = client3.get24HrPriceStatistics(symbol).getLastPrice();
          String[] splits = lastPrice.split("\\.");
          int decimalLength = splits[1].length();
          double buyPrice = Double.valueOf(lastPrice);
          String finalSellPrice = buyToSellPrice(lastPrice, percentGain);
          String finalStopPrice = buyToStopPrice(lastPrice, percentGain);
          String quantity
              = client3.getAccount().getAssetBalance(symbol).getFree();
          double quant = Double.parseDouble(quantity);
          double quantToTrade = quant / fraction;
          BigDecimal quantBD = new BigDecimal(quantToTrade).setScale(2,
              RoundingMode.HALF_DOWN);
          BuyThread bt
              = new BuyThread(username, "TaBuyThread", symbol, lastPrice,
                  finalSellPrice, finalStopPrice, quantBD.toPlainString());
          foundMap.replace(symbol, true);
        }
      }
    }
    try {
      Thread.sleep(20000);
    } catch (InterruptedException e) {

    }

  }

  private void initializeMaps(Set<String> symbols) {
    for (String symbol : symbols) {
      foundMap.put(symbol, false);
    }
  }

  private String sellToBuyPrice(String lastPrice, double percentGain) {
    String[] splits = lastPrice.split("\\.");
    int decimalLength = splits[1].length();
    double sellPrice = Double.valueOf(lastPrice);
    double buyPrice = sellPrice - (sellPrice * percentGain);
    BigDecimal bp = new BigDecimal(buyPrice).setScale(decimalLength,
        RoundingMode.HALF_DOWN);
    return bp.toPlainString();
  }

  private String sellToStopPrice(String lastPrice, double percentGain) {
    String[] splits = lastPrice.split("\\.");
    int decimalLength = splits[1].length();
    double sellPrice = Double.valueOf(lastPrice);
    double stopPrice = sellPrice * (1.00 + (percentGain / 2.00));
    BigDecimal bp = new BigDecimal(stopPrice).setScale(decimalLength,
        RoundingMode.HALF_UP);
    return bp.toPlainString();
  }

  private String buyToSellPrice(String lastPrice, double percentGain) {
    String[] splits = lastPrice.split("\\.");
    int decimalLength = splits[1].length();
    double buyPrice = Double.valueOf(lastPrice);
    double sellPrice = buyPrice * (1.00 + percentGain);
    BigDecimal bp = new BigDecimal(sellPrice).setScale(decimalLength,
        RoundingMode.HALF_UP);
    return bp.toPlainString();
  }

  private String buyToStopPrice(String lastPrice, double percentGain) {
    String[] splits = lastPrice.split("\\.");
    int decimalLength = splits[1].length();
    double buyPrice = Double.valueOf(lastPrice);
    double stopPrice = buyPrice - (buyPrice * (percentGain / 2.0));
    BigDecimal sp = new BigDecimal(stopPrice).setScale(decimalLength,
        RoundingMode.HALF_DOWN);
    return sp.toPlainString();
  }

  private void updateFrontEnd(String message) {
    RsiWebSocket.sendAutomaticradingUpdateMessage(username, message);
  }
}
