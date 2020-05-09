package edu.brown.cs.term.thread;
import java.util.List;
import java.util.Set;

import com.binance.api.client.domain.market.CandlestickInterval;

import edu.brown.cs.term.trading.TradingAlgorithm;
public class TradingAlgorithmThread implements Runnable {
  String name;
  Thread t;
  Set<String> symbols;
  CandlestickInterval interval = CandlestickInterval.HALF_HOURLY;
  String upperRSI;
  String lowerRSI;
  String multiplier;
  String fraction;
  String username;
  public TradingAlgorithmThread(String user, String thread, Set<String> symb,
      String upperLimit, String lowerLimit, String mult, String frac) {
    username = user;
    name = thread;
    symbols = symb;
    upperRSI = upperLimit;
    lowerRSI = lowerLimit;
    multiplier = mult;
    fraction = frac;
    t = new Thread(this, name);
    t.start();
  }
  
  public void run() {
    try {
      TradingAlgorithm ta = new TradingAlgorithm(interval, username, symbols, upperRSI,
          lowerRSI, multiplier, fraction);
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      System.out.println(name + " was interrupted");
    }
  }
}
