package com.j4va.kair;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class KanbanController {

    @FXML private HBox topBar;
    @FXML private ListView<TaskData> toDoList;
    @FXML private ListView<TaskData> inProgressList;
    @FXML private ListView<TaskData> doneList;

    @FXML private Button toDoButton;
    @FXML private Button inProgressButton;
    @FXML private Button doneButton;

    // Heights for dynamic resizing
    private static final double LIST_START_HEIGHT = 250; // empty list height
    private static final double TASK_CELL_HEIGHT = 40;  // match your task card height

    @FXML
    private void initialize() {
        setupListView(toDoList);
        setupListView(inProgressList);
        setupListView(doneList);

        // Buttons to create tasks in respective columns
        toDoButton.setOnAction(e -> openCreateTaskPopup(toDoList, "TODO"));
        inProgressButton.setOnAction(e -> openCreateTaskPopup(inProgressList, "IN_PROGRESS"));
        doneButton.setOnAction(e -> openCreateTaskPopup(doneList, "DONE"));

        // Initialize the ListViews with start height
        setInitialListHeight(toDoList);
        setInitialListHeight(inProgressList);
        setInitialListHeight(doneList);
    }

    /* Open popup to create a new task */
    private void openCreateTaskPopup(ListView<TaskData> column, String status) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("task_popup.fxml"));
            Parent root = loader.load();
            TaskPopup popup = loader.getController();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Create Task");
            stage.setScene(new Scene(root));

            // When task is created, add it and grow ListView
            popup.setOnTaskCreated(task -> {
                task.setStatus(status);
                addTaskAndKeepHeight(column, task);
            });

            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load task popup: " + e.getMessage());
        }
    }

    /* Setup ListView with task cells and drag-and-drop */
    private void setupListView(ListView<TaskData> listView) {
        listView.setCellFactory(lv -> {
            ListCell<TaskData> cell = new ListCell<>() {
                @Override
                protected void updateItem(TaskData task, boolean empty) {
                    super.updateItem(task, empty);
                    if (empty || task == null) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("taskcard.fxml"));
                            HBox taskCard = loader.load();
                            TaskCardController controller = loader.getController();
                            controller.setData(task);
                            setGraphic(taskCard);
                            setText(null);
                        } catch (IOException e) {
                            System.err.println("Failed to load task card: " + e.getMessage());
                        }
                    }
                }
            };
            enableDragAndDrop(cell, listView);
            return cell;
        });

        enableDragAndDrop(listView, listView);
    }

    /* Enable drag-and-drop for tasks */
    private void enableDragAndDrop(javafx.scene.Node node, ListView<TaskData> parentList) {
        if (node instanceof ListCell<?> cell) {
            cell.setOnDragDetected(event -> {
                if (cell.isEmpty()) return;

                Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(((TaskData) cell.getItem()).getId());
                db.setContent(content);

                cell.setOpacity(0.5);
                event.consume();
            });

            cell.setOnDragDone(event -> {
                cell.setOpacity(1);
                event.consume();
            });
        }

        node.setOnDragOver(event -> {
            if (event.getGestureSource() != parentList && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        node.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                String taskId = db.getString();
                TaskData movedTask = findAndRemoveTask(taskId);
                if (movedTask != null) {
                    if (parentList == toDoList) movedTask.setStatus("TODO");
                    else if (parentList == inProgressList) movedTask.setStatus("IN_PROGRESS");
                    else if (parentList == doneList) movedTask.setStatus("DONE");

                    addTaskAndKeepHeight(parentList, movedTask); // grow target list
                    success = true;
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

    /* Add task and keep ListView height fixed */
    private void addTaskAndKeepHeight(ListView<TaskData> listView, TaskData task) {
        listView.getItems().add(task);

        double newHeight = LIST_START_HEIGHT + listView.getItems().size() * TASK_CELL_HEIGHT;
        listView.setMinHeight(newHeight);
        listView.setPrefHeight(newHeight);
        listView.setMaxHeight(newHeight); // locks height so it won't shrink
    }

    /* Set initial height for empty list */
    private void setInitialListHeight(ListView<TaskData> listView) {
        listView.setMinHeight(LIST_START_HEIGHT);
        listView.setPrefHeight(LIST_START_HEIGHT);
        listView.setMaxHeight(Double.MAX_VALUE); // allow growth
    }

    /* Find a task by ID in all columns and remove it */
    private TaskData findAndRemoveTask(String id) {
        for (ListView<TaskData> list : List.of(toDoList, inProgressList, doneList)) {
            for (TaskData task : list.getItems()) {
                if (task.getId().equals(id)) {
                    list.getItems().remove(task);
                    return task;
                }
            }
        }
        return null;
    }

}
