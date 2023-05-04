package ru.itlc.testproject.clientside;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.itlc.testproject.clientside.utils.HttpWorkUtils;

import java.io.IOException;

public class AppLauncher extends Application {

    private static boolean connectionToRestStatus;

    @Override
    public void init() {
        connectionToRestStatus = HttpWorkUtils.getServerCheckHealth();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppLauncher.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}