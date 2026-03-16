package edu.ntnu.idi.idatt2003.repository;
import edu.ntnu.idi.idatt2003.model.Stock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvStockWriterTest {
  private CsvStockWriter stockWriter;
  @BeforeEach
  void setup() {
    stockWriter=new CsvStockWriter();
  }
  @Nested
  class WriteValidTests{
    @Test
    void writeMultipleStocksToFile (@TempDir Path tempDir) throws IOException {
      //Arrange
      Path csvFile=tempDir.resolve("stocks.csv");
      List<Stock> stocks=new ArrayList<>();
      stocks.add(new Stock("AAPL","Apple Inc.",new BigDecimal("150.00")));
      stocks.add(new Stock("GOOGL","Alphabet Inc.",new BigDecimal("2800.00")));
      stocks.add(new Stock("MSFT","Microsoft.",new BigDecimal("300.00")));

      //Act
      stockWriter.writeStocksToFile(csvFile.toString(),stocks);
      String content=Files.readString(csvFile);
      //Assert
      String expected= """
          AAPL,Apple Inc.,150.00
          GOOGL,Alphabet Inc.,2800.00
          MSFT,Microsoft.,300.00
          """;
      assertEquals(expected,content);

    }
    @Test
    void writesSingleStockToFile(@TempDir Path tempDir) throws IOException {
      //Arrange
      Path csvFile=tempDir.resolve("stocks.csv");
      List<Stock> stocks=List.of(new Stock("AAPL","Apple Inc.",new BigDecimal("150.00")));
      //Act
      stockWriter.writeStocksToFile(csvFile.toString(),stocks);
      String content=Files.readString(csvFile);
      String expected= """
      AAPL,Apple Inc.,150.00
      """;
      //Assert
      assertEquals(expected,content);
    }
    @Test
    void writeEmptyListCreateEmptyFile(@TempDir Path tempDir) throws IOException {
      //Arrange
      Path csvFile=tempDir.resolve("empty.csv");
      List<Stock> stocks=new ArrayList<>();
      String expected="";
      //Act
      stockWriter.writeStocksToFile(csvFile.toString(),stocks);
      String content=Files.readString(csvFile);

      //Assert
      assertEquals(expected,content);
    }
    @Test
    void fileCorrectNumberOfLines(@TempDir Path tempDir) throws IOException {
      //Arrange
      Path csvFile=tempDir.resolve("stocks.csv");
      List<Stock> stocks=new ArrayList<>();
      stocks.add(new Stock("AAPL","Apple Inc.",new BigDecimal("150.00")));
      stocks.add(new Stock("GOOGL","Alphabet Inc.",new BigDecimal("2800.00")));
      stocks.add(new Stock("MSFT","Microsoft.",new BigDecimal("300.00")));
      //Act
      stockWriter.writeStocksToFile(csvFile.toString(),stocks);
      List<String> content=Files.readAllLines(csvFile);
      //Assert
      assertEquals(3,content.size());
    }
    @Test
    void writeCorrectDecimalPrices(@TempDir Path tempDir) throws IOException {
      //Arrange
      Path csvFile=tempDir.resolve("correct_prices.csv");
      List<Stock> stocks=List.of(new Stock("AAPL","Apple Inc.",new BigDecimal("150.55")));
      String expected="AAPL,Apple Inc.,150.55";
      //Act
      stockWriter.writeStocksToFile(csvFile.toString(),stocks);
      String content=Files.readString(csvFile);
      //Assert

      assertEquals(expected,content);
    }


  }
  @Nested
  class ValidationTests{
    @Test
    void throwExceptionFileNameIsNull() {
      //Arrange
      List<Stock> stocks=List.of(new Stock("AAPL","Apple Inc.",new BigDecimal("150.00")));
      //Act & Assert
      assertThrows(IllegalArgumentException.class, () -> stockWriter.writeStocksToFile(null,stocks));

    }
    @Test
    void throwExceptionFileNameIsEmpty() {
      //Arrange
      List<Stock> stocks=List.of(new Stock("AAPL","Apple Inc.",new BigDecimal("150.00")));
      //Act & Assert
      assertThrows(IllegalArgumentException.class, () -> stockWriter.writeStocksToFile("",stocks));

    }
    @Test
    void throwExceptionFileNameIsBlank() {
      //Arrange
      List<Stock> stocks=List.of(new Stock("AAPL","Apple Inc.",new BigDecimal("150.00")));
      assertThrows(IllegalArgumentException.class, () -> stockWriter.writeStocksToFile("   ",stocks));

    }
    @Test
    void throwExceptionStockListIsNull() {
      //Arrange
      String filename="stocks.csv";
      //Act & Assert
      assertThrows(IllegalArgumentException.class, () -> stockWriter.writeStocksToFile(filename,null));
    }
  }
  @Nested
  class RoundTripTest {
    @Test
    void writeFileCanBeReadCorrect(@TempDir Path tempDir) throws IOException {
      //Arrange
      Path csvFile = tempDir.resolve("stocks.csv");
      List<Stock> orginalStocks=new ArrayList<>();
      orginalStocks.add(new Stock("AAPL","Apple Inc.",new BigDecimal("150.00")));
      orginalStocks.add(new Stock("GOOGL","Alphabet Inc.",new BigDecimal("2800.00")));

      CsvStockReader stockReader=new CsvStockReader();
      //Act
      stockWriter.writeStocksToFile(csvFile.toString(),orginalStocks);
      List<Stock> readStocks=stockReader.readStocksFromFile(csvFile.toString());
      //Assert
      assertEquals(orginalStocks.size(),readStocks.size());
      assertEquals("AAPL",readStocks.get(0).getSymbol());
      assertEquals("Apple Inc.",readStocks.get(0).getCompany());
      assertEquals("GOOGL",readStocks.get(1).getSymbol());
      assertEquals("Alphabet Inc.",readStocks.get(1).getCompany());
    }
    @Test
    void emptyListTest(@TempDir Path tempDir) throws IOException {
      //Arrange
      Path csvFile=tempDir.resolve("emptyRoundTrip.csv");
      List<Stock> stocks=new ArrayList<>();
      CsvStockReader stockReader=new CsvStockReader();
      //Act
      stockWriter.writeStocksToFile(csvFile.toString(),stocks);
      List<Stock> readStocks=stockReader.readStocksFromFile(csvFile.toString());
      //Assert
      assertTrue(readStocks.isEmpty());
    }
  }
}
