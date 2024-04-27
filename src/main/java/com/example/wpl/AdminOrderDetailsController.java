package com.example.wpl;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class AdminOrderDetailsController {

    @FXML
    private Label orderIdLabel;
    @FXML
    private Label customerNameLabel;
    @FXML
    private Label orderDateLabel;
    @FXML
    private Label dealDescriptionLabel;

    // Reference to the content area where pages are loaded
    private StackPane contentArea;

    public void setOrderDetails(String orderId, String customerName, String orderDate, String description) {
        orderIdLabel.setText("Order ID: " + orderId);
        customerNameLabel.setText("Customer Name: " + customerName);
        orderDateLabel.setText("Order Date: " + orderDate);
        dealDescriptionLabel.setText("Description: " + description);
    }

    // Method to set the content area, this should be called when the controller is initialized
    public void setContentArea(StackPane contentArea) {
        this.contentArea = contentArea;
    }

    @FXML
    private void handleBack() {
        try {
            // Use the AdminController's static method or singleton instance to load the view
            AdminController.getInstance().loadContent("AdminViewOrdersPage.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load view orders page.");
        }
    }


}
