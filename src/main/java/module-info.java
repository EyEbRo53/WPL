module com.example.wpl {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.wpl to javafx.fxml;
    exports com.example.wpl;
}