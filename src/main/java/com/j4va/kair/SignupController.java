package com.j4va.kair;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

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

        // Basic validation
        if (email != null && !email.trim().isEmpty() && email.contains("@")) {
            System.out.println("Signup initiated for: " + email);

            // No try/catch needed because showPasswordScreen no longer throws IOException
            MainApplication.showPasswordScreen(email);

        } else {
            System.out.println("Please enter a valid email address");
        }
    }

    @FXML
    private void handleGoogleSignup() {
        System.out.println("Google signup clicked");
        MainApplication.showMainScreen();
    }

    @FXML
    private void handleMicrosoftSignup() {
        System.out.println("Microsoft signup clicked");
        MainApplication.showMainScreen();
    }

    @FXML
    private void handleAppleSignup() {
        System.out.println("Apple signup clicked");
        MainApplication.showMainScreen();
    }

    @FXML
    private void handleSlackSignup() {
        System.out.println("Slack signup clicked");
        MainApplication.showMainScreen();
    }

    @FXML
    private void handleBackToLogin(MouseEvent event) {
        System.out.println("Back to login clicked");
        MainApplication.showLoginScreen();
    }
}
