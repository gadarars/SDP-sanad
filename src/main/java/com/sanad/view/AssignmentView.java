package com.sanad.view;

import com.sanad.controller.AssignmentController;
import com.sanad.controller.EventController;
import com.sanad.controller.VolunteerController;
import com.sanad.model.event.Event;
import com.sanad.model.volunteer.Assignment;
import com.sanad.model.volunteer.Volunteer;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.util.List;
import java.util.stream.Collectors;

/**
 * VIEW: volunteer assignments screen (full CRUD).
 */
public class AssignmentView extends CrudView<Assignment> {

    private final AssignmentController controller = new AssignmentController();
    private final VolunteerController volunteers = new VolunteerController();
    private final EventController events = new EventController();
    private final ComboBox<Event> eventBox = ViewUtil.picker(e -> e.getName() + (e.getDate() == null ? "" : " (" + e.getDate() + ")"));
    private final ComboBox<String> skillBox = ViewUtil.picker(ViewUtil::pretty);
    private final ComboBox<Volunteer> volunteerBox = ViewUtil.picker(Volunteer::getName);
    private final Label suitable = new Label();

    public AssignmentView() {
        super("Volunteer assignments", "Assignment");
        suitable.setWrapText(true);
        eventBox.valueProperty().addListener((o, old, now) -> showSuitable());
        skillBox.valueProperty().addListener((o, old, now) -> showSuitable());

        table.getColumns().addAll(ViewUtil.<Assignment>idCol(), ViewUtil.<Assignment>col("Volunteer", "volunteerName"),
                ViewUtil.<Assignment>col("Event", "eventName"), ViewUtil.<Assignment>col("Date", "eventDate"),
                ViewUtil.<Assignment>col("Skill", "skill", ViewUtil::pretty),
                ViewUtil.<Assignment>numberCol("Hours logged", "hours", "%,.2f"));

        build(ViewUtil.field("Event", eventBox), ViewUtil.field("Skill", skillBox),
                ViewUtil.field("Volunteer", volunteerBox), ViewUtil.field("Suitable volunteers", suitable));
    }

    @Override
    protected List<Assignment> load() {
        eventBox.getItems().setAll(events.getAll());
        skillBox.getItems().setAll(volunteers.getSkills());
        volunteerBox.getItems().setAll(volunteers.getAll());
        showSuitable();
        return controller.getAll();
    }

    @Override
    protected void fill(Assignment a) {
        eventBox.setValue(eventBox.getItems().stream().filter(e -> e.getId() == a.getEventId()).findFirst().orElse(null));
        skillBox.setValue(a.getSkill());
        volunteerBox.setValue(volunteerBox.getItems().stream().filter(v -> v.getId() == a.getVolunteerId())
                .findFirst().orElse(null));
    }

    @Override
    protected void clearForm() {
        eventBox.setValue(null);
        skillBox.setValue(null);
        volunteerBox.setValue(null);
    }

    @Override
    protected void add() {
        controller.add(volunteerBox.getValue(), eventBox.getValue(), skillBox.getValue());
    }

    @Override
    protected void update(Assignment a) {
        controller.update(a.getId(), volunteerBox.getValue(), eventBox.getValue(), skillBox.getValue());
    }

    @Override
    protected void delete(Assignment a) {
        controller.delete(a.getId());
    }

    @Override
    protected String describe(Assignment a) {
        return "the assignment of " + a.getVolunteerName() + " to " + a.getEventName();
    }

    private void showSuitable() {
        if (eventBox.getValue() == null || skillBox.getValue() == null) {
            suitable.setText("Pick an event and a skill");
            return;
        }
        List<Volunteer> matches = controller.suitableVolunteers(eventBox.getValue(), skillBox.getValue());
        suitable.setText(matches.isEmpty() ? "None" : matches.stream().map(Volunteer::getName).collect(Collectors.joining(", ")));
    }
}
