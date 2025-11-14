package com.j4va.kair;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class TaskPopup {

    @FXML private TextField titleField;
    @FXML private TextArea descriptionField;

    private Consumer<TaskData> callback;

    public TaskPopup() {
    }

    public TaskPopup(Consumer<TaskData> callback) {
        this.callback = callback;
    }

    public void setOnTaskCreated(Consumer<TaskData> callback) {
        this.callback = callback;
    }

    @FXML
    private void onCreate() {
        String title = titleField.getText();
        String desc = descriptionField.getText();

        TaskData task = new TaskData(title, desc, "TODO");

        if (callback != null) callback.accept(task);

        closePopup();
    }

    @FXML
    private void onCancel() {
        closePopup();
    }

    private void closePopup() {
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }
}
