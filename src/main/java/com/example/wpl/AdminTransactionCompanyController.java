package com.example.wpl;

import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminTransactionCompanyController {

    @FXML
    private TextField dealIdField;
    @FXML
    private TextField companyIdField;
    @FXML
    private TextField transactionAmountField;
    @FXML
    private TextField dateAndTimeField;
    @FXML
    private TextField paymentMethodField;
    @FXML
    private Button addButton;
    @FXML
    private TableView<TransactionCompany> transactionTable;
    @FXML
    private TableColumn<TransactionCompany, Integer> transactionIdColumn; // Changed to Integer
    @FXML
    private TableColumn<TransactionCompany, Integer> dealIdColumn; // Changed to Integer
    @FXML
    private TableColumn<TransactionCompany, Integer> companyIdColumn; // Changed to Integer
    @FXML
    private TableColumn<TransactionCompany, String> transactionAmountColumn;
    @FXML
    private TableColumn<TransactionCompany, String> dateAndTimeColumn;
    @FXML
    private TableColumn<TransactionCompany, String> paymentMethodColumn;

    // Static transaction list shared across all instances of this controller
    private static ObservableList<TransactionCompany> transactions = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Set up columns
        transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        dealIdColumn.setCellValueFactory(new PropertyValueFactory<>("dealId"));
        companyIdColumn.setCellValueFactory(new PropertyValueFactory<>("companyId")); // Changed to Integer
        transactionAmountColumn.setCellValueFactory(new PropertyValueFactory<>("transactionAmount"));
        dateAndTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateAndTime"));
        paymentMethodColumn.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));

        // Fetch existing transaction data from the database and add it to the transactions list
        transactions.clear();
        fetchExistingTransactions();

        // Set the static transactions list to the table
        transactionTable.setItems(transactions);
    }

    private void fetchExistingTransactions() {
        // Call a method in DB_Admin to retrieve existing transaction data from the database
        // and populate the transactions list with it
        ArrayList<TransactionCompany> existingTransactions = DB_Admin.getExistingCompanyTransactions();
        transactions.addAll(existingTransactions);
    }

    @FXML
    private void handleAddTransaction() throws SQLException {
        int dealId;
        int companyId;
        try {
            dealId = Integer.parseInt(dealIdField.getText());
            companyId = Integer.parseInt(companyIdField.getText());
        } catch (NumberFormatException e) {
            // Handle invalid input
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please enter valid IDs.");
            alert.showAndWait();
            return;
        }

        String transactionAmount = transactionAmountField.getText();
        String dateAndTime = dateAndTimeField.getText();
        String paymentMethod = paymentMethodField.getText();

        // Validate input values
        if (transactionAmount.isEmpty() || dateAndTime.isEmpty() || paymentMethod.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields.");
            alert.showAndWait();
            return;
        }

        // Create a new TransactionCompany object
        TransactionCompany newTransaction = new TransactionCompany(DB_Admin.getLastTransactionId()+1, dealId,companyId, transactionAmount, dateAndTime, paymentMethod);

        // Add the new transaction to the transactions list
        if (DB_Admin.addCompanyTransaction(newTransaction)) {
            transactions.add(newTransaction);
        } else {
            // If adding to the list fails, display an error alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Failed to add transaction to the list.");
            alert.showAndWait();
        }

        // Clear input fields
        dealIdField.clear();
        companyIdField.clear();
        transactionAmountField.clear();
        dateAndTimeField.clear();
        paymentMethodField.clear();
    }

    public static class TransactionCompany {
        private int transactionId; // Changed to int
        private int dealId; // Changed to int
        private int companyId; // Changed to int
        private String transactionAmount;
        private String dateAndTime;
        private String paymentMethod;

        public TransactionCompany(int t,int dealId, int companyId, String transactionAmount, String dateAndTime, String paymentMethod) {
            transactionId = t;
            this.dealId = dealId;
            this.companyId = companyId;
            this.transactionAmount = transactionAmount;
            this.dateAndTime = dateAndTime;
            this.paymentMethod = paymentMethod;
        }

        public int getTransactionId() {
            return transactionId;
        }

        public int getDealId() {
            return dealId;
        }

        public int getCompanyId() {
            return companyId;
        }

        public String getTransactionAmount() {
            return transactionAmount;
        }

        public String getDateAndTime() {
            return dateAndTime;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }
    }
}
