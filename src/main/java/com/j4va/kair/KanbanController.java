package com.j4va.kair;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class KanbanController {

    @FXML private VBox todoTasks;
    @FXML private VBox doingTasks;
    @FXML private VBox doneTasks;
    @FXML private BorderPane kanbanRoot;
    @FXML private VBox todoColumn;
    @FXML private VBox doingColumn;
    @FXML private VBox doneColumn;


    private Project currentProject; // if you wire projects later

    @FXML
    private void initialize() {
        enableColumn(todoTasks);
        enableColumn(doingTasks);
        enableColumn(doneTasks);
    }

    // Allow dropping cards on a column
    private void enableColumn(VBox column) {
        column.setOnDragOver(event -> {
            if (event.getGestureSource() != column && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        column.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                String taskId = db.getString();
                Node card = findAndRemoveTask(taskId);
                if (card != null) {
                    column.getChildren().add(card);
                    // update task status stored in userData
                    TaskData td = (TaskData) card.getUserData();
                    if (td != null) {
                        if (column == todoTasks) td.setStatus("TODO");
                        else if (column == doingTasks) td.setStatus("IN_PROGRESS");
                        else if (column == doneTasks) td.setStatus("DONE");
                    }
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    // Search each column for the node that contains the TaskData with id, remove and return node
    private Node findAndRemoveTask(String id) {
        for (VBox col : new VBox[]{todoTasks, doingTasks, doneTasks}) {
            for (Node n : col.getChildren()) {
                Object ud = n.getUserData();
                if (ud instanceof TaskData) {
                    if (((TaskData) ud).getId().equals(id)) {
                        col.getChildren().remove(n);
                        return n;
                    }
                }
            }
        }
        return null;
    }

    // open popup -> TaskPopup must call setOnTaskCreated(Consumer<TaskData>)
    @FXML
    private void openCreateTaskPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("task_popup.fxml"));
            Parent root = loader.load();
            TaskPopup popup = loader.getController();

            // When popup creates a TaskData, add it to the correct column
            popup.setOnTaskCreated(this::addTaskToColumn);

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Create Task");
            popupStage.setScene(new Scene(root));
            popupStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goBack() {
        try {
            MainApplication.showMainScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    // place TaskData in the right column according to status
    private void addTaskToColumn(TaskData task) {
        switch (task.getStatus()) {
            case "IN_PROGRESS" -> addCard(task, doingTasks);
            case "DONE" -> addCard(task, doneTasks);
            default -> addCard(task, todoTasks);
        }
    }

    private void addCard(TaskData task, VBox column) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("taskcard.fxml"));
            Node card = loader.load();
            TaskCardController c = loader.getController();
            c.setData(task);

            // store the TaskData on the node to find it later during drag/drop
            card.setUserData(task);

            // make the card draggable
            card.setOnDragDetected(e -> {
                Dragboard db = card.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(task.getId());
                db.setContent(content);
                e.consume();
            });

            column.getChildren().add(card);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // optional: you can call this from Dashboard when opening project
    public void loadProject(Project project) {
        this.currentProject = project;
        // TODO: load tasks for this project from DB and call addCard(...) for each
        todoTasks.getChildren().clear();
        doingTasks.getChildren().clear();
        doneTasks.getChildren().clear();
    }
}
