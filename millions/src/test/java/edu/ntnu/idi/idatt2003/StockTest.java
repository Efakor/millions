package edu.ntnu.idi.idatt2003;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StockTest {
  @Test
    public void testStock() {
        Stock stock = new Stock();
        assertNotNull(stock);
    }
}
