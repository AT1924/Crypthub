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
import com.twilio.Twilio;

import edu.brown.cs.term.database.CrypthubUser;
import edu.brown.cs.term.general.Tuple;
import websockets.RsiWebSocket;

public class RSICalculator {

  Map<String, Double> currentRSI = new HashMap<String, Double>();
  private Map<String, List<Double>> prevGains
      = new HashMap<String, List<Double>>();
  private Map<String, List<Double>> prevLosses
      = new HashMap<String, List<Double>>();
  private Map<String, List<Candlestick>> previousCandles
      = new HashMap<String, List<Candlestick>>();
  private Map<String, Boolean> firstNewCandle
      = new HashMap<String, Boolean>();
  private Map<String, Boolean> twilioHold = new HashMap<String, Boolean>();
  private List<Double> rsiVals = new ArrayList<Double>();
  private boolean finishedInitialCalculations = false;
  private CandlestickInterval interval = CandlestickInterval.HALF_HOURLY;
  private Map<CandlestickInterval, Integer> intervalConversion
      = new HashMap<CandlestickInterval, Integer>();
  private String APIkey
      = "aXGg0J5kFv08YOytKZrCav9rWnjgzdFx3BvjIjY5M2gQQCaYSEdznhAGG4kspVr2";
  private String secretKey
      = "pgbthhPJBg8FS29FCeyYvlF5ay1CV0we4xxilxL9pytc5djb7RxDCs7k4kChthMI";
  private String twilioKey = "AC740a42cac1fc2a9567eb53594736f458";
  private String twilioSecret = "9478ee8347691c3410cce9c8f00e0681";
  private Set<String> symbols = new HashSet<String>();
  private String username = "js";
  private BinanceApiClientFactory fact;
  private BinanceApiRestClient client;
  private Gson GSON;

  public RSICalculator(CandlestickInterval i, String user,
      Set<String> symb) {
    GSON = new Gson();
    Twilio.init(twilioKey, twilioSecret);
    username = user;
    interval = i;
    symbols = symb;
    setPublicPrivateKey(username);
    setIntervalConversions();
  }

  public void calculate() {
    runCalculationsCurrent(symbols);
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
    System.out.println("here1");
    return GSON.toJson(ImmutableMap.of("success", true));
  }

  private void runCalculationsCurrent(Set<String> symbols) {
    initiateCalculation(symbols);
    while (finishedInitialCalculations) {
      continueCalculation(symbols);
    }
  }

