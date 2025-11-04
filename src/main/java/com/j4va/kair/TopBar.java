package com.j4va.kair;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class TopBar{
    private static double xOffset = 0;
    private static double yOffset = 0;
    public static void setupTransparentStage(Stage stage, Scene scene, String title) {
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.setTitle(title);
        Node root = scene.getRoot();
        Rectangle clip = new Rectangle();
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        clip.widthProperty().bind(root.layoutBoundsProperty().map(Bounds::getWidth));
        clip.heightProperty().bind(root.layoutBoundsProperty().map(Bounds::getHeight));
        root.setClip(clip);
    }
    public static void enableWindowDragging(Stage stage, Node dragArea) {
        dragArea.setOnMousePressed((MouseEvent e) -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });

        dragArea.setOnMouseDragged((MouseEvent e) -> {
            if (!stage.isMaximized()) {
                stage.setX(e.getScreenX() - xOffset);
                stage.setY(e.getScreenY() - yOffset);
            }
        });

        dragArea.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                stage.setMaximized(!stage.isMaximized());
            }
        });
    }

    public static void setupWindowButtons(Stage stage, Label minimizeBtn, Label maximizeBtn, Label closeBtn) {
        if (minimizeBtn != null) {
            minimizeBtn.setOnMouseClicked(e -> stage.setIconified(true));
        }

        if (closeBtn != null) {
            closeBtn.setOnMouseClicked(e -> stage.close());
        }

        if (maximizeBtn != null) {
            maximizeBtn.setOnMouseClicked(e -> {
                stage.setMaximized(!stage.isMaximized());
                maximizeBtn.setText(stage.isMaximized() ? "❐" : "⬜");
            });
        }
    }

    public static void initializeTopBar(Node topBar) {
        Platform.runLater(() -> {
            Stage stage = (Stage) topBar.getScene().getWindow();

            Label minimizeBtn = (Label) topBar.lookup("#minimizeBtn");
            Label maximizeBtn = (Label) topBar.lookup("#maximizeBtn");
            Label closeBtn = (Label) topBar.lookup("#closeBtn");

            enableWindowDragging(stage, topBar);
            setupWindowButtons(stage, minimizeBtn, maximizeBtn, closeBtn);


        });
    }
}


