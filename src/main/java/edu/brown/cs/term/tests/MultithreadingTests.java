package edu.brown.cs.term.tests;

import edu.brown.cs.term.connection.DatabaseConnection;
import edu.brown.cs.term.thread.WalletThread;
import org.junit.Before;
import org.junit.Test;

public class MultithreadingTests {
  @Before
  public void setup(){
    DatabaseConnection.setConnection("data/db.sqlite3");
  }

  @Test
  public void WalletTest(){
  }
}
