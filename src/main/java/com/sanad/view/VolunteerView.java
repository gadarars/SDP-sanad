package com.sanad.view;

import com.sanad.controller.VolunteerController;
import com.sanad.model.volunteer.Volunteer;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * VIEW: volunteers screen (full CRUD).
 */
public class VolunteerView extends VBox {

    private final VolunteerController controller = new VolunteerController();
    private final TableView<Volunteer> table = new TableView<>();
    private final TextField nameF = new TextField(), skillsF = new TextField(),
            availF = new TextField(), hoursF = new TextField();
    private final Label message = new Label();

    public VolunteerView() {
        setPadding(new Insets(12));
        setSpacing(10);
        nameF.setPromptText("Name");
        skillsF.setPromptText("Skills");
        availF.setPromptText("Availability");
        hoursF.setPromptText("Hours");

        table.getColumns().addAll(ViewUtil.<Volunteer>col("ID", "id"), ViewUtil.<Volunteer>col("Name", "name"),
                ViewUtil.<Volunteer>col("Skills", "skills"), ViewUtil.<Volunteer>col("Availability", "availability"),
                ViewUtil.<Volunteer>col("Hours", "hours"));
        table.getSelectionModel().selectedItemProperty().addListener((o, old, v) -> {
            if (v != null) {
                nameF.setText(v.getName());
                skillsF.setText(v.getSkills());
                availF.setText(v.getAvailability());
                hoursF.setText(String.valueOf(v.getHours()));
            }
        });

        Button add = new Button("Add"), update = new Button("Update"), delete = new Button("Delete");
        add.setOnAction(e -> run(() -> controller.add(nameF.getText(), skillsF.getText(), availF.getText(), hours()), "Volunteer added"));
        update.setOnAction(e -> {
            Volunteer s = table.getSelectionModel().getSelectedItem();
            if (s == null) {
                message.setText("Select a row first");
                return;
            }
            run(() -> controller.update(s.getId(), nameF.getText(), skillsF.getText(), availF.getText(), hours()), "Volunteer updated");
        });
        delete.setOnAction(e -> {
            Volunteer s = table.getSelectionModel().getSelectedItem();
            if (s == null) {
                message.setText("Select a row first");
                return;
            }
            run(() -> controller.delete(s.getId()), "Volunteer deleted");
        });

        getChildren().addAll(new Label("Volunteers"), new HBox(8, nameF, skillsF, availF, hoursF, add, update, delete), message, table);
        refresh();
    }

    private double hours() {
        return hoursF.getText().isBlank() ? 0 : Double.parseDouble(hoursF.getText());
    }

    private void run(Runnable action, String ok) {
        try {
            action.run();
            message.setText(ok);
            nameF.clear();
            skillsF.clear();
            availF.clear();
            hoursF.clear();
            refresh();
        } catch (Exception ex) {
            message.setText("Error: " + ex.getMessage());
        }
    }

    void refresh() {
        table.setItems(FXCollections.observableArrayList(controller.getAll()));
    }
}
