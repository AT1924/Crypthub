package edu.brown.cs.term.thread;

import java.util.HashSet;
import java.util.Set;

import com.binance.api.client.domain.market.CandlestickInterval;

import edu.brown.cs.term.trading.RSINotifier;

public class RSINotifierThread implements Runnable {
  private CandlestickInterval interval = CandlestickInterval.HALF_HOURLY;
  private String username = "js";
  private String name;
  private Set<String> symbols = new HashSet<String>();
  private String rsiLimitHigh;
  private String rsiLimitLow;
  private Thread t;
  private RSINotifier rsiNotifier;

  public RSINotifierThread(String user, String thread, Set<String> symb,
      String highLimit, String lowLimit) {
    username = user;
    name = thread;
    symbols = symb;
    rsiLimitHigh = highLimit;
    rsiLimitLow = lowLimit;
    name = thread;
    t = new Thread(this, name);
    t.start();
  }

  @Override
  public void run() {
    try {
      rsiNotifier = new RSINotifier(interval, username, symbols,
          rsiLimitHigh, rsiLimitLow);
      rsiNotifier.calculate();
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      System.out.println(name + " was interrupted.");
    }
  }

}
