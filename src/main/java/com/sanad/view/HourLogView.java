package com.sanad.view;

import com.sanad.controller.AssignmentController;
import com.sanad.controller.HourLogController;
import com.sanad.model.volunteer.Assignment;
import com.sanad.model.volunteer.HourLog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.List;

/**
 * VIEW: hours worked on each assignment (full CRUD).
 */
public class HourLogView extends CrudView<HourLog> {

    private final HourLogController controller = new HourLogController();
    private final AssignmentController assignments = new AssignmentController();
    private final ComboBox<Assignment> assignmentBox = ViewUtil.picker(
            a -> a.getVolunteerName() + " at " + a.getEventName() + " (" + a.getEventDate() + ")");
    private final TextField hoursF = new TextField();

    public HourLogView() {
        super("Volunteer hours", "Hours");
        hoursF.setPromptText("0");

        table.getColumns().addAll(ViewUtil.<HourLog>idCol(), ViewUtil.<HourLog>col("Volunteer", "volunteerName"),
                ViewUtil.<HourLog>col("Event", "eventName"), ViewUtil.<HourLog>col("Date", "eventDate"),
                ViewUtil.<HourLog>numberCol("Hours", "hours", "%,.2f"));

        build(ViewUtil.field("Assignment", assignmentBox), ViewUtil.field("Hours", hoursF));
    }

    @Override
    protected List<HourLog> load() {
        assignmentBox.getItems().setAll(assignments.getAll());
        return controller.getAll();
    }

    @Override
    protected void fill(HourLog h) {
        assignmentBox.setValue(assignmentBox.getItems().stream().filter(a -> a.getId() == h.getAssignmentId())
                .findFirst().orElse(null));
        hoursF.setText(ViewUtil.plain(h.getHours()));
    }

    @Override
    protected void clearForm() {
        assignmentBox.setValue(null);
        hoursF.clear();
    }

    @Override
    protected void add() {
        controller.add(assignmentBox.getValue(), ViewUtil.number(hoursF, "Hours"));
    }

    @Override
    protected void update(HourLog h) {
        controller.update(h.getId(), assignmentBox.getValue(), ViewUtil.number(hoursF, "Hours"));
    }

    @Override
    protected void delete(HourLog h) {
        controller.delete(h.getId());
    }

    @Override
    protected String describe(HourLog h) {
        return ViewUtil.plain(h.getHours()) + " hours of " + h.getVolunteerName() + " at " + h.getEventName();
    }
}
