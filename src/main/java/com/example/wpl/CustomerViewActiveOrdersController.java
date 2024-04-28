package com.example.wpl;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class CustomerViewActiveOrdersController {
    @FXML
    private VBox ordersContainer;
    @FXML
    private void initialize() {
        loadOrders();
    }

    private void loadOrders() {
        for (int i = 0; i < 10; i++) {
            final String orderId = "Order " + (100 + i);
            final String orderDate = "2024-02-" + (10 + i);

            final Boolean Status = false;
            final String description = "Description for " + orderId;

            VBox orderBox = new VBox();
            orderBox.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-cursor: hand;");
            orderBox.getChildren().add(new Text("Order ID: " + orderId));
            orderBox.getChildren().add(new Text("Order Date: " + orderDate));
            if(!Status){
                orderBox.getChildren().add(new Text("Status: processing"));
            }
            orderBox.getChildren().add(new Text("Description: " + description));
            ordersContainer.getChildren().add(orderBox);
        }
    }
}