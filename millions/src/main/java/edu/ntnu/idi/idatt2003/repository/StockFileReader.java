package edu.ntnu.idi.idatt2003.repository;

import edu.ntnu.idi.idatt2003.model.Stock;
import java.io.IOException;
import java.util.List;

/**
 * Interface for reading stock data from a file.
 * Implementations may support different file formats such as CSV or JSON.
 *
 */
public interface StockFileReader {
  /**
   * Reads all stocks from the given file.
   *
   * @param filename the path to the file to read
   * @return a list of stocks parsed from the file
   * @throws IOException              if the file cannot be read
   * @throws IllegalArgumentException if the filename is null or empty
   */
  List<Stock> readStocksFromFile(String filename) throws IOException;
}
