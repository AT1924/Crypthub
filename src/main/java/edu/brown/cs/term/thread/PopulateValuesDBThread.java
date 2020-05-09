package edu.brown.cs.term.thread;

import edu.brown.cs.term.database.CrypthubUser;
import edu.brown.cs.term.repl.Main;

public class PopulateValuesDBThread implements Runnable {
  private String username;
  private CrypthubUser chUser;
  Thread t;

  public PopulateValuesDBThread(String user) {
    username = user;
    chUser = new CrypthubUser();
    Main.threadMap.add(user + " populator");
    t = new Thread(this, user + " populator");
    t.start();
  }

  @Override
  public void run() {
    try {
      chUser.insertAmounts(username, chUser.totalAmount(username));
      while (true) {
        chUser.insertAmounts(username, chUser.totalAmount(username));
        Thread.sleep(5000);
      }
    } catch (InterruptedException e) {
      System.out.println("ERROR: Interrupted thread");
    }
  }
}
