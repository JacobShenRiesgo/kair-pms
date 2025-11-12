package com.j4va.kair;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private CheckBox rememberMeCheckBox;

    @FXML
    private Button continueButton;

    @FXML
    private Button googleButton;

    @FXML
    private Button microsoftButton;

    @FXML
    private Button appleButton;

    @FXML
    private Button slackButton;

    @FXML
    private void handleContinue() {
        String email = emailField.getText();
        boolean rememberMe = rememberMeCheckBox.isSelected();

        // checking if email is empty
        if (email != null && !email.trim().isEmpty()) {
            System.out.println("Login successful for: " + email + ", Remember me: " + rememberMe);

            // main screen
            try {
                MainApplication.showMainScreen();
            } catch (IOException e) {
                System.err.println("Error loading main screen: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Please enter an email address");
            // error handling
        }
    }

    @FXML
    private void handleGoogleLogin() {
        System.out.println("Google login clicked");
        // navigate to main screen
        try {
            MainApplication.showMainScreen();
        } catch (IOException e) {
            System.err.println("Error loading main screen: " + e.getMessage());
        }
    }

    @FXML
    private void handleMicrosoftLogin() {
        System.out.println("Microsoft login clicked");
        // navigate to main screen
        try {
            MainApplication.showMainScreen();
        } catch (IOException e) {
            System.err.println("Error loading main screen: " + e.getMessage());
        }
    }

    @FXML
    private void handleAppleLogin() {
        System.out.println("Apple login clicked");
        // navigate
        try {
            MainApplication.showMainScreen();
        } catch (IOException e) {
            System.err.println("Error loading main screen: " + e.getMessage());
        }
    }

    @FXML
    private void handleSlackLogin() {
        System.out.println("Slack login clicked");
        // navigate
        try {
            MainApplication.showMainScreen();
        } catch (IOException e) {
            System.err.println("Error loading main screen: " + e.getMessage());
        }
    }

    @FXML
    private void handleCreateAccount(MouseEvent event) {
        System.out.println("Create account clicked");
        try {
            MainApplication.showSignupScreen();
        } catch (IOException e) {
            System.err.println("Error loading signup screen: " + e.getMessage());
        }
    }
}
