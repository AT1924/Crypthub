package edu.brown.cs.term.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import edu.brown.cs.term.errorhandlers.SQLErrorHandler;

/**
 * Reads the database and returns the connection from a given
 * filepath.
 */
public final class ReadDatabase {

  private ReadDatabase() {
  }

  /**
   * Sets up a connection with the database.
   *
   * @param filepath
   *          filpath to the database file
   * @return Connection if it is found, null if it is not found.
   */
  public static Connection setupConnection(String filepath) {
    if (!isValidDb(filepath)) {
      System.out.println("ERROR: database is not valid");
      return null;
    }
    try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException e) {
      System.out.println("ERROR: JDBC class not found");
      return null;
    }
    String urlToDB = "jdbc:sqlite:" + filepath;
    try {
      Connection conn = DriverManager.getConnection(urlToDB);
      Statement stat = conn.createStatement();
      stat.executeUpdate("PRAGMA foreign_keys = ON;");
      return conn;
    } catch (SQLException e) {
      System.out.println(SQLErrorHandler.getGenericErrorMessage(e));
      return null;
    }
  }

  /**
   * Returns true if the filepath is a valid filepath to a database.
   * False if it is not.
   *
   * @param filepath
   *          possible filepath to the database.
   * @return true if the filepath points to a valid db file.
   */
  public static boolean isValidDb(String filepath) {
    File f = new File(filepath);
    final String extension = ".sqlite3";
    if (!f.exists() || filepath.length() <= extension.length()
        || filepath.substring(filepath.length() - extension.length() - 1)
            .equals(extension)) {
      return false;
    }
    return true;
  }
}
