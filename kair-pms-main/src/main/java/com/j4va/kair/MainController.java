package com.j4va.kair;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.Node;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.io.IOException;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TabPane;
import javafx.stage.StageStyle;

public class MainController implements Initializable {

    @FXML
    private Label activeProjectsLabel;

    @FXML
    private Label completedTasksLabel;

    @FXML
    private Label teamMembersLabel;

    @FXML
    private Button viewProjectsButton;

    @FXML
    private Button teamDashboardButton;

    @FXML
    private Button createProjectButton; // NEW: Button to open Create Project popup

    @FXML
    private Tab projectsTab;

    @FXML
    private HBox projectsContainer;

    @FXML
    private TabPane mainTabPane;

    @FXML
    private HBox topBar;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDashboardCounters();
        loadProjectsTab();
        TopBar.initializeTopBar(topBar);

        viewProjectsButton.setOnAction(e -> {
            try {
                MainApplication.showViewProjectsScreen();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        teamDashboardButton.setOnAction(e -> {
            try {
                MainApplication.showTeamDashboardScreen();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        if (createProjectButton != null) {
            createProjectButton.setOnAction(e -> openCreateProjectPopup());
        }
    }

    public void handleKanban(MouseEvent mouseEvent) {
        try {
            MainApplication.showKanbanScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDashboardCounters() {
        loadActiveProjects();
        loadCompletedTasks();
        loadTeamMembers();
    }

    private void loadActiveProjects() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT COUNT(*) FROM project WHERE status = 'active'"
            );
            ResultSet rs = ps.executeQuery();
            if (rs.next()) activeProjectsLabel.setText(String.valueOf(rs.getInt(1)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCompletedTasks() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT COUNT(*) FROM task"
            );
            ResultSet rs = ps.executeQuery();
            if (rs.next()) completedTasksLabel.setText(String.valueOf(rs.getInt(1)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTeamMembers() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT COUNT(id) FROM users"
            );
            ResultSet rs = ps.executeQuery();
            if (rs.next()) teamMembersLabel.setText(String.valueOf(rs.getInt(1)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadDashboardData() {
        loadCompletedTasks();
        loadTeamMembers();
    }

    // NEW: Method to open Create Project popup
    private void openCreateProjectPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("create_project.fxml"));
            Parent root = loader.load();

            Stage popupStage = new Stage();
            popupStage.setTitle("Create New Project");
            popupStage.initModality(Modality.APPLICATION_MODAL); // block interaction with main window
            popupStage.setScene(new Scene(root));
            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadProjectsTab() {
        if (projectsContainer == null) return;

        projectsContainer.getChildren().clear();

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT id, name, status FROM project ORDER BY id DESC"
            );
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int projectId = rs.getInt("id");
                String name = rs.getString("name");
                String status = rs.getString("status");

                // Create project card VBox
                HBox card = new HBox(5);
                card.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; " +
                        "-fx-border-radius: 6px; -fx-background-radius: 6px; -fx-padding: 15px;");

                // Project name label
                Label nameLabel = new Label(name);
                nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #6a0dad;");

                // Project status label
                Label statusLabel = new Label("Status: " + status);
                statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " +
                        (status.equalsIgnoreCase("Active") ? "#28a745" : "#fd7e14") + ";");

                card.getChildren().addAll(nameLabel, statusLabel);

                // Click to open Kanban tab
                card.setOnMouseClicked(e -> openKanban(projectId, name));

                // Add card to container
                projectsContainer.getChildren().add(card);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void openKanban(int projectId, String projectName) {
        try {
            // Load the Kanban FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/j4va/kair/kanban.fxml"));
            BorderPane kanbanRoot = loader.load();

            // Get the controller and set the project
            KanbanController kanbanController = loader.getController();
            kanbanController.setProject(projectId, projectName);

            // Get the current stage
            Stage stage = (Stage) projectsContainer.getScene().getWindow();

            // Set the new scene
            Scene kanbanScene = new Scene(kanbanRoot);
            stage.setScene(kanbanScene);

            // Initialize the top bar in the new scene
            TopBar.initializeTopBar(kanbanRoot);

        } catch (IOException e) {
            e.printStackTrace();
        }



    }
    public void initializeDashboard() {
        loadDashboardData();
    }
    }

