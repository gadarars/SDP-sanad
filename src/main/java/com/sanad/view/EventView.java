package com.sanad.view;

import com.sanad.controller.EventController;
import com.sanad.model.event.Event;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.converter.LocalDateStringConverter;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * VIEW: events screen (full CRUD).
 */
public class EventView extends CrudView<Event> {

    private final EventController controller = new EventController();
    private final TextField nameF = new TextField(), locF = new TextField(), catF = new TextField();
    private final DatePicker datePicker = new DatePicker();

    public EventView() {
        super("Events", "Event");
        datePicker.setConverter(new LocalDateStringConverter(DateTimeFormatter.ISO_LOCAL_DATE, DateTimeFormatter.ISO_LOCAL_DATE));
        datePicker.setPromptText("2026-12-01");

        table.getColumns().addAll(ViewUtil.<Event>idCol(), ViewUtil.<Event>col("Name", "name"),
                ViewUtil.<Event>col("Date", "date"), ViewUtil.<Event>col("Location", "location"),
                ViewUtil.<Event>col("Category", "category"));

        build(ViewUtil.field("Event name", nameF), ViewUtil.field("Date", datePicker),
                ViewUtil.field("Location", locF), ViewUtil.field("Category", catF));
    }

    @Override
    protected List<Event> load() {
        return controller.getAll();
    }

    @Override
    protected void fill(Event v) {
        nameF.setText(v.getName());
        datePicker.setValue(v.getDate());
        locF.setText(v.getLocation());
        catF.setText(v.getCategory());
    }

    @Override
    protected void clearForm() {
        nameF.clear();
        datePicker.setValue(null);
        locF.clear();
        catF.clear();
    }

    @Override
    protected void add() {
        controller.add(nameF.getText(), datePicker.getValue(), locF.getText(), catF.getText());
    }

    @Override
    protected void update(Event v) {
        controller.update(v.getId(), nameF.getText(), datePicker.getValue(), locF.getText(), catF.getText());
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
