package edu.brown.cs.term.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.Account;
import com.binance.api.client.domain.account.AssetBalance;
import com.binance.api.client.exception.BinanceApiException;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.term.connection.DatabaseConnection;
import edu.brown.cs.term.errorhandlers.SQLErrorHandler;
import edu.brown.cs.term.general.Tuple;
import edu.brown.cs.term.trading.Symbols;

public class CrypthubUser {
  private Gson GSON;
  private BinanceApiRestClient client;
  private Set<String> symbols = Symbols.getAllSymbols();

  public CrypthubUser() {
    GSON = new Gson();
  }

  /**
   * Checks if a user exists in the database.
   *
   * @param username
   *          Username of the potential user.
   * @return JSON Object that indicates if a user exists in the database
   *         as well as the username.
   */
  public String checkUser(String username) {
    String st = "SELECT USERNAME FROM USER WHERE USERNAME = ? LIMIT 1";
    try (
        PreparedStatement prep = DatabaseConnection.prepareStatement(st)) {
      prep.setString(1, username);
      ResultSet rs = prep.executeQuery();
      Map<String, Object> exists;
      if (rs.next()) {
        exists = ImmutableMap.of("username", username, "exists", true,
            "error", false);
      } else {
        exists = ImmutableMap.of("username", username, "exists", false,
            "error", false);
      }
      rs.close();
      return GSON.toJson(exists);
    } catch (SQLException e) {
      System.out.println(SQLErrorHandler.getGenericErrorMessage(e));
      return GSON.toJson(ImmutableMap.of("username", username, "exists",
          false, "error", true));
    } catch (Exception e1) {
      System.out.println(e1.getMessage());
      return GSON.toJson(ImmutableMap.of("username", username, "exists",
          false, "error", true));
    }
  }

  /**
   * Adds a user to the database
   *
   * @param username
   *          username of the user
   * @param password
   *          password of the user
   * @param firstName
   *          first name of the user
   * @param lastName
   *          last name of the user
   * @return JSON Object that indicates if the user has been added to
   *         the database
   */
  public String addUser(String username, String password, String firstName,
      String lastName) {
    String st
        = "INSERT INTO USER (FIRSTNAME, LASTNAME, USERNAME, PASSWORD) "
            + "VALUES (?, ?, ?, ?)";
    try (PreparedStatement prep
        = DatabaseConnection.prepareStatement(st);) {
      prep.setString(1, firstName);
      prep.setString(2, lastName);
      prep.setString(3, username);
      prep.setString(4, password);
      prep.addBatch();
      prep.executeBatch();
      prep.close();
      return checkUser(username);
    } catch (SQLException e) {
      return checkUser(username);
    } catch (Exception e1) {
      return checkUser(username);
    }
  }

  /**
   * Adds a user to the database (WITH PHONE NUMBER)
   *
   * @param username
   *          username of the user
   * @param password
   *          password of the user
   * @param firstName
   *          first name of the user
   * @param lastName
   *          last name of the user
   * @param number
   *          phone number of the user
   * @return JSON Object that indicates if the user has been added to
   *         the database
   */
  public String addUser(String username, String password, String firstName,
      String lastName, String number) {
    String st
        = "INSERT INTO USER (FIRSTNAME, LASTNAME, USERNAME, PASSWORD, PHONE) "
            + "VALUES (?, ?, ?, ?, ?)";
    try (
        PreparedStatement prep = DatabaseConnection.prepareStatement(st)) {
      prep.setString(1, firstName);
      prep.setString(2, lastName);
      prep.setString(3, username);
      prep.setString(4, password);
      prep.setString(5, number);
      prep.addBatch();
      prep.executeBatch();
      prep.close();
      return checkUser(username);
    } catch (SQLException e) {
      return checkUser(username);
    } catch (Exception e1) {
      return checkUser(username);
    }
  }

  /**
   * Logins a user.
   *
   * @param username
   *          username of the user
   * @param password
   *          password of the user
   * @return Json object representing if the login exists in the
   *         database
   */
  public String login(String username, String password) {
    String st
        = "SELECT USERNAME, FIRSTNAME, LASTNAME FROM USER WHERE USERNAME = ? AND PASSWORD = ?";
    try (
        PreparedStatement prep = DatabaseConnection.prepareStatement(st)) {
      prep.setString(1, username);
      prep.setString(2, password);
      ResultSet rs = prep.executeQuery();
      if (rs.next()) {
        return GSON.toJson(ImmutableMap.of("username", username,
            "firstname", rs.getString(2), "lastname", rs.getString(3),
            "login_found", true, "error", false));
      }
      return GSON.toJson(ImmutableMap.of("username", username, "firstname",
          "", "lastname", "", "login_found", false, "error", false));
    } catch (SQLException e) {
      System.out.println(SQLErrorHandler.getGenericErrorMessage(e));
      return GSON.toJson(ImmutableMap.of("username", username, "firstname",
          "", "lastname", "", "login_found", false, "error", true));

    } catch (Exception e1) {
      System.out.println(e1.getMessage());
      return GSON.toJson(ImmutableMap.of("username", username, "firstname",
          "", "lastname", "", "login_found", false, "error", true));

    }
  }

