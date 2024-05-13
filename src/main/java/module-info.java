module com.example.wpl {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.example.wpl to javafx.fxml;
    exports com.example.wpl;
}