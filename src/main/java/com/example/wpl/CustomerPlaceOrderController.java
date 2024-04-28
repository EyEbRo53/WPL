package com.example.wpl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class CustomerPlaceOrderController implements EmailHandler  {

    @FXML
    private TextField itemNameField;
    @FXML
    private TextField itemQuantityField;
    @FXML
    private TextField itemWeightField;
    @FXML
    private Button addButton;
    @FXML
    private TableView<Item> itemsTable;
    @FXML
    private TableColumn<Item, String> itemNameColumn;
    @FXML
    private TableColumn<Item, Integer> itemQuantityColumn;
    @FXML
    private TableColumn<Item, Double> itemWeightColumn;
    @FXML
    private Label messageLabel;
    private String email;

    public void setEmail(String s){
        email = s;
    }
    // Static items list shared across all instances of this controller
    private static ObservableList<Item> items = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        itemQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("itemQuantity"));
        itemWeightColumn.setCellValueFactory(new PropertyValueFactory<>("itemWeight"));
        itemsTable.setItems(items); // Set the static items list to the table
        setupDeleteButtonColumn();
    }
    private void addButtonClicked() {
        // Get input values from text fields
        String itemName = itemNameField.getText();
        int itemQuantity = Integer.parseInt(itemQuantityField.getText());
        double itemWeight = Double.parseDouble(itemWeightField.getText());

        // Validate input values
        if (itemName.isEmpty() || itemQuantity <= 0 || itemWeight <= 0) {
            messageLabel.setText("Please fill in all fields correctly.");
            return;
        }

        // Create a new Item object
        Item newItem = new Item(itemName, itemQuantity, itemWeight);

        // Add the new item to the items list
        items.add(newItem);

        // Clear input fields
        itemNameField.clear();
        itemQuantityField.clear();
        itemWeightField.clear();

        // Clear message label
        messageLabel.setText("");
    }
    @FXML
    private void handleAddItem() {
        String itemName = itemNameField.getText();
        int itemQuantity = Integer.parseInt(itemQuantityField.getText());
        double itemWeight = Double.parseDouble(itemWeightField.getText());

        Item newItem = new Item(itemName, itemQuantity, itemWeight);

        //String itemDetails = String.format("Item: %s, Quantity: %d, Weight: %.2f", itemName, itemQuantity, itemWeight);

        //System.out.println(itemDetails); // Print item details


        items.add(newItem); // Add to the static list

        itemNameField.clear();
        itemQuantityField.clear();
        itemWeightField.clear();
    }

    @FXML
    private void handleConfirmOrder() {

        DB_CustomerFunctions.AddOrder(items,email);

        items.clear(); // Clear the static items from the table
        messageLabel.setText("Order placed successfully.");
        messageLabel.setVisible(true);
        itemsTable.setVisible(false);

        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> {
            messageLabel.setText("");
            messageLabel.setVisible(false);
            itemsTable.setVisible(true);
        });
        delay.play();
    }

    private void setupDeleteButtonColumn() {
        TableColumn<Item, Void> colBtn = new TableColumn<>("Action");
        colBtn.setCellFactory(col -> new TableCell<Item, Void>() {
            private final Button btn = new Button("x");

            {
                btn.setOnAction(e -> {
                    Item data = getTableView().getItems().get(getIndex());
                    items.remove(data);
                });
                btn.setStyle("-fx-background-color: #347999; -fx-text-fill: white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
                setContentDisplay(ContentDisplay.CENTER);
            }
        });

        itemsTable.getColumns().add(colBtn);
    }

    public static class Item {
        private String itemName;
        private int itemQuantity;
        private double itemWeight;

        public Item(String itemName, int itemQuantity, double itemWeight) {
            this.itemName = itemName;
            this.itemQuantity = itemQuantity;
            this.itemWeight = itemWeight;
        }

        public String getItemName() {
            return itemName;
        }

        public int getItemQuantity() {
            return itemQuantity;
        }

        public double getItemWeight() {
            return itemWeight;
        }
    }
}