  /**
   * Adds a public and private key to the user.
   *
   * @param username
   *          username of the user
   * @param publicKey
   *          public key of the user
   * @return JSON Object indicating that the execution was successful
   */
  public String addPublicKey(String username, String publicKey) {
    String st = "UPDATE USER SET PUBLIC_KEY = ? WHERE USERNAME = ?";
    try (
        PreparedStatement prep = DatabaseConnection.prepareStatement(st)) {
      prep.setString(1, publicKey);
      prep.setString(2, username);
      prep.executeUpdate();
      return GSON
          .toJson(ImmutableMap.of("username", username, "success", true));
    } catch (SQLException e) {
      System.out.println(SQLErrorHandler.getGenericErrorMessage(e));
      return GSON
          .toJson(ImmutableMap.of("username", username, "success", false));
    } catch (Exception e1) {
      System.out.println(e1.getMessage());
      return GSON
          .toJson(ImmutableMap.of("username", username, "success", false));
    }
  }

  /**
   * Adds a public and private key to the user.
   *
   * @param username
   *          username of the user
   * @param privateKey
   *          private key of the user
   * @return JSON Object indicating that the execution was successful
   */
  public String addPrivateKey(String username, String privateKey) {

    String st = "UPDATE USER SET PRIVATE_KEY = ? WHERE USERNAME = ?";
    try (
        PreparedStatement prep = DatabaseConnection.prepareStatement(st)) {
      prep.setString(1, privateKey);
      prep.setString(2, username);
      prep.executeUpdate();
      return GSON.toJson(ImmutableMap.of("success", true));
    } catch (SQLException e) {
      System.out.println(SQLErrorHandler.getGenericErrorMessage(e));
      return GSON.toJson(ImmutableMap.of("success", false));
    } catch (Exception e1) {
      System.out.println(e1.getMessage());
      return GSON.toJson(ImmutableMap.of("success", false));
    }
  }

  /**
   * Adds a public and private key to the user.
   *
   * @param username
   *          username of the user
   * @param publicKey
   *          public key of the user
   * @param privateKey
   *          private key of the user
   * @return JSON Object indicating that the execution was successful
   */
  public String addPublicPrivateKey(String username, String publicKey,
      String privateKey) {

    if (!this.verifyKeys(publicKey, privateKey)) {
      return GSON.toJson(ImmutableMap.of("success", false));
    }

    String st
        = "UPDATE USER SET PUBLIC_KEY = ?, PRIVATE_KEY = ? WHERE USERNAME = ?";
    try {
      PreparedStatement prep = DatabaseConnection.prepareStatement(st);
      prep.setString(1, publicKey);
      prep.setString(2, privateKey);
      prep.setString(3, username);
      prep.executeUpdate();
      prep.close();
      return GSON.toJson(ImmutableMap.of("success", true));
    } catch (SQLException e) {
      return GSON.toJson(ImmutableMap.of("success", false));
    } catch (Exception e) {
      return GSON.toJson(ImmutableMap.of("success", false));
    }
  }

  /**
   * Verify the keys of the user.
   *
   * @param publicKey
   *          Public binance key of the user
   * @param privateKey
   *          private binance key of the user
   * @return true if keys are valid, false if not.
   */
  public boolean verifyKeys(String publicKey, String privateKey) {
    BinanceApiClientFactory fact
        = BinanceApiClientFactory.newInstance(publicKey, privateKey);
    BinanceApiRestClient client = fact.newRestClient();
    try {
      Account acc = client.getAccount();
      return true;
    } catch (BinanceApiException e) {
      return false;
    }
  }

  /**
   * Returns true if the keys are valid.
   *
   * @param publicKey
   *          Public key
   * @param privateKey
   *          Private key
   * @return true if the keys are valid
   */
  public String keysValid(String publicKey, String privateKey) {
    if (verifyKeys(publicKey, privateKey)) {
      return GSON.toJson(ImmutableMap.of("valid", true));
    } else {
      return GSON.toJson(ImmutableMap.of("valid", false));
    }
  }

