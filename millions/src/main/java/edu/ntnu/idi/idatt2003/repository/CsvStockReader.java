package edu.ntnu.idi.idatt2003.repository;

import edu.ntnu.idi.idatt2003.model.Stock;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads stock data from a comma-separated values (CSV) file.
 * Implements {@link StockFileReader} to provide CSV-specific file reading.
 * Lines starting with {@code #} are treated as comments and skipped.
 * Blank lines are also skipped. Each valid line must follow the format:
 * {@code symbol, company name, price}.
 *
 */
public class CsvStockReader implements StockFileReader {

  /**
   * Reads all stocks from the given CSV file.
   * Skips comment lines starting with {@code #} and blank lines.
   * Malformed lines are silently ignored.
   *
   * @param filename the path to the CSV file to read
   * @return a list of stocks parsed from the file, empty if none found
   * @throws IOException              if the file cannot be read
   * @throws IllegalArgumentException if the filename is null or empty
   */
  @Override
  public List<Stock> readStocksFromFile(String filename) throws IOException {
    if (filename == null || filename.trim().isEmpty()) {
      throw new IllegalArgumentException("Filename is null or empty");
    }
    List<Stock> stocks = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      String line;
      while ((line = reader.readLine()) != null) {
        // Skip comments starting with #
        if (line.trim().startsWith("#")) {
          continue;
        }
        if (line.trim().isEmpty()) {
          continue;
        }
        // Process valid line
        Stock stock = parseStockLine(line);
        if (stock != null) {
          stocks.add(stock);
        }
      }
    }
    // File automatically close here
    return stocks;
  }
  /**
   * Parses a single CSV line into a Stock object.
   * The line must contain exactly three comma-separated values:
   * symbol, company name and price.
   * Returns null if the line is malformed or cannot be parsed.
   *
   * @param line the CSV line to parse
   * @return a Stock object, or null if the line is invalid
   */

  private Stock parseStockLine(String line) {
    try {
      String[] parts = line.split(","); // Split by comma
      if (parts.length != 3) {
        return null;
      }

      String symbol = parts[0].trim(); // Example:AAPL
      String name = parts[1].trim(); // Example: 150.00
      BigDecimal price = new BigDecimal(parts[2].trim());
      // Create and return Stock
      return new Stock(symbol, name, price);
    } catch (Exception e) {
      return null;
    }
  }
}















