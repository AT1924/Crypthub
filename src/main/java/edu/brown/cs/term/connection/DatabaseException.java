package edu.brown.cs.term.connection;

/**
 * This is the DatabaseException class. It handles exceptions that occur when
 * creating a prepared statements from the connection.
 * @author Chrissy
 *
 */
public class DatabaseException extends Exception {

  /**
   * This is the constructor for a DatabaseException that takes in no
   * parameters.
   */
  public DatabaseException() {
    super();
  }

  /**
   * This is the constructor for a DatabaseConnection that will take in the
   * error message for the exception.
   * @param message
   *          is the informative error message that describes the exception.
   */
  public DatabaseException(String message) {
    super(message);
  }
}
