package com.j4va.kair;

import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // initialize main application
        System.out.println("Main application loaded successfully!");
    }

    public void handleKanban(MouseEvent mouseEvent) {
        try {
            MainApplication.showKanbanScreen();
        } catch (IOException e) {
            System.err.println("Error loading kanban screen: " + e.getMessage());
        }
    }

    // Add methods for handling main application actions here
}