  private void initiateCalculation(Set<String> symbols) {
    initializeMaps(symbols);
    BinanceApiClientFactory factory
        = BinanceApiClientFactory.newInstance(APIkey, secretKey);
    BinanceApiAsyncRestClient client = factory.newAsyncRestClient();
    System.out.println("here2");
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
      long startTime
          = endTime - (256 * intervalConversion.get(interval) * 60 * 1000);
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
      int count = 15;
      while (currentRSI.get(symbol) == -1.0) {
        // if previous gains and losses are populated, use wilder smoothing
        if (count > 15) {
          List<Double> prev_gains = prevGains.get(symbol);
          double previousGain = prev_gains.get(prev_gains.size() - 1);
          List<Double> prev_loss = prevLosses.get(symbol);
          double previousLoss = prev_loss.get(prev_loss.size() - 1);

          double lastDiff = Double
              .parseDouble(candlesticks.get(count).getClose())
              - Double.parseDouble(candlesticks.get(count - 1).getClose());

          double rsi = rsiFromDiff(lastDiff, previousGain, previousLoss,
              symbol, true);
          count++;

        } else {
          List<Candlestick> firstFifteen = candlesticks.subList(0, 15);
          List<Double> differences = getDifferences(firstFifteen);
          List<Double> gainsClipped = clipGains(differences);
          List<Double> lossesClipped = clipLoss(differences);

          double gain_average = averageGains(gainsClipped);
          double loss_average = averageLoss(lossesClipped);
          prevGains.get(symbol).add(gain_average);
          prevLosses.get(symbol).add(loss_average);
          double rs = gain_average / loss_average;
          double rsi = 100 - 100 / (1 + rs);
          rsiVals.add(rsi);
          count++;
        }
        if (count == 256) {
          double lastRSI = rsiVals.get(rsiVals.size() - 1);
          currentRSI.replace(symbol, lastRSI);
          previousCandles.get(symbol)
              .add(candlesticks.get(candlesticks.size() - 1));
          String message
              = symbol + " Current RSI: " + Double.toString(lastRSI);
          System.out.println(message);
//          updateFrontEnd(message);
          finishedInitialCalculations = true;
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
      long startTime = endTime - (2 * 3600 * 1000);
      CompletableFuture<List<Candlestick>> candles
          = new CompletableFuture<List<Candlestick>>();
      List<Candlestick> candlesticks = new ArrayList<Candlestick>();
      client.getCandlestickBars(symbol, interval, 4, startTime, endTime,
          (List<Candlestick> response) -> {
            candles.complete(response);
          });
      try {
        candlesticks = candles.get();
      } catch (Exception e) {
        System.out.println("Error getting candlesticks.");
      }
      if (firstNewCandle.get(symbol) == false) {
        List<Double> prev_gains = prevGains.get(symbol);
        double prev_gain = prev_gains.get(prev_gains.size() - 2);
        List<Double> prev_losses = prevLosses.get(symbol);
        double prev_loss = prev_losses.get(prev_losses.size() - 2);
        double lastDiff = Double.parseDouble(
            candlesticks.get(candlesticks.size() - 1).getClose())
            - Double.parseDouble(
                candlesticks.get(candlesticks.size() - 2).getClose());

        double rsi
            = rsiFromDiff(lastDiff, prev_gain, prev_loss, symbol, false);
        String message = symbol + " Current RSI No New Candle Yet: "
            + Double.toString(rsi);
        System.out.println(message);
//        this.updateFrontEnd(message);
        currentRSI.replace(symbol, rsi);
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

          double prev_gain = prevGains.get(symbol)
              .get(prevGains.get(symbol).size() - 2);
          double prev_loss = prevLosses.get(symbol)
              .get(prevGains.get(symbol).size() - 2);
          double lastDiff = Double.parseDouble(
              candlesticks.get(candlesticks.size() - 2).getClose())
              - Double.parseDouble(
                  candlesticks.get(candlesticks.size() - 3).getClose());

          double rsi
              = rsiFromDiff(lastDiff, prev_gain, prev_loss, symbol, false);
          currentRSI.replace(symbol, rsi);
          String message = symbol + " Current RSI Old Candle Final: "
              + Double.toString(rsi);
          System.out.println(message);
//          this.updateFrontEnd(message);

          double prev_gain_new = prevGains.get(symbol)
              .get(prevGains.get(symbol).size() - 1);
          double prev_loss_new = prevLosses.get(symbol)
              .get(prevLosses.get(symbol).size() - 1);

          lastDiff = Double.parseDouble(
              candlesticks.get(candlesticks.size() - 1).getClose())
              - Double.parseDouble(
                  candlesticks.get(candlesticks.size() - 2).getClose());

          rsi = rsiFromDiff(lastDiff, prev_gain_new, prev_loss_new, symbol,
              true);
          currentRSI.replace(symbol, rsi);
          message = symbol + " Current RSI New Candle: "
              + Double.toString(rsi);
          System.out.println(message);
//          this.updateFrontEnd(message);

        } else {
          double prev_gain = prevGains.get(symbol)
              .get(prevGains.get(symbol).size() - 2);
          double prev_loss = prevLosses.get(symbol)
              .get(prevLosses.get(symbol).size() - 2);
          double lastDiff = Double.parseDouble(
              candlesticks.get(candlesticks.size() - 1).getClose())
              - Double.parseDouble(
                  candlesticks.get(candlesticks.size() - 2).getClose());
          double rsi
              = rsiFromDiff(lastDiff, prev_gain, prev_loss, symbol, false);
          currentRSI.replace(symbol, rsi);
          String message
              = symbol + " Current RSI: " + Double.toString(rsi);
          System.out.println(message);
//          updateFrontEnd(message);
        }
      }
    }
    try {
      Thread.sleep(30000);
    } catch (InterruptedException e) {
      System.out.println("Thread interrupted");
      continueCalculation(symbols);
    }
  }

