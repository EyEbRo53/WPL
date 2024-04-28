package com.example.wpl;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AdminViewOrdersController {

    @FXML
    private VBox ordersContainer;

    private AdminController adminController;

    public void setAdminController(AdminController adminController) {
        this.adminController = adminController;
    }

    @FXML
    private void initialize() {
        loadOrders();
    }

    private void loadOrders() {
        for (int i = 0; i < 10; i++) {
            final String orderId = "Order " + (100 + i);
            final String customerName = "Customer " + i;
            final String orderDate = "2024-02-" + (10 + i);
            final String description = "Description for " + orderId;

            VBox orderBox = new VBox();
            orderBox.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-cursor: hand;");
            orderBox.getChildren().add(new Text("Order ID: " + orderId));
            orderBox.getChildren().add(new Text("Customer Name: " + customerName));
            orderBox.getChildren().add(new Text("Order Date: " + orderDate));
            orderBox.setOnMouseClicked(event -> handleViewOrderDetails(orderId, customerName, orderDate, description));
            ordersContainer.getChildren().add(orderBox);
        }
    }

    @FXML
    private void handleViewOrderDetails(String orderId, String customerName, String orderDate, String description) {
        try {
            AdminOrderDetailsController.setOrderDetails(orderId, customerName, orderDate, description);
            AdminController.getInstance().loadContent("AdminOrderDetailsPage.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load order details page.");
        }
    }

}
