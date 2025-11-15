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

        if (email != null && !email.trim().isEmpty() && email.contains("@")) {
            // Check if email already exists
            if (UserDAO.emailExists(email)) {
                System.out.println("Email already registered!");
                return;
            }

            try {
                MainApplication.showPasswordScreen(email);
            } catch (Exception e) {
                System.err.println("Error loading password screen: " + e.getMessage());
            }
        } else {
            System.out.println("Please enter a valid email address");
        }
    }


    @FXML
    private void handleGoogleSignup() {
        System.out.println("Google signup clicked");
        // For now, just navigate to main screen
        try {
            MainApplication.showMainScreen();
        } catch (Exception e) {
            System.err.println("Error loading main screen: " + e.getMessage());
        }
    }

    @FXML
    private void handleMicrosoftSignup() {
        System.out.println("Microsoft signup clicked");
        try {
            MainApplication.showMainScreen();
        } catch (Exception e) {
            System.err.println("Error loading main screen: " + e.getMessage());
        }
    }

    @FXML
    private void handleAppleSignup() {
        System.out.println("Apple signup clicked");
        try {
            MainApplication.showMainScreen();
        } catch (Exception e) {
            System.err.println("Error loading main screen: " + e.getMessage());
        }
    }

    @FXML
    private void handleSlackSignup() {
        System.out.println("Slack signup clicked");
        try {
            MainApplication.showMainScreen();
        } catch (Exception e) {
            System.err.println("Error loading main screen: " + e.getMessage());
        }
    }

    @FXML
    private void handleBackToLogin(MouseEvent event) {
        System.out.println("Back to login clicked");
        try {
            MainApplication.showLoginScreen();
        } catch (Exception e) {
            System.err.println("Error loading login screen: " + e.getMessage());
        }
    }
}
