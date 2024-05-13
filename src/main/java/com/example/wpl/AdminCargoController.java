package com.example.wpl;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.util.List;
public class AdminCargoController {

    @FXML
    private TableView<Item> itemsTable;

    @FXML
    private TableView<Vehicle> vehiclesTable;

    @FXML
    private Button confirmButton;

    @FXML
    private Button backButton;

    private int biltyID;

    public void initialize(int biltyID) {
        this.biltyID = biltyID;
        loadCargoData();
        loadAvailableVehicles();
        vehiclesTable.setEditable(true); // Ensure TableView is editable
        itemsTable.setEditable(true); // Ensure TableView is editable

        initSelectColumn(vehiclesTable);
        initSelectColumn(itemsTable);
    }

    public <T> void initSelectColumn(TableView<T> table) {
        TableColumn<T, Boolean> selectColumn = new TableColumn<>("Select");
        selectColumn.setCellValueFactory(cellData -> {
            if (table == vehiclesTable) {
                return ((Vehicle) cellData.getValue()).selectedProperty();
            } else if (table == itemsTable) {
                return ((Item) cellData.getValue()).selectedProperty(); // Ensure this is correctly bound
            }
            return null;
        });

        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));
        table.getColumns().add(selectColumn);
    }

    private void loadCargoData() {
        List<Item> cargoList = DB_Admin.getCargoByBiltyID(biltyID);
        itemsTable.getItems().addAll(cargoList);
    }

    private void loadAvailableVehicles() {
        List<Vehicle> availableVehicles = DB_Admin.getAvailableVehicles(biltyID);
        vehiclesTable.getItems().addAll(availableVehicles);
    }

    @FXML
    private void backAction() {
        System.out.println("Back button clicked!");
        // Add logic for back action
    }

    public void setBiltyID(int biltyID) {
        this.biltyID = biltyID;
    }

    public static class Item {
        private final int cargoID;
        private final String name;
        private final int quantity;
        private final double weight;
        private final String vehicleAssigned;
        private final BooleanProperty selected; // New property to track selection

        public Item(int cargoID, String name, int quantity, double weight, String vehicleAssigned) {
            this.cargoID = cargoID;
            this.name = name;
            this.quantity = quantity;
            this.weight = weight;
            this.vehicleAssigned = vehicleAssigned;
            this.selected = new SimpleBooleanProperty(false); // Initialize to false
        }

        // Getter methods for existing properties

        public int getCargoID() {
            return cargoID;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getWeight() {
            return weight;
        }

        public String getVehicleAssigned() {
            return vehicleAssigned;
        }

        // Getter and setter for the selected property

        public boolean isSelected() {
            return selected.get();
        }

        public BooleanProperty selectedProperty() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected.set(selected);
        }
    }

    public static class Vehicle {
        private final String numberPlate;
        private final String rentedBy;
        private final int cargoAssigned;
        private final BooleanProperty selected; // New property to track selection

        public Vehicle(String numberPlate, String rentedBy, int cargoAssigned) {
            this.numberPlate = numberPlate;
            this.rentedBy = rentedBy;
            this.cargoAssigned = cargoAssigned;
            this.selected = new SimpleBooleanProperty(false); // Initialize to false
        }

        // Getter methods for existing properties

        public String getNumberPlate() {
            return numberPlate;
        }

        public String getRentedBy() {
            return rentedBy;
        }

        public int getCargoAssigned() {
            return cargoAssigned;
        }

        // Getter and setter for the selected property

        public boolean isSelected() {
            return selected.get();
        }

        public BooleanProperty selectedProperty() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected.set(selected);
        }
    }

    @FXML
    private void confirmAction() {
        System.out.println("Confirm button clicked!");

        Vehicle selectedVehicle = getSelectedVehicle();
        Item selectedItem = getSelectedItem();

        if (selectedItem == null) {
            System.out.println("No vehicle or item selected.");
            return;
        }

        // Link the selected vehicle and item
        DB_Admin.linkVehicleAndCargo(selectedVehicle, selectedItem);

        System.out.println("Vehicle and cargo linked successfully!");
    }

    private Vehicle getSelectedVehicle() {
        List<Vehicle> vehicleList = vehiclesTable.getItems();
        for (Vehicle vehicle : vehicleList) {
            if (vehicle.isSelected()) {
                vehiclesTable.refresh(); // Refresh the table
                return vehicle;
            }
        }
        return null;
    }

    private Item getSelectedItem() {
        itemsTable.refresh(); // Refresh the table
        return itemsTable.getSelectionModel().getSelectedItem();
    }


}
