package edu.brown.cs.term.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.binance.api.client.domain.account.Order;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.term.connection.DatabaseConnection;
import edu.brown.cs.term.errorhandlers.SQLErrorHandler;
import websockets.RsiWebSocket;

public class Trades {
  private static Gson GSON;

  public Trades() {
    GSON = new Gson();
  }

  /**
   * Adds a trade to the database.
   * @param username
   *          username of user
   * @param ord
   *          order
   * @return GSON representing whether adding the username was a success
   */
  public int addTrade(String username, Order ord) {
    float price = Float.parseFloat(ord.getPrice());
    StringBuilder sb = new StringBuilder();
    sb.append(ord.getType());
    sb.append(" ");
    sb.append(ord.getSide());
    String type = sb.toString();
    float quantity = Float.parseFloat(ord.getOrigQty());
    String symbol = ord.getSymbol();
    String status = ord.getStatus().toString();
    int orderId = new Long(ord.getOrderId()).intValue();

    String time = new Timestamp(System.currentTimeMillis()).toString();
    String st
        = "INSERT INTO TRADES (PRICE, TYPE, QUANTITY, SYMBOL, ORDER_TIME, ORDER_ID, USERNAME, STATUS) "
            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
    try (
        PreparedStatement prep = DatabaseConnection.prepareStatement(st)) {
      prep.setFloat(1, price);
      prep.setString(2, type);
      prep.setFloat(3, quantity);
      prep.setString(4, symbol);
      prep.setString(5, time);
      prep.setInt(6, orderId);
      prep.setString(7, username);
      prep.setString(8, status);
      prep.addBatch();
      prep.executeBatch();

      RsiWebSocket.sendCustomTradingUpdateMessage(username, ord.getOrderId(),
          "Welcome to your new trade");

      return orderId;
    } catch (SQLException e) {
      System.out.println(SQLErrorHandler.getGenericErrorMessage(e));
      return 0; // TODO throw an exception.
    } catch (Exception e) {
      return 0;
    }
  }

  public String getTrade(String username, Order order) {
    String st = "SELECT * FROM TRADES WHERE USERNAME = ? AND ORDER_ID = ?";
    try (
        PreparedStatement prep = DatabaseConnection.prepareStatement(st)) {
      prep.setString(1, username);
      prep.setLong(2, new Long(order.getOrderId()).intValue());
      ResultSet rs = prep.executeQuery();

      if (rs.next()) {
        Map<String, Object> trade
            = buildJSONTrade(rs.getFloat(1), rs.getString(2),
                rs.getFloat(3), rs.getString(4), rs.getString(5),
                rs.getInt(6), rs.getString(7), rs.getString(8), true);
        return GSON.toJson(trade);
      }
      Map<String, Object> trade = buildJSONTrade(0, "", 0, "",
          new Timestamp(System.currentTimeMillis()).toString(), 0,
          username, "", false);
      rs.close();
      return GSON.toJson(trade);
    } catch (SQLException e) {
      Map<String, Object> trade = buildJSONTrade(0, "", 0, "",
          new Timestamp(System.currentTimeMillis()).toString(), 0,
          username, "", false);
      return GSON.toJson(trade);
    } catch (Exception e) {
      Map<String, Object> trade = buildJSONTrade(0, "", 0, "",
          new Timestamp(System.currentTimeMillis()).toString(), 0,
          username, "", false);
      return GSON.toJson(trade);
    }
  }

  private Map<String, Object> buildJSONTrade(float price, String type,
      float quantity, String symbol, String time, int order_id,
      String username, String status, boolean success) {
    Map<String, Object> map = new ImmutableMap.Builder<String, Object>()
        .put("price", price).put("type", type).put("quantity", quantity)
        .put("symbol", symbol).put("time", time).put("order_id", order_id)
        .put("username", username).put("status", status)
        .put("success", success).build();
    return map;
  }

