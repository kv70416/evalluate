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

    public ErrorWindow(String msg) {
        message = msg;
    }

    public void showOverStage(Stage primaryStage) {
        Stage errWindow = new Stage();
        errWindow.setWidth(400);
        errWindow.setHeight(300);
        errWindow.initModality(Modality.WINDOW_MODAL);
        errWindow.initOwner(primaryStage);
        errWindow.setTitle("Error");

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
        Stage errWindow = new Stage();
        errWindow.setWidth(400);
        errWindow.setHeight(300);
        errWindow.setTitle("Error");

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
}
