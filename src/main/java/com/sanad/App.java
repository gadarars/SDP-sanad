package com.sanad;

import atlantafx.base.theme.PrimerLight;
import com.sanad.controller.LoginController;
import com.sanad.controller.UserController;
import com.sanad.model.user.User;
import com.sanad.view.LoginView;
import com.sanad.view.MainView;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Entry point. Run with:  ./mvnw javafx:run
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Sanad - Non-Profit Management System");
        new UserController().ensureDefaultAdmin();   // makes sure someone can log in
        showLogin(stage);
        stage.show();
    }

    private void showLogin(Stage stage) {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        stage.setScene(scene(new LoginView(user -> showMain(stage, user)), 420, 440));
        stage.centerOnScreen();
    }

    private void showMain(Stage stage, User user) {
        stage.setScene(scene(new MainView(user, () -> {
            new LoginController().logout();
            showLogin(stage);
        }), 1200, 720));
        stage.centerOnScreen();
    }

    private static Scene scene(Parent root, double width, double height) {
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(App.class.getResource("/app.css").toExternalForm());
        return scene;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
