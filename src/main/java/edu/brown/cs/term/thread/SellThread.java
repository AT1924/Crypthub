package edu.brown.cs.term.thread;

import edu.brown.cs.term.trading.FullTradeSell;

public class SellThread implements Runnable {
  String name;
  Thread t;
  String symbol;
  String sellPrice;
  String buyPrice;
  String stopPrice;
  String quantity;
  String username;
  public SellThread(String user, String thread, String symb, String sP,
      String bP, String stopP, String quant) {
    name = thread;
    symbol = symb;
    sellPrice = sP;
    buyPrice = bP;
    stopPrice = stopP;
    quantity = quant;
    username = user;
    t = new Thread(this, name);
    t.start();
  }
  
  public void run() {
    try {
      FullTradeSell fts = new FullTradeSell(username);
      fts.fullTrade(symbol, sellPrice, buyPrice, stopPrice, quantity);
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      System.out.println(name + " was interrupted.");
    }
  }
  public void setSellPrice(String sp) {
    sellPrice = sp;
  }

  public void setBuyPrice(String bp) {
    buyPrice = bp;
  }
  
  public void setStopPrice(String stop) {
    stopPrice = stop;
  }
  
  public void setQuantity(String quant) {
    quantity = quant;
  }

}
