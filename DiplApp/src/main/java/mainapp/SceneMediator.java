package mainapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mainapp.controllers.MainGUIController;
import mainapp.controllers.TitleGUIController;

public class SceneMediator {

    private Stage mainWindow = null;

    public SceneMediator(Stage window) {
        mainWindow = window;
    }

    public void showTitleScene() {
        // prepare gui loader
        FXMLLoader titleLoader = new FXMLLoader();
        titleLoader.setController(new TitleGUIController());
        titleLoader.setLocation(getClass().getResource("/guis/TitleGUI.fxml"));
        
        try {
            //load the title gui
            VBox titleVBox = titleLoader.<VBox>load();

            // set the scene to show the title gui
            Scene titleScene = new Scene(titleVBox);
            mainWindow.setScene(titleScene);

            // bind the start button to start the evaluation cycle
            titleLoader.<TitleGUIController>getController().startBtn.setOnAction(ev -> {
                showMainScene();
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
            mainSceneHandler.runEvaluationCycle();
        } catch (Exception e) {
            ErrorWindow ew = new ErrorWindow("Evaluation cycle interrupted by an error.");
            ew.showOverStage(mainWindow);
        }
    }


    public void showErrorWindow(ErrorWindow ew) {
        ew.showOverStage(mainWindow);
    }
}
