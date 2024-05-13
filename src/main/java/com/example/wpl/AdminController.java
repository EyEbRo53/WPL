package com.example.wpl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminController {

    @FXML
    private StackPane contentArea;

    // Singleton instance
    private static AdminController instance;

    @FXML
    private void initialize() {
        instance = this;
        loadDefaultView();
    }

    public static AdminController getInstance() {
        return instance;
    }

    public void loadDefaultView() {
        loadContent("AdminDefaultPage.fxml");
    }

    public void loadViewOrders() {
        loadContent("AdminViewOrdersPage.fxml");
    }

    public void loadPastRecords(){
        loadContent("AdminPastRecordsPage.fxml");
    }

    public void loadVehiclePage() {
        loadContent("AdminVehiclePage.fxml");
    }

    public void loadTransportCompany(){
        loadContent("AdminTransportCompanyPage.fxml");
    }

    public void loadTransactionCompany(){
        loadContent("AdminTransactionCompanyPage.fxml");
    }

    public void loadTransactionCustomer(){
        loadContent("AdminTransactionCustomerPage.fxml");
    }
    public void loadGraphs() {
        ProfitLossChartController controller = new ProfitLossChartController();
        Stage stage = new Stage();
        try {
            controller.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void loadAdminBilty() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/wpl/Admin_Bilty.fxml"));
        try {
            Node node = loader.load();
            AdminBiltyController controller = loader.getController();
            controller.setAdminController(this); // Set the adminController property
            contentArea.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, perhaps show an error message
        }
    }

    public void logout() throws Exception {
        loadScene("LoginPage.fxml");
    }



    public void loadCargo() {

    }
    public void loadCargo(int biltyID) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/wpl/Admin_Cargo.fxml"));
        try {
            Node node = loader.load();
            AdminCargoController controller = loader.getController();
            controller.initialize(biltyID);
            controller.setBiltyID(biltyID);
            contentArea.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, perhaps show an error message
        }
    }

    public void loadContent(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/wpl/" + fxml));
            Node node = loader.load();
            contentArea.getChildren().setAll(node);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception, perhaps show an error message
        }
    }

    public void loadScene(String fxml) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/wpl/" + fxml));
        Scene scene = new Scene(root);
        Stage stage = (Stage) contentArea.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
