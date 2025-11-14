package com.j4va.kair;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class PasswordController implements Initializable {

    @FXML private Label emailLabel;
    @FXML private PasswordField passwordField;
    @FXML private Button createAccountButton;

    @FXML private Label lengthRequirement;
    @FXML private Label uppercaseRequirement;
    @FXML private Label lowercaseRequirement;
    @FXML private Label numberRequirement;

    private String userEmail;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        passwordField.textProperty().addListener((observable, oldValue, newValue) ->
                validatePassword(newValue));
    }

    public void setUserEmail(String email) {
        this.userEmail = email;
        if (emailLabel != null) {
            emailLabel.setText(email);
        }
    }

    private void validatePassword(String password) {
        // Length
        if (password.length() >= 8) {
            lengthRequirement.setStyle("-fx-font-size: 11px; -fx-text-fill: #00875a;");
            lengthRequirement.setText("✓ At least 8 characters");
        } else {
            lengthRequirement.setStyle("-fx-font-size: 11px; -fx-text-fill: #de350b;");
            lengthRequirement.setText("• At least 8 characters");
        }

        // Uppercase
        if (password.matches(".*[A-Z].*")) {
            uppercaseRequirement.setStyle("-fx-font-size: 11px; -fx-text-fill: #00875a;");
            uppercaseRequirement.setText("✓ At least 1 uppercase letter");
        } else {
            uppercaseRequirement.setStyle("-fx-font-size: 11px; -fx-text-fill: #de350b;");
            uppercaseRequirement.setText("• At least 1 uppercase letter");
        }

        // Lowercase
        if (password.matches(".*[a-z].*")) {
            lowercaseRequirement.setStyle("-fx-font-size: 11px; -fx-text-fill: #00875a;");
            lowercaseRequirement.setText("✓ At least 1 lowercase letter");
        } else {
            lowercaseRequirement.setStyle("-fx-font-size: 11px; -fx-text-fill: #de350b;");
            lowercaseRequirement.setText("• At least 1 lowercase letter");
        }

        // Number
        if (password.matches(".*[0-9].*")) {
            numberRequirement.setStyle("-fx-font-size: 11px; -fx-text-fill: #00875a;");
            numberRequirement.setText("✓ At least 1 number");
        } else {
            numberRequirement.setStyle("-fx-font-size: 11px; -fx-text-fill: #de350b;");
            numberRequirement.setText("• At least 1 number");
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*[0-9].*");
    }

    @FXML
    private void handleCreateAccount() {
        String password = passwordField.getText();

        if (isPasswordValid(password)) {
            System.out.println("Account created successfully for: " + userEmail);
            MainApplication.showMainScreen(); // ← FIXED (no IOException)
        } else {
            System.out.println("Password does not meet requirements");
        }
    }

    @FXML
    private void handleBackToLogin(MouseEvent event) {
        System.out.println("Back to login clicked");
        MainApplication.showLoginScreen(); // ← FIXED (no IOException)
    }
}
