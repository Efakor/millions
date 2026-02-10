package edu.ntnu.idi.idatt2003;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ShareTest {
  @Test
  void shareCanBeCreated() {
    Share share = new Share();
    assertNotNull(share);
  }
}
