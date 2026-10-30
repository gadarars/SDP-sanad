package com.sanad.view;

import com.sanad.controller.UserController;
import com.sanad.model.user.User;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.List;

/**
 * VIEW: user control panel (full CRUD for users).
 */
public class UserView extends CrudView<User> {

    private final UserController controller = new UserController();
    private final TextField usernameF = new TextField(), emailF = new TextField();
    private final ComboBox<String> roleBox = ViewUtil.choice("ADMIN", "STAFF", "DONOR", "VOLUNTEER", "BENEFICIARY");

    public UserView() {
        super("Users", "User");
        roleBox.setValue("STAFF");

        table.getColumns().addAll(ViewUtil.<User>idCol(), ViewUtil.<User>col("Username", "username"),
                ViewUtil.<User>col("Email", "email"), ViewUtil.<User>col("Role", "role", ViewUtil::pretty));

        build(ViewUtil.field("Username", usernameF), ViewUtil.field("Email", emailF), ViewUtil.field("Role", roleBox));
    }

    @Override
    protected List<User> load() {
        return controller.getAll();
    }

    @Override
    protected void fill(User v) {
        usernameF.setText(v.getUsername());
        emailF.setText(v.getEmail());
        roleBox.setValue(v.getRole());
    }

    @Override
    protected void clearForm() {
        usernameF.clear();
        emailF.clear();
    }

    @Override
    protected void add() {
        controller.add(usernameF.getText(), emailF.getText(), roleBox.getValue());
    }

    @Override
    protected void update(User v) {
        controller.update(v.getId(), usernameF.getText(), emailF.getText(), roleBox.getValue());
    }

    @Override
    protected void delete(User v) {
        controller.delete(v.getId());
    }

    @Override
    protected String describe(User v) {
        return "user " + v.getUsername();
    }
}
