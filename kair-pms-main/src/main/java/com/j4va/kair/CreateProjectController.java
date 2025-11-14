package com.j4va.kair;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

public class CreateProjectController implements Initializable {

    @FXML
    private TextField projectNameField;

    @FXML
    private DatePicker startDatePicker, endDatePicker;

    @FXML
    private ComboBox<String> statusCombo;

    @FXML
    private Button createButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate ComboBox here instead of FXML
        statusCombo.getItems().addAll("Active", "Pending", "Completed");
    }

    @FXML
    private void createProject() {
        String name = projectNameField.getText();
        String start = startDatePicker.getValue() != null ? startDatePicker.getValue().toString() : null;
        String end = endDatePicker.getValue() != null ? endDatePicker.getValue().toString() : null;
        String status = statusCombo.getValue();

        if (name == null || name.isEmpty() || start == null || end == null || status == null) {
            showAlert("Please fill in all fields.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO project (name, start_date, end_date, status) VALUES (?, ?, ?, ?)"
            );
            ps.setString(1, name);
            ps.setString(2, start);
            ps.setString(3, end);
            ps.setString(4, status);
            ps.executeUpdate();

            showAlert("Project created successfully!");
            closeWindow();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error creating project.");
        }
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) createButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
