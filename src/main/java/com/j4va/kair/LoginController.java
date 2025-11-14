package com.j4va.kair;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

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

        if (email != null && !email.trim().isEmpty()) {
            // try to load user record from DB
            User user = DatabaseService.getUserByEmail(email);
            if (user != null) {
                Session.setCurrentUser(user);
                System.out.println("Login successful for: " + user.getEmail());
                MainApplication.showMainScreen();
            } else {
                System.out.println("No such user in DB: " + email);
                // optionally show dialog or create user automatically
                MainApplication.showMainScreen(); // keep previous behavior if desired
            }
        } else {
            System.out.println("Please enter an email address");
        }
    }


    @FXML
    private void handleGoogleLogin() {
        System.out.println("Google login clicked");
        MainApplication.showMainScreen();
    }

    @FXML
    private void handleMicrosoftLogin() {
        System.out.println("Microsoft login clicked");
        MainApplication.showMainScreen();
    }

    @FXML
    private void handleAppleLogin() {
        System.out.println("Apple login clicked");
        MainApplication.showMainScreen();
    }

    @FXML
    private void handleSlackLogin() {
        System.out.println("Slack login clicked");
        MainApplication.showMainScreen();
    }

    @FXML
    private void handleCreateAccount(MouseEvent event) {
        System.out.println("Create account clicked");
        MainApplication.showSignupScreen();
    }
}
