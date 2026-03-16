package edu.ntnu.idi.idatt2003.repository;
import edu.ntnu.idi.idatt2003.model.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.io.TempDir;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


class CsvStockReaderTest {
  private CsvStockReader reader;
  @BeforeEach
  void setUp() {
    reader=new  CsvStockReader();
  }
  /**
   * Tests for valid CSV files.
   */
  @Nested
    class ValidFileTests{

    @Test
        void testValidFile(@TempDir Path tempDir) throws IOException {
      //Arrange
      Path csvFile = tempDir.resolve("stock.csv");
      String content= """
          AAPL,Apple Inc. ,150.00
          GOOGL,Alphabet Inc.,2800.00
          """;
      Files.writeString(csvFile, content);
      //Act
      List<Stock>stocks= reader.readStocksFromFile(csvFile.toString());
      //Assert
      assertEquals(2,stocks.size());
      assertEquals("AAPL",stocks.get(0).getSymbol());
      assertEquals("Apple Inc.",stocks.get(0).getCompany());
      assertEquals(0,new BigDecimal("150.00").compareTo(stocks.get(0).getSalesPrice()));
    }
    /**
     * Tests for reading multiple tests
     */
    @Test
    void testReadMultipleStocks(@TempDir Path tempDir) throws IOException {
      Path csvFile = tempDir.resolve("stock.csv");
      String content= """
          AAPL,Apple Inc. ,150.00
          GOOGL,Alphabet Inc.,311.11
          MSFT,Microdoft.,414.11
          TSLA,Tesla Inc.,426.52
      """;
      Files.writeString(csvFile, content);

      //Act
      List<Stock>stocks=reader.readStocksFromFile(csvFile.toString());

      //Assert
      assertEquals(4,stocks.size());
      assertEquals("AAPL",stocks.get(0).getSymbol());
      assertEquals("GOOGL",stocks.get(1).getSymbol());
      assertEquals("MSFT",stocks.get(2).getSymbol());
      assertEquals("TSLA",stocks.get(3).getSymbol());
    }
  }
  /**
   * Tests for handling comments and blank lines.
   */
  @Nested
  class CommentAndBlankLineTests{
    @Test
    void testSkipComments(@TempDir Path tempDir) throws IOException {
      //Arrange
      Path csvFile = tempDir.resolve("stock.csv");
      String content= """
          #This is a comment
          #Comment
          AAPL,Apple Inc. ,150.00
          #Comment
          GOOGL,Alphabet Inc.,311.11
      """;
      Files.writeString(csvFile, content);
      //Act
      List<Stock>stocks= reader.readStocksFromFile(csvFile.toString());
      //Assert
      assertEquals(2,stocks.size());
      assertEquals("AAPL",stocks.get(0).getSymbol());
      assertEquals("GOOGL",stocks.get(1).getSymbol());
    }
    /**
     * Tests that blank lines are skipped.
     */
    @Test
    void testSkipBlankLines(@TempDir Path tempDir) throws IOException {
      Path csvFile = tempDir.resolve("stock.csv");
      String content= """
     
          AAPL,Apple Inc. ,150.00
          
          GOOGL,Alphabet Inc.,311.11
      """;
      Files.writeString(csvFile, content);
      //Act
      List<Stock>stocks= reader.readStocksFromFile(csvFile.toString());
      assertEquals(2,stocks.size());
      assertEquals("AAPL",stocks.get(0).getSymbol());
      assertEquals("GOOGL",stocks.get(1).getSymbol());
    }
    @Test
    void testSkipBlankLinesAndComment(@TempDir Path tempDir) throws IOException {
      //Arrange
    Path csvFile = tempDir.resolve("stock.csv");
    String content= """
        #Comment
        
        AAPL,Apple Inc. ,150.00
        
        #Comment
        GOOGL,Alphabet Inc.,311.11
        
        """;
    Files.writeString(csvFile, content);
    //Act
    List<Stock>stocks= reader.readStocksFromFile(csvFile.toString());
    //Assert
    assertEquals(2,stocks.size());


    }
  }
  /**
   * Tests for validation and error handling.
   */
  @Nested
  class ValidationTests{
    @Test
    void testReadNullFileName(){
      assertThrows(IllegalArgumentException.class, () -> reader.readStocksFromFile(null));
    }

    /**
     * Empty file name
     */

