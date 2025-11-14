package com.j4va.kair;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.util.List;

public class CreateProjectController {

    @FXML private TextField nameField;
    @FXML private ComboBox<User> assigneeBox;
    @FXML private ComboBox<String> statusBox;
    @FXML private DatePicker dueDatePicker;

    @FXML
    public void initialize() {
        // load users from DB
        List<User> users = DatabaseService.getUsers();
        assigneeBox.getItems().addAll(users);

        // default choices for status
        statusBox.getItems().addAll("To Do", "Ongoing", "Done");

        // If a user is logged in, preselect them
        User cur = Session.getCurrentUser();
        if (cur != null) {
            for (User u : users) {
                if (u.getId() == cur.getId()) {
                    assigneeBox.setValue(u);
                    break;
                }
            }
        }

        // default due date = +7 days
        dueDatePicker.setValue(LocalDate.now().plusDays(7));
    }

    @FXML
    private void handleCreate() {
        String name = nameField.getText();
        if (name == null || name.isBlank()) {
            System.err.println("Project name is required");
            return;
        }

        User assignee = assigneeBox.getValue();
        String status = statusBox.getValue() != null ? statusBox.getValue() : "To Do";
        LocalDate start = LocalDate.now();
        LocalDate end = dueDatePicker.getValue();

        int projectId = DatabaseService.insertProject(name, start, end, status);
        if (projectId <= 0) {
            System.err.println("Failed to create project");
            return;
        }

        if (assignee != null) {
            DatabaseService.assignUserToProject(projectId, assignee.getId());
        }

        DatabaseService.insertProjectStatus(projectId, status);

        // return to project list screen
        MainApplication.showProjectListScreen();
    }

    @FXML
    private void handleBack() {
        MainApplication.showProjectListScreen();
    }
}
