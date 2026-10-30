package com.sanad;

import com.sanad.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Entry point. Run with:  ./mvnw javafx:run
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new MainView(), 1200, 720);
        scene.getStylesheets().add(App.class.getResource("/app.css").toExternalForm());
        stage.setTitle("Sanad - Non-Profit Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
