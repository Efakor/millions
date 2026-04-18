module edu.ntnu.idi.idatt2003 {
  requires javafx.graphics;
  requires javafx.controls;
  requires javafx.fxml;
  requires java.desktop;
  opens edu.ntnu.idi.idatt2003 to javafx.graphics;
  opens edu.ntnu.idi.idatt2003.controller to javafx.graphics;
  opens edu.ntnu.idi.idatt2003.view to javafx.graphics;
  opens edu.ntnu.idi.idatt2003.model to javafx.graphics;
  opens edu.ntnu.idi.idatt2003.observer to javafx.graphics;
  opens edu.ntnu.idi.idatt2003.repository to javafx.graphics;
  opens edu.ntnu.idi.idatt2003.util to javafx.graphics;
}
