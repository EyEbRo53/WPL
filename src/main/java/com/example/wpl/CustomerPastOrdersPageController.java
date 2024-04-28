package com.example.wpl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CustomerPastOrdersPageController {
    @FXML
    private VBox ordersContainer;
    @FXML
    private void initialize() {
        loadOrders();
    }

    private void loadOrders() {
        for (int i = 0; i < 10; i++) {
            final String orderId = "Order " + (10 + i);
            final String orderDate = "2024-02-" + (5 + i);

            final Boolean Status = true;
            final String description = "Description for " + orderId;

            VBox orderBox = new VBox();
            orderBox.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-cursor: hand;");
            orderBox.getChildren().add(new Text("Order ID: " + orderId));
            orderBox.getChildren().add(new Text("Order Date: " + orderDate));
            if(Status){
                orderBox.getChildren().add(new Text("Status: done"));
            }
            orderBox.getChildren().add(new Text("Description: " + description));
            ordersContainer.getChildren().add(orderBox);
        }
    }
}
