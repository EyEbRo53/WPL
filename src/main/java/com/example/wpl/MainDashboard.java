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
        public static void loadScene(String fxml ) throws Exception{
        loadScene(fxml, null);
        }

         public static void loadScene(String fxml, String email ) throws Exception {
            URL url = MainDashboard.class.getResource("/com/example/wpl/" + fxml);
            if (url == null) {
            throw new IllegalArgumentException("Cannot find resource: " + fxml);
         }
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            if (email != null) {
                 if (loader.getController() instanceof CustomerController) {
                        ((CustomerController) loader.getController()).setEmail(email);
                     ((CustomerController) loader.getController()).getEmail();
             }
        // Add similar checks for other controllers if needed
        }

        primaryStage.setScene(new Scene(root, 1024, 576)); // Consistent size for all scenes
        }



    public static void main(String[] args) {
        launch(args);
    }
}
