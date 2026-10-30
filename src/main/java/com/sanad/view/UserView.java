package com.sanad.view;

import com.sanad.controller.UserController;
import com.sanad.model.user.User;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * VIEW: user control panel (full CRUD for users).
 */
public class UserView extends VBox {

    private final UserController controller = new UserController();
    private final TableView<User> table = new TableView<>();
    private final TextField usernameF = new TextField(), emailF = new TextField();
    private final ComboBox<String> roleBox = new ComboBox<>(
            FXCollections.observableArrayList("ADMIN", "STAFF", "DONOR", "VOLUNTEER", "BENEFICIARY"));
    private final Label message = new Label();

    public UserView() {
        setPadding(new Insets(12));
        setSpacing(10);
        usernameF.setPromptText("Username");
        emailF.setPromptText("Email");
        roleBox.setValue("STAFF");

        table.getColumns().addAll(ViewUtil.<User>col("ID", "id"), ViewUtil.<User>col("Username", "username"),
                ViewUtil.<User>col("Email", "email"), ViewUtil.<User>col("Role", "role"));
        table.getSelectionModel().selectedItemProperty().addListener((o, old, v) -> {
            if (v != null) {
                usernameF.setText(v.getUsername());
                emailF.setText(v.getEmail());
                roleBox.setValue(v.getRole());
            }
        });

        Button add = new Button("Add"), update = new Button("Update"), delete = new Button("Delete");
        add.setOnAction(e -> run(() -> controller.add(usernameF.getText(), emailF.getText(), roleBox.getValue()), "User added"));
        update.setOnAction(e -> {
            User s = table.getSelectionModel().getSelectedItem();
            if (s == null) {
                message.setText("Select a row first");
                return;
            }
            run(() -> controller.update(s.getId(), usernameF.getText(), emailF.getText(), roleBox.getValue()), "User updated");
        });
        delete.setOnAction(e -> {
            User s = table.getSelectionModel().getSelectedItem();
            if (s == null) {
                message.setText("Select a row first");
                return;
            }
            run(() -> controller.delete(s.getId()), "User deleted");
        });

        getChildren().addAll(new Label("User control panel"), new HBox(8, usernameF, emailF, roleBox, add, update, delete), message, table);
        refresh();
    }

    private void run(Runnable action, String ok) {
        try {
            action.run();
            message.setText(ok);
            usernameF.clear();
            emailF.clear();
            refresh();
        } catch (Exception ex) {
            message.setText("Error: " + ex.getMessage());
        }
    }

    void refresh() {
        table.setItems(FXCollections.observableArrayList(controller.getAll()));
    }
}
