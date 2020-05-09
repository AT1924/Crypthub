package edu.brown.cs.term.trading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.binance.api.client.BinanceApiAsyncRestClient;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.term.database.CrypthubUser;
import edu.brown.cs.term.general.Tuple;
import websockets.RsiWebSocket;

public class MovingAverage {
  Map<String, Double> currentMA = new HashMap<String, Double>();
  Map<String, Double> currentSD = new HashMap<String, Double>();
  Map<String, List<Double>> previousMAs
      = new HashMap<String, List<Double>>();
  Map<String, List<Double>> previousSDs
      = new HashMap<String, List<Double>>();
  Map<String, List<Candlestick>> previousCandles
      = new HashMap<String, List<Candlestick>>();
  Map<String, Boolean> firstNewCandle = new HashMap<String, Boolean>();
  Map<String, Boolean> twilioHold = new HashMap<String, Boolean>();
  List<Double> rsiVals = new ArrayList<Double>();
  boolean finishedInitialCalculations = false;
  double bandMultiplier = 2;
  String username = "js";
  private Gson GSON;
  CandlestickInterval interval = CandlestickInterval.HALF_HOURLY;
  private String APIkey
      = "aXGg0J5kFv08YOytKZrCav9rWnjgzdFx3BvjIjY5M2gQQCaYSEdznhAGG4kspVr2";
  private String secretKey
      = "pgbthhPJBg8FS29FCeyYvlF5ay1CV0we4xxilxL9pytc5djb7RxDCs7k4kChthMI";
  private String twilioKey = "AC740a42cac1fc2a9567eb53594736f458";
  private String twilioSecret = "9478ee8347691c3410cce9c8f00e0681";
  private boolean finishedInitialCalculation = false;
  private BinanceApiClientFactory fact;
  private BinanceApiRestClient client;
  private Set<String> symbols = new HashSet<String>();
  private String message;

  public MovingAverage(CandlestickInterval i, String user,
      Set<String> symb, String multiplier) {
    interval = i;
    symbols = symb;
    bandMultiplier = Double.valueOf(multiplier);
    GSON = new Gson();
    username = user;
    setPublicPrivateKey(username);
  }

  public void calculate() {
    runCalculations(symbols);
  }

  private void runCalculations(Set<String> symbols) {
    initiateCalculation(symbols);
    while (finishedInitialCalculation) {
      continueCalculation(symbols);
    }
  }

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

  private void initiateCalculation(Set<String> symbols) {
    initializeMaps(symbols);
    BinanceApiClientFactory factory
        = BinanceApiClientFactory.newInstance(APIkey, secretKey);
    BinanceApiAsyncRestClient client = factory.newAsyncRestClient();
    for (String symbol : symbols) {
      CompletableFuture<Long> time = new CompletableFuture<Long>();
      client.getServerTime(response -> {
        time.complete(response.getServerTime());
      });
      long endTime = -1;
      try {
        endTime = time.get();
      } catch (Exception e) {
        System.out.println("Time was not retrieved.");
      }
      long startTime = endTime - (128 * 60 * 60 * 1000);
      CompletableFuture<List<Candlestick>> candles
          = new CompletableFuture<List<Candlestick>>();
      List<Candlestick> candlesticks = new ArrayList<Candlestick>();
      client.getCandlestickBars(symbol, interval, 256, startTime, endTime,
          (List<Candlestick> response) -> {
            candles.complete(response);
          });
      try {
        candlesticks = candles.get();
      } catch (Exception e) {
        System.out.println("Error getting candlesticks.");
      }
      int start = 0;
      int end = 21;
      while (currentMA.get(symbol) == 0.0) {
        if (end < 257) {
          List<Candlestick> group21 = candlesticks.subList(start, end);
          double firstAverage = calculateAverage(group21);
          double firstSD = calculateStandardDev(group21, firstAverage);
          previousMAs.get(symbol).add(firstAverage);
          previousSDs.get(symbol).add(firstSD);
          start++;
          end++;
        }
        if (end == 257) {
          currentMA.replace(symbol, previousMAs.get(symbol)
              .get(previousMAs.get(symbol).size() - 1));
          currentSD.replace(symbol, previousSDs.get(symbol)
              .get(previousMAs.get(symbol).size() - 1));
          message = symbol + " Current Moving Average: "
              + currentMA.get(symbol);
//          this.updateFrontEnd(message);

          previousCandles.get(symbol)
              .add(candlesticks.get(candlesticks.size() - 1));
          finishedInitialCalculation = true;
        }
      }
    }
  }

