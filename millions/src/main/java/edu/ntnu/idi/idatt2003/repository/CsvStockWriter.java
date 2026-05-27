package edu.ntnu.idi.idatt2003.repository;

import edu.ntnu.idi.idatt2003.model.Stock;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Writes stock data to a comma-separated values (CSV) file.
 * Implements {@link StockFileWriter} to provide CSV-specific file writing.
 * Each stock is written on a separate line in the format:
 * {@code symbol, company name, price}.
 *
 */
public class CsvStockWriter implements StockFileWriter {
  /**
   * Writes all stocks to the given CSV file.
   * Each stock is written as a single comma-separated line
   * containing the symbol, company name and current sales price.
   * The file is created if it does not exist and overwritten if it does.
   *
   * @param filename the path to the CSV file to write
   * @param stocks   the list of stocks to write
   * @throws IOException              if the file cannot be written
   * @throws IllegalArgumentException if filename is null or empty,
   *                                  or if stocks is null
   */

  @Override
  public void writeStocksToFile(String filename, List<Stock> stocks) throws IOException {
    if (filename == null || (filename.trim().isEmpty())) {
      throw new IllegalArgumentException("filename is null or empty");
    }
    if (stocks == null) {
      throw new IllegalArgumentException("stocks is null");
    }


    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {

      for (Stock stock : stocks) {
        String line = String.format("%s,%s,%s",
            stock.getSymbol(),
            stock.getCompany(),
            stock.getSalesPrice());
        writer.write(line);
        writer.newLine();
      }
    }
  }
}
