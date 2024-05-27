module luh.hci.slidersexample {
    requires javafx.controls;
    requires javafx.fxml;


    opens luh.hci.slidersexample to javafx.fxml;
    exports luh.hci.slidersexample;
}