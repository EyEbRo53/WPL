package com.example.wpl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class AdminController {

    @FXML
    private StackPane contentArea;

    public void loadViewOrders() {
        loadContent("ViewOrders.fxml");
    }

    public void loadAssignCargo() {
        loadContent("AssignCargo.fxml");
    }

    private void loadContent(String fxml) {
        try {
            Node node = FXMLLoader.load(getClass().getResource(fxml));
            contentArea.getChildren().setAll(node);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception, perhaps show an error message
        }
    }
}
