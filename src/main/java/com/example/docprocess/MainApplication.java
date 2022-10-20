package com.example.docprocess;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    private static FXMLLoader fxmlLoader;
    public static FXMLLoader getFxmlLoader() {
        return fxmlLoader;
    }

    @Override
    public void start(Stage stage) throws IOException {
        fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Document Agreement");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        MainApplication.launch();
    }
}