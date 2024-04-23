module com.example.wpl {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.wpl to javafx.fxml;
    exports com.example.wpl;
}