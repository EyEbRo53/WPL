package com.example.wpl;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class AdminOrderDetailsController {

    @FXML
    private Label orderIdLabel;
    @FXML
    private Label customerNameLabel;
    @FXML
    private Label orderDateLabel;
    @FXML

    private static int orderId;
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
    static public void setOrderDetails(int id, String name, String date, String desc) {
        orderId = id;
        customerName = name;
        orderDate = date;
    }
    public void updateLabels() {
        orderIdLabel.setText("Order ID: " + orderId);
        customerNameLabel.setText("Customer Name: " + customerName);
        orderDateLabel.setText("Order Date: " + orderDate);
    }


    @FXML
    public void initialize() {
        // Set up the table columns using PropertyValueFactory
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        // vehicleAssignmentColumn should also be set up if it's part of your table

        updateLabels();

        // Assuming orderId is available at this point
        ArrayList<Item> items = DB_Admin.getOrderItemDetails(orderId);

        if (items != null && !items.isEmpty()) {
            // Now add the fetched items to the table view
            orderDetailsTable.getItems().setAll(items);
        } else {
            System.out.println("No items found for the order.");
        }
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