  private List<Double> getDifferences(List<Candlestick> candles) {
    List<Double> diffs = new ArrayList<Double>();
    for (int i = 1; i < candles.size(); i++) {
      double diff = Double.parseDouble(candles.get(i).getClose())
          - Double.parseDouble(candles.get(i - 1).getClose());
      diffs.add(diff);
    }
    return diffs;
  }

  private List<Double> clipGains(List<Double> differences) {
    List<Double> gains = differences;
    for (double gain : gains) {
      if (gain < 0) {
        gain = 0;
      }
    }
    return gains;
  }

  private List<Double> clipLoss(List<Double> differences) {
    List<Double> losses = differences;
    for (double loss : losses) {
      if (loss > 0) {
        loss = 0;
      }
    }
    return losses;
  }

  private double averageGains(List<Double> gains) {
    double sum = 0;
    for (double gain : gains) {
      sum += gain;
    }
    double average = sum / 14;
    return average;
  }

  private double averageLoss(List<Double> losses) {
    double sum = 0;
    for (double loss : losses) {
      sum += Math.abs(loss);
    }
    double average = sum / 14;
    return average;
  }

  private void initializeMaps(Set<String> symbols) {
    for (String symbol : symbols) {
      currentRSI.put(symbol, -1.0);
      prevGains.put(symbol, new ArrayList<Double>());
      prevLosses.put(symbol, new ArrayList<Double>());
      previousCandles.put(symbol, new ArrayList<Candlestick>());
      firstNewCandle.put(symbol, false);
      twilioHold.put(symbol, false);

    }
  }

  private double rsiFromDiff(double lastDiff, double previousGain,
      double previousLoss, String symbol, boolean newCandle) {
    double rsi = -1.0;
    if (lastDiff < 0) {
      double gain_average = previousGain * 13 / 14;
      double loss_average = (Math.abs(previousLoss * 13 - lastDiff)) / 14;
      if (newCandle) {
        prevGains.get(symbol).add(gain_average);
        prevLosses.get(symbol).add(loss_average);
      } else {
        prevGains.get(symbol).set(prevGains.get(symbol).size() - 1,
            gain_average);
        prevLosses.get(symbol).set(prevLosses.get(symbol).size() - 1,
            loss_average);
      }
      double rs = gain_average / loss_average;
      rsi = 100 - 100 / (1 + rs);
      rsiVals.add(rsi);

    } else {
      double gain_average = (previousGain * 13 + lastDiff) / 14;
      double loss_average = (Math.abs(previousLoss * 13)) / 14;
      if (newCandle) {
        prevGains.get(symbol).add(gain_average);
        prevLosses.get(symbol).add(loss_average);
      } else {
        prevGains.get(symbol).set(prevGains.get(symbol).size() - 1,
            gain_average);
        prevLosses.get(symbol).set(prevLosses.get(symbol).size() - 1,
            loss_average);
      }
      double rs = gain_average / loss_average;
      rsi = 100 - 100 / (1 + rs);
      rsiVals.add(rsi);
    }
    return rsi;
  }

  public Map<String, Double> getRSIMap() {
    return currentRSI;
  }

  public void setInterval(CandlestickInterval setInterval) {
    interval = setInterval;
  }

  public void setIntervalConversions() {
    intervalConversion.put(CandlestickInterval.HALF_HOURLY, 30);
    intervalConversion.put(CandlestickInterval.HOURLY, 60);
    intervalConversion.put(CandlestickInterval.TWO_HOURLY, 120);
    intervalConversion.put(CandlestickInterval.FOUR_HOURLY, 240);
    intervalConversion.put(CandlestickInterval.EIGHT_HOURLY, 480);
    intervalConversion.put(CandlestickInterval.TWELVE_HOURLY, 720);
    intervalConversion.put(CandlestickInterval.DAILY, 1440);

  }

  private void updateFrontEnd(String message) {
    if (username != null) {
      RsiWebSocket.sendAutomaticradingUpdateMessage(username, message);
    }
  }
}