  private void continueCalculation(Set<String> symbols) {
    BinanceApiClientFactory factory
        = BinanceApiClientFactory.newInstance(APIkey, secretKey);
    BinanceApiAsyncRestClient client = factory.newAsyncRestClient();
    for (String symbol : symbols) {
      List<Candlestick> previous = previousCandles.get(symbol);
      Candlestick previousC = previous.get(previous.size() - 1);
      CompletableFuture<Long> time = new CompletableFuture<Long>();
      client.getServerTime(response -> {
        time.complete(response.getServerTime());
      });
      long endTime = -1;
      try {
        endTime = time.get();
      } catch (Exception e) {
        System.out.println("Time was not retrieved.");
      }
      long startTime = endTime - (long) (10.5 * 3600 * 1000);
      CompletableFuture<List<Candlestick>> candles
          = new CompletableFuture<List<Candlestick>>();
      List<Candlestick> candlesticks = new ArrayList<Candlestick>();
      client.getCandlestickBars(symbol, interval, 21, startTime, endTime,
          (List<Candlestick> response) -> {
            candles.complete(response);
          });
      try {
        candlesticks = candles.get();
      } catch (Exception e) {
        System.out.println("Error getting candlesticks.");
      }
      if (firstNewCandle.get(symbol) == false) {
        double currentAverage = calculateAverage(candlesticks);
        double currentStandardDev
            = calculateStandardDev(candlesticks, currentAverage);
        previousMAs.get(symbol).set(previousMAs.get(symbol).size() - 1,
            currentAverage);
        previousSDs.get(symbol).set(previousMAs.get(symbol).size() - 1,
            currentStandardDev);
        currentMA.replace(symbol, currentAverage);
        currentSD.replace(symbol, currentStandardDev);

        message = symbol + " Lower Band: " + Double.toString(
            (currentAverage - bandMultiplier * currentStandardDev));
        System.out.println(message);
//        this.updateFrontEnd(message);

        message = symbol + " Current MA No New Candle Yet: "
            + Double.toString(currentAverage);
        System.out.println(message);
//        this.updateFrontEnd(message);

        message = symbol + " Upper Band: " + Double.toString(
            (currentAverage + bandMultiplier * currentStandardDev));
        System.out.println(message);
//        this.updateFrontEnd(message);

        if (!candlesticks.get(candlesticks.size() - 1).getOpenTime()
            .equals(previousCandles.get(symbol)
                .get(previousCandles.get(symbol).size() - 1)
                .getOpenTime())) {
          firstNewCandle.replace(symbol, true);
        }
      } else {
        if (!candlesticks.get(candlesticks.size() - 1).getOpenTime()
            .equals(previousCandles.get(symbol)
                .get(previousCandles.get(symbol).size() - 1)
                .getOpenTime())) {
          previousCandles.get(symbol)
              .add(candlesticks.get(candlesticks.size() - 1));
          long tempStart = startTime - (long) (0.5 * 3600 * 1000);
          CompletableFuture<List<Candlestick>> tempCandles
              = new CompletableFuture<List<Candlestick>>();
          List<Candlestick> tempSticks = new ArrayList<Candlestick>();
          client.getCandlestickBars(symbol, interval, 22, tempStart,
              endTime, (List<Candlestick> response) -> {
                tempCandles.complete(response);
              });
          try {
            tempSticks = tempCandles.get();
          } catch (Exception e) {
            System.out.println("Error getting candlesticks.");
          }
          double previousAverage
              = calculateAverage(tempSticks.subList(0, 21));
          double previousSD = calculateStandardDev(
              tempSticks.subList(0, 21), previousAverage);
          previousMAs.get(symbol).set(previousMAs.get(symbol).size() - 1,
              previousAverage);
          previousSDs.get(symbol).set(previousSDs.get(symbol).size() - 1,
              previousSD);

          message = symbol + " Current MA Old Candle Final: "
              + previousAverage;

          System.out.println(message);
//          this.updateFrontEnd(message);

          double newAverage = calculateAverage(candlesticks);
          double newSD = calculateStandardDev(candlesticks, newAverage);
          previousMAs.get(symbol).add(newAverage);
          previousSDs.get(symbol).add(newSD);
          currentMA.replace(symbol, newAverage);
          currentSD.replace(symbol, newSD);

          message = symbol + " Current MA New Candle: " + newAverage;
          System.out.println(message);
//          this.updateFrontEnd(message);

        } else {
          double currentAverage = calculateAverage(candlesticks);
          double currentStandardDev
              = calculateStandardDev(candlesticks, currentAverage);
          previousMAs.get(symbol).set(previousMAs.get(symbol).size() - 1,
              currentAverage);
          previousSDs.get(symbol).set(previousMAs.get(symbol).size() - 1,
              currentStandardDev);
          currentMA.replace(symbol, currentAverage);
          currentSD.replace(symbol, currentStandardDev);

          message = symbol + " Lower Band: " + Double.toString(
              (currentAverage - bandMultiplier * currentStandardDev));
          System.out.println(message);
//          this.updateFrontEnd(message);

          message = symbol + " Current MA: " + currentAverage;
          System.out.println(message);
//          this.updateFrontEnd(message);

          message = symbol + " Upper Band: " + Double.toString(
              (currentAverage + bandMultiplier * currentStandardDev));
          System.out.println(message);
//          this.updateFrontEnd(message);
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
      currentMA.put(symbol, 0.0);
      currentSD.put(symbol, 0.0);
      previousMAs.put(symbol, new ArrayList<Double>());
      previousSDs.put(symbol, new ArrayList<Double>());
      previousCandles.put(symbol, new ArrayList<Candlestick>());
      firstNewCandle.put(symbol, false);
    }
  }

  private double calculateAverage(List<Candlestick> candles) {
    double sum = 0.0;
    for (Candlestick c : candles) {
      sum += Double.parseDouble(c.getClose());
    }
    double average = sum / candles.size();
    return average;
  }

  private double calculateStandardDev(List<Candlestick> candles,
      double currentAverage) {
    double sum = 0.0;
    for (Candlestick c : candles) {
      sum += Math.pow((Double.parseDouble(c.getClose()) - currentAverage),
          2);
    }
    double variance = sum / (candles.size() - 1);
    double standardDev = Math.sqrt(variance);
    return standardDev;
  }

  public Map<String, Double> getMAMap() {
    return currentMA;
  }

  public Map<String, Double> getSDMap() {
    return currentSD;
  }

  private void updateFrontEnd(String message) {
    if (username != null) {
      RsiWebSocket.sendAutomaticradingUpdateMessage(username, message);
    }
  }
}