    @Test
    void testReadEmptyFileName(){
      //Act & Assert
      assertThrows(IllegalArgumentException.class, () -> reader.readStocksFromFile(""));
    }
    @Test
    void testReadBlankFileName(){
      //Act & Assert
      assertThrows(IllegalArgumentException.class, () -> reader.readStocksFromFile(" "));
    }
    @Test
    void testNonExistentFileName(){
      assertThrows(IOException.class, () -> reader.readStocksFromFile("non-existent-file.csv"));
    }

    }
    @Nested
    class InvalidFormatTests{
    @Test
      void testSkipInvalidFormat(@TempDir Path tempDir) throws IOException {
      //Arrange
      Path csvFile = tempDir.resolve("stock.csv");
      String content = """
          AAPL,Apple Inc. ,150.00
          Invalid
          GOOGL,Alphabet Inc.,311.11
          Invalid
          MSFT,Microsoft,404.68
      """;
      Files.writeString(csvFile, content);
      //Act
      List<Stock>stocks= reader.readStocksFromFile(csvFile.toString());
      //Assert
      assertEquals(3,stocks.size());
      assertEquals("AAPL",stocks.get(0).getSymbol());
      assertEquals("GOOGL",stocks.get(1).getSymbol());
      assertEquals("MSFT",stocks.get(2).getSymbol());


    }
    @Test
      void testSkipInvalidPrice(@TempDir Path tempDir) throws IOException {
      Path csvFile = tempDir.resolve("stock.csv");
      String content= """
          AAPL,Apple Inc. ,150.00
          TSLA,Tesla Inc.,$Invalid_Format
          GOOGL,Alphabet Inc.,2800.00
          """;
      Files.writeString(csvFile, content);
      List<Stock>stocks= reader.readStocksFromFile(csvFile.toString());
      assertEquals(2,stocks.size());
      assertEquals("AAPL",stocks.get(0).getSymbol());
      assertEquals("GOOGL",stocks.get(1).getSymbol());
    }
    @Test
    void testAllInvalidLineReturnsEmptyList(@TempDir Path tempDir) throws IOException {
      Path csvFile = tempDir.resolve("stock.csv");
      String content= """
          INVALID LINE
          INVALID SYMBOLS
          INVALID PRICE
          """;
      Files.writeString(csvFile, content);
      List<Stock>stocks= reader.readStocksFromFile(csvFile.toString());
      assertTrue(stocks.isEmpty());
    }
      /**
       * Tests for edge cases.
       */
      @Nested
      class EdgeCaseTests{
        @Test
        void testReadingEmptyFile(@TempDir Path tempDir) throws IOException {
          //Arrange
          Path csvFile = tempDir.resolve("stock.csv");
          Files.writeString(csvFile,"");
          //Act
          List<Stock>stocks= reader.readStocksFromFile(csvFile.toString());
          //Assert
          assertTrue(stocks.isEmpty());
        }
        @Test
        void testFileOnlyWithComments(@TempDir Path tempDir) throws IOException {
          //Arrange
          Path csvFile = tempDir.resolve("stock.csv");
          String content= """
            #Comment
            #Comment
            #Comment
            """;
          Files.writeString(csvFile, content);
          //Act
          List<Stock>stocks= reader.readStocksFromFile(csvFile.toString());
          //Assert
          assertTrue(stocks.isEmpty());
        }
        @Test
        void testFileOnlyWithBlankLines(@TempDir Path tempDir) throws IOException {
          Path csvFile = tempDir.resolve("stock.csv");
          String content= """
            
            
        """;
          Files.writeString(csvFile, content);
          List<Stock>stocks= reader.readStocksFromFile(csvFile.toString());
          assertTrue(stocks.isEmpty());
        }
        @Test
        void testStocksWithDecimalPrice(@TempDir Path tempDir) throws IOException {
          Path csvFile = tempDir.resolve("stock.csv");
          String content= """
            AAPL,Apple Inc. ,150.75
            
            """;
          Files.writeString(csvFile, content);
          List<Stock>stocks= reader.readStocksFromFile(csvFile.toString());
          assertEquals(0,new BigDecimal("150.75").compareTo(stocks.get(0).getSalesPrice()));
        }
        @Test
        void testStockWithLongName(@TempDir Path tempDir) throws IOException {
          //Arrange
          Path csvFile = tempDir.resolve("stock.csv");
          String longName= "Long company Name International Company";
          String content="LONG," +longName+ ",160.00\n" ;
          Files.writeString(csvFile,content);
          //Act
          List<Stock>stocks= reader.readStocksFromFile(csvFile.toString());
          //Assert
          assertEquals(1, stocks.size());
          assertEquals("LONG",stocks.get(0).getSymbol());
          assertEquals(longName,stocks.get(0).getCompany());
          assertEquals(0, new BigDecimal("160.00").compareTo(stocks.get(0).getSalesPrice()));

        }


        @Test
        void testWhiteSpaceIsTrimmed(@TempDir Path tempDir) throws IOException {
          Path csvFile = tempDir.resolve("stock.csv");
          String content="AAPL  ,Apple Inc.   ,150.00\n" ;
          Files.writeString(csvFile, content);
          List<Stock>stocks= reader.readStocksFromFile(csvFile.toString());
          assertEquals(1, stocks.size());
          assertEquals("AAPL",stocks.get(0).getSymbol());
          assertEquals("Apple Inc.",stocks.get(0).getCompany());

      }


      }


  }
  @Nested
  class ResourceFiles{
    @Test
    void testFromTestResources() throws IOException {
      //Act
      List<Stock>stocks= reader.readStocksFromFile("src/test/resources/test-stocks.csv");
      //Assert
      assertFalse(stocks.isEmpty());
      assertTrue(stocks.size()>=1);
    }

  }
}