  /**
   * Gets the public and private key based on a given username
   *
   * @param username
   *          username of the user
   * @return Tuple representing both the public and private key
   */
  public Tuple<String, String> getPublicPrivateKey(String username) {
    if (checkUser(username).contains("\"exists\":false")) {
      return new Tuple<>("", "");
    }

    String st
        = "SELECT PUBLIC_KEY, PRIVATE_KEY FROM USER WHERE USERNAME = ?";
    try {
      PreparedStatement prep = DatabaseConnection.prepareStatement(st);
      prep.setString(1, username);
      ResultSet rs = prep.executeQuery();
      String publicKey = "";
      String privateKey = "";
      if (rs.next()) {
        publicKey = rs.getString(1);
        privateKey = rs.getString(2);
      }
      return new Tuple<>(publicKey, privateKey);
    } catch (SQLException e) {
      return new Tuple<>("", "");
    } catch (Exception e) {
      return new Tuple<>("", "");
    }
  }

  /**
   * Keys exist and valid.
   *
   * @param username
   *          username of the user
   * @return true if the keys exist in the database and are valid
   *         Binance Keys
   */
  public String keysExistAndValid(String username) {
    if (getPublicPrivateKey(username).equals(new Tuple<>("", ""))) {
      return GSON.toJson(
          ImmutableMap.of("keys_exist", false, "keys_valid", false));
    } else {
      Tuple<String, String> keys = getPublicPrivateKey(username);
      if (verifyKeys(keys.getSecondEntry(), keys.getSecondEntry())) {
        return GSON.toJson(
            ImmutableMap.of("keys_exist", true, "keys_valid", true));
      } else {
        return GSON.toJson(
            ImmutableMap.of("keys_exist", true, "keys_valid", true));
      }
    }
  }

  /**
   * Gets the wallet of the users and displays the entire wallet thats
   * greater than 0.
   *
   * @param username
   *          of the user
   * @return JSON containing all the balances of the user.
   */
  public String wallet(String username) {
    if (getPublicPrivateKey(username).equals(new Tuple<>("", ""))) {
      ImmutableMap<String, Object> map
          = ImmutableMap.of("keys_set", false);
      return GSON.toJson(map);
    } else {
      Tuple<String, String> tuple = getPublicPrivateKey(username);
      String public_key = tuple.getFirstEntry();
      String private_key = tuple.getSecondEntry();
      // TODO: check if public/private key is valid
      BinanceApiClientFactory factory
          = BinanceApiClientFactory.newInstance(public_key, private_key);
      client = factory.newRestClient();
      Account acc = client.getAccount();
      List<AssetBalance> assets = acc.getBalances();
      List<AssetBalance> possAssets = new ArrayList<>();
      List<AssetBalance> tradable = new ArrayList<>();
      double totalAmount = 0;
      boolean totalAccurate = true;
      for (AssetBalance asset : assets) {
        if (Double.parseDouble(asset.getFree()) > 0.0001
            || Double.parseDouble(asset.getLocked()) > 0.0001) {
          possAssets.add(asset);
          if (symbols.contains(asset.getAsset() + "ETH")
              || asset.getAsset().equals("ETH")) {
            tradable.add(asset);
          }
          double value = valueOfAsset(asset);
          if (totalAmount == 0) {
            totalAccurate = false;
          }
          totalAmount += value;
        }
      }
      return GSON.toJson(ImmutableMap.of("keys_set", true, "assets",
          possAssets, "tradable", tradable, "total", totalAmount,
          "accurateTotal", totalAccurate));
    }
  }

  /**
   * Gets the total amount of $ in a users wallet
   * @param username
   *          username of a user
   * @return $ amount returned as a double of all the assets in the
   *         account
   */
  public double totalAmount(String username) {
    List<AssetBalance> assets = assetsInWallet(username);
    if (assets.size() == 0) {
      return 0;
    }
    double totalAmount = 0;
    for (AssetBalance asset : assets) {
      totalAmount = totalAmount + valueOfAsset(asset);
    }
    return totalAmount;
  }

  /**
   * Inserts amount of money into a db.
   * @param username
   *          username of the user
   * @param value
   *          amount of money that the user has in their account
   * @return String representing success
   */
  public String insertAmounts(String username, double value) {
    String time = new Timestamp(System.currentTimeMillis()).toString();
    String st
        = "INSERT INTO VALUE (USERNAME, TIME, AMOUNT) VALUES (?, ?, ?);";
    try (
        PreparedStatement prep = DatabaseConnection.prepareStatement(st)) {
      prep.setString(1, username);
      prep.setString(2, time);
      prep.setDouble(3, value);
      prep.addBatch();
      prep.executeBatch();
      return GSON.toJson(ImmutableMap.of("success", true));
    } catch (SQLException e) {
      System.out.println(SQLErrorHandler.getGenericErrorMessage(e));
      return GSON.toJson(ImmutableMap.of("success", false));
    } catch (Exception f) {
      System.out.println(f.getMessage());
      return GSON.toJson(ImmutableMap.of("success", false));
    }
  }

