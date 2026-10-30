package com.sanad.view;

import com.sanad.controller.LookupController;
import com.sanad.model.lookup.Lookup;
import javafx.scene.control.TextField;

import java.util.List;

/**
 * VIEW: builds the Lookup tables screen. It only talks to the controller, never to the database.
 */
public class LookupView extends CrudView<Lookup> {

    private final LookupController controller = new LookupController();
    private final TextField kindField = new TextField();
    private final TextField nameField = new TextField();

    public LookupView() {
        super("Lookup tables", "Value");
        kindField.setPromptText("e.g. EVENT_CATEGORY");

        table.getColumns().addAll(ViewUtil.<Lookup>idCol(), ViewUtil.<Lookup>col("Kind", "kind"),
                ViewUtil.<Lookup>col("Value", "name"));

        build(ViewUtil.field("Kind", kindField), ViewUtil.field("Value", nameField));
    }

    @Override
    protected List<Lookup> load() {
        return controller.getAll();
    }

    @Override
    protected void fill(Lookup sel) {
        kindField.setText(sel.getKind());
        nameField.setText(sel.getName());
    }

    @Override
    protected void clearForm() {
        kindField.clear();
        nameField.clear();
    }

    @Override
    protected void add() {
        controller.add(kindField.getText(), nameField.getText());
    }

    @Override
    protected void update(Lookup sel) {
        controller.update(sel.getId(), kindField.getText(), nameField.getText());
    }

    @Override
    protected void delete(Lookup sel) {
        controller.delete(sel.getId());
    }

    @Override
    protected String describe(Lookup sel) {
        return "\"" + sel.getName() + "\" from " + sel.getKind();
    }
}
