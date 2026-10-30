package com.sanad.view;

import com.sanad.controller.UserController;
import com.sanad.model.user.User;
import com.sanad.model.user.UserType;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.*;
import java.util.stream.Collectors;

/**
 * VIEW: user control panel (full CRUD for every user type, including its own fields).
 */
public class UserView extends CrudView<User> {

    private static final UserType DEFAULT_TYPE = UserType.STAFF;

    private final UserController controller = new UserController();
    private final TextField usernameF = new TextField(), emailF = new TextField();
    private final PasswordField passwordF = new PasswordField();
    private final ComboBox<UserType> roleBox = ViewUtil.choice(UserType.values());
    private final Map<String, TextField> detailFields = new LinkedHashMap<>();

    public UserView() {
        super("Users", "User");
        roleBox.setValue(DEFAULT_TYPE);

        table.getColumns().addAll(ViewUtil.<User>idCol(), ViewUtil.<User>col("Username", "username"),
                ViewUtil.<User>col("Email", "email"), ViewUtil.<User>col("Role", "role", ViewUtil::pretty),
                ViewUtil.<User>col("Details", "details", UserView::describeDetails),
                ViewUtil.<User>col("Can open", "features", UserView::describeFeatures));

        passwordF.setPromptText("blank = keep current");

        List<Node> fields = new ArrayList<>(List.of(ViewUtil.field("Username", usernameF), ViewUtil.field("Email", emailF),
                ViewUtil.field("Password", passwordF), ViewUtil.field("Role", roleBox)));
        for (String column : UserType.allFields()) {
            TextField input = new TextField();
            VBox box = ViewUtil.field(ViewUtil.pretty(column), input);
            // only the fields of the chosen role are shown
            box.visibleProperty().bind(Bindings.createBooleanBinding(
                    () -> roleBox.getValue() != null && roleBox.getValue().getFields().contains(column), roleBox.valueProperty()));
            box.managedProperty().bind(box.visibleProperty());
            detailFields.put(column, input);
            fields.add(box);
        }
        build(fields.toArray(Node[]::new));
    }

    @Override
    protected List<User> load() {
        return controller.getAll();
    }

    @Override
    protected void fill(User v) {
        usernameF.setText(v.getUsername());
        emailF.setText(v.getEmail());
        roleBox.setValue(v.getType());
        detailFields.forEach((column, input) -> input.setText(v.getDetails().getOrDefault(column, "")));
    }

    @Override
    protected void clearForm() {
        usernameF.clear();
        emailF.clear();
        passwordF.clear();
        detailFields.values().forEach(TextField::clear);
        roleBox.setValue(DEFAULT_TYPE);
    }

    @Override
    protected void add() {
        controller.add(usernameF.getText(), emailF.getText(), roleBox.getValue(), passwordF.getText(), details());
    }

    @Override
    protected void update(User v) {
        controller.update(v.getId(), usernameF.getText(), emailF.getText(), roleBox.getValue(), passwordF.getText(), details());
    }

    @Override
    protected void delete(User v) {
        controller.delete(v.getId());
    }

    @Override
    protected String describe(User v) {
        return "user " + v.getUsername();
    }

    private Map<String, String> details() {
        Map<String, String> values = new HashMap<>();
        detailFields.forEach((column, input) -> values.put(column, input.getText()));
        return values;
    }

    /**
     * Shows {"job_title": "Manager"} as "Job title: Manager".
     */
    private static String describeDetails(Object details) {
        return ((Map<?, ?>) details).entrySet().stream()
                .filter(e -> !e.getValue().toString().isEmpty())
                .map(e -> ViewUtil.pretty(e.getKey()) + ": " + e.getValue())
                .collect(Collectors.joining(", "));
    }

    /**
     * Shows {EVENTS, DONATIONS} as "Donations, Events".
     */
    private static String describeFeatures(Object features) {
        return ((Set<?>) features).stream().map(ViewUtil::pretty).sorted().collect(Collectors.joining(", "));
    }
}
