package com.j4va.kair;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML private HBox topBar;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // initialize main application
        TopBar.initializeTopBar(topBar);
        System.out.println("Main application loaded successfully!");
    }

    // Add methods for handling main application actions here
}
