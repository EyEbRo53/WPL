package com.example.wpl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import static com.example.wpl.MainDashboard.loadScene;

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
    public void logout() throws Exception {
        loadScene("LoginPage.fxml");
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


}
