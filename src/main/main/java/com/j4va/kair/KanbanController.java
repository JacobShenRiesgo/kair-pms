package com.j4va.kair;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;
import com.j4va.kair.TaskData;
import com.j4va.kair.TaskCardController;
import com.j4va.kair.DatabaseConnection;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.Node;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.sql.*;
import java.time.LocalDate;

public class KanbanController {

    @FXML
    private ListView<TaskData> toDoList;
    @FXML
    private ListView<TaskData> inProgressList;
    @FXML
    private ListView<TaskData> doneList;

    @FXML
    private VBox todoColumn;

    @FXML
    private VBox doingColumn;

    @FXML
    private VBox doneColumn;

    @FXML
    private Button toDoButton, inProgressButton, doneButton;

    private int projectId;
    private Connection conn;

    private ObservableList<TaskData> toDoTasks = FXCollections.observableArrayList();
    private ObservableList<TaskData> inProgressTasks = FXCollections.observableArrayList();
    private ObservableList<TaskData> doneTasks = FXCollections.observableArrayList();

    public void setProject(int projectId, String projectName) {
        this.projectId = projectId;
        connectDatabase();
        loadTasksFromDatabase();
        setupListViews();
        setupAddButtons();
    }

    private void connectDatabase() {
        try {
            conn = DatabaseConnection.getConnection(); // assign to class field
            System.out.println("Database connected successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadTasksFromDatabase() {
        try {
            String sql = "SELECT * FROM task WHERE project_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, projectId);
            ResultSet rs = stmt.executeQuery();

            toDoTasks.clear();
            inProgressTasks.clear();
            doneTasks.clear();

            while (rs.next()) {
                TaskData task = new TaskData(
                        rs.getString("name"),
                        rs.getString("description1"),
                        rs.getString("priority"),
                        rs.getString("status")
                );

                switch (task.getStatus()) {
                    case "To Do" -> toDoTasks.add(task);
                    case "In Progress" -> inProgressTasks.add(task);
                    case "Done" -> doneTasks.add(task);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupListViews() {
        toDoList.setItems(toDoTasks);
        inProgressList.setItems(inProgressTasks);
        doneList.setItems(doneTasks);

        setupDragAndDrop(toDoList);
        setupDragAndDrop(inProgressList);
        setupDragAndDrop(doneList);

        // Show title in ListView
        Callback<ListView<TaskData>, ListCell<TaskData>> cellFactory = lv -> new ListCell<>() {
            @Override
            protected void updateItem(TaskData item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getTitle() + " (" + item.getPriority() + ")");
            }
        };

        toDoList.setCellFactory(cellFactory);
        inProgressList.setCellFactory(cellFactory);
        doneList.setCellFactory(cellFactory);
    }

    private void setupDragAndDrop(ListView<TaskData> listView) {
        listView.setOnDragDetected(event -> {
            TaskData selected = listView.getSelectionModel().getSelectedItem();
            if (selected == null) return;

            Dragboard db = listView.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(selected.getId());
            db.setContent(content);
            event.consume();
        });

        listView.setOnDragOver(event -> {
            if (event.getGestureSource() != listView && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        listView.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                TaskData task = findTaskById(db.getString());
                if (task != null) {
                    String newStatus = getStatusForList(listView);

                    removeTaskFromLists(task);
                    task.setStatus(newStatus);
                    listView.getItems().add(task);
                    updateTaskStatusInDatabase(task);

                    event.setDropCompleted(true);
                }
            }
            event.consume();
        });
    }

    private TaskData findTaskById(String id) {
        for (TaskData t : toDoTasks) if (t.getId().equals(id)) return t;
        for (TaskData t : inProgressTasks) if (t.getId().equals(id)) return t;
        for (TaskData t : doneTasks) if (t.getId().equals(id)) return t;
        return null;
    }

    private void removeTaskFromLists(TaskData task) {
        toDoTasks.remove(task);
        inProgressTasks.remove(task);
        doneTasks.remove(task);
    }

    private String getStatusForList(ListView<TaskData> listView) {
        if (listView == toDoList) return "To Do";
        if (listView == inProgressList) return "In Progress";
        return "Done";
    }

    private void updateTaskStatusInDatabase(TaskData task) {
        try {
            String sql = "UPDATE tasks SET status = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, task.getStatus());
            stmt.setString(2, task.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupAddButtons() {
        toDoButton.setOnAction(e -> openCreateTaskPopup("To Do"));
        inProgressButton.setOnAction(e -> openCreateTaskPopup("In Progress"));
        doneButton.setOnAction(e -> openCreateTaskPopup("Done"));
    }

    private void openCreateTaskPopup(String status) {
        // Example: just add a new task; you can replace this with a real popup
        TaskData newTask = new TaskData("New Task", "Description", "Medium", status);

        try {
            String sql = "INSERT INTO task (id, name, description1, priority, status, project_id) VALUES (?,?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newTask.getId());
            stmt.setString(2, newTask.getTitle());
            stmt.setString(3, newTask.getDescription());
            stmt.setString(4, newTask.getPriority());
            stmt.setString(5, newTask.getStatus());
            stmt.setInt(6, projectId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Add to the correct list
        switch (status) {
            case "To Do" -> toDoTasks.add(newTask);
            case "In Progress" -> inProgressTasks.add(newTask);
            case "Done" -> doneTasks.add(newTask);
        }
    }

    public void openCreateTaskPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/j4va/kair/taskcard.fxml"));
            HBox taskRoot = loader.load();

            // Get the controller to set any necessary data
            TaskCardController controller = loader.getController();
            controller.setKanbanController(this); // Optional: to call addTaskFromPopup()

            Stage popupStage = new Stage();
            popupStage.setTitle("Create New Task");
            popupStage.setScene(new Scene(taskRoot));
            popupStage.initModality(Modality.APPLICATION_MODAL); // blocks parent window
            popupStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addTaskFromPopup(String title, String priority) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("taskcard.fxml"));
            Node taskCardNode = loader.load(); // ← THIS is the node

            TaskCardController controller = loader.getController();
            controller.setKanbanController(this);
            controller.setData(title, priority);

            // add card to the first column (To Do)
            addTaskCardToColumn(taskCardNode);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void addTaskCardToColumn(Node taskCard) {
        // Example: add new task to TODO column (VBox)
        todoColumn.getChildren().add(taskCard);
    }


}