package edu.ntnu.idi.idatt2003;
import edu.ntnu.idi.idatt2003.model.Stock;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class CsvStockReader implements StockFileReader {
  @Override
  public List<Stock>readStocksFromFile(String filename) throws IOException {
    if(filename == null||filename.trim().isEmpty()) {
    throw new IllegalArgumentException("Filename is null or empty");
    }
    List<Stock> stocks = new ArrayList<>();
    try(BufferedReader reader=new BufferedReader(new FileReader(filename))) {
      String line;
      while((line=reader.readLine())!=null){
        //Skip comments starting with #
        if (line.trim().startsWith("#")) {
          continue;
        }
        if (line.trim().isEmpty()) {
          continue;
        }
        //Process valid line
        Stock stock = parseStockLine(line);
        if (stock != null) {
          stocks.add(stock);
        }
      }
    }
    //File automatically close here
    return stocks;
  }
      private Stock parseStockLine(String line) {
    try {
      String[] parts = line.split(",");//Split by comma
      if(parts.length != 3) {
        return null;
      }

        String symbol = parts[0].trim(); //Example:AAPL
        String name = parts[1].trim(); //Example: 150.00
        BigDecimal price = new BigDecimal(parts[2].trim());
        //Create and return Stock
        return new Stock(symbol,name,price);
      } catch (Exception e) {
      return null;
    }
  }
}















