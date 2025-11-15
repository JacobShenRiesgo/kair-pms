package com.j4va.kair;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;


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
    private Label emailErrorLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label passwordErrorLabel;

    @FXML
    private void handleContinue(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText(); // Get password from the new PasswordField

        // Check empty email
        if (email.isEmpty()) {
            emailErrorLabel.setText("Please enter your email.");
            emailErrorLabel.setVisible(true);
            return;
        } else {
            emailErrorLabel.setVisible(false);
        }

        // Check empty password
        if (password.isEmpty()) {
            passwordErrorLabel.setText("Please enter your password.");
            passwordErrorLabel.setVisible(true);
            return;
        } else {
            passwordErrorLabel.setVisible(false);
        }

        try {
            // Check if email exists
            if (!UserDAO.emailExists(email)) {
                emailErrorLabel.setText("Account not found, please register!");
                emailErrorLabel.setVisible(true);
                return;
            } else {
                emailErrorLabel.setVisible(false);
            }

            // Check if password is correct for that email
            if (!UserDAO.checkPassword(email, password)) {
                passwordErrorLabel.setText("Incorrect password!");
                passwordErrorLabel.setVisible(true);
                return;
            } else {
                passwordErrorLabel.setVisible(false);
            }

            // Both email and password are valid, navigate to main screen
            MainApplication.showMainScreen();

        } catch (Exception e) {
            e.printStackTrace();
            emailErrorLabel.setText("Error connecting to database.");
            emailErrorLabel.setVisible(true);
        }
    }




    @FXML
    private void handleGoogleLogin() {
        System.out.println("Google login clicked");
        // navigate to main screen
        try {
            MainApplication.showMainScreen();
        } catch (Exception e) {
            System.err.println("Error loading main screen: " + e.getMessage());
        }
    }

    @FXML
    private void handleMicrosoftLogin() {
        System.out.println("Microsoft login clicked");
        // navigate to main screen
        try {
            MainApplication.showMainScreen();
        } catch (Exception e) {
            System.err.println("Error loading main screen: " + e.getMessage());
        }
    }

    @FXML
    private void handleAppleLogin() {
        System.out.println("Apple login clicked");
        // navigate
        try {
            MainApplication.showMainScreen();
        } catch (Exception e) {
            System.err.println("Error loading main screen: " + e.getMessage());
        }
    }

    @FXML
    private void handleSlackLogin() {
        System.out.println("Slack login clicked");
        // navigate
        try {
            MainApplication.showMainScreen();
        } catch (Exception e) {
            System.err.println("Error loading main screen: " + e.getMessage());
        }
    }

    @FXML
    private void handleCreateAccount(MouseEvent event) {
        System.out.println("Create account clicked");
        try {
            MainApplication.showSignupScreen();
        } catch (Exception e) {
            System.err.println("Error loading signup screen: " + e.getMessage());
        }
    }
}
