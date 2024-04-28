package com.example.wpl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class MainDashboard extends Application {

    private static Stage primaryStage; // Single stage instance

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        loadScene("LoginPage.fxml");

        // Set window properties
        primaryStage.setTitle("WoodPecker Logistics");
        primaryStage.setWidth(1024);  // Default width
        primaryStage.setHeight(576);  // Default height
        primaryStage.setResizable(true); // Make window not resizable or set true if you want it resizable

        primaryStage.show();
    }

//    public static void loadScene(String fxml) throws Exception {
//        Parent root = FXMLLoader.load(MainDashboard.class.getResource("/com/example/wpl/" + fxml));
//        primaryStage.setScene(new Scene(root, 1024, 576)); // Consistent size for all scenes
//    }

    public static void loadScene(String fxml) throws Exception {
        URL url = MainDashboard.class.getResource("/com/example/wpl/" + fxml);
        if (url == null) {
            throw new IllegalArgumentException("Cannot find resource: " + fxml);
        }
        Parent root = FXMLLoader.load(url);
        primaryStage.setScene(new Scene(root, 1024, 576)); // Consistent size for all scenes
    }


    public static void main(String[] args) {
        launch(args);
    }
}
