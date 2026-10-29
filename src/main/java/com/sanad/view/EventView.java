package com.sanad.view;

import com.sanad.controller.EventController;
import com.sanad.model.event.Event;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/** VIEW: events screen (full CRUD). */
public class EventView extends VBox {

    private final EventController controller = new EventController();
    private final TableView<Event> table = new TableView<>();
    private final TextField nameF = new TextField(), dateF = new TextField(),
            locF = new TextField(), catF = new TextField();
    private final Label message = new Label();

    public EventView() {
        setPadding(new Insets(12));
        setSpacing(10);
        nameF.setPromptText("Event name"); dateF.setPromptText("Date (2026-12-01)");
        locF.setPromptText("Location"); catF.setPromptText("Category");

        table.getColumns().addAll(ViewUtil.<Event>col("ID", "id"), ViewUtil.<Event>col("Name", "name"),
                ViewUtil.<Event>col("Date", "date"), ViewUtil.<Event>col("Location", "location"),
                ViewUtil.<Event>col("Category", "category"));
        table.getSelectionModel().selectedItemProperty().addListener((o, old, v) -> {
            if (v != null) { nameF.setText(v.getName()); dateF.setText(v.getDate());
                locF.setText(v.getLocation()); catF.setText(v.getCategory()); }
        });

        Button add = new Button("Add"), update = new Button("Update"), delete = new Button("Delete");
        add.setOnAction(e -> run(() -> controller.add(nameF.getText(), dateF.getText(), locF.getText(), catF.getText()), "Event added"));
        update.setOnAction(e -> {
            Event s = table.getSelectionModel().getSelectedItem();
            if (s == null) { message.setText("Select a row first"); return; }
            run(() -> controller.update(s.getId(), nameF.getText(), dateF.getText(), locF.getText(), catF.getText()), "Event updated");
        });
        delete.setOnAction(e -> {
            Event s = table.getSelectionModel().getSelectedItem();
            if (s == null) { message.setText("Select a row first"); return; }
            run(() -> controller.delete(s.getId()), "Event deleted");
        });

        getChildren().addAll(new Label("Events"), new HBox(8, nameF, dateF, locF, catF, add, update, delete), message, table);
        refresh();
    }

    private void run(Runnable action, String ok) {
        try { action.run(); message.setText(ok); nameF.clear(); dateF.clear(); locF.clear(); catF.clear(); refresh(); }
        catch (Exception ex) { message.setText("Error: " + ex.getMessage()); }
    }

    private void refresh() { table.setItems(FXCollections.observableArrayList(controller.getAll())); }
}
