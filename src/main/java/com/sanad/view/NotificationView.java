package com.sanad.view;

import com.sanad.controller.NotificationController;
import com.sanad.model.notification.Notification;
import javafx.scene.control.Label;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;

/**
 * VIEW: list of sent notifications (read only, so it has no form).
 */
public class NotificationView extends CrudView<Notification> {

    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final NotificationController controller = new NotificationController();

    public NotificationView() {
        super("Notifications", "Notification");
        table.setPlaceholder(new Label("No notifications sent yet."));
        table.getColumns().addAll(ViewUtil.<Notification>idCol(), ViewUtil.<Notification>col("Recipient", "recipient"),
                ViewUtil.<Notification>col("Channel", "channel"), ViewUtil.<Notification>col("To", "address"),
                ViewUtil.<Notification>col("Message", "message"),
                ViewUtil.<Notification>col("Sent at", "sentAt", t -> TIME.format((TemporalAccessor) t)));
    }

    @Override
    protected List<Notification> load() {
        return controller.getAll();
    }

    @Override
    protected void fill(Notification n) {
    }

    @Override
    protected void clearForm() {
    }

    @Override
    protected void add() {
    }

    @Override
    protected void update(Notification n) {
    }

    @Override
    protected void delete(Notification n) {
    }

    @Override
    protected String describe(Notification n) {
        return "the notification to " + n.getRecipient();
    }
}
