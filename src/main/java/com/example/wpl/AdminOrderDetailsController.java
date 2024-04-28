package com.example.wpl;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminOrderDetailsController {

    @FXML
    private Label orderIdLabel;
    @FXML
    private Label customerNameLabel;
    @FXML
    private Label orderDateLabel;
    @FXML
    private Label dealDescriptionLabel;

    private static String orderId;
    private static String customerName;
    private static String orderDate;
    @FXML
    private TableView<Item> orderDetailsTable;
    @FXML
    private TableColumn<Item, String> itemNameColumn;
    @FXML
    private TableColumn<Item, Integer> quantityColumn;
    @FXML
    private TableColumn<Item, Double> weightColumn;
    @FXML
    private TableColumn<Item, String> vehicleAssignmentColumn;

    // Model class for table
    public static class Item {
        private final SimpleStringProperty name;
        private final SimpleIntegerProperty quantity;
        private final SimpleDoubleProperty weight;

        public Item(String name, int quantity, double weight) {
            this.name = new SimpleStringProperty(name);
            this.quantity = new SimpleIntegerProperty(quantity);
            this.weight = new SimpleDoubleProperty(weight);
        }

        // Getters
        public String getName() { return name.get(); }
        public int getQuantity() { return quantity.get(); }
        public double getWeight() { return weight.get(); }

        // Setters
        public void setName(String value) { name.set(value); }
        public void setQuantity(int value) { quantity.set(value); }
        public void setWeight(double value) { weight.set(value); }
    }

    public static void setOrderDetails(String id, String name, String date, String desc) {
        orderId = id;
        customerName = name;
        orderDate = date;
    }


    @FXML
    public void initialize() {
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));

        // Load data for testing
        loadTestData();

        // Set the labels with stored order details
        // Set the labels with stored order details
        if (orderId != null && !orderId.isEmpty()) {
            orderIdLabel.setText("Order ID: " + orderId);
        }
        if (customerName != null && !customerName.isEmpty()) {
            customerNameLabel.setText("Customer Name: " + customerName);
        }
        if (orderDate != null && !orderDate.isEmpty()) {
            orderDateLabel.setText("Order Date: " + orderDate);
        }
    }

    private void loadTestData() {
        orderDetailsTable.getItems().add(new Item("Widget", 10, 2.5));
        orderDetailsTable.getItems().add(new Item("Gadget", 20, 1.5));
    }

    @FXML
    private void handleConfirmOrder() {
        System.out.println("Order Confirmed");
        // Add your logic for confirming the order here
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
