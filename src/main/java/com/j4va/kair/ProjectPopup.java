package com.j4va.kair;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.function.Consumer;

public class ProjectPopup {

    @FXML private TextField nameField;
    @FXML private TextField assigneeField;
    @FXML private ComboBox<String> priorityBox;
    @FXML private ComboBox<String> statusBox;
    @FXML private DatePicker dueDatePicker;

    private Consumer<Project> callback;

    public void setOnProjectCreated(Consumer<Project> callback) {
        this.callback = callback;
    }

    @FXML
    private void onCreate() {
        Project project = new Project(
                nameField.getText(),
                assigneeField.getText(),
                priorityBox.getValue(),
                statusBox.getValue(),
                LocalDate.now(),
                dueDatePicker.getValue()

        );

        if (callback != null) callback.accept(project);

        closePopup();
    }

    @FXML
    private void onCancel() {
        closePopup();
    }

    private void closePopup() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}
