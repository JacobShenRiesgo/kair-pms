package com.j4va.kair;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controller for the top bar that handles window controls and logout functionality.
 */
public class TopBarController {

    @FXML private Label titleLabel;
    @FXML private Label subtitleLabel;
    @FXML private Button logoutBtn;
    @FXML private Label minimizeBtn;
    @FXML private Label maximizeBtn;
    @FXML private Label closeBtn;

    @FXML
    private void initialize() {
        // Set up logout button handler
        if (logoutBtn != null) {
            logoutBtn.setOnAction(e -> handleLogout());
            // Hide logout button by default (will be shown only for authenticated screens)
            logoutBtn.setVisible(false);
            logoutBtn.setManaged(false);
        }

        // Set up window control buttons
        setupWindowControls();
    }

    /**
     * Shows the logout button for authenticated screens.
     */
    public void showLogoutButton() {
        if (logoutBtn != null) {
            logoutBtn.setVisible(true);
            logoutBtn.setManaged(true);
        }
    }

    /**
     * Hides the logout button for unauthenticated screens.
     */
    public void hideLogoutButton() {
        if (logoutBtn != null) {
            logoutBtn.setVisible(false);
            logoutBtn.setManaged(false);
        }
    }

    /**
     * Sets up window control button functionality.
     */
    private void setupWindowControls() {
        if (minimizeBtn != null) {
            minimizeBtn.setOnMouseClicked(e -> {
                Stage stage = (Stage) minimizeBtn.getScene().getWindow();
                stage.setIconified(true);
            });
        }

        if (maximizeBtn != null) {
            maximizeBtn.setOnMouseClicked(e -> {
                Stage stage = (Stage) maximizeBtn.getScene().getWindow();
                stage.setMaximized(!stage.isMaximized());
                maximizeBtn.setText(stage.isMaximized() ? "❐" : "⬜");
            });
        }

        if (closeBtn != null) {
            closeBtn.setOnMouseClicked(e -> {
                Stage stage = (Stage) closeBtn.getScene().getWindow();
                stage.close();
            });
        }
    }

    /**
     * Handles logout functionality with confirmation dialog.
     */
    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to log out?");

        // Create custom buttons
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        alert.getButtonTypes().setAll(yesButton, noButton);

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == yesButton) {
                // Clear the current user session for security
                Session.setCurrentUser(null);
                // User confirmed logout - navigate to signup screen
                navigateToSignup();
            }
            // If No button is clicked, dialog simply closes (no action needed)
        });
    }

    /**
     * Navigates to the signup screen after logout.
     */
    private void navigateToSignup() {
        // Use MainApplication's navigation method for consistency
        MainApplication.showSignupScreen();
    }
}
