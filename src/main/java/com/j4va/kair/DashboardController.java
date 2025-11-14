package com.j4va.kair;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class DashboardController {

    @FXML private TableView<Project> projectTable;

    @FXML private TableColumn<Project, String> colName;
    @FXML private TableColumn<Project, String> colAssignee;
    @FXML private TableColumn<Project, String> colPriority;
    @FXML private TableColumn<Project, String> colStatus;
    @FXML private TableColumn<Project, LocalDate> colCreated;
    @FXML private TableColumn<Project, LocalDate> colDue;


    private final ObservableList<Project> projectList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        // Bind columns
        colName.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getWork()));

        colAssignee.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getAssignee()));

        colPriority.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getPriority()));

        colStatus.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));

        colCreated.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getCreatedDate()));

        colDue.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getDueDate()));


        projectTable.setItems(projectList);

        // Double-click to open project Kanban
        projectTable.setRowFactory(tv -> {
            TableRow<Project> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && !row.isEmpty()) {
                    openProjectKanban(row.getItem());
                }
            });
            return row;
        });
    }

    @FXML
    private void openCreateProjectPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("project_popup.fxml"));
            Parent root = loader.load();

            ProjectPopup popup = loader.getController();
            popup.setOnProjectCreated(projectList::add);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Create Project");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            System.err.println("Failed to load project popup: " + e.getMessage());
        }
    }

    private void openProjectKanban(Project project) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("kanban.fxml"));
            Parent root = loader.load();

            KanbanController controller = loader.getController();
            controller.loadProject(project);

            Stage stage = new Stage();
            stage.setTitle("Project: " + project.getWork());
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            System.err.println("Failed to open Kanban: " + e.getMessage());
        }
    }
}
