package com.sanad.view;

import com.sanad.controller.EventController;
import com.sanad.model.event.Event;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.converter.LocalDateStringConverter;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * VIEW: events screen (full CRUD for staff, read-only for volunteers).
 */
public class EventView extends CrudView<Event> {

    private final EventController controller = new EventController();
    private final TextField nameF = new TextField(), locF = new TextField();
    private final ComboBox<String> categoryBox = ViewUtil.picker(ViewUtil::pretty);
    private final DatePicker datePicker = new DatePicker();

    public EventView() {
        super("Events", "Event");
        datePicker.setConverter(new LocalDateStringConverter(DateTimeFormatter.ISO_LOCAL_DATE, DateTimeFormatter.ISO_LOCAL_DATE));
        datePicker.setPromptText("2026-12-01");

        table.getColumns().addAll(ViewUtil.<Event>idCol(), ViewUtil.<Event>col("Name", "name"),
                ViewUtil.<Event>col("Date", "date"), ViewUtil.<Event>col("Location", "location"),
                ViewUtil.<Event>col("Category", "category", ViewUtil::pretty));

        build(ViewUtil.field("Event name", nameF), ViewUtil.field("Date", datePicker),
                ViewUtil.field("Location", locF), ViewUtil.field("Category", categoryBox));
        if (!controller.canEdit()) readOnly();
    }

    @Override
    protected List<Event> load() {
        categoryBox.getItems().setAll(controller.getCategories());
        return controller.getAll();
    }

    @Override
    protected void fill(Event v) {
        nameF.setText(v.getName());
        datePicker.setValue(v.getDate());
        locF.setText(v.getLocation());
        categoryBox.setValue(v.getCategory());
    }

    @Override
    protected void clearForm() {
        nameF.clear();
        datePicker.setValue(null);
        locF.clear();
        categoryBox.setValue(null);
    }

    @Override
    protected void add() {
        controller.add(nameF.getText(), datePicker.getValue(), locF.getText(), categoryBox.getValue());
    }

    @Override
    protected void update(Event v) {
        controller.update(v.getId(), nameF.getText(), datePicker.getValue(), locF.getText(), categoryBox.getValue());
    }

    @Override
    protected void delete(Event v) {
        controller.delete(v.getId());
    }

    @Override
    protected String describe(Event v) {
        return "event " + v.getName();
    }
}
