package com.j4va.kair;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class SignupController {

    @FXML
    private TextField emailField;

    @FXML
    private Button signUpButton;

    @FXML
    private Button googleButton;

    @FXML
    private Button microsoftButton;

    @FXML
    private Button appleButton;

    @FXML
    private Button slackButton;

    @FXML
    private void handleSignUp() {
        String email = emailField.getText();

        // Simple validation - just check if email is not empty and has @ symbol
        if (email != null && !email.trim().isEmpty() && email.contains("@")) {
            System.out.println("Signup initiated for: " + email);

            // Navigate to password screen
            try {
                MainApplication.showPasswordScreen(email);
            } catch (IOException e) {
                System.err.println("Error loading password screen: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Please enter a valid email address");
            // You could show an error dialog here
        }
    }

    @FXML
    private void handleGoogleSignup() {
        System.out.println("Google signup clicked");
        // For now, just navigate to main screen
        try {
            MainApplication.showMainScreen();
        } catch (IOException e) {
            System.err.println("Error loading main screen: " + e.getMessage());
        }
    }

    @FXML
    private void handleMicrosoftSignup() {
        System.out.println("Microsoft signup clicked");
        try {
            MainApplication.showMainScreen();
        } catch (IOException e) {
            System.err.println("Error loading main screen: " + e.getMessage());
        }
    }

    @FXML
    private void handleAppleSignup() {
        System.out.println("Apple signup clicked");
        try {
            MainApplication.showMainScreen();
        } catch (IOException e) {
            System.err.println("Error loading main screen: " + e.getMessage());
        }
    }

    @FXML
    private void handleSlackSignup() {
        System.out.println("Slack signup clicked");
        try {
            MainApplication.showMainScreen();
        } catch (IOException e) {
            System.err.println("Error loading main screen: " + e.getMessage());
        }
    }

    @FXML
    private void handleBackToLogin(MouseEvent event) {
        System.out.println("Back to login clicked");
        try {
            MainApplication.showLoginScreen();
        } catch (IOException e) {
            System.err.println("Error loading login screen: " + e.getMessage());
        }
    }
}
