package com.j4va.kair;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.io.IOException;

public class ViewProjectsController implements Initializable {

    @FXML
    private VBox projectsContainer;

    @FXML
    private Button backButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadProjects();

        backButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/j4va/kair/main.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) backButton.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadProjects() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT name, status FROM project ORDER BY id DESC"
            );
            ResultSet rs = ps.executeQuery();

            projectsContainer.getChildren().clear(); // remove placeholder cards

            HBox row = new HBox(15); // Row container for cards
            int cardCount = 0;

            while (rs.next()) {
                String projectName = rs.getString("name");
                String status = rs.getString("status");

                VBox card = new VBox(10);
                card.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 6px;" +
                        "-fx-background-radius: 6px; -fx-padding: 15px; -fx-min-width: 200px;");

                Label nameLabel = new Label(projectName);
                nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #6a0dad;");

                Label statusLabel = new Label("Status: " + status);
                statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " +
                        (status.equalsIgnoreCase("Active") ? "#28a745" :
                                status.equalsIgnoreCase("Completed") ? "#6c757d" : "#fd7e14") + ";");

                card.getChildren().addAll(new Label("Project Name"), nameLabel, statusLabel);

                row.getChildren().add(card);
                cardCount++;

                // wrap row every 3 cards
                if (cardCount % 3 == 0) {
                    projectsContainer.getChildren().add(row);
                    row = new HBox(15);
                }
            }

            // Add remaining cards
            if (!row.getChildren().isEmpty()) {
                projectsContainer.getChildren().add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
