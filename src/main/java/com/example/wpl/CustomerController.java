package com.example.wpl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import static com.example.wpl.MainDashboard.loadScene;

public class CustomerController {

    @FXML
    private StackPane contentArea;

    @FXML
    private void initialize() {
        loadDefaultView();
    }

    public void loadDefaultView() {
        loadContent("CustomerDefaultPage.fxml");
    }

    public void loadDashboard() {
        // Code to load Dashboard view
    }

    public void loadPlaceOrder() {
        loadContent("CustomerPlaceOrderPage.fxml");
    }

    public void loadActiveViewOrder() {
        loadContent("CustomerViewActiveOrdersPage.fxml");
    }

    public void loadViewPastOrders() {
        loadContent("CustomerPastOrdersPage.fxml");
    }

    public void loadViewLoginPage() throws Exception {
        loadScene("LoginPage.fxml");
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
