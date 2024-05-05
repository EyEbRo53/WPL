package com.example.wpl;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class AdminTransportCompany {

    public VBox companyContainer;
    @FXML
    private TextField companyNameField;
    @FXML
    private TextField contactNumberField;
    @FXML
    private Button addButton;
    @FXML
    private TableView<TransportCompany> companyTable;
    @FXML
    private TableColumn<TransportCompany, Integer> companyIdColumn; // Changed to Integer
    @FXML
    private TableColumn<TransportCompany, String> companyNameColumn;
    @FXML
    private TableColumn<TransportCompany, String> contactNumberColumn;

    // Static company list shared across all instances of this controller
    private static ObservableList<TransportCompany> companies = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Set up columns
        companyIdColumn.setCellValueFactory(new PropertyValueFactory<>("companyId")); // Changed to Integer
        companyNameColumn.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        contactNumberColumn.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));

        // Fetch existing company data from the database and add it to the companies list
        companies.clear();
        fetchExistingCompanies();

        // Set the static companies list to the table
        companyTable.setItems(companies);
    }

    private void fetchExistingCompanies() {
        // Call a method in DB_Admin to retrieve existing company data from the database
        // and populate the companies list with it
        ArrayList<TransportCompany> existingCompanies = DB_Admin.getExistingTransportCompanies();
        companies.addAll(existingCompanies);
    }

    @FXML
    private void handleAddCompany() {
        String companyName = companyNameField.getText();
        String contactNumber = contactNumberField.getText();

        // Validate input values
        if (companyName.isEmpty() || contactNumber.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields.");
            alert.showAndWait();
            return;
        }

        // Create a new TransportCompany object
        TransportCompany newCompany = new TransportCompany(companyName, contactNumber);

        // Add the new company to the companies list
        if(DB_Admin.addTransportCompany(newCompany))
            companies.add(newCompany);

        // Clear input fields
        companyNameField.clear();
        contactNumberField.clear();
    }

    public static class TransportCompany {
        private int companyId; // Changed to int
        private String companyName;
        private String contactNumber;

        public TransportCompany(int companyId, String companyName, String contactNumber) {
            this.companyId = companyId;
            this.companyName = companyName;
            this.contactNumber = contactNumber;
        }

        // Constructor without Company ID parameter
        public TransportCompany(String companyName, String contactNumber) {
            this.companyName = companyName;
            this.contactNumber = contactNumber;
        }

        public int getCompanyId() {
            return companyId;
        }

        public String getCompanyName() {
            return companyName;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setCompanyID(int ne) {
            companyId = ne;
        }
    }
}
