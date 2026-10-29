package com.sanad.view;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

/** Small helper used by all views to build table columns. */
public class ViewUtil {
    public static <T> TableColumn<T, Object> col(String title, String property) {
        TableColumn<T, Object> c = new TableColumn<>(title);
        c.setCellValueFactory(new PropertyValueFactory<>(property));
        return c;
    }
}
