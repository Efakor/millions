package edu.ntnu.idi.idatt2003.repository;
import edu.ntnu.idi.idatt2003.model.Stock;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvStockWriter implements StockFileWriter {

  @Override
  public void writeStocksToFile(String filename,List<Stock> stocks) throws IOException {
    if (filename == null || (filename.trim().isEmpty())){
      throw new IllegalArgumentException("filename is null or empty");
    }
    if(stocks==null){
      throw new IllegalArgumentException("stocks is null");
    }


    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
      //write headers
      writer.write("#Stock data");
      writer.newLine();
      writer.write("Symbol,Name,Price");
      writer.newLine();
      for(Stock stock : stocks){
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
