package edu.ntnu.idi.idatt2003.repository;

import edu.ntnu.idi.idatt2003.model.Stock;
import java.io.IOException;
import java.util.List;

/**
 * Interface for reading stock data from a file.
 * Implementations handle specific file formats such as CSV.
 */
public interface StockFileReader {

  /**
   * Reads stock data from the specified file and returns a list of stocks.
   * Lines starting with # and blank lines are ignored.
   *
   * @param filename the path to the file to read
   * @return a list of stocks parsed from the file
   * @throws IOException if the file cannot be read or does not exist
   */

  List<Stock> readStocksFromFile(String filename) throws IOException;
}
