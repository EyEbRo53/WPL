package com.example.wpl;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SignupController {

    @FXML
    private TextField fullNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField confirmPasswordField;

    @FXML
    private void handleRegister() throws Exception{
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Error", "All fields must be filled out.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match.");
            return;
        }

        //Actual registration logic (e.g., saving these details in a database)
        int signUp_result = DB_Functions.signUp(fullName, email, password);

        if (signUp_result == 1){
            showAlert("Success", "Registration Successful.");
            handleBack();
        }
        else if(signUp_result==0)
            showAlert("Failure", "Registration failed: Error.");
        else if(signUp_result==2)
            showAlert("Failure", "Email is already in use.");
        else if(signUp_result==3)
            showAlert("Failure", "Email not in valid format: Please enter a valid email address ending with '@gmail.com' and containing at least one character before it.");
    }

    @FXML
    private void handleBack() throws Exception {
        MainDashboard.loadScene("LoginPage.fxml");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}