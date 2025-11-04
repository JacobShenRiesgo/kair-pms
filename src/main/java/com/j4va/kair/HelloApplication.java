package com.j4va.kair;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("System.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        TopBar.setupTransparentStage(stage, scene, "Kair");
        stage.show();
    }
}
