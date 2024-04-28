package com.example.wpl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import static com.example.wpl.MainDashboard.loadScene;

public class CustomerController implements EmailHandler {

    public Button placeOrder;
    @FXML
    private StackPane contentArea;
    String email;

    @FXML
    private void initialize() {
        loadDefaultView();
    }

    public void loadDefaultView() {
        loadContent("CustomerDefaultPage.fxml",email);
    }

    public void loadPlaceOrder() {
        loadContent("CustomerPlaceOrderPage.fxml",email);
    }

    public void loadActiveViewOrder() {
        loadContent("CustomerViewActiveOrdersPage.fxml",email);
    }

    public void loadViewPastOrders() {
        loadContent("CustomerPastOrdersPage.fxml",email);
    }

    public void loadViewLoginPage() throws Exception {
        loadScene("LoginPage.fxml");
    }
    public void setEmail(String s){
        email = s;
    }

private void loadContent(String fxml, String email) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = loader.load();
        Object controller = loader.getController();

        // Set email for all controllers
        if (controller instanceof EmailHandler) {
            ((EmailHandler) controller).setEmail(email);
        }

        contentArea.getChildren().setAll(root);
    } catch (Exception e) {
        e.printStackTrace();
        // Consider showing a user-friendly error message
    }
}

    public String getEmail() {
        return email;
    }
}
