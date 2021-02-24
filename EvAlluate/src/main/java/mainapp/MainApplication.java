package mainapp;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // set app title
        primaryStage.setTitle("EvAlluate");
        
        // set up the window
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        
        // show title scene
        SceneMediator mediator = new SceneMediator(primaryStage);
        mediator.showTitleScene();
    }
    
        
    public static void launchApp(String[] args) {
        launch(args);
    }

}
