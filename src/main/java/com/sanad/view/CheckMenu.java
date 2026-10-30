package com.sanad.view;

import javafx.scene.control.CheckBox;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.MenuButton;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * VIEW: drop-down of check boxes for picking several values.
 */
class CheckMenu<T> extends MenuButton {

    private final Map<T, CheckBox> boxes = new LinkedHashMap<>();

    void setOptions(Collection<T> options) {
        List<T> checked = getChecked();
        boxes.clear();
        getItems().clear();
        for (T option : options) {
            CheckBox box = new CheckBox(ViewUtil.pretty(option));
            box.selectedProperty().addListener((o, old, now) -> showChecked());
            CustomMenuItem item = new CustomMenuItem(box);
            item.setHideOnClick(false);
            boxes.put(option, box);
            getItems().add(item);
        }
        setChecked(checked);
    }

    List<T> getChecked() {
        return boxes.entrySet().stream().filter(e -> e.getValue().isSelected()).map(Map.Entry::getKey).toList();
    }

    void setChecked(Collection<T> values) {
        boxes.forEach((option, box) -> box.setSelected(values.contains(option)));
        showChecked();
    }

    private void showChecked() {
        List<T> checked = getChecked();
        setText(checked.isEmpty() ? "None" : checked.stream().map(ViewUtil::pretty).collect(Collectors.joining(", ")));
    }
}
