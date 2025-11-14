package com.j4va.kair;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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
import java.util.List;

public class ProjectListController {

    @FXML private TableView<Project> projectTable;
    @FXML private TableColumn<Project, String> colWork;
    @FXML private TableColumn<Project, String> colAssignee;
    @FXML private TableColumn<Project, String> colPriority;
    @FXML private TableColumn<Project, String> colStatus;
    @FXML private TableColumn<Project, LocalDate> colCreated;
    @FXML private TableColumn<Project, LocalDate> colDue;

    @FXML
    public void initialize() {
        loadProjects();

        // double-click row to open Kanban (if kanban loads by project)
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

    private void loadProjects() {
        List<Project> projects = DatabaseService.getAllProjects();

        colWork.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getWork()));
        colAssignee.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAssignee()));
        colPriority.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPriority()));
        colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        colCreated.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCreatedDate()));
        colDue.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDueDate()));

        projectTable.getItems().setAll(projects);
    }

    @FXML
    private void openCreateProject() {
        MainApplication.showCreateProjectScreen();
    }

    @FXML
    private void handleBack() {
        MainApplication.showMainScreen();
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
            e.printStackTrace();
        }
    }
}
