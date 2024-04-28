package com.example.wpl;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {

    @FXML
    private RadioButton customerRadioButton;
    @FXML
    private RadioButton adminRadioButton;
    @FXML
    private final ToggleGroup roleToggleGroup = new ToggleGroup();

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField visiblePasswordField;
    @FXML
    private Button toggleVisibilityButton;

    @FXML
    public void initialize() {
        customerRadioButton.setToggleGroup(roleToggleGroup);
        adminRadioButton.setToggleGroup(roleToggleGroup);

        visiblePasswordField.managedProperty().bind(visiblePasswordField.visibleProperty());
        passwordField.managedProperty().bind(passwordField.visibleProperty());
        visiblePasswordField.setVisible(false);
    }

    @FXML
    private void togglePasswordVisibility() {
        boolean isVisible = visiblePasswordField.isVisible();
        visiblePasswordField.setVisible(!isVisible);
        passwordField.setVisible(isVisible);
        if (isVisible) {
            passwordField.setText(visiblePasswordField.getText());
        } else {
            visiblePasswordField.setText(passwordField.getText());
        }
    }

    @FXML
    private void handleLogin() throws Exception {
        String email = emailField.getText();
        String password = passwordField.isVisible() ? passwordField.getText() : visiblePasswordField.getText();
        RadioButton selectedRole = (RadioButton) roleToggleGroup.getSelectedToggle();

        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("Email or password cannot be empty.");
            // Optionally show an error message to the user here.
        } else if (selectedRole == adminRadioButton) { // Check if the admin radio button is selected
            // Check if the provided credentials belong to an admin user
            boolean isAdmin = DB_Functions.checkAdminCredentials(email, password);
            if (isAdmin) {
                MainDashboard.loadScene("AdminPagePrimary.fxml");
            } else {
                System.out.println("Invalid credentials for admin user.");
                showAlert("Failed Admin Login","Invalid Admin Credentials");
                // Optionally show an error message to the user here.
            }
        } else if (selectedRole == customerRadioButton) { // Check if the customer radio button is selected
            boolean isCustomer = DB_Functions.checkCustomerCredentials(email, password);

            if (isCustomer) {
                MainDashboard.loadScene("CustomerPagePrimary.fxml" , email); // change this to customer page later
            } else {
                showAlert("Failed Customer Login","Invalid Customer Credentials");
            }
        } else {
            System.out.println("Select a role to login.");
        }
    }

    @FXML
    private void handleSignup() throws Exception {
        MainDashboard.loadScene("SignupPage.fxml");
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
