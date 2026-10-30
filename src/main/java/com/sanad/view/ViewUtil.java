package com.sanad.view;

import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.util.function.Function;

/**
 * Small helpers used by all views to build table columns and form fields.
 */
public class ViewUtil {
    public static <T> TableColumn<T, Object> col(String title, String property) {
        TableColumn<T, Object> c = new TableColumn<>(title);
        c.setCellValueFactory(new PropertyValueFactory<>(property));
        return c;
    }

    /**
     * Column whose values are shown through {@code format}.
     */
    public static <T> TableColumn<T, Object> col(String title, String property, Function<Object, String> format) {
        TableColumn<T, Object> c = col(title, property);
        c.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : format.apply(item));
            }
        });
        return c;
    }

    public static <T> TableColumn<T, Object> idCol() {
        TableColumn<T, Object> c = col("ID", "id");
        c.setMaxWidth(90);
        return c;
    }

    /**
     * Right-aligned column for numbers, shown with thousands separators.
     */
    public static <T> TableColumn<T, Object> numberCol(String title, String property, String pattern) {
        TableColumn<T, Object> c = col(title, property, v -> String.format(pattern, v));
        c.getStyleClass().add(Tweaks.ALIGN_RIGHT);
        return c;
    }

    /**
     * Turns a stored code like {@code IN_KIND} into {@code In kind}.
     */
    public static String pretty(Object code) {
        String s = code.toString().replace('_', ' ').toLowerCase();
        return s.isEmpty() ? s : Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    /**
     * Drop-down of stored codes that displays them with {@link #pretty}.
     */
    public static ComboBox<String> choice(String... codes) {
        ComboBox<String> box = new ComboBox<>(FXCollections.observableArrayList(codes));
        box.setConverter(new StringConverter<>() {
            @Override
            public String toString(String code) {
                return code == null ? "" : pretty(code);
            }

            @Override
            public String fromString(String text) {
                return null; // only used by editable combo boxes
            }
        });
        box.setValue(codes[0]);
        return box;
    }

    /**
     * Input with a caption above it; grows to share the form row.
     */
    public static VBox field(String caption, Control input) {
        Label label = new Label(caption);
        label.getStyleClass().add(Styles.TEXT_MUTED);
        input.setMaxWidth(Double.MAX_VALUE);
        if (input instanceof TextField text) {
            text.textProperty().addListener((o, old, now) -> text.pseudoClassStateChanged(Styles.STATE_DANGER, false));
        }
        VBox box = new VBox(6, label, input);
        box.setPrefWidth(160);
        HBox.setHgrow(box, Priority.ALWAYS);
        return box;
    }

    /**
     * Number as typed in a form field: 300.00 becomes "300", 1250.50 becomes "1250.5".
     */
    public static String plain(BigDecimal value) {
        return value.stripTrailingZeros().toPlainString();
    }

    /**
     * Parses a number field, outlining it in red when the text is not a number.
     */
    public static BigDecimal number(TextField field, String caption) {
        try {
            return new BigDecimal(field.getText().strip());
        } catch (NumberFormatException ex) {
            field.pseudoClassStateChanged(Styles.STATE_DANGER, true);
            throw new IllegalArgumentException(caption + " must be a number");
        }
    }
}
