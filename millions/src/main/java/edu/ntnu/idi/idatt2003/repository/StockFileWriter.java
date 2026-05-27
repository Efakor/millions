package edu.ntnu.idi.idatt2003.repository;

import edu.ntnu.idi.idatt2003.model.Stock;
import java.io.IOException;
import java.util.List;

/**
 * Interface for writing stock data to a file.
 * Implementations handle specific file formats such as CSV.
 */
public interface StockFileWriter {

  /**
   * Writes the given list of stocks to the specified file.
   * Each stock is written on a separate line in the format symbol,name,price.
   *
   * @param filename the path to the file to write to
   * @param stocks   the list of stocks to write
   * @throws IOException if the file cannot be written or created
   */

  void writeStocksToFile(String filename, List<Stock> stocks) throws IOException;
}
