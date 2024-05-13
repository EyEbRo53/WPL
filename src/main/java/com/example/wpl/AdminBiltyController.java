package com.example.wpl;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class AdminBiltyController {

    @FXML
    private VBox pairsContainer;

    private AdminController adminController;

    public void setAdminController(AdminController adminController) {
        this.adminController = adminController;
    }

    @FXML
    private void initialize() {
        loadBiltyDealPairs();
    }

    private void loadBiltyDealPairs() {
        // Retrieve BiltyID-DealID pairs from the database or any other data source
        ArrayList<BiltyDealPair> pairs = DB_Admin.getBiltyDealPairsFromDatabase();

        // Populate the pairs in the UI
        for (BiltyDealPair pair : pairs) {
            VBox pairBox = createPairBox(pair.getBiltyID(), pair.getDealID());
            pairsContainer.getChildren().add(pairBox);
        }
    }

    private VBox createPairBox(int biltyID, int dealID) {
        VBox pairBox = new VBox();
        pairBox.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-cursor: hand;");
        pairBox.getChildren().add(new Text("Bilty ID: " + biltyID));
        pairBox.getChildren().add(new Text("Deal ID: " + dealID));

        // Add event handler to view bilty details
        pairBox.setOnMouseClicked(event -> handleViewBiltyDetails(biltyID));

        return pairBox;
    }

    private void handleViewBiltyDetails(int biltyID) {
        if (adminController != null) {
            adminController.loadCargo(biltyID); // Navigate to the Cargo page
        } else {
            System.err.println("AdminController is null. Cannot load cargo.");
        }
    }

    public static class BiltyDealPair {
        private int biltyID;
        private int dealID;

        public BiltyDealPair(int biltyID, int dealID) {
            this.biltyID = biltyID;
            this.dealID = dealID;
        }

        public int getBiltyID() {
            return biltyID;
        }

        public void setBiltyID(int biltyID) {
            this.biltyID = biltyID;
        }

        public int getDealID() {
            return dealID;
        }

        public void setDealID(int dealID) {
            this.dealID = dealID;
        }
    }
}
