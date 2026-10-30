package com.sanad.view;

import com.sanad.controller.BeneficiaryController;
import com.sanad.model.beneficiary.Beneficiary;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * VIEW: beneficiaries screen (full CRUD).
 */
public class BeneficiaryView extends VBox {

    private final BeneficiaryController controller = new BeneficiaryController();
    private final TableView<Beneficiary> table = new TableView<>();
    private final TextField nameF = new TextField(), needF = new TextField(), contactF = new TextField();
    private final Label message = new Label();

    public BeneficiaryView() {
        setPadding(new Insets(12));
        setSpacing(10);
        nameF.setPromptText("Name");
        needF.setPromptText("Need");
        contactF.setPromptText("Contact");

        table.getColumns().addAll(ViewUtil.<Beneficiary>col("ID", "id"), ViewUtil.<Beneficiary>col("Name", "name"),
                ViewUtil.<Beneficiary>col("Need", "need"), ViewUtil.<Beneficiary>col("Contact", "contact"));
        table.getSelectionModel().selectedItemProperty().addListener((o, old, v) -> {
            if (v != null) {
                nameF.setText(v.getName());
                needF.setText(v.getNeed());
                contactF.setText(v.getContact());
            }
        });

        Button add = new Button("Add"), update = new Button("Update"), delete = new Button("Delete");
        add.setOnAction(e -> run(() -> controller.add(nameF.getText(), needF.getText(), contactF.getText()), "Beneficiary added"));
        update.setOnAction(e -> {
            Beneficiary s = table.getSelectionModel().getSelectedItem();
            if (s == null) {
                message.setText("Select a row first");
                return;
            }
            run(() -> controller.update(s.getId(), nameF.getText(), needF.getText(), contactF.getText()), "Beneficiary updated");
        });
        delete.setOnAction(e -> {
            Beneficiary s = table.getSelectionModel().getSelectedItem();
            if (s == null) {
                message.setText("Select a row first");
                return;
            }
            run(() -> controller.delete(s.getId()), "Beneficiary deleted");
        });

        getChildren().addAll(new Label("Beneficiaries"), new HBox(8, nameF, needF, contactF, add, update, delete), message, table);
        refresh();
    }

    private void run(Runnable action, String ok) {
        try {
            action.run();
            message.setText(ok);
            nameF.clear();
            needF.clear();
            contactF.clear();
            refresh();
        } catch (Exception ex) {
            message.setText("Error: " + ex.getMessage());
        }
    }

    void refresh() {
        table.setItems(FXCollections.observableArrayList(controller.getAll()));
    }
}
