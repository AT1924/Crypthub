package edu.brown.cs.term.tests;

import edu.brown.cs.term.connection.DatabaseConnection;
import edu.brown.cs.term.database.Trades;
import edu.brown.cs.term.database.CrypthubUser;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.assertEquals;

public class DatabaseTests {

  @Before
  public void connect(){
    DatabaseConnection.setConnection("data/db.sqlite3");
  }

  /**
   * Checks if we can add a user successfully to the database.
   */
  @Test
  public void checkAndAddUser(){
    CrypthubUser user = new CrypthubUser();
    int i = 0;
    StringBuilder sb = new StringBuilder();
    String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    while(i<10){
      sb.append(characterSet.charAt((int)(Math.random()*characterSet.length())));
      i++;
    }
    assertTrue(user.checkUser(sb.toString()).contains("\"exists\":false"));
    user.addUser(sb.toString(), "pass", "John", "Smith");
    assertTrue(user.checkUser(sb.toString()).contains("\"exists\":true"));
  }

  /**
   * Checks if a user exists in the database.
   */
  @Test
  public void checkUser(){
    CrypthubUser user = new CrypthubUser();
    int i = 0;
    StringBuilder sb = new StringBuilder();
    String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    while(i<10){
      sb.append(characterSet.charAt((int)(Math.random()*characterSet.length())));
      i++;
    }
    user.addUser(sb.toString(), "pass", "John", "Smith");
    assertTrue(user.login(sb.toString(), "pass").contains("\"login_found\":true"));
    assertTrue(user.login(sb.toString(), "pass1").contains("\"login_found\":false"));
  }

  /**
   * Tests adding a public and private key to the DB.
   */
  @Test
  public void addPublicPrivate(){
    CrypthubUser user = new CrypthubUser();
    int i = 0;
    StringBuilder sb = new StringBuilder();
    String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    while(i<10){
      sb.append(characterSet.charAt((int)(Math.random()*characterSet.length())));
      i++;
    }
    user.addUser(sb.toString(), "pass", "John", "Smith");
    assertTrue(user.addPublicPrivateKey(sb.toString(), "test1",
            "test2").contains("\"success\":true"));
    assertTrue(user.addPublicPrivateKey("22", "test1",
            "test2").contains("\"success\":false"));
  }

  /**
   * tests getting a public and private key from the database.
   */
  @Test
  public void getPublicPrivate(){
    CrypthubUser user = new CrypthubUser();
    System.out.println(user.getPublicPrivateKey("22BCWTTWTE").toString());
  }

  /**
   * Tests getting the wallet information for all the assets above 0.
   */
  @Test
  public void getWalletInformation(){
    CrypthubUser user = new CrypthubUser();
    assertTrue(user.wallet("js").contains("\"assets\":\"ETH\""));
  }

  @Test
  public void insertValueIntoValueDatabase(){
    CrypthubUser user = new CrypthubUser();
    assertTrue(user.insertAmounts("js",
            user.totalAmount("js")).contains("\"success\":true"));
  }


}
