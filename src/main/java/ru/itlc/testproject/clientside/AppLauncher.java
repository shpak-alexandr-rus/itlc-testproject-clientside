package ru.itlc.testproject.clientside;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import ru.itlc.testproject.clientside.components.MainView;
import ru.itlc.testproject.clientside.utils.HttpWorkUtils;

import java.util.Optional;

public class AppLauncher extends Application {

    private static boolean connectionToRestStatus;

    @Override
    public void init() {
        connectionToRestStatus = HttpWorkUtils.getServerCheckHealth();
    }

    @Override
    public void start(Stage stage) {
        if (connectionToRestStatus) {
            new MainView();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Сервер недоступен");
            alert.setHeaderText("Не удалось соединиться с сервером.\nПроверьте его работу на localhost:8080");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                System.exit(1);
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}