package edu.brown.cs.term.thread;

import edu.brown.cs.term.trading.FullTradeBuy;

public class BuyThread implements Runnable {
  String name;
  Thread t;
  String symbol;
  String buyPrice;
  String sellPrice;
  String stopPrice;
  String quantity;
  String username;

  public BuyThread(String user, String thread, String symb, String bP,
      String sP, String stopP, String quant) {
    username = user;
    name = thread;
    symbol = symb;
    sellPrice = sP;
    buyPrice = bP;
    stopPrice = stopP;
    quantity = quant;
    t = new Thread(this, name);
    t.start();
  }

  @Override
  public void run() {
    try {
      FullTradeBuy ftb = new FullTradeBuy(username);
      ftb.fullTrade(symbol, buyPrice, sellPrice, stopPrice, quantity);
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
