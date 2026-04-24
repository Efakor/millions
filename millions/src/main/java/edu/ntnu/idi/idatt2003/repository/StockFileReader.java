package edu.ntnu.idi.idatt2003.repository;

import edu.ntnu.idi.idatt2003.model.Stock;
import java.io.IOException;
import java.util.List;

/**
 * .
 */
public interface StockFileReader {
  /**
   *
   * @param filename .
   * @return .
   * @throws IOException .
   */
  List<Stock> readStocksFromFile(String filename) throws IOException;
}
