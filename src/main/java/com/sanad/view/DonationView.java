package com.sanad.view;

import atlantafx.base.theme.Styles;
import com.sanad.controller.DonationController;
import com.sanad.model.donation.AddOn;
import com.sanad.model.donation.Donation;
import com.sanad.model.donation.DonationComponent;
import com.sanad.model.donation.DonationMethod;
import com.sanad.model.donation.DonationMethods;
import com.sanad.model.user.User;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * VIEW: donations screen (full CRUD) with donor history, receipts, methods (Strategy) and add-ons (Decorator).
 */
public class DonationView extends CrudView<Donation> {

    private final DonationController controller = new DonationController();
    private final ComboBox<User> donorBox = ViewUtil.picker(User::getUsername);
    private final TextField donorF = new TextField();
    private final TextField amountF = new TextField();
    private final ComboBox<DonationMethod> methodBox = ViewUtil.picker(DonationMethod::getLabel);
    private final List<AddOn> addOns = new ArrayList<>();
    private final Label previewLabel = new Label();

    public DonationView() {
        super("Donations", "Donation");
        amountF.setPromptText("0.00");
        donorBox.setPromptText("None");
        previewLabel.getStyleClass().add(Styles.TEXT_MUTED);
        previewLabel.setWrapText(true);

        methodBox.getItems().setAll(DonationMethods.all());
        methodBox.getSelectionModel().selectFirst();

        donorBox.setDisable(!controller.choosesDonor());
        donorF.disableProperty().bind(donorBox.valueProperty().isNotNull());
        donorBox.valueProperty().addListener((o, old, u) -> {
            if (u != null) {
                donorF.setText(u.getUsername());
            }
        });

        amountF.textProperty().addListener((o, old, text) -> updatePreview());
        updatePreview();

        table.getColumns().addAll(
                ViewUtil.<Donation>idCol(),
                ViewUtil.<Donation>col("Donor", "donorName"),
                ViewUtil.<Donation>numberCol("Amount", "amount", "EGP %,.2f"),
                ViewUtil.<Donation>col("Method", "method", ViewUtil::pretty)
        );

        HBox addOnButtons = new HBox(8);
        for (AddOn addOn : AddOn.values()) {
            Button b = new Button(addOn.getLabel() + " (+" + ViewUtil.plain(addOn.getCost()) + ")", new FontIcon(Feather.PLUS));
            b.setOnAction(e -> {
                addOns.add(addOn);
                updatePreview();
            });
            addOnButtons.getChildren().add(b);
        }
        Button clear = new Button("Clear", new FontIcon(Feather.X));
        clear.getStyleClass().add(Styles.FLAT);
        clear.setOnAction(e -> {
            addOns.clear();
            updatePreview();
        });
        addOnButtons.getChildren().add(clear);

        Label addOnLabel = new Label("Add-ons");
        addOnLabel.getStyleClass().add(Styles.TEXT_MUTED);
        VBox addOnsBox = new VBox(6, addOnLabel, addOnButtons, previewLabel);
        addOnsBox.setPrefWidth(320);
        HBox.setHgrow(addOnsBox, Priority.ALWAYS);

        build(
                ViewUtil.field("Donor user", donorBox),
                ViewUtil.field("Donor name", donorF),
                ViewUtil.field("Amount (EGP)", amountF),
                ViewUtil.field("Method", methodBox),
                addOnsBox
        );

        action("History", Feather.CLOCK, this::showHistory, "History loaded");
        action("Receipt", Feather.FILE_TEXT, this::showReceipt, "Receipt generated");
    }

    @Override
    protected List<Donation> load() {
        List<User> donors = new ArrayList<>();
        if (controller.choosesDonor()) donors.add(null);
        donors.addAll(controller.getDonors());
        donorBox.getItems().setAll(donors);
        if (!controller.choosesDonor()) donorBox.setValue(donors.get(0));
        return controller.getAll();
    }

    @Override
    protected void fill(Donation d) {
        donorBox.setValue(donorBox.getItems().stream()
                .filter(u -> u != null && d.getUserId() != null && u.getId() == d.getUserId())
                .findFirst().orElse(null));
        donorF.setText(d.getDonorName());
        amountF.setText(ViewUtil.plain(d.getAmount()));
        methodBox.setValue(d.getDonationMethod());
        addOns.clear();
        addOns.addAll(d.getAddOns());
        updatePreview();
    }

    @Override
    protected void clearForm() {
        if (controller.choosesDonor()) donorBox.setValue(null);
        donorF.setText(controller.choosesDonor() ? "" : donorBox.getValue().getUsername());
        amountF.clear();
        methodBox.getSelectionModel().selectFirst();
        addOns.clear();
        updatePreview();
    }

    @Override
    protected void add() {
        controller.add(donorId(), donorF.getText(), ViewUtil.number(amountF, "Amount"), methodBox.getValue(), addOns);
    }

    @Override
    protected void update(Donation d) {
        controller.update(d.getId(), donorId(), donorF.getText(), ViewUtil.number(amountF, "Amount"),
                methodBox.getValue(), addOns);
    }

    @Override
    protected void delete(Donation d) {
        controller.delete(d.getId());
    }

    @Override
    protected String describe(Donation d) {
        return "the donation from " + d.getDonorName();
    }

    private Integer donorId() {
        return donorBox.getValue() == null ? null : donorBox.getValue().getId();
    }

    private void updatePreview() {
        try {
            String text = amountF.getText().strip();
            BigDecimal amount = text.isEmpty() ? BigDecimal.ZERO : new BigDecimal(text);
            DonationComponent donation = controller.preview(amount, addOns);
            previewLabel.setText(donation.getDescription() + " — EGP " + String.format("%,.2f", donation.cost()));
        } catch (NumberFormatException ex) {
            previewLabel.setText("Invalid amount");
        }
    }

    private void showHistory(Donation d) {
        List<Donation> history = controller.getHistory(d);
        BigDecimal total = controller.total(history);

        TableView<Donation> historyTable = new TableView<>();
        historyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        historyTable.getStyleClass().add(Styles.STRIPED);
        historyTable.getColumns().addAll(
                ViewUtil.<Donation>idCol(),
                ViewUtil.<Donation>numberCol("Amount", "amount", "EGP %,.2f"),
                ViewUtil.<Donation>col("Method", "method", ViewUtil::pretty)
        );
        historyTable.getItems().setAll(history);
        historyTable.setPrefHeight(260);

        Label summary = new Label("Total with add-ons: EGP " + String.format("%,.2f", total) + " (" + history.size() + " records)");
        summary.getStyleClass().add(Styles.TEXT_BOLD);

        VBox content = new VBox(12, summary, historyTable);
        content.setPadding(new Insets(16));
        content.setPrefWidth(480);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(getScene().getWindow());
        dialog.setTitle("Donor history");
        dialog.setHeaderText("Donation history for " + d.getDonorName());
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    private void showReceipt(Donation d) {
        TextArea area = new TextArea(controller.generateReceipt(d));
        area.setEditable(false);
        area.setStyle("-fx-font-family: monospace;");
        area.setPrefColumnCount(52);
        area.setPrefRowCount(16);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(getScene().getWindow());
        dialog.setTitle("Donation receipt");
        dialog.setHeaderText("Receipt for donation #" + d.getId() + " (" + d.getDonorName() + ")");
        dialog.getDialogPane().setContent(area);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }
}
