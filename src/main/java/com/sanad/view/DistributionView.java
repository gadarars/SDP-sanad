package com.sanad.view;

import com.sanad.controller.BeneficiaryController;
import com.sanad.model.beneficiary.Beneficiary;
import com.sanad.model.beneficiary.Distribution;
import javafx.geometry.Insets;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.converter.LocalDateStringConverter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * VIEW: one beneficiary's distribution history (full CRUD).
 */
public class DistributionView extends CrudView<Distribution> {

    private final BeneficiaryController controller = new BeneficiaryController();
    private final TextField itemF = new TextField(), quantityF = new TextField();
    private final DatePicker datePicker = new DatePicker();
    private Beneficiary beneficiary;

    public DistributionView() {
        super("Distribution history", "Distribution");
        setPadding(Insets.EMPTY);
        datePicker.setConverter(new LocalDateStringConverter(DateTimeFormatter.ISO_LOCAL_DATE, DateTimeFormatter.ISO_LOCAL_DATE));
        itemF.setPromptText("e.g. Food basket");
        quantityF.setPromptText("1");

        table.getColumns().addAll(ViewUtil.<Distribution>idCol(), ViewUtil.<Distribution>col("Date", "givenOn"),
                ViewUtil.<Distribution>col("What", "item"),
                ViewUtil.<Distribution>col("How much", "quantity", q -> ViewUtil.plain((BigDecimal) q)));

        build(ViewUtil.field("What", itemF), ViewUtil.field("How much", quantityF), ViewUtil.field("Date", datePicker));
        showFor(null);
    }

    void showFor(Beneficiary b) {
        beneficiary = b;
        table.setPlaceholder(new Label(b == null ? "Select a beneficiary above to see what they received."
                : b.getName() + " has not received anything yet."));
        clearForm();
        refresh();
    }

    @Override
    protected List<Distribution> load() {
        return beneficiary == null ? List.of() : controller.getHistory(beneficiary.getId());
    }

    @Override
    protected void fill(Distribution d) {
        itemF.setText(d.getItem());
        quantityF.setText(ViewUtil.plain(d.getQuantity()));
        datePicker.setValue(d.getGivenOn());
    }

    @Override
    protected void clearForm() {
        itemF.clear();
        quantityF.clear();
        datePicker.setValue(LocalDate.now());
    }

    @Override
    protected void add() {
        controller.addDistribution(beneficiary == null ? 0 : beneficiary.getId(), itemF.getText(),
                ViewUtil.number(quantityF, "How much"), datePicker.getValue());
    }

    @Override
    protected void update(Distribution d) {
        controller.updateDistribution(d.getId(), itemF.getText(), ViewUtil.number(quantityF, "How much"), datePicker.getValue());
    }

    @Override
    protected void delete(Distribution d) {
        controller.deleteDistribution(d.getId());
    }

    @Override
    protected String describe(Distribution d) {
        return "the " + d.getItem() + " given on " + d.getGivenOn();
    }
}
