package com.sanad.view;

import com.sanad.controller.LookupController;
import com.sanad.model.lookup.Lookup;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.List;

/**
 * VIEW: builds the Lookup tables screen. It only talks to the controller, never to the database.
 */
public class LookupView extends CrudView<Lookup> {

    private final LookupController controller = new LookupController();
    private final ComboBox<String> kindBox = new ComboBox<>();
    private final TextField nameField = new TextField();

    public LookupView() {
        super("Lookup tables", "Value");
        kindBox.setEditable(true);
        kindBox.setPromptText("e.g. EVENT_CATEGORY");

        table.getColumns().addAll(ViewUtil.<Lookup>idCol(), ViewUtil.<Lookup>col("Kind", "kind"),
                ViewUtil.<Lookup>col("Value", "name"));

        build(ViewUtil.field("Kind", kindBox), ViewUtil.field("Value", nameField));
    }

    /**
     * Also offers every kind already in the table as a choice.
     */
    @Override
    void refresh() {
        super.refresh();
        kindBox.getItems().setAll(table.getItems().stream().map(Lookup::getKind).distinct().sorted().toList());
    }

    private String kind() {
        return kindBox.getEditor().getText();
    }

    @Override
    protected List<Lookup> load() {
        return controller.getAll();
    }

    @Override
    protected void fill(Lookup sel) {
        kindBox.setValue(sel.getKind());
        nameField.setText(sel.getName());
    }

    @Override
    protected void clearForm() {
        kindBox.setValue(null);
        kindBox.getEditor().clear();
        nameField.clear();
    }

    @Override
    protected void add() {
        controller.add(kind(), nameField.getText());
    }

    @Override
    protected void update(Lookup sel) {
        controller.update(sel.getId(), kind(), nameField.getText());
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
