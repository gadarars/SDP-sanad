package com.sanad.view;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.Message;
import atlantafx.base.theme.Styles;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;
import java.util.function.Consumer;

/**
 * VIEW: shared layout for a CRUD screen (heading, form card, buttons, status message, table).
 * Subclasses supply the fields and the controller calls; they never touch the database directly.
 */
public abstract class CrudView<T> extends VBox {

    protected final TableView<T> table = new TableView<>();
    private final String noun;
    private final Label count = new Label();
    private final HBox footer = new HBox(8);
    private final HBox fieldRow = new HBox(12);
    private final Button addButton = button("Add", Feather.PLUS, Styles.ACCENT);
    private final Button updateButton = button("Update", Feather.EDIT_2);
    private final Button deleteButton = button("Delete", Feather.TRASH_2, Styles.DANGER, Styles.BUTTON_OUTLINED);
    private Message status;

    /**
     * {@code noun} is the singular, capitalised name of one row, e.g. "Donation".
     */
    protected CrudView(String title, String noun) {
        this.noun = noun;
        setPadding(new Insets(24));
        setSpacing(16);

        Label heading = new Label(title);
        heading.getStyleClass().add(Styles.TITLE_3);
        count.getStyleClass().add(Styles.TEXT_MUTED);


        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.getStyleClass().add(Styles.STRIPED);
        table.setPlaceholder(new Label("Nothing here yet. Fill in the form above and press Add."));
        VBox.setVgrow(table, Priority.ALWAYS);
        table.getSelectionModel().selectedItemProperty().addListener((o, old, item) -> {
            if (item != null) fill(item);
        });

        getChildren().addAll(new VBox(2, heading, count), table);
    }

    protected abstract List<T> load();

    /**
     * Copies the selected row into the form.
     */
    protected abstract void fill(T item);

    protected abstract void clearForm();

    protected abstract void add();

    protected abstract void update(T item);

    protected abstract void delete(T item);

    /**
     * Short description for the delete confirmation, e.g. "the donation from Sara".
     */
    protected abstract String describe(T item);

    /**
     * Adds the form card with the given fields; call at the end of the subclass constructor.
     */
    protected void build(Node... fields) {
        BooleanBinding nothingSelected = table.getSelectionModel().selectedItemProperty().isNull();
        updateButton.disableProperty().bind(nothingSelected);
        deleteButton.disableProperty().bind(nothingSelected);

        addButton.setOnAction(e -> run(this::add, noun + " added"));
        updateButton.setOnAction(e -> run(() -> update(selected()), noun + " updated"));
        deleteButton.setOnAction(e -> {
            if (confirmDelete(selected())) run(() -> delete(selected()), noun + " deleted");
        });

        fieldRow.getChildren().setAll(fields);
        fieldRow.setAlignment(Pos.BOTTOM_LEFT);
        Card card = new Card();
        card.setBody(fieldRow);
        footer.getChildren().addAll(0, List.of(addButton, updateButton, deleteButton));
        card.setFooter(footer);
        getChildren().add(1, card);
    }

    /**
     * Hides the form and its Add, Update and Delete buttons, keeping the table and any extra actions.
     */
    protected void readOnly() {
        hide(fieldRow, addButton, updateButton, deleteButton);
    }

    /**
     * Hides Add and Delete, so the user can only update the rows they are shown.
     */
    protected void updateOnly() {
        hide(addButton, deleteButton);
    }

    private static void hide(Node... nodes) {
        for (Node n : nodes) {
            n.setVisible(false);
            n.setManaged(false);
        }
    }

    /**
     * Adds a footer button that runs {@code action} on the selected row.
     */
    protected void action(String text, Ikon icon, Consumer<T> action, String okMessage) {
        Button b = button(text, icon);
        b.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        b.setOnAction(e -> run(() -> action.accept(selected()), okMessage));
        footer.getChildren().add(b);
    }

    void refresh() {
        try {
            List<T> items = load();
            table.setItems(FXCollections.observableArrayList(items));
            count.setText(items.size() + (items.size() == 1 ? " record" : " records"));
        } catch (RuntimeException ex) {
            show(ex.getMessage(), true);
        }
    }

    private T selected() {
        return table.getSelectionModel().getSelectedItem();
    }

    private void run(Runnable action, String okMessage) {
        try {
            action.run();
            show(okMessage, false);
            clearForm();
            refresh();
        } catch (Exception ex) {
            show(ex.getMessage(), true);
        }
    }

    private void show(String text, boolean error) {
        Message message = new Message(text, null, new FontIcon(error ? Feather.ALERT_CIRCLE : Feather.CHECK_CIRCLE));
        message.getStyleClass().add(error ? Styles.DANGER : Styles.SUCCESS);
        message.setOnClose(e -> getChildren().remove(message));
        getChildren().remove(status);
        status = message;
        getChildren().add(getChildren().indexOf(table), message);
    }

    private boolean confirmDelete(T item) {
        ButtonType deleteType = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This cannot be undone.", ButtonType.CANCEL, deleteType);
        alert.initOwner(getScene().getWindow());
        alert.setTitle("Delete " + noun.toLowerCase());
        alert.setHeaderText("Delete " + describe(item) + "?");
        return alert.showAndWait().filter(deleteType::equals).isPresent();
    }

    private static Button button(String text, Ikon icon, String... styles) {
        Button b = new Button(text, new FontIcon(icon));
        b.getStyleClass().addAll(styles);
        return b;
    }
}
