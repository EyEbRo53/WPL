package com.example.wpl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
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
    private PasswordField passwordField;  // Ensure this is PasswordField in your FXML as well
    @FXML
    private TextField visiblePasswordField;
    @FXML
    private Button signupButton;
    @FXML
    private Button toggleVisibilityButton;

    @FXML
    public void initialize() {
        customerRadioButton.setToggleGroup(roleToggleGroup);
        adminRadioButton.setToggleGroup(roleToggleGroup);

        visiblePasswordField.managedProperty().bind(visiblePasswordField.visibleProperty());
        visiblePasswordField.setVisible(false);

        passwordField.managedProperty().bind(passwordField.visibleProperty());
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
        // Determine which field is currently visible and use its text
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/wpl/signupPage.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) signupButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
