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

    public HBox topBar;
    @FXML private ListView<TaskData> toDoList;
    @FXML private ListView<TaskData> inProgressList;
    @FXML private ListView<TaskData> doneList;

    @FXML private Button toDoButton;
    @FXML private Button inProgressButton;
    @FXML private Button doneButton;

    @FXML
    private void initialize() {
        setupListView(toDoList);
        setupListView(inProgressList);
        setupListView(doneList);

        // Buttons to create tasks in respective columns
        //toDoButton.setOnAction(e -> createTask(toDoList, "To Do Task", "TODO"));
        //inProgressButton.setOnAction(e -> createTask(inProgressList, "In Progress Task", "IN_PROGRESS"));
        //doneButton.setOnAction(e -> createTask(doneList, "Done Task", "DONE"));
    }

    // Open popup to create a new task
    @FXML
    private void openCreateTaskPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("task_popup.fxml"));
            Parent root = loader.load();
            TaskPopup popup = loader.getController();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Create Task");
            stage.setScene(new Scene(root));

            // When task is created in popup, add it to the proper column
            popup.setOnTaskCreated(this::addTaskToColumn);

            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load task popup: " + e.getMessage());
        }
    }

    // Create a task directly in a given column
    /*private void createTask(ListView<TaskData> list, String defaultTitle, String status) {
        TaskData newTask = new TaskData(
                defaultTitle + " " + (list.getItems().size() + 1),
                "Description...",
                priority,
                status
        );
        list.getItems().add(newTask);
    }*/

    // Add task to correct column based on status
    private void addTaskToColumn(TaskData task) {
        switch (task.getStatus()) {
            case "TODO" -> toDoList.getItems().add(task);
            case "IN_PROGRESS" -> inProgressList.getItems().add(task);
            case "DONE" -> doneList.getItems().add(task);
            default -> toDoList.getItems().add(task); // fallback
        }
    }

    // Setup list view to display task cards and enable drag-and-drop
    private void setupListView(ListView<TaskData> listView) {
        listView.setCellFactory(lv -> {
            ListCell<TaskData> cell = new ListCell<>() {
                @Override
                protected void updateItem(TaskData task, boolean empty) {
                    super.updateItem(task, empty);
                    if (empty || task == null) {
                        setGraphic(null);
                    } else {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("taskcard.fxml"));
                            HBox taskCard = loader.load();
                            TaskCardController controller = loader.getController();
                            controller.setData(task);
                            setGraphic(taskCard);
                        } catch (IOException e) {
                            System.err.println("Failed to load task card: " + e.getMessage());
                        }
                    }
                }
            };

            enableDragAndDrop(cell, listView);
            return cell;
        });
    }

    // Enable drag-and-drop for moving tasks between columns
    private void enableDragAndDrop(ListCell<TaskData> cell, ListView<TaskData> parentList) {
        cell.setOnDragDetected(event -> {
            if (cell.isEmpty()) return;

            Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(cell.getItem().getId());
            db.setContent(content);

            cell.setOpacity(0.5);
            event.consume();
        });

        cell.setOnDragDone(event -> {
            cell.setOpacity(1);
            event.consume();
        });

        parentList.setOnDragOver(event -> {
            if (event.getGestureSource() != parentList && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        parentList.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                String taskId = db.getString();
                TaskData movedTask = findAndRemoveTask(taskId);
                if (movedTask != null) {
                    // Update status based on target list
                    if (parentList == toDoList) movedTask.setStatus("TODO");
                    else if (parentList == inProgressList) movedTask.setStatus("IN_PROGRESS");
                    else if (parentList == doneList) movedTask.setStatus("DONE");

                    parentList.getItems().add(movedTask);
                    success = true;
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

    // Find a task by ID in all columns and remove it
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
