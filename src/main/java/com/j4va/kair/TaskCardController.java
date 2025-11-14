package com.j4va.kair;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

//Controller of the Task card
public class TaskCardController {
    @FXML
    private Label titleLabel;
    @FXML
    private Label priorityLabel;

    public void setData(TaskData data) {
        titleLabel.setText(data.getTitle());
        priorityLabel.setText("Priority: " + data.getPriority());

    }
}
