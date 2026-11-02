package com.sanad.view;

import atlantafx.base.theme.Styles;
import com.sanad.controller.DonationController;
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

import java.math.BigDecimal;
import java.util.List;

/**
 * VIEW: donations screen (full CRUD).
 * Integrates donor linking & history, Strategy pattern (methods), and Decorator pattern (add-ons).
 */
public class DonationView extends CrudView<Donation> {

    private final DonationController controller = new DonationController();
    private final ComboBox<User> donorBox = ViewUtil.picker(User::getUsername);
    private final TextField donorF = new TextField();
    private final TextField amountF = new TextField();
    private final ComboBox<DonationMethod> methodBox = ViewUtil.picker(DonationMethod::getLabel);

    // Decorator add-ons
    private final CheckBox giftWrapCb = new CheckBox("Gift wrapping (+25)");
    private final CheckBox thankYouCb = new CheckBox("Thank-you card (+15)");
    private final Label previewLabel = new Label("Donation — EGP 0.00");

    public DonationView() {
        super("Donations", "Donation");
        amountF.setPromptText("0.00");
        previewLabel.getStyleClass().add(Styles.TEXT_MUTED);

        methodBox.getItems().setAll(DonationMethods.all());
        methodBox.getSelectionModel().selectFirst();

        donorBox.valueProperty().addListener((o, old, u) -> {
            if (u != null) {
                donorF.setText(u.getUsername());
            }
        });

        amountF.textProperty().addListener((o, old, text) -> updatePreview());
        giftWrapCb.selectedProperty().addListener((o, old, val) -> updatePreview());
        thankYouCb.selectedProperty().addListener((o, old, val) -> updatePreview());

        table.getColumns().addAll(
                ViewUtil.<Donation>idCol(),
                ViewUtil.<Donation>col("Donor", "donorName"),
                ViewUtil.<Donation>numberCol("Amount", "amount", "EGP %,.2f"),
                ViewUtil.<Donation>col("Method", "method", ViewUtil::pretty)
        );

        Label addOnLabel = new Label("Add-ons (Decorator)");
        addOnLabel.getStyleClass().add(Styles.TEXT_MUTED);
        VBox addOnsBox = new VBox(6, addOnLabel, new HBox(8, giftWrapCb, thankYouCb), previewLabel);
        addOnsBox.setPrefWidth(240);
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
        donorBox.getItems().setAll(controller.getDonors());
        return controller.getAll();
    }

    @Override
    protected void fill(Donation d) {
        donorF.setText(d.getDonorName());
        donorBox.setValue(donorBox.getItems().stream()
                .filter(u -> d.getUserId() != null && u.getId() == d.getUserId())
                .findFirst().orElse(null));
        amountF.setText(ViewUtil.plain(d.getAmount()));
        methodBox.setValue(d.getDonationMethod());
        updatePreview();
    }

    @Override
    protected void clearForm() {
        donorF.clear();
        donorBox.setValue(null);
        amountF.clear();
        methodBox.getSelectionModel().selectFirst();
        giftWrapCb.setSelected(false);
        thankYouCb.setSelected(false);
        updatePreview();
    }

    @Override
    protected void add() {
        User u = donorBox.getValue();
        Integer userId = u != null ? u.getId() : null;
        String name = !donorF.getText().isBlank() ? donorF.getText() : (u != null ? u.getUsername() : null);
        controller.add(userId, name, ViewUtil.number(amountF, "Amount"), methodBox.getValue());
    }

    @Override
    protected void update(Donation d) {
        User u = donorBox.getValue();
        Integer userId = u != null ? u.getId() : d.getUserId();
        String name = !donorF.getText().isBlank() ? donorF.getText() : (u != null ? u.getUsername() : d.getDonorName());
        controller.update(d.getId(), userId, name, ViewUtil.number(amountF, "Amount"), methodBox.getValue());
    }

    @Override
    protected void delete(Donation d) {
        controller.delete(d.getId());
    }

    @Override
    protected String describe(Donation d) {
        return "the donation from " + d.getDonorName();
    }

    private void updatePreview() {
        try {
            String text = amountF.getText();
            BigDecimal baseAmt = (text == null || text.isBlank()) ? BigDecimal.ZERO : new BigDecimal(text.strip());
            DonationComponent comp = controller.decorate(baseAmt, giftWrapCb.isSelected(), thankYouCb.isSelected());
            previewLabel.setText(comp.getDescription() + " — EGP " + String.format("%,.2f", comp.getAmount()));
        } catch (Exception ex) {
            previewLabel.setText("Invalid amount");
        }
    }

    private void showHistory(Donation d) {
        List<Donation> history = controller.getHistory(d);
        BigDecimal total = history.stream().map(Donation::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

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

        Label summary = new Label("Total: EGP " + String.format("%,.2f", total) + " (" + history.size() + " records)");
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
        String receiptText = controller.generateReceipt(d, giftWrapCb.isSelected(), thankYouCb.isSelected());
        TextArea area = new TextArea(receiptText);
        area.setEditable(false);
        area.setStyle("-fx-font-family: monospace;");
        area.setPrefColumnCount(52);
        area.setPrefRowCount(18);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(getScene().getWindow());
        dialog.setTitle("Donation receipt");
        dialog.setHeaderText("Receipt for donation #" + d.getId() + " (" + d.getDonorName() + ")");
        dialog.getDialogPane().setContent(area);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }
}
