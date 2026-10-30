package com.sanad.view;

import atlantafx.base.theme.Styles;
import com.sanad.controller.LoginController;
import com.sanad.model.user.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

/**
 * VIEW: the login screen. Calls onSuccess(user) when the login works.
 */
public class LoginView extends VBox {

    public LoginView(Consumer<User> onSuccess) {
        LoginController controller = new LoginController();
        setPadding(new Insets(32));
        setSpacing(12);
        setAlignment(Pos.CENTER);

        Label title = new Label("Sanad");
        title.getStyleClass().add(Styles.TITLE_2);
        Label subtitle = new Label("Sign in to continue");
        subtitle.getStyleClass().add(Styles.TEXT_MUTED);
        TextField usernameF = new TextField();
        usernameF.setPromptText("Username");
        PasswordField passwordF = new PasswordField();
        passwordF.setPromptText("Password");
        Button loginBtn = new Button("Login");
        loginBtn.getStyleClass().add(Styles.ACCENT);
        loginBtn.setDefaultButton(true);   // Enter key logs in
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        Label message = new Label();
        message.getStyleClass().add(Styles.DANGER);
        Label hint = new Label("First run: admin / admin123");
        hint.getStyleClass().add(Styles.TEXT_MUTED);

        loginBtn.setOnAction(e -> {
            try {
                onSuccess.accept(controller.login(usernameF.getText(), passwordF.getText()));
            } catch (Exception ex) {
                message.setText(ex.getMessage());
                passwordF.clear();
            }
        });

        getChildren().addAll(title, subtitle, usernameF, passwordF, loginBtn, message, hint);
    }
}
