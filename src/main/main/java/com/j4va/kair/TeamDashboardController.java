package com.j4va.kair;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class TeamDashboardController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private Label totalMembersLabel;

    @FXML
    private Label activeTasksLabel;

    @FXML
    private Label completedTasksLabel;

    @FXML
    private ListView<String> teamMembersList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDashboardCounters();
        backButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/j4va/kair/main.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) backButton.getScene().getWindow();
                stage.setScene(new Scene(root));

                // Re-initialize topbar
                Node topBar = root.lookup("#topBar"); // make sure your fx:id="topBar" in main.fxml
                TopBar.initializeTopBar(topBar);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadDashboardCounters() {
        try (Connection conn = DatabaseConnection.getConnection()) {

            // Total Members
            PreparedStatement ps1 = conn.prepareStatement("SELECT COUNT(email) FROM users");
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) totalMembersLabel.setText(String.valueOf(rs1.getInt(1)));

            // Active Tasks
            PreparedStatement ps2 = conn.prepareStatement("SELECT COUNT(*) FROM task WHERE priority IS NOT NULL");
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) activeTasksLabel.setText(String.valueOf(rs2.getInt(1)));

            // Completed Tasks
            PreparedStatement ps3 = conn.prepareStatement("SELECT COUNT(*) FROM task");
            ResultSet rs3 = ps3.executeQuery();
            if (rs3.next()) completedTasksLabel.setText(String.valueOf(rs3.getInt(1)));

            // Load Team Member emails into ListView
            PreparedStatement ps4 = conn.prepareStatement("SELECT email FROM users");
            ResultSet rs4 = ps4.executeQuery();
            ObservableList<String> emails = FXCollections.observableArrayList();
            while (rs4.next()) {
                emails.add(rs4.getString("email"));
            }
            teamMembersList.setItems(emails);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}