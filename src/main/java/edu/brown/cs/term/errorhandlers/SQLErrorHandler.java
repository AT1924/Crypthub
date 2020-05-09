package edu.brown.cs.term.errorhandlers;

import java.sql.SQLException;

/**
 * This class is used by all the classes the query the database, ActorTable,
 * FilmTable, DatabaseConnection, etc. to produce a generic SQL error message
 * from the passed in SQLException e.
 * @author Chrissy
 *
 */
public abstract class SQLErrorHandler {

  /**
   * This method takes in a SQLException and returns a generic error message.
   * @param e
   *          is the SQLException this method is getting an error message for.
   * @return a String that is the generic SQL error message.
   */
  public static String getGenericErrorMessage(SQLException e) {
    String errorMessage = e.getMessage().replace("[SQLITE_ERROR] ", "");
    return "ERROR: " + errorMessage;
  }
}
