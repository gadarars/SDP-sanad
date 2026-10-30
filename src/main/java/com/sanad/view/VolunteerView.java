package com.sanad.view;

import com.sanad.controller.VolunteerController;
import com.sanad.model.user.User;
import com.sanad.model.volunteer.Volunteer;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.kordamp.ikonli.feather.Feather;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * VIEW: volunteers screen (full CRUD).
 */
public class VolunteerView extends CrudView<Volunteer> {

    private final VolunteerController controller = new VolunteerController();
    private final TextField nameF = new TextField();
    private final ComboBox<User> userBox = ViewUtil.picker(User::getUsername);
    private final CheckMenu<String> skillsMenu = new CheckMenu<>();
    private final CheckMenu<DayOfWeek> daysMenu = new CheckMenu<>();

    public VolunteerView() {
        super("Volunteers", "Volunteer");
        daysMenu.setOptions(Arrays.asList(DayOfWeek.values()));

        table.getColumns().addAll(ViewUtil.<Volunteer>idCol(), ViewUtil.<Volunteer>col("Name", "name"),
                ViewUtil.<Volunteer>col("User", "username"),
                ViewUtil.<Volunteer>col("Skills", "skills", VolunteerView::prettyList),
                ViewUtil.<Volunteer>col("Available days", "availableDays", VolunteerView::prettyList),
                ViewUtil.<Volunteer>numberCol("Total hours", "totalHours", "%,.2f"));

        build(ViewUtil.field("Name", nameF), ViewUtil.field("User", userBox), ViewUtil.field("Skills", skillsMenu),
                ViewUtil.field("Available days", daysMenu));
        action("Certificate", Feather.AWARD, this::showCertificate, "Certificate created");
    }

    @Override
    protected List<Volunteer> load() {
        userBox.getItems().setAll(controller.getVolunteerUsers());
        skillsMenu.setOptions(controller.getSkills());
        return controller.getAll();
    }

    @Override
    protected void fill(Volunteer v) {
        nameF.setText(v.getName());
        userBox.setValue(userBox.getItems().stream().filter(u -> v.getUserId() != null && u.getId() == v.getUserId())
                .findFirst().orElse(null));
        skillsMenu.setChecked(v.skillNames());
        daysMenu.setChecked(v.days());
    }

    @Override
    protected void clearForm() {
        nameF.clear();
        userBox.setValue(null);
        skillsMenu.setChecked(List.of());
        daysMenu.setChecked(List.of());
    }

    @Override
    protected void add() {
        controller.add(nameF.getText(), userBox.getValue(), skillsMenu.getChecked(), daysMenu.getChecked());
    }

    @Override
    protected void update(Volunteer v) {
        controller.update(v.getId(), nameF.getText(), userBox.getValue(), skillsMenu.getChecked(), daysMenu.getChecked());
    }

    @Override
    protected void delete(Volunteer v) {
        controller.delete(v.getId());
    }

    @Override
    protected String describe(Volunteer v) {
        return "volunteer " + v.getName();
    }

    private void showCertificate(Volunteer v) {
        TextArea area = new TextArea(controller.certificate(v.getId()));
        area.setEditable(false);
        area.setStyle("-fx-font-family: monospace;");
        area.setPrefColumnCount(70);
        area.setPrefRowCount(16);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(getScene().getWindow());
        dialog.setTitle("Volunteer certificate");
        dialog.setHeaderText("Certificate for " + v.getName());
        dialog.getDialogPane().setContent(area);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    private static String prettyList(Object csv) {
        return Arrays.stream(csv.toString().split(",")).map(ViewUtil::pretty).collect(Collectors.joining(", "));
    }
}