  private double valueOfAsset(AssetBalance asset) {
    if (asset.getAsset().equals("USDT")) {
      return Double.parseDouble(asset.getFree())
          + Double.parseDouble(asset.getLocked());
    } else {
      try {
        String symbol = asset.getAsset() + "USDT";
        return (Double.parseDouble(asset.getFree())
            + Double.parseDouble(asset.getLocked()))
            * Double.parseDouble(client.getPrice(symbol).getPrice());
      } catch (BinanceApiException e) {
        try {
          String symbol = asset.getAsset() + "ETH";
          return (Double.parseDouble(asset.getFree())
              + Double.parseDouble(asset.getLocked()))
              * Double.parseDouble(client.getPrice(symbol).getPrice())
              * Double.parseDouble(client.getPrice("ETHUSDT").getPrice());
        } catch (BinanceApiException f) {
          return 0;
        }
      }
    }
  }

  /**
   * Gets all the symbols in the wallet.
   * @param username
   *          username of the user
   * @return all the symbols that correspond to the users database
   */
  public Set<String> symbolsInWallet(String username) {
    Set<String> possAssets = new HashSet<>();
    if (getPublicPrivateKey(username).equals(new Tuple<>("", ""))) {
      return possAssets;
    } else {
      Tuple<String, String> tuple = getPublicPrivateKey(username);
      String public_key = tuple.getFirstEntry();
      String private_key = tuple.getSecondEntry();
      // TODO: check if public/private key is valid

      BinanceApiClientFactory factory
          = BinanceApiClientFactory.newInstance(public_key, private_key);
      BinanceApiRestClient client = factory.newRestClient();
      Account acc = client.getAccount();

      List<AssetBalance> assets = acc.getBalances();
      possAssets = new HashSet<>();
      for (AssetBalance asset : assets) {
        if (Double.parseDouble(asset.getFree()) > 0.000001
            || Double.parseDouble(asset.getLocked()) > 0.000001) {
          String coin = asset.getAsset();

          if (coin.equals("BTC")) {
            possAssets.add("BTCUSDT");
          } else if (coin.equals("ETH")) {
            possAssets.add("ETHUSDT");
          } else if (coin.equals("USDT")) {
            continue;
          } else {
            possAssets.add(coin + "ETH");
          }
        }
      }
      return possAssets;
    }
  }

  /**
   * gets all the assets in a users wallet and returns a list
   * @param username
   *          username of the user
   * @return all the assets in a users wallet
   */
  public List<AssetBalance> assetsInWallet(String username) {
    if (getPublicPrivateKey(username).equals(new Tuple<>("", ""))) {
      return new ArrayList<>();
    } else {
      Tuple<String, String> tuple = getPublicPrivateKey(username);
      String public_key = tuple.getFirstEntry();
      String private_key = tuple.getSecondEntry();

      BinanceApiClientFactory factory
          = BinanceApiClientFactory.newInstance(public_key, private_key);
      client = factory.newRestClient();
      Account acc = client.getAccount();

      List<AssetBalance> assets = acc.getBalances();
      List<AssetBalance> possAssets = new ArrayList<>();
      for (AssetBalance asset : assets) {
        if (Double.parseDouble(asset.getFree()) > 0.000001
            || Double.parseDouble(asset.getLocked()) > 0.000001) {
          possAssets.add(asset);
        }
      }
      return possAssets;
    }
  }

  /**
   * Gets the historical wallet amount over time of the user
   * @param username
   *          username of the user
   * @param numOfDataPoints
   *          number of entries you want to receive
   * @return String representing the the wallet value over time
   */
  public String historicalWalletValue(String username,
      int numOfDataPoints) {
    String st
        = "SELECT * FROM VALUE WHERE USERNAME = ? ORDER BY TIME DESC LIMIT ?;";
    try (
        PreparedStatement prep = DatabaseConnection.prepareStatement(st)) {
      prep.setString(1, username);
      prep.setInt(2, numOfDataPoints);
      ResultSet rs = prep.executeQuery();
      List<Amount> amounts = new ArrayList<>();
      while (rs.next()) {
        amounts.add(new Amount(rs.getString(2), rs.getDouble(3)));
      }
      rs.close();
      return GSON.toJson(ImmutableMap.of("success", true, "username",
          username, "amounts", amounts));
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return GSON.toJson(ImmutableMap.of("success", false));
    }
  }

  /**
   * Class for getting the amount from a user.
   */
  private class Amount {
    private String time;
    private double value;

    public Amount(String t, double v) {
      this.time = t;
      this.value = v;
    }
  }

}
