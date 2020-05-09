package edu.brown.cs.term.thread;

import edu.brown.cs.term.database.CrypthubUser;
import websockets.RsiWebSocket;

public class WalletThread implements Runnable {
  private String username;
  Thread t;
  CrypthubUser chUser;

  public WalletThread(String user) {
    username = user;
    chUser = new CrypthubUser();
    t = new Thread(this, username + "wallet");
    t.start();
  }

  @Override
  public void run() {
    try {
      while (true) {
        double total = chUser.totalAmount(username);
        RsiWebSocket.sendWalletUpdate(total, username);
        chUser.insertAmounts(username, total);
        Thread.sleep(3000);
      }
    } catch (InterruptedException e) {
      System.out.println("Wallet Thread was interrupted");
    }
  }

}
