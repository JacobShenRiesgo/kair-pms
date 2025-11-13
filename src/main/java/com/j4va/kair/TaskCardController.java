package com.j4va.kair;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

//Controller of the Task card
public class TaskCardController {
    @FXML
    private Label titleLabel;
    @FXML private Label descriptionLabel;

    public void setData(TaskData data) {
        titleLabel.setText(data.getTitle());
        descriptionLabel.setText(data.getDescription());
    }
}
