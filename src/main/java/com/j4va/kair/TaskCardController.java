package com.j4va.kair;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TaskCardController {

    private KanbanController kanbanController; // reference to parent KanbanController

    @FXML
    private TextField titleField; // for inputting title
    @FXML
    private Label titleLabel;

    @FXML
    private Label priorityLabel;



    public void setKanbanController(KanbanController kanbanController) {
        this.kanbanController = kanbanController;
    }

    @FXML
    private void saveTask() {
        String title = titleField.getText();
        String priority = priorityLabel.getText();

        if (kanbanController != null) {
            kanbanController.addTaskFromPopup(title, priority);
        }

        // Close popup
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }
    public void setData(String title, String priority) {
        titleLabel.setText(title);
        priorityLabel.setText(priority);
    }

}
