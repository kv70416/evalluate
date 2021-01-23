package mainapp;

import java.io.IOException;
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
    
    public void showTitleScene(Stage primaryStage) throws Exception {
        // load the title gui
        FXMLLoader titleLoader = new FXMLLoader();
        titleLoader.setController(new TitleGUIController());
        titleLoader.setLocation(getClass().getResource("/guis/TitleGUI.fxml"));
        VBox titleVBox = titleLoader.<VBox>load();
        
        // set the scene to show the title gui
        Scene titleScene = new Scene(titleVBox);
        primaryStage.setScene(titleScene);
        
        // bind the start button to start the evaluation cycle
        titleLoader.<TitleGUIController>getController().startBtn.setOnAction(ev -> {
            try {
                startEvaluationCycle(primaryStage);
            }
            catch (IOException e) {
                // TODO
                System.err.println(e.getMessage());
            }
        });

        // show the window
        primaryStage.show();
    }
    
    public void startEvaluationCycle(Stage primaryStage) throws IOException {
        // load the configuration phase gui
        FXMLLoader mainLoader = new FXMLLoader();
        mainLoader.setController(new MainGUIController());
        mainLoader.setLocation(getClass().getResource("/guis/MainGUI.fxml"));
        VBox mainVBox = mainLoader.<VBox>load();
        
        // set the scene to show the configuration phase gui
        Scene mainScene = new Scene(mainVBox);
        primaryStage.setScene(mainScene);

        // run evaluation cycle
        evaluator = new Evaluator();
        evaluator.runEvaluationCycle(mainLoader.<MainGUIController>getController(), primaryStage);
    }
        
    public static void main(String[] args) {
        launch(args);
    }

}
