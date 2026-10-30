package com.sanad.view;

import com.sanad.controller.BeneficiaryController;
import com.sanad.model.beneficiary.Beneficiary;
import com.sanad.model.user.User;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * VIEW: beneficiaries screen (full CRUD).
 */
public class BeneficiaryView extends CrudView<Beneficiary> {

    private final BeneficiaryController controller = new BeneficiaryController();
    private final TextField nameF = new TextField(), contactF = new TextField();
    private final ComboBox<String> needBox = ViewUtil.picker(ViewUtil::pretty);
    private final ComboBox<User> userBox = ViewUtil.picker(User::getUsername);
    private final Map<Integer, String> usernames = new HashMap<>();
    private final DistributionView history = new DistributionView();

    public BeneficiaryView() {
        super("Beneficiaries", "Beneficiary");
        needBox.setPromptText("Pick a need");
        userBox.setPromptText("None");

        table.getColumns().addAll(ViewUtil.<Beneficiary>idCol(), ViewUtil.<Beneficiary>col("Name", "name"),
                ViewUtil.<Beneficiary>col("Need", "need", ViewUtil::pretty), ViewUtil.<Beneficiary>col("Contact", "contact"),
                ViewUtil.<Beneficiary>col("User", "userId", id -> usernames.getOrDefault(id, "")));

        build(ViewUtil.field("Name", nameF), ViewUtil.field("Need", needBox), ViewUtil.field("Contact", contactF),
                ViewUtil.field("User", userBox));

        table.getSelectionModel().selectedItemProperty().addListener((o, old, b) -> history.showFor(b));
        VBox.setVgrow(history, Priority.ALWAYS);
        getChildren().add(history);
    }

    @Override
    protected List<Beneficiary> load() {
        needBox.getItems().setAll(controller.getNeedTypes());
        List<User> users = new ArrayList<>();
        users.add(null);
        users.addAll(controller.getBeneficiaryUsers());
        userBox.getItems().setAll(users);
        usernames.clear();
        users.stream().skip(1).forEach(u -> usernames.put(u.getId(), u.getUsername()));
        return controller.getAll();
    }

    @Override
    protected void fill(Beneficiary v) {
        nameF.setText(v.getName());
        needBox.setValue(v.getNeed());
        contactF.setText(v.getContact());
        userBox.setValue(userBox.getItems().stream()
                .filter(u -> u != null && v.getUserId() != null && u.getId() == v.getUserId())
                .findFirst().orElse(null));
    }

    @Override
    protected void clearForm() {
        nameF.clear();
        needBox.setValue(null);
        contactF.clear();
        userBox.setValue(null);
    }

    @Override
    protected void add() {
        controller.add(nameF.getText(), needBox.getValue(), contactF.getText(), userId());
    }

    @Override
    protected void update(Beneficiary v) {
        controller.update(v.getId(), nameF.getText(), needBox.getValue(), contactF.getText(), userId());
    }

    @Override
    protected void delete(Beneficiary v) {
        controller.delete(v.getId());
    }

    @Override
    protected String describe(Beneficiary v) {
        return "beneficiary " + v.getName() + " and their distribution history";
    }

    private Integer userId() {
        return userBox.getValue() == null ? null : userBox.getValue().getId();
    }
}
