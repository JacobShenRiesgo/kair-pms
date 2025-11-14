package com.j4va.kair;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import java.io.IOException;

public class MainApplication extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        //Set original top bar to transparent
        stage.initStyle(StageStyle.TRANSPARENT);
        // appears when you launch the project
        showLoginScreen();
        stage.setResizable(true);
        stage.setMinWidth(600);
        stage.setMinHeight(500);
        stage.show();
    }

    public static void showLoginScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 950, 750);
        TopBar.setupTransparentStage(primaryStage, scene);
        TopBar.initializeTopBar(scene.getRoot().lookup("#topBar"));

    }

    public static void showMainScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("main.fxml"));
        Parent root = loader.load();

        // MainController is now initialized properly
        MainController controller = loader.getController();
        controller.initializeDashboard();   // Call your method to load stats if needed

        Stage stage = MainApplication.getPrimaryStage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void showKanbanScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("kanban.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        TopBar.setupTransparentStage(primaryStage, scene);
        TopBar.initializeTopBar(scene.getRoot().lookup("#topBar"));
    }

    public static void showSignupScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("signup.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 950, 750);
        TopBar.setupTransparentStage(primaryStage, scene);
        TopBar.initializeTopBar(scene.getRoot().lookup("#topBar"));
    }

    public static void showPasswordScreen(String email) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("password.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 950, 750);

        // Get the controller and set the email after FXML is loaded
        PasswordController controller = fxmlLoader.getController();
        if (controller != null) {
            controller.setUserEmail(email);
        }
        TopBar.setupTransparentStage(primaryStage, scene);
        TopBar.initializeTopBar(scene.getRoot().lookup("#topBar"));
    }
    public static void showViewProjectsScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("ViewProjects.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800); // adjust width/height if needed
        TopBar.setupTransparentStage(primaryStage, scene);
        TopBar.initializeTopBar(scene.getRoot().lookup("#topBar"));
    }

    public static void showTeamDashboardScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("TeamDashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        TopBar.setupTransparentStage(primaryStage, scene);
        TopBar.initializeTopBar(scene.getRoot().lookup("#topBar"));
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch();
    }

}
