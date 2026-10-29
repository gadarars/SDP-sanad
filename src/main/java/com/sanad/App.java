package com.sanad;

import com.sanad.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Entry point. Run with:  ./mvnw javafx:run */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Sanad - Non-Profit Management System");
        stage.setScene(new Scene(new MainView(), 1000, 560));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
