package com.sanad.view;

import com.sanad.controller.DonationController;
import com.sanad.model.donation.Donation;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * VIEW: only builds the screen. It never touches the database directly.
 */
public class DonationView extends VBox {

    private final DonationController controller = new DonationController();
    private final TableView<Donation> table = new TableView<>();
    private final TextField donorF = new TextField();
    private final TextField amountF = new TextField();
    private final ComboBox<String> typeBox =
            new ComboBox<>(FXCollections.observableArrayList("ONLINE", "CHECK", "IN_KIND"));
    private final Label message = new Label();

    public DonationView() {
        setPadding(new Insets(12));
        setSpacing(10);
        donorF.setPromptText("Donor name");
        amountF.setPromptText("Amount");
        typeBox.setValue("ONLINE");

        table.getColumns().addAll(ViewUtil.<Donation>col("ID", "id"), ViewUtil.<Donation>col("Donor", "donorName"),
                ViewUtil.<Donation>col("Amount", "amount"), ViewUtil.<Donation>col("Type", "type"));
        table.getSelectionModel().selectedItemProperty().addListener((o, old, d) -> {
            if (d != null) {
                donorF.setText(d.getDonorName());
                amountF.setText(String.valueOf(d.getAmount()));
                typeBox.setValue(d.getType());
            }
        });

        Button add = new Button("Add"), update = new Button("Update"), delete = new Button("Delete");
        add.setOnAction(e -> run(() -> controller.add(donorF.getText(), Double.parseDouble(amountF.getText()), typeBox.getValue()), "Donation added"));
        update.setOnAction(e -> {
            Donation s = table.getSelectionModel().getSelectedItem();
            if (s == null) {
                message.setText("Select a row first");
                return;
            }
            run(() -> controller.update(s.getId(), donorF.getText(), Double.parseDouble(amountF.getText()), typeBox.getValue()), "Donation updated");
        });
        delete.setOnAction(e -> {
            Donation s = table.getSelectionModel().getSelectedItem();
            if (s == null) {
                message.setText("Select a row first");
                return;
            }
            run(() -> controller.delete(s.getId()), "Donation deleted");
        });

        getChildren().addAll(new Label("Donations"), new HBox(8, donorF, amountF, typeBox, add, update, delete), message, table);
        refresh();
    }

    private void run(Runnable action, String okMessage) {
        try {
            action.run();
            message.setText(okMessage);
            donorF.clear();
            amountF.clear();
            refresh();
        } catch (Exception ex) {
            message.setText("Error: " + ex.getMessage());
        }
    }

    void refresh() {
        table.setItems(FXCollections.observableArrayList(controller.getAll()));
    }
}
