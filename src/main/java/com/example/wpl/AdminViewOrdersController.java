package com.example.wpl;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;

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
        ArrayList<DB_Admin.DealInfo> df = DB_Admin.viewOrders();

        for (int i = 0; i < df.size(); i++) {
            final int CustId = df.get(i).getCustomerId();
            final int orderId = df.get(i).getDealId();
            final String customerName = df.get(i).getCustomerName();
            final String status = df.get(i).getDealStatus();
            final String orderDate = df.get(i).getOrderDate();
            final String description = df.get(i).getDealDescription();

            VBox orderBox = new VBox();
            orderBox.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-cursor: hand;");
            orderBox.getChildren().add(new Text("Order ID: " + orderId));
            orderBox.getChildren().add(new Text("Customer Name: " + customerName));
            orderBox.getChildren().add(new Text("Customer Id: " + CustId));
            orderBox.getChildren().add(new Text("Order Date: " + orderDate));
            orderBox.getChildren().add(new Text("Order Description " + description));
            orderBox.getChildren().add(new Text("Status: " + status));

            //orderBox.setOnMouseClicked(event -> handleViewOrderDetails(orderId, customerName, orderDate, description));
            orderBox.setOnMouseClicked(event -> handleViewOrderDetails(orderId, customerName, orderDate, description));
            ordersContainer.getChildren().add(orderBox);
        }
    }

    @FXML
    private void handleViewOrderDetails(int orderId, String customerName, String orderDate, String description) {
        try {
            AdminOrderDetailsController.setOrderDetails(orderId, customerName, orderDate, description);
            AdminController.getInstance().loadContent("AdminOrderDetailsPage.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load order details page.");
        }
    }

}
