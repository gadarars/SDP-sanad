package com.sanad.view;

import com.sanad.controller.LookupController;
import com.sanad.model.lookup.Lookup;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/** VIEW: builds the Lookup tables screen. It only talks to the controller, never to the database. */
public class LookupView extends VBox {

    private final LookupController controller = new LookupController();
    private final TableView<Lookup> table = new TableView<>();
    private final Label message = new Label();

    private final TextField kindField = new TextField();
    private final TextField nameField = new TextField();

    public LookupView() {
        setPadding(new Insets(12));
        setSpacing(10);

        table.getColumns().addAll(ViewUtil.<Lookup>col("ID", "id"), ViewUtil.<Lookup>col("Kind (e.g. DONATION_TYPE)", "kind"),
                ViewUtil.<Lookup>col("Value", "name"));
        kindField.setPromptText("Kind (e.g. DONATION_TYPE)");
        nameField.setPromptText("Value");

        // clicking a row fills the form so you can edit it
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                kindField.setText(sel.getKind());
                nameField.setText(sel.getName());
            }
        });

        Button addBtn = new Button("Add");
        Button updateBtn = new Button("Update selected");
        Button deleteBtn = new Button("Delete selected");

        addBtn.setOnAction(e -> run(() -> controller.add(kindField.getText(), nameField.getText()), "Added"));
        updateBtn.setOnAction(e -> {
            Lookup sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { message.setText("Select a row first"); return; }
            run(() -> controller.update(sel.getId(), kindField.getText(), nameField.getText()), "Updated");
        });
        deleteBtn.setOnAction(e -> {
            Lookup sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { message.setText("Select a row first"); return; }
            run(() -> controller.delete(sel.getId()), "Deleted");
        });

        HBox form = new HBox(8, kindField, nameField);
        HBox buttons = new HBox(8, addBtn, updateBtn, deleteBtn);
        getChildren().addAll(new Label("Lookup tables"), form, buttons, message, table);
        refresh();
    }

    private void run(Runnable action, String okMessage) {
        try {
            action.run();
            message.setText(okMessage);
            kindField.clear();
            nameField.clear();
            refresh();
        } catch (Exception ex) {
            message.setText("Error: " + ex.getMessage());
        }
    }

    private void refresh() {
        table.setItems(FXCollections.observableArrayList(controller.getAll()));
    }
}
