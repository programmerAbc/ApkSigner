package com.practice;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        AppController.show(primaryStage);
    }


    public static void main(String[] args) {
        launch();
    }
}
