package com.j4va.kair;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
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
    public static void setupTransparentStage(Stage stage, Scene scene) {
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
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

    private static void enableWindowResizing(Stage stage) {
        final double border = 8;
        final double minW = 770, minH = 385;

        Scene scene = stage.getScene();
        final boolean[] resizingLeft = {false}, resizingRight = {false}, resizingTop = {false}, resizingBottom = {false};
        final boolean[] resizingNW = {false}, resizingNE = {false}, resizingSE = {false}, resizingSW = {false};
        final double[] startX = new double[1], startY = new double[1];

        scene.setOnMouseMoved(e -> {
            double x = e.getX(), y = e.getY();
            double w = stage.getWidth(), h = stage.getHeight();

            resizingLeft[0] = x < border;
            resizingRight[0] = x > w - border;
            resizingTop[0] = y < border;
            resizingBottom[0] = y > h - border;

            resizingNW[0] = resizingLeft[0] && resizingTop[0];
            resizingNE[0] = resizingRight[0] && resizingTop[0];
            resizingSW[0] = resizingLeft[0] && resizingBottom[0];
            resizingSE[0] = resizingRight[0] && resizingBottom[0];

            if (resizingNW[0] || resizingSE[0]) scene.setCursor(Cursor.NW_RESIZE);
            else if (resizingNE[0] || resizingSW[0]) scene.setCursor(Cursor.NE_RESIZE);
            else if (resizingLeft[0] || resizingRight[0]) scene.setCursor(Cursor.H_RESIZE);
            else if (resizingTop[0] || resizingBottom[0]) scene.setCursor(Cursor.V_RESIZE);
            else scene.setCursor(Cursor.DEFAULT);
        });

        scene.setOnMousePressed(e -> {
            startX[0] = e.getScreenX();
            startY[0] = e.getScreenY();
        });

        scene.setOnMouseDragged(e -> {
            double dx = e.getScreenX() - startX[0];
            double dy = e.getScreenY() - startY[0];

            // Left
            if (resizingLeft[0]) {
                double newW = stage.getWidth() - dx;
                if (newW > minW) {
                    stage.setWidth(newW);
                    stage.setX(stage.getX() + dx);
                }
            }
            // Right
            if (resizingRight[0]) {
                double newW = stage.getWidth() + dx;
                if (newW > minW) stage.setWidth(newW);
            }
            // Top
            if (resizingTop[0]) {
                double newH = stage.getHeight() - dy;
                if (newH > minH) {
                    stage.setHeight(newH);
                    stage.setY(stage.getY() + dy);
                }
            }
            // Bottom
            if (resizingBottom[0]) {
                double newH = stage.getHeight() + dy;
                if (newH > minH) stage.setHeight(newH);
            }

            startX[0] = e.getScreenX();
            startY[0] = e.getScreenY();
        });

        scene.setOnMouseReleased(e -> {
            resizingLeft[0] = resizingRight[0] = resizingTop[0] = resizingBottom[0] =
                    resizingNW[0] = resizingNE[0] = resizingSE[0] = resizingSW[0] = false;
            scene.setCursor(Cursor.DEFAULT);
        });
    }



    public static void initializeTopBar(Node topBar) {
        Platform.runLater(() -> {
            Stage stage = (Stage) topBar.getScene().getWindow();

            Label minimizeBtn = (Label) topBar.lookup("#minimizeBtn");
            Label maximizeBtn = (Label) topBar.lookup("#maximizeBtn");
            Label closeBtn = (Label) topBar.lookup("#closeBtn");

            enableWindowDragging(stage, topBar);
            setupWindowButtons(stage, minimizeBtn, maximizeBtn, closeBtn);
            enableWindowResizing(stage);
        });
    }
}



