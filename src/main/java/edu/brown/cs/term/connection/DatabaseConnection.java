package edu.brown.cs.term.connection;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import edu.brown.cs.term.errorhandlers.SQLErrorHandler;

/**
 * This is the class for DatabaseConnection. It models a connection to
 * a database which can created PreparedStatements and set new
 * connections.
 *
 * @author Chrissy
 */
public abstract class DatabaseConnection {

  private static Connection conn = null;

  /**
   * This method creates PreparedStatements from a passed in sql string.
   *
   * @param sql
   *          is the sql string that will be used to create
   *          PreparedStatements.
   * @return the PreparedStatement created from the passed in sql
   *         String.
   * @throws Exception
   *           throws the exception.
   */
  public static PreparedStatement prepareStatement(String sql)
      throws Exception {
    if (connectionIsEstablished()) {
      try {
        return conn.prepareStatement(sql);
      } catch (SQLException e) {
        throw new DatabaseException(
            SQLErrorHandler.getGenericErrorMessage(e));
      }
    } else {
      throw new DatabaseException(
          "ERROR: no database is currently connected, please "
              + "first load in a database");
    }

  }

  /**
   * This method sets the connection for a DatabaseConnection. It first
   * checks to make sure the passed in database file exists. Then it
   * uses DriveManager to get a connection from the database.
   *
   * @param db
   *          is the String that is the path to the database that will
   *          be set to
   */
  public static void setConnection(String db) {
    conn = null;
    File tmpFile = new File(db);
    if (!tmpFile.exists() || tmpFile.isDirectory()) {
      System.out
          .println("ERROR: " + db + " SQL database file does not exist");
      return;
    }

    try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException e) {
      System.out.println("ERROR: Something went wrong finding JDBC class");
    }
    String urlToDB = "jdbc:sqlite:" + db;
    try {
      conn = DriverManager.getConnection(urlToDB);
    } catch (SQLException e) {
      System.out.println(
          "ERROR: Something went wrong when trying to establish a connection");
    }
  }

  /**
   * This method determines whether or not the DatabaseConnection has
   * been established or not.
   *
   * @return true if the DatabaseConnection was established and false
   *         otherwise.
   */
  public static boolean connectionIsEstablished() {
    try {
      return conn != null && !conn.isClosed();
    } catch (SQLException e) {
      System.out.println(
          "ERROR: Something went wrong when trying to check if the connection "
              + "is closed");
      return false;
    }
  }

  /**
   * This method closes the Database Connection.
   */
  public static void closeConnection() {
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        System.out.println(
            "ERROR: Something went wrong when closing the connection");
      }
    }
  }

}
