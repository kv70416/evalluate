package mainapp.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mainapp.DuplicateRatings;
import mainapp.Results;
import mainapp.configurations.CodeCompilationConfiguration;
import mainapp.configurations.DuplicateDetectionConfiguration;
import mainapp.configurations.FileFetchingConfiguration;
import mainapp.configurations.ModuleConfiguration;
import mainapp.configurations.SolutionScoringConfiguration;
import mainapp.services.CodeCompilationService;
import mainapp.services.DuplicateDetectionService;
import mainapp.services.FileFetchingService;
import mainapp.services.ModuleService;
import mainapp.services.SolutionScoringService;

public class MainGUIController {
    public Pane phasePane = null;
    public Button nextBtn = null;
    
    private ModuleService currentService = null;
    private ModuleConfiguration currentConfig = null;
        
    private boolean showInPhasePane(Node node) {
        System.out.println("--- PHASE PANE UPDATE ---");
        
        boolean ret = false;
        
        phasePane.getChildren().clear();
        if (node != null) {
            ret = phasePane.getChildren().add(node);
        }

        Platform.requestNextPulse();
        
        return ret;
    }
    
    public void showFileFecthingPhase(Stage mainWindow, FileFetchingService service, FileFetchingConfiguration config, Runnable nextPhase) {
        FXMLLoader ffLoader = new FXMLLoader();
        ffLoader.setController(new FileFetchingGUIController(service, config));
        ffLoader.setLocation(getClass().getResource("/guis/FileFetchingGUI.fxml"));
        
        try {
            VBox ffBox = ffLoader.<VBox>load();
            
            FileFetchingGUIController controller = ffLoader.<FileFetchingGUIController>getController();
            controller.setupFileFetchingModuleGUI(mainWindow, () -> { this.refreshScene(); });

            nextBtn.setOnAction(ev -> {
                nextPhase.run();
            });
            
            currentService = service;
            currentConfig = config;

            refreshScene();
            
            showInPhasePane(ffBox);
        }
        catch (IOException e) {
            // TODO
            System.out.println(e.getMessage());
        }
    }
    
    public void showCodeCompilationPhase(Stage mainWindow, CodeCompilationService service, CodeCompilationConfiguration config, Runnable nextPhase) {
        FXMLLoader ccLoader = new FXMLLoader();
        ccLoader.setController(new CodeCompilationGUIController(service, config));
        ccLoader.setLocation(getClass().getResource("/guis/CodeCompilationGUI.fxml"));
        
        try {
            VBox ccBox = ccLoader.<VBox>load();

            CodeCompilationGUIController controller = ccLoader.<CodeCompilationGUIController>getController();
            controller.setupCodeCompilationModuleGUI(mainWindow, () -> { this.refreshScene(); });

            nextBtn.setOnAction(ev -> {
                nextPhase.run();
            });

            currentService = service;
            currentConfig = config;

            refreshScene();
            
            showInPhasePane(ccBox);
        }
        catch (IOException e) {
            // TODO
            System.out.println(e.getMessage());
        }
    }
    
    public void showSolutionScoringPhase(Stage mainWindow, SolutionScoringService service, SolutionScoringConfiguration config, Runnable nextPhase) {
        FXMLLoader ssLoader = new FXMLLoader();
        ssLoader.setController(new SolutionScoringGUIController(service, config));
        ssLoader.setLocation(getClass().getResource("/guis/SolutionScoringGUI.fxml"));
        try {
            VBox ssBox = ssLoader.<VBox>load();

            SolutionScoringGUIController controller = ssLoader.<SolutionScoringGUIController>getController();
            controller.setupSolutionScoringModuleGUI(mainWindow, () -> { this.refreshScene(); });

            nextBtn.setOnAction(ev -> {
                nextPhase.run();
            });

            currentService = service;
            currentConfig = config;

            refreshScene();
            
            showInPhasePane(ssBox);
        }
        catch (IOException e) {
            // TODO
            System.out.println(e.getMessage());
        }
    }
    
