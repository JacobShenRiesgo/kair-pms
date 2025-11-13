package com.j4va.kair;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import java.io.IOException;
import java.util.List;

//Linked to the kanban.fxml
public class KanbanController {
    @FXML
    private ListView<TaskData> todoList;
    @FXML
    private ListView<TaskData> inProgressList;
    @FXML
    private ListView<TaskData> doneList;

    @FXML
    private void initialize() {
        setupListView(todoList);
        setupListView(inProgressList);
        setupListView(doneList);
    }

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
                            e.printStackTrace();
                        }
                    }
                }
            };

            enableDragAndDrop(cell, listView);
            return cell;
        });
    }
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
                    parentList.getItems().add(movedTask);
                    success = true;
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

    private TaskData findAndRemoveTask(String id) {
        for (ListView<TaskData> list : List.of(todoList, inProgressList, doneList)) {
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
