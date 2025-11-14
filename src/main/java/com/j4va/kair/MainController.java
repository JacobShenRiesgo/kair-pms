package com.j4va.kair;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Main application loaded successfully!");
    }

    public void handleKanban(MouseEvent mouseEvent) {
        MainApplication.showKanbanScreen();
    }
    @FXML
    private void handleCreateProject() {
        try {
            MainApplication.showCreateProjectScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // You can add other navigation handlers here
}
