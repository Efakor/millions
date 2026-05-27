package edu.ntnu.idi.idatt2003.repository;

import edu.ntnu.idi.idatt2003.model.Stock;
import java.io.IOException;
import java.util.List;

/**
 * Interface for writing stock data to a file.
 * Implementations may support different file formats such as CSV or JSON.
 *
 */
public interface StockFileWriter {
  /**
   * Writes all stocks to the given file
   * @param filename filename the path to the file to write
   * @param stocks  the list of stocks to write
   * @throws IOException the file cannot be written
   * @throws IllegalArgumentException if filename is null or empty,
   */

  void writeStocksToFile(String filename, List<Stock> stocks) throws IOException;
}
