package mainapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mainapp.controllers.MainGUIController;
import mainapp.controllers.TitleGUIController;

public class MainApplication extends Application {
    
    private Evaluator evaluator = null;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // set app title - TODO
        primaryStage.setTitle("MainApp");
        
        // set up the window
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        
        // show title scene
        showTitleScene(primaryStage);
        
    }
    
    public void showTitleScene(Stage primaryStage) {
        // prepare gui loader
        FXMLLoader titleLoader = new FXMLLoader();
        titleLoader.setController(new TitleGUIController());
        titleLoader.setLocation(getClass().getResource("/guis/TitleGUI.fxml"));
        
        try {
            //load the title gui
            VBox titleVBox = titleLoader.<VBox>load();

            // set the scene to show the title gui
            Scene titleScene = new Scene(titleVBox);
            primaryStage.setScene(titleScene);

            // bind the start button to start the evaluation cycle
            titleLoader.<TitleGUIController>getController().startBtn.setOnAction(ev -> {
                startEvaluationCycle(primaryStage);
            });
    
            // show the window
            primaryStage.show();
        } catch (Exception e) {
            ErrorWindow ew = new ErrorWindow("Application GUI load failed.");
            ew.showAlone();
        }
    }
    
    public void startEvaluationCycle(Stage primaryStage) {
        // prepare gui loader
        FXMLLoader mainLoader = new FXMLLoader();
        mainLoader.setController(new MainGUIController());
        mainLoader.setLocation(getClass().getResource("/guis/MainGUI.fxml"));

        try {
            // load the configuration phase gui
            VBox mainVBox = mainLoader.<VBox>load();

            // set the scene to show the configuration phase gui
            Scene mainScene = new Scene(mainVBox);
            primaryStage.setScene(mainScene);

            // run evaluation cycle
            evaluator = new Evaluator();
            evaluator.runEvaluationCycle(mainLoader.<MainGUIController>getController(), primaryStage);
        } catch (Exception e) {
            ErrorWindow ew = new ErrorWindow("Evaluation cycle interrupted by an error.");
            ew.showOverStage(primaryStage);
        }
    }
        
    public static void launchApp(String[] args) {
        launch(args);
    }

}
