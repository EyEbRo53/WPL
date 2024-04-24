package com.example.wpl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button signupButton;

    @FXML
    private void handleLogin() {
        // Here you can add your authentication logic
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("Email or password cannot be empty.");
        } else {
            System.out.println("Processing login for: " + email);
            // Perform the login process, possibly checking against a database
            // For now, just print success
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