  /**
   * Gets all the trades from the user
   * @param username
   *          username of the user
   * @return JSON representing all the trades
   */
  public List<Map<String, Object>> getAllTrades(String username) {
    String st
        = "SELECT PRICE, TYPE, QUANTITY, SYMBOL, ORDER_TIME, ORDER_ID, USERNAME, STATUS FROM TRADES WHERE USERNAME = ? ORDER BY ORDER_TIME DESC";
    List<Map<String, Object>> trades = new ArrayList<>();
    try (
        PreparedStatement prep = DatabaseConnection.prepareStatement(st)) {
      prep.setString(1, username);
      ResultSet rs = prep.executeQuery();
      while (rs.next()) {

        Map<String, Object> trade
            = buildJSONTrade(rs.getFloat(1), rs.getString(2),
                rs.getFloat(3), rs.getString(4), rs.getString(5),
                rs.getInt(6), rs.getString(7), rs.getString(8), true);
        trades.add(trade);
      }
      rs.close();
      return trades;
    } catch (SQLException e) {
      System.out.println(SQLErrorHandler.getGenericErrorMessage(e));
      return trades;
    } catch (Exception e1) {
      System.out.println(e1.getMessage());
      return trades;
    }
  }

  /**
   * Modifies the trade status of an order for a user.
   * @param username
   *          username of the user
   * @param order
   *          order of the user
   * @return JSON String representing success and failure
   */
  public String updateTrade(String username, Order ord, String message) {
    float price = Float.parseFloat(ord.getPrice());
    StringBuilder sb = new StringBuilder();
    sb.append(ord.getType());
    sb.append(" ");
    sb.append(ord.getSide());
    String type = sb.toString();
    float quantity = Float.parseFloat(ord.getOrigQty());
    String symbol = ord.getSymbol();
    String status = ord.getStatus().toString();
    int orderId = new Long(ord.getOrderId()).intValue();

    String time = new Timestamp(System.currentTimeMillis()).toString();

    String st
        = "UPDATE TRADES SET PRICE = ?, SET TYPE = ?, SET QUANTITY = ?, SET SYMBOL = ?, SET ORDER_TIME = ?, SET STATUS = ? WHERE USERNAME = ? AND ORDER_ID = ?";
    try (
        PreparedStatement prep = DatabaseConnection.prepareStatement(st)) {
      prep.setFloat(1, price);
      prep.setString(2, type);
      prep.setFloat(3, quantity);
      prep.setString(4, symbol);
      prep.setString(5, time);
      prep.setString(6, status);
      prep.setString(7, username);
      prep.setInt(8, orderId);
      prep.executeUpdate();

      Map<String, Object> trade = this.buildJSONTrade(price, type,
          quantity, symbol, time, orderId, username, status, true);

      RsiWebSocket.sendCustomTradingUpdateMessage(username, ord.getOrderId(),
          message);

      return GSON.toJson(ImmutableMap.of("success", true));
    } catch (SQLException e) {
      return GSON.toJson(ImmutableMap.of("success", false));
    } catch (Exception e) {
      return GSON.toJson(ImmutableMap.of("success", false));
    }
  }

  /**
   * Modifies the trade status of an order for a user.
   * @param username
   *          username of the user
   * @param order
   *          order of the user
   * @return JSON String representing success and failure
   */
  public String modifyTradeStatus(String username, int orderId,
      String status) {
    String st
        = "UPDATE TRADES SET STATUS = ? WHERE USERNAME = ? AND ORDER_ID = ?";
    try (
        PreparedStatement prep = DatabaseConnection.prepareStatement(st)) {
      prep.setString(1, status);
      prep.setString(2, username);
      prep.setInt(3, orderId);
      prep.executeUpdate();
      return GSON.toJson(ImmutableMap.of("success", true));
    } catch (SQLException e) {
      return GSON.toJson(ImmutableMap.of("success", false));
    } catch (Exception e) {
      return GSON.toJson(ImmutableMap.of("success", false));
    }
  }

}
