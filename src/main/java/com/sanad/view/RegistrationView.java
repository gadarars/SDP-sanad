package com.sanad.view;

import com.sanad.controller.EventController;
import com.sanad.controller.RegistrationController;
import com.sanad.controller.UserController;
import com.sanad.model.event.Event;
import com.sanad.model.event.Registration;
import com.sanad.model.user.User;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import org.kordamp.ikonli.feather.Feather;

import java.util.List;

/**
 * VIEW: event registrations and attendance (full CRUD; delete cancels a registration).
 */
public class RegistrationView extends CrudView<Registration> {

    private final RegistrationController controller = new RegistrationController();
    private final EventController events = new EventController();
    private final UserController users = new UserController();
    private final ComboBox<Event> eventBox = ViewUtil.picker(e -> e.getName() + (e.getDate() == null ? "" : " (" + e.getDate() + ")"));
    private final ComboBox<User> userBox = ViewUtil.picker(u -> u.getUsername() + " (" + ViewUtil.pretty(u.getRole()) + ")");
    private final Label registered = new Label();

    public RegistrationView() {
        super("Event registrations", "Registration");
        registered.setWrapText(true);
        eventBox.valueProperty().addListener((o, old, now) -> showRegistered());

        table.getColumns().addAll(ViewUtil.<Registration>idCol(), ViewUtil.<Registration>col("Event", "eventName"),
                ViewUtil.<Registration>col("Date", "eventDate"), ViewUtil.<Registration>col("User", "username"),
                ViewUtil.<Registration>col("Attended", "attended", v -> (Boolean) v ? "Yes" : "No"));

        build(ViewUtil.field("Event", eventBox), ViewUtil.field("User", userBox),
                ViewUtil.field("Registered", registered));
        action("Mark attended", Feather.CHECK_SQUARE, controller::markAttended, "Attendance marked");
    }

    @Override
    protected List<Registration> load() {
        eventBox.getItems().setAll(events.getAll());
        userBox.getItems().setAll(users.getAll());
        showRegistered();
        return controller.getAll();
    }

    @Override
    protected void fill(Registration r) {
        eventBox.setValue(eventBox.getItems().stream().filter(e -> e.getId() == r.getEventId()).findFirst().orElse(null));
        userBox.setValue(userBox.getItems().stream().filter(u -> u.getId() == r.getUserId()).findFirst().orElse(null));
    }

    @Override
    protected void clearForm() {
        eventBox.setValue(null);
        userBox.setValue(null);
    }

    @Override
    protected void add() {
        controller.add(userBox.getValue(), eventBox.getValue());
    }

    @Override
    protected void update(Registration r) {
        controller.update(r.getId(), userBox.getValue(), eventBox.getValue());
    }

    @Override
    protected void delete(Registration r) {
        controller.delete(r.getId());
    }

    @Override
    protected String describe(Registration r) {
        return "the registration of " + r.getUsername() + " for " + r.getEventName();
    }

    private void showRegistered() {
        if (eventBox.getValue() == null) {
            registered.setText("Pick an event");
            return;
        }
        List<String> names = controller.registeredNames(eventBox.getValue());
        registered.setText(names.isEmpty() ? "None" : String.join(", ", names));
    }
}
