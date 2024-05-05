package com.example.wpl;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class TransportController {

    @FXML
    private TextField numberPlateField;
    @FXML
    private TextField rentedByField; // Changed from modelNumberField
    @FXML
    private Button addButton;
    @FXML
    private TableView<Vehicle> vehicleTable;
    @FXML
    private TableColumn<Vehicle, String> numberPlateColumn;
    @FXML
    private TableColumn<Vehicle, Integer> rentedByColumn; // Changed from modelNumberColumn
    @FXML
    private TableColumn<Vehicle, Integer> cargoIDColumn; // Changed to Integer

    // Static vehicle list shared across all instances of this controller
    private static ObservableList<Vehicle> vehicles = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Set up columns
        numberPlateColumn.setCellValueFactory(new PropertyValueFactory<>("numberPlate"));
        rentedByColumn.setCellValueFactory(new PropertyValueFactory<>("rentedBy"));
        cargoIDColumn.setCellValueFactory(new PropertyValueFactory<>("cargoID"));

        // Fetch existing vehicle data from the database and add it to the vehicles list

        vehicles.clear();
        fetchExistingVehicles();

        // Set the static vehicles list to the table
        vehicleTable.setItems(vehicles);
    }

    private void fetchExistingVehicles() {
        // Call a method in DB_Admin to retrieve existing vehicle data from the database
        // and populate the vehicles list with it

        ArrayList<Vehicle> existingVehicles = DB_Admin.getExistingVehicles();
        vehicles.addAll(existingVehicles);
    }

    @FXML
    private void handleAddVehicle() {
        String numberPlate = numberPlateField.getText();
        int rentedBy;
        try {
            rentedBy = Integer.parseInt(rentedByField.getText());
        } catch (NumberFormatException e) {
            rentedBy = 0; // Defaulted to 0 if not a valid number
        }

        // Validate input values
        if (numberPlate.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields.");
            alert.showAndWait();
            return;
        }

        // Create a new Vehicle object
        numberPlate = numberPlate.toUpperCase();

        Vehicle newVehicle = new Vehicle(numberPlate, rentedBy, 0); // Initialize cargoID as 0

        // Add the new vehicle to the vehicles list

        if ( DB_Admin.AddVehicle(newVehicle.getNumberPlate(), newVehicle.getRentedBy())) {
            vehicles.add(newVehicle);
        } else {
            // If adding to the list fails, display an error alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Failed to add vehicle to the list.");
            alert.showAndWait();
        }

        // Clear input fields
        numberPlateField.clear();
        rentedByField.clear(); // Clear rentedBy field
    }

    public static class Vehicle {
        private String numberPlate;
        private int rentedBy;
        private int cargoID; // Changed to int

        public Vehicle(String numberPlate, int rentedBy, int cargoID) { // Adjust constructor
            this.numberPlate = numberPlate;
            this.rentedBy = rentedBy;
            this.cargoID = cargoID;
        }

        public String getNumberPlate() {
            return numberPlate;
        }

        public int getRentedBy() {
            return rentedBy;
        }

        public int getCargoID() { // Adjust getter
            return cargoID;
        }

        public void setCargoID(int cargoID) { // Adjust setter
            this.cargoID = cargoID;
        }
    }
}
