package com.sanad;

import atlantafx.base.theme.PrimerLight;
import com.sanad.controller.LoginController;
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
        showLogin(stage);
        stage.show();
    }

    private void showLogin(Stage stage) {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        stage.setScene(scene(new LoginView(() -> showMain(stage)), 420, 440));
        stage.centerOnScreen();
    }

    private void showMain(Stage stage) {
        stage.setScene(scene(new MainView(() -> {
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
