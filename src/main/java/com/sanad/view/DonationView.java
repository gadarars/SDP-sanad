package com.sanad.view;

import com.sanad.controller.DonationController;
import com.sanad.model.donation.Donation;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.List;

/**
 * VIEW: donations screen (full CRUD).
 */
public class DonationView extends CrudView<Donation> {

    private final DonationController controller = new DonationController();
    private final TextField donorF = new TextField();
    private final TextField amountF = new TextField();
    private final ComboBox<String> typeBox = ViewUtil.choice("ONLINE", "CHECK", "IN_KIND");

    public DonationView() {
        super("Donations", "Donation");
        amountF.setPromptText("0.00");

        table.getColumns().addAll(ViewUtil.<Donation>idCol(), ViewUtil.<Donation>col("Donor", "donorName"),
                ViewUtil.<Donation>numberCol("Amount", "amount", "EGP %,.2f"),
                ViewUtil.<Donation>col("Type", "type", ViewUtil::pretty));

        build(ViewUtil.field("Donor name", donorF), ViewUtil.field("Amount (EGP)", amountF), ViewUtil.field("Type", typeBox));
    }

    @Override
    protected List<Donation> load() {
        return controller.getAll();
    }

    @Override
    protected void fill(Donation d) {
        donorF.setText(d.getDonorName());
        amountF.setText(ViewUtil.plain(d.getAmount()));
        typeBox.setValue(d.getType());
    }

    @Override
    protected void clearForm() {
        donorF.clear();
        amountF.clear();
    }

    @Override
    protected void add() {
        controller.add(donorF.getText(), ViewUtil.number(amountF, "Amount"), typeBox.getValue());
    }

    @Override
    protected void update(Donation d) {
        controller.update(d.getId(), donorF.getText(), ViewUtil.number(amountF, "Amount"), typeBox.getValue());
    }

    @Override
    protected void delete(Donation d) {
        controller.delete(d.getId());
    }

    @Override
    protected String describe(Donation d) {
        return "the donation from " + d.getDonorName();
    }
}
