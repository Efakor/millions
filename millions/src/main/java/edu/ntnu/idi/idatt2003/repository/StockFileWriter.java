package edu.ntnu.idi.idatt2003.repository;
import edu.ntnu.idi.idatt2003.model.Stock;
import java.io.IOException;
import java.util.List;

public interface StockFileWriter {
  void writeStocksToFile(String filename,List<Stock> stocks) throws IOException;
}
