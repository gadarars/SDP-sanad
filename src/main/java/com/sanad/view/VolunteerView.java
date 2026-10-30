package com.sanad.view;

import com.sanad.controller.VolunteerController;
import com.sanad.model.volunteer.Volunteer;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.util.List;

/**
 * VIEW: volunteers screen (full CRUD).
 */
public class VolunteerView extends CrudView<Volunteer> {

    private final VolunteerController controller = new VolunteerController();
    private final TextField nameF = new TextField(), skillsF = new TextField(),
            availF = new TextField(), hoursF = new TextField();

    public VolunteerView() {
        super("Volunteers", "Volunteer");
        hoursF.setPromptText("0");

        table.getColumns().addAll(ViewUtil.<Volunteer>idCol(), ViewUtil.<Volunteer>col("Name", "name"),
                ViewUtil.<Volunteer>col("Skills", "skills"), ViewUtil.<Volunteer>col("Availability", "availability"),
                ViewUtil.<Volunteer>numberCol("Hours", "hours", "%,.1f"));

        build(ViewUtil.field("Name", nameF), ViewUtil.field("Skills", skillsF),
                ViewUtil.field("Availability", availF), ViewUtil.field("Hours", hoursF));
    }

    private BigDecimal hours() {
        return hoursF.getText().isBlank() ? BigDecimal.ZERO : ViewUtil.number(hoursF, "Hours");
    }

    @Override
    protected List<Volunteer> load() {
        return controller.getAll();
    }

    @Override
    protected void fill(Volunteer v) {
        nameF.setText(v.getName());
        skillsF.setText(v.getSkills());
        availF.setText(v.getAvailability());
        hoursF.setText(ViewUtil.plain(v.getHours()));
    }

    @Override
    protected void clearForm() {
        nameF.clear();
        skillsF.clear();
        availF.clear();
        hoursF.clear();
    }

    @Override
    protected void add() {
        controller.add(nameF.getText(), skillsF.getText(), availF.getText(), hours());
    }

    @Override
    protected void update(Volunteer v) {
        controller.update(v.getId(), nameF.getText(), skillsF.getText(), availF.getText(), hours());
    }

    @Override
    protected void delete(Volunteer v) {
        controller.delete(v.getId());
    }

    @Override
    protected String describe(Volunteer v) {
        return "volunteer " + v.getName();
    }
}
