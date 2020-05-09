package edu.brown.cs.term.thread;

import java.util.Map;
import java.util.Set;

import com.binance.api.client.domain.market.CandlestickInterval;

import edu.brown.cs.term.trading.RSICalculator;
import websockets.RsiWebSocket;

public class RsiThread implements Runnable {
  String name;
  Thread t;
  RSICalculator rsi;
  private String username;
  private Set<String> symbols;
  private RsiWebSocket webSocket;

  public RsiThread(String user, String thread, Set<String> symb) {
    name = thread;
    username = user;
    symbols = symb;
    t = new Thread(this, name);
    t.start();
  }

  @Override
  public void run() {
    try {
      rsi = new RSICalculator(CandlestickInterval.HALF_HOURLY, username, symbols);
      rsi.calculate();
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      System.out.println(name + " was interrupted");
    }
  }

  public Map<String, Double> getRSIMap() {
    return rsi.getRSIMap();
  }

}
