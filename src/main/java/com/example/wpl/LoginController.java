package com.example.wpl;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Button;

public class LoginController {

    @FXML
    private RadioButton customerRadioButton;
    @FXML
    private RadioButton adminRadioButton;
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
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.isVisible() ? passwordField.getText() : visiblePasswordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("Email or password cannot be empty.");
        } else {
            System.out.println("Processing login for: " + email);
            System.out.println("Login successful.");
        }
    }

    @FXML
    private void handleSignup() throws Exception {
        MainDashboard.loadScene("SignupPage.fxml");
    }
}
