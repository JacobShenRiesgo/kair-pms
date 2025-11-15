package com.j4va.kair;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainApplication extends Application {

    private static Stage primaryStage;
    private static BorderPane rootLayout;   // Holds the whole application

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        // Transparent custom window
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        // Root layout (shared by all screens)
        rootLayout = new BorderPane();

        Scene scene = new Scene(rootLayout, 1200, 800);

        // Transparent top bar setup
        TopBar.setupTransparentStage(primaryStage, scene);

        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.show();

        // FIRST screen to show
        showLoginScreen();
    }

    /* ---------------------------------------------------------
       UNIVERSAL SCREEN LOADER
       --------------------------------------------------------- */
    public static void setView(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(fxml));
            Parent view = loader.load();

            // Load screen into CENTER of main window
            rootLayout.setCenter(view);

            // Reinitialize topbar (if screen contains #topBar)
            Node topBar = view.lookup("#topBar");
            if (topBar != null) {
                TopBar.initializeTopBar(topBar);

                // Control logout button visibility based on screen type
                if (isAuthenticatedScreen(fxml)) {
                    TopBar.showLogoutButton(topBar);
                } else {
                    TopBar.hideLogoutButton(topBar);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Determines if a screen requires authentication and should show the logout button.
     */
    private static boolean isAuthenticatedScreen(String fxml) {
        // Define which screens should show the logout button
        return fxml.equals("main.fxml") ||
               fxml.equals("kanban.fxml") ||
               fxml.equals("projectlist.fxml") ||
               fxml.equals("create_project.fxml");
    }

    /* ---------------------------------------------------------
       NAMED SCREEN SWITCHERS
       --------------------------------------------------------- */

    public static void showLoginScreen() {
        setView("login.fxml");
    }

    public static void showSignupScreen() {
        setView("signup.fxml");
    }

    public static void showMainScreen() {
        setView("main.fxml");
    }

    public static void showKanbanScreen() {
        setView("kanban.fxml");
    }

    public static void showProjectListScreen() {
        setView("projectlist.fxml");
    }

    public static void showCreateProjectScreen() {
        setView("create_project.fxml");
    }

    /* ---------------------------------------------------------
       SPECIAL CASE: Password Screen (needs email)
       --------------------------------------------------------- */

    public static void showPasswordScreen(String email) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("password.fxml"));
            Parent view = loader.load();

            PasswordController controller = loader.getController();
            controller.setUserEmail(email);

            rootLayout.setCenter(view);
            Node topBar = view.lookup("#topBar");
            if (topBar != null) {
                TopBar.initializeTopBar(topBar);
                // Password screen is not authenticated, so hide logout button
                TopBar.hideLogoutButton(topBar);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ---------------------------------------------------------
       GET PRIMARY STAGE
       --------------------------------------------------------- */

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch();
    }
}