    public void showDuplicateDetectionPhase(Stage mainWindow, DuplicateDetectionService service, DuplicateDetectionConfiguration config, Runnable nextPhase) {
        FXMLLoader ddLoader = new FXMLLoader();
        ddLoader.setController(new DuplicateDetectionGUIController(service, config)); // TODO edit constructor
        ddLoader.setLocation(getClass().getResource("/guis/DuplicateDetectionGUI.fxml"));
        
        try {
            VBox ddBox = ddLoader.<VBox>load();
            
            DuplicateDetectionGUIController controller = ddLoader.<DuplicateDetectionGUIController>getController();
            controller.setupDuplicateDetectionModuleGUI(mainWindow, () -> { this.refreshScene(); });
        
            nextBtn.setOnAction(ev -> {
                nextPhase.run();
            });
            
            currentService = service;
            currentConfig = config;

            refreshScene();

            //showInPhasePane(new Label("TBA"));
            
            showInPhasePane(ddBox);
        }
        catch (IOException e) {
            // TODO
            System.out.println(e.getMessage());
        }
    }
    
    public void showEvaluationPhase() {
        nextBtn.setOnAction(ev -> {
            return;
        });
        nextBtn.setVisible(false);
        
        refreshScene();
        
        showInPhasePane(new Label("Evaluating..."));
    }
    
    public void showResultsPhase(Results results, DuplicateRatings ratings) {
        FXMLLoader resLoader = new FXMLLoader();
        resLoader.setController(new ResultsGUIController());
        resLoader.setLocation(getClass().getResource("/guis/ResultsGUI.fxml"));

        try {
            ScrollPane resultPane = resLoader.<ScrollPane>load();

            ResultsGUIController ctrl = resLoader.<ResultsGUIController>getController();

            ctrl.fill(results, ratings);

            resultPane.getStylesheets().add("/css/scrollpane.css");

            showInPhasePane(resultPane);
        }
        catch (IOException e) {
            // TODO
            System.out.println(e.getMessage());
        }

        /*
        refreshScene();
        
        VBox resultBox = new VBox();
        
        for (Results.StudentScore studentScore : results.getStudentScores()) {
            VBox studentResultBox = new VBox();
            
            Text studentResultLabel = new Text("Results for student " + studentScore.getStudent() + ": ");
            studentResultBox.getChildren().add(studentResultLabel);

            for (Results.ModuleScore moduleScore : studentScore.getModuleScores()) {
                String moduleName = moduleScore.getModuleName();
                
                Text resultForModuleLabel = new Text("Module " + moduleName + ": ");
                studentResultBox.getChildren().add(resultForModuleLabel);
                
                GridPane studentModuleResultGrid = new GridPane();
                int pos = 0;
                
                for (Results.TestScore testScore : moduleScore.getTestScores()) {
                    String test = testScore.getTestName();
                    double score = testScore.getTestScore();
                    
                    studentModuleResultGrid.add(new Text(test), pos, 0, 1, 1);
                    studentModuleResultGrid.add(new Text(Double.toString(score)), pos, 1, 1, 1);

                    pos++;
                }

                studentResultBox.getChildren().add(studentModuleResultGrid);
            }
            
            resultBox.getChildren().add(studentResultBox);
        }
        
        for (DuplicateRatings.StudentDupRating studentRating : ratings.getStudentRatings()) {
            VBox studentRatingBox = new VBox();
            
            Text studentRatingLabel = new Text("Duplicate detection ratings for " + studentRating.getStudent() + ": ");
            studentRatingBox.getChildren().add(studentRatingLabel);

            for (DuplicateRatings.ModuleDupRating moduleRating : studentRating.getModuleRatings()) {
                String moduleName = moduleRating.getModuleName();
                
                Text ratingForModuleLabel = new Text("Module " + moduleName + ": ");
                studentRatingBox.getChildren().add(ratingForModuleLabel);
                
                GridPane studentModuleRatingTable = new GridPane();
                int pos = 0;
                
                for (DuplicateRatings.PairwiseDupRating pairwiseRating : moduleRating.getPairwiseRatings()) {
                    String compStudent = pairwiseRating.getComparisonStudent();
                    double compRating = pairwiseRating.getComparisonRating();
                    
                    studentModuleRatingTable.add(new Text(Double.toString(compRating)), 0, pos, 1, 1);
                    studentModuleRatingTable.add(new Text(compStudent), 1, pos, 1, 1);

                    pos++;
                }

                studentRatingBox.getChildren().add(studentModuleRatingTable);
            }
            
            resultBox.getChildren().add(studentRatingBox);
        }
        
        showInPhasePane(resultBox);
        */
    }
        
    
    public void refreshScene() {
        System.out.println("refresh");
        
        if (currentConfig == null || currentService == null) {
            nextBtn.setDisable(true);
        }
        
        nextBtn.setDisable(!currentService.isConfigurationValid(currentConfig));
    }
}
