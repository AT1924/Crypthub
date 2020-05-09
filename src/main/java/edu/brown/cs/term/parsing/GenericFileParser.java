package edu.brown.cs.term.parsing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * GenericFileParser checks to see if a given CSV file is of the correct format,
 * meaning it has the correct header, and it parses a given CSV file into an
 * arary of String, where each line in the CSV is stored as a String.
 *
 * @author Chrissy
 *
 */
public class GenericFileParser {

  private String correctHeader;

  /**
   * Constructor of GenericFileParser. This is used for files that are CSV files
   *
   * @param correctHeader
   *          is the header that the CSV should have
   */
  public GenericFileParser(String correctHeader) {
    this.correctHeader = correctHeader;
  }

  /**
   * This is the overloaded constructor for the GenericFileParser. This is used
   * for files that doesn't need to have a correct header (ie is not a CSV
   * file).
   */
  public GenericFileParser() {
    this.correctHeader = null;
  }

  /**
   * This method checks to see if he inputted header file, is the correct header
   * for this given instance of GenericFileParser.
   *
   * @param headert
   *          is what is passed in and compared to the known correctHeader.
   * @return this returns a boolean stating whether or not the passed in header
   *         (the one of a given CSV file) matches what we have determined to be
   *         a correct header.
   */
  private boolean headerIsCorrect(String header) {
    return correctHeader.equals(header);
  }

  /**
   * This method parses all the lines of a file into an array of String. Also,
   * it first checks if a given file can be found and whether or not the file
   * has the correct header.
   *
   * @param filename
   *          is the path to the CSV file that we are going to read from.
   * @return this returns an ArrayList of type string, where each string is a
   *         line of the CSV.
   */
  public ArrayList<String> parseLinesToStringArray(String filename) {
    ArrayList<String> linesOfCSV = new ArrayList<String>();
    String currLine = "";
    try (BufferedReader bufferedReader = new BufferedReader(
        new FileReader(filename))) {

      /*
       * First check if the header of the CSV file is correct or not, before
       * parsing the other lines.
       */
      currLine = bufferedReader.readLine();
      if (correctHeader != null && !this.headerIsCorrect(currLine)) {
        System.out.println("ERROR: " + filename
            + " misformatted, the header should read: " + correctHeader);
        return null;
      } else {
        if (correctHeader == null) {
          linesOfCSV.add(currLine);
        }
        while ((currLine = bufferedReader.readLine()) != null) {
          linesOfCSV.add(currLine);
        }
      }
    } catch (FileNotFoundException e) {
      System.out.println("ERROR: could not find " + filename);
      return null;
    } catch (IOException e) {
      System.out
          .println("ERROR: something went wrong while reading " + filename);
      return null;
    }

    return linesOfCSV;
  }

}
