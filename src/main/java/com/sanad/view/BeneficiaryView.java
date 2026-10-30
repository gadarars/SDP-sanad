package com.sanad.view;

import com.sanad.controller.BeneficiaryController;
import com.sanad.model.beneficiary.Beneficiary;
import javafx.scene.control.TextField;

import java.util.List;

/**
 * VIEW: beneficiaries screen (full CRUD).
 */
public class BeneficiaryView extends CrudView<Beneficiary> {

    private final BeneficiaryController controller = new BeneficiaryController();
    private final TextField nameF = new TextField(), needF = new TextField(), contactF = new TextField();

    public BeneficiaryView() {
        super("Beneficiaries", "Beneficiary");

        table.getColumns().addAll(ViewUtil.<Beneficiary>idCol(), ViewUtil.<Beneficiary>col("Name", "name"),
                ViewUtil.<Beneficiary>col("Need", "need"), ViewUtil.<Beneficiary>col("Contact", "contact"));

        build(ViewUtil.field("Name", nameF), ViewUtil.field("Need", needF), ViewUtil.field("Contact", contactF));
    }

    @Override
    protected List<Beneficiary> load() {
        return controller.getAll();
    }

    @Override
    protected void fill(Beneficiary v) {
        nameF.setText(v.getName());
        needF.setText(v.getNeed());
        contactF.setText(v.getContact());
    }

    @Override
    protected void clearForm() {
        nameF.clear();
        needF.clear();
        contactF.clear();
    }

    @Override
    protected void add() {
        controller.add(nameF.getText(), needF.getText(), contactF.getText());
    }

    @Override
    protected void update(Beneficiary v) {
        controller.update(v.getId(), nameF.getText(), needF.getText(), contactF.getText());
    }

    @Override
    protected void delete(Beneficiary v) {
        controller.delete(v.getId());
    }

    @Override
    protected String describe(Beneficiary v) {
        return "beneficiary " + v.getName();
    }
}
