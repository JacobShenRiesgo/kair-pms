package com.j4va.kair;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;




public class MainController {
    @FXML private HBox topBar;
    public void initialize() {
        TopBar.initializeTopBar(topBar);

    }
}
