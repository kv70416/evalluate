package mainapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mainapp.controllers.MainGUIController;
import mainapp.controllers.TitleGUIController;

public class SceneMediator {

    private Stage mainWindow = null;
    private String config = null;

    public SceneMediator(Stage window) {
        mainWindow = window;
    }

    public void showTitleScene() {
        // reset any imported configuration
        config = null;

        // prepare gui loader
        FXMLLoader titleLoader = new FXMLLoader();
        titleLoader.setController(new TitleGUIController());
        titleLoader.setLocation(getClass().getResource("/guis/TitleGUI.fxml"));

        try {
            // load the title gui
            VBox titleVBox = titleLoader.<VBox>load();

            // set the scene to show the title gui
            Scene titleScene = new Scene(titleVBox);
            mainWindow.setScene(titleScene);

            // bind the start button to start the evaluation cycle
            titleLoader.<TitleGUIController>getController().startBtn.setOnAction(ev -> {
                showMainScene();
            });

            // bind the config import button
            titleLoader.<TitleGUIController>getController().configImportBtn.setOnAction(ev -> {
                config = importConfigFile();
                if (config != null) {
                    showMainScene();
                }
                else {
                    // TODO error
                }
            });

            // show the window
            mainWindow.show();
        } catch (Exception e) {
            ErrorWindow ew = new ErrorWindow("Application GUI load failed.");
            ew.showAlone();
        }
    }

    public void showMainScene() {
        // prepare gui loader
        FXMLLoader mainLoader = new FXMLLoader();
        mainLoader.setController(new MainGUIController());
        mainLoader.setLocation(getClass().getResource("/guis/MainGUI.fxml"));

        try {
            // load the configuration phase gui
            VBox mainVBox = mainLoader.<VBox>load();

            // set the scene to show the configuration phase gui
            Scene mainScene = new Scene(mainVBox);
            mainWindow.setScene(mainScene);

            // run evaluation cycle
            PhaseMediator mainSceneHandler = new PhaseMediator(mainLoader.<MainGUIController>getController(), mainWindow, this);
            if (config != null) {
                mainSceneHandler.runEvaluationCycleWithConfig(config);
            }
            else {
                mainSceneHandler.runEvaluationCycle();
            }
        } catch (Exception e) {
            ErrorWindow ew = new ErrorWindow("Evaluation cycle interrupted by an error.");
            ew.showOverStage(mainWindow);
        }
    }


    private String importConfigFile() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(mainWindow);
        if (selectedFile == null) {
            return null;
        }

        try {
            BufferedReader rdr = new BufferedReader(new FileReader(selectedFile));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = rdr.readLine()) != null) {
                sb.append(line);
            }
            rdr.close();

            return sb.toString();
        } 
        catch (FileNotFoundException e) {
            e.printStackTrace(); // TODO?
            return null;
        }
        catch (IOException e) {
            e.printStackTrace(); // TODO?
            return null;
        }
    }


    public void showErrorWindow(ErrorWindow ew) {
        ew.showOverStage(mainWindow);
    }
}
