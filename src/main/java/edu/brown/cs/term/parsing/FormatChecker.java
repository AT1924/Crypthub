package edu.brown.cs.term.parsing;

/**
 * This abstract class holds methods that check strings for the correct format.
 *
 * @author Chrissy
 *
 */
public abstract class FormatChecker {

  /**
   * This method checks to see if a given String contains only digits and
   * decimals, and also accepts Strings that have the negative sign.
   *
   * @param input
   *          is the String that the function checks if it only contains digits
   *          and decimals and possibly a negative sign.
   *
   * @return this method returns true if input contains only digits and
   *         decimals, and false otherwise.
   */
  public static boolean stringIsANumber(String input) {
    return input.matches("-?\\d+(\\.\\d+)?");
  }

  /**
   * This method checks to see if a given String is a non-negative integer,
   * meaning it only contains digits from 0 to 9.
   *
   * @param input
   *          is the String that will be checked to be an non-negative integer.
   * @return this method returns true if input is a non-negative integer, and
   *         false otherwise.
   */
  public static boolean stringIsNonNegativeInteger(String input) {
    return input.matches("[0-9]+");
  }

  /**
   * This method checks if a given input String is a non-negative number,
   * meaning it can contain decimals and digits, but not a negative sign or any
   * other characters.
   *
   * @param input
   *          is the String that will be checked to be an non-negative number.
   * @return this method will return true if the input is a non-negative number
   *         and false otherwise.
   */
  public static boolean stringIsNonNegativeNumber(String input) {
    return input.matches("\\d*\\.?\\d+");
  }
}
