package com.example.wpl;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.util.ArrayList;
public class CustomerViewActiveOrdersController implements EmailHandler  {
    @FXML
    private VBox ordersContainer;
    @FXML
    private void initialize() {
        Platform.runLater(this::loadOrders);
    }
    private String email;

    public void setEmail(String s){
        email = s;
    }

    private void loadOrders() {

        ArrayList<DB_CustomerFunctions.DealInfo> df=  DB_CustomerFunctions.viewOrders(email);

        for (int i = 0;i<df.size();i++) {
            final int orderId = df.get(i).getDealId();
            final String orderDate = df.get(i).getOrderDate();
            final String Status = df.get(i).getDealStatus();
            final String description = df.get(i).getDealDescription();
            VBox orderBox = new VBox();
            orderBox.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-cursor: hand;");
            orderBox.getChildren().add(new Text("Order ID: " + orderId));
            orderBox.getChildren().add(new Text("Order Date: " + orderDate));
            orderBox.getChildren().add(new Text("Status: "+Status));
            orderBox.getChildren().add(new Text("Description: " + description));
            ordersContainer.getChildren().add(orderBox);
        }
    }
}
