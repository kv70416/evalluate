package mainapp.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mainapp.DuplicateRatings;
import mainapp.Results;
import mainapp.configurations.CodeCompilationConfiguration;
import mainapp.configurations.DuplicateDetectionConfiguration;
import mainapp.configurations.FileFetchingConfiguration;
import mainapp.configurations.SolutionScoringConfiguration;
import mainapp.services.CodeCompilationService;
import mainapp.services.DuplicateDetectionService;
import mainapp.services.FileFetchingService;
import mainapp.services.SolutionScoringService;

public class MainGUIController {
    public Pane phasePane = null;
    public Button nextBtn = null;
    
    private Node preparedNode = null;
    private Runnable configCheck = () -> {};
        

    public boolean prepareFileFetchingPhase(Stage mainWindow, FileFetchingService service, FileFetchingConfiguration config) {
        configCheck = () -> {
            nextBtn.setDisable(service == null || config == null || !service.isConfigurationValid(config));
        };
        
        FXMLLoader ffLoader = new FXMLLoader();
        ffLoader.setController(new FileFetchingGUIController(service, config));
        ffLoader.setLocation(getClass().getResource("/guis/FileFetchingGUI.fxml"));
        
        try {
            VBox ffBox = ffLoader.<VBox>load();

            FileFetchingGUIController controller = ffLoader.<FileFetchingGUIController>getController();
            controller.setupFileFetchingModuleGUI(mainWindow, configCheck);

            preparedNode = ffBox;
            return true;
        }
        catch (IOException e) {
            // TODO
            System.out.println(e.getMessage());

            preparedNode = null;
            return false;
        }
    }

    public boolean prepareCodeCompilationPhase(Stage mainWindow, CodeCompilationService service, CodeCompilationConfiguration config) {
        configCheck = () -> {
            nextBtn.setDisable(service == null || config == null || !service.isConfigurationValid(config));
        };

        FXMLLoader ccLoader = new FXMLLoader();
        ccLoader.setController(new CodeCompilationGUIController(service, config));
        ccLoader.setLocation(getClass().getResource("/guis/CodeCompilationGUI.fxml"));
        
        try {
            VBox ccBox = ccLoader.<VBox>load();

            CodeCompilationGUIController controller = ccLoader.<CodeCompilationGUIController>getController();
            controller.setupCodeCompilationModuleGUI(mainWindow, configCheck);

            preparedNode = ccBox;
            return true;
        }
        catch (IOException e) {
            // TODO
            System.out.println(e.getMessage());

            preparedNode = null;
            return false;
        }
    }

    public boolean prepareSolutionScoringPhase(Stage mainWindow, SolutionScoringService service, SolutionScoringConfiguration config) {
        configCheck = () -> {
            nextBtn.setDisable(service == null || config == null || !service.isConfigurationValid(config));
        };

        FXMLLoader ssLoader = new FXMLLoader();
        ssLoader.setController(new SolutionScoringGUIController(service, config));
        ssLoader.setLocation(getClass().getResource("/guis/SolutionScoringGUI.fxml"));
        try {
            VBox ssBox = ssLoader.<VBox>load();

            SolutionScoringGUIController controller = ssLoader.<SolutionScoringGUIController>getController();
            controller.setupSolutionScoringModuleGUI(mainWindow, configCheck);

            preparedNode = ssBox;
            return true;
        }
        catch (IOException e) {
            // TODO
            System.out.println(e.getMessage());

            preparedNode = null;
            return false;
        }
    }

    public boolean prepareDuplicateDetectionPhase(Stage mainWindow, DuplicateDetectionService service, DuplicateDetectionConfiguration config) {
        configCheck = () -> {
            nextBtn.setDisable(service == null || config == null || !service.isConfigurationValid(config));
        };
        
        FXMLLoader ddLoader = new FXMLLoader();
        ddLoader.setController(new DuplicateDetectionGUIController(service, config)); // TODO edit constructor
        ddLoader.setLocation(getClass().getResource("/guis/DuplicateDetectionGUI.fxml"));
        
        try {
            VBox ddBox = ddLoader.<VBox>load();
            
            DuplicateDetectionGUIController controller = ddLoader.<DuplicateDetectionGUIController>getController();
            controller.setupDuplicateDetectionModuleGUI(mainWindow, configCheck);
        
            preparedNode = ddBox;
            return true;
        }
        catch (IOException e) {
            // TODO
            System.out.println(e.getMessage());

            preparedNode = null;
            return false;
        }
    }

    public boolean prepareEvaluationPhase() {
        configCheck = () -> { nextBtn.setDisable(true); };

        preparedNode = new Label("Evaluating..."); // TODO proper GUI
        return true;
    }

    public boolean prepareResultsPhase(Results results, DuplicateRatings ratings) {
        configCheck = () -> { nextBtn.setDisable(true); };

        FXMLLoader resLoader = new FXMLLoader();
        resLoader.setController(new ResultsGUIController());
        resLoader.setLocation(getClass().getResource("/guis/ResultsGUI.fxml"));

        try {
            ScrollPane resultPane = resLoader.<ScrollPane>load();

            ResultsGUIController controller = resLoader.<ResultsGUIController>getController();
            controller.fill(results, ratings);

            resultPane.getStylesheets().add("/css/scrollpane.css");

            preparedNode = resultPane;
            return true;
        }
        catch (IOException e) {
            // TODO
            System.out.println(e.getMessage());

            preparedNode = null;
            return false;
        }
    }

    
    public void setNextPhase(Runnable nextPhaseSetup) {
        nextBtn.setOnAction(ev -> {
            nextPhaseSetup.run();
        });
        nextBtn.setVisible(true);
    }

    public void removeNextPhase() {
        nextBtn.setOnAction(ev -> {
            return;
        });
        nextBtn.setVisible(false);
    }


    public boolean showPreparedPhase() {
        boolean ret = false;

        configCheck.run();
        
        phasePane.getChildren().clear();
        if (preparedNode != null) {
            ret = phasePane.getChildren().add(preparedNode);
        }

        Platform.requestNextPulse();
        
        return ret;
    }
    
}
