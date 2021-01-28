package mainapp;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mainapp.controllers.ErrorWindowGUIController;

public class ErrorWindow {
    private String message = null;
    private Stage errWindow = null;

    public ErrorWindow(String msg) {
        message = msg;
        errWindow = new Stage();
        errWindow.setWidth(400);
        errWindow.setHeight(200);
        errWindow.setTitle("Error");
    }

    public void showOverStage(Stage primaryStage) {
        errWindow.initModality(Modality.WINDOW_MODAL);
        errWindow.initOwner(primaryStage);

        FXMLLoader errLoader = new FXMLLoader();
        errLoader.setController(new ErrorWindowGUIController(message));
        errLoader.setLocation(getClass().getResource("/guis/ErrorWindowGUI.fxml"));

        try {
            Pane errPane = errLoader.<Pane>load();
            errWindow.setScene(new Scene(errPane));
            errWindow.show();
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }
    }

    public void showAlone() {
        FXMLLoader errLoader = new FXMLLoader();
        errLoader.setController(new ErrorWindowGUIController(message));
        errLoader.setLocation(getClass().getResource("/guis/ErrorWindowGUI.fxml"));

        try {
            Pane errPane = errLoader.<Pane>load();
            errWindow.setScene(new Scene(errPane));
            errWindow.show();
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }
    }

    public void setActionOnClose(Runnable r) {
        errWindow.setOnHiding(ev -> { r.run(); });
    }
}
