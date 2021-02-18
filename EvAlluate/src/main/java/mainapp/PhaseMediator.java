package mainapp;

import java.io.IOException;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mainapp.configurations.CodeCompilationConfiguration;
import mainapp.configurations.DuplicateDetectionConfiguration;
import mainapp.configurations.FileFetchingConfiguration;
import mainapp.configurations.SolutionScoringConfiguration;
import mainapp.controllers.CodeCompilationGUIController;
import mainapp.controllers.DuplicateDetectionGUIController;
import mainapp.controllers.FileFetchingGUIController;
import mainapp.controllers.MainGUIController;
import mainapp.controllers.ResultExportDialogGUIController;
import mainapp.controllers.ResultsGUIController;
import mainapp.controllers.SolutionScoringGUIController;
import mainapp.results.ratings.DuplicateRatings;
import mainapp.results.scores.Results;
import mainapp.services.CodeCompilationService;
import mainapp.services.DuplicateDetectionService;
import mainapp.services.FileFetchingService;
import mainapp.services.ResultExportService;
import mainapp.services.SolutionScoringService;

public class PhaseMediator {

    private SceneMediator mediator = null;
    private Stage mainWindow = null;
    private MainGUIController ctrl = null;

    private Evaluator evaluator = null;

    public PhaseMediator(MainGUIController ctrl, Stage mainWindow, SceneMediator mediator) {
        this.ctrl = ctrl;
        this.mainWindow = mainWindow;
        this.mediator = mediator;
    }

    public void runEvaluationCycle() {
        evaluator = new Evaluator(mediator);
        goToFileFetchingPhase();
    }

    // UTIL

    public Stage getMainWindow() {
        return mainWindow;
    }

    public void updateForConfigStatus(boolean valid) {
        ctrl.updateForConfigStatus(valid);
    }

    // FILE FETCHING PHASE

    private void goToFileFetchingPhase() {
        FileFetchingService ffService = evaluator.getFFService();
        FileFetchingConfiguration ffConfig = evaluator.getFFConfig();
        Node phaseNode = prepareFileFetchingPhase(ffService, ffConfig);

        ctrl.setPreviousPhase(() -> {
            mediator.showTitleScene();
        });
        ctrl.setNextPhase(() -> {
            goToCodeCompilationPhase();
        });
        ctrl.updateForConfigStatus(ffService != null && ffConfig != null && ffService.isConfigurationValid(ffConfig));
        ctrl.showPhaseNode(phaseNode);
    }

    private Node ffNode = null;
    private Node prepareFileFetchingPhase(FileFetchingService service, FileFetchingConfiguration config) {
        if (ffNode != null) {
            return ffNode;
        }
        FXMLLoader ffLoader = new FXMLLoader();
        ffLoader.setController(new FileFetchingGUIController(this, service, config));
        ffLoader.setLocation(getClass().getResource("/guis/FileFetchingGUI.fxml"));
        try {
            ffNode = ffLoader.<Node>load();
            return ffNode;
        } catch (IOException e) {
            // TODO
            System.out.println(e.getMessage());
            return null;
        }
    }

    // CODE COMPILATION PHASE

    private void goToCodeCompilationPhase() {
        CodeCompilationService ccService = evaluator.getCCService();
        CodeCompilationConfiguration ccConfig = evaluator.getCCConfig();
        Node phaseNode = prepareCodeCompilationPhase(ccService, ccConfig);

        ctrl.setPreviousPhase(() -> {
            goToFileFetchingPhase();
        });
        ctrl.setNextPhase(() -> {
            goToSolutionScoringPhase();
        });
        ctrl.updateForConfigStatus(ccService != null && ccConfig != null && ccService.isConfigurationValid(ccConfig));
        ctrl.showPhaseNode(phaseNode);
    }

    private Node ccNode = null;
    private Node prepareCodeCompilationPhase(CodeCompilationService service, CodeCompilationConfiguration config) {
        if (ccNode != null) {
            return ccNode;
        }
        FXMLLoader ccLoader = new FXMLLoader();
        ccLoader.setController(new CodeCompilationGUIController(this, service, config));
        ccLoader.setLocation(getClass().getResource("/guis/CodeCompilationGUI.fxml"));
        try {
            ccNode = ccLoader.<Node>load();
            return ccNode;
        } catch (IOException e) {
            // TODO
            System.out.println(e.getMessage());
            return null;
        }

    }

    // SOLUTION SCORING PHASE

    private void goToSolutionScoringPhase() {
        SolutionScoringService ssService = evaluator.getSSService();
        SolutionScoringConfiguration ssConfig = evaluator.getSSConfig();
        Node phaseNode = prepareSolutionScoringPhase(ssService, ssConfig);

        ctrl.setPreviousPhase(() -> {
            goToCodeCompilationPhase();
        });
        ctrl.setNextPhase(() -> {
            goToDuplicateDetectionPhase();
        });
        ctrl.updateForConfigStatus(ssService != null && ssConfig != null && ssService.isConfigurationValid(ssConfig));
        ctrl.showPhaseNode(phaseNode);
    }

    private Node ssNode = null;
    private Node prepareSolutionScoringPhase(SolutionScoringService service, SolutionScoringConfiguration config) {
        if (ssNode != null) {
            return ssNode;
        }
        FXMLLoader ssLoader = new FXMLLoader();
        ssLoader.setController(new SolutionScoringGUIController(this, service, config));
        ssLoader.setLocation(getClass().getResource("/guis/SolutionScoringGUI.fxml"));
        try {
            ssNode = ssLoader.<Node>load();
            return ssNode;
        } catch (IOException e) {
            // TODO
            System.out.println(e.getMessage());
            return null;
        }
    }

    // DUPLICATE DETECTION PHASE

    private void goToDuplicateDetectionPhase() {
        DuplicateDetectionService ddService = evaluator.getDDService();
        DuplicateDetectionConfiguration ddConfig = evaluator.getDDConfig();
        Node phaseNode = prepareDuplicateDetectionPhase(ddService, ddConfig);

        ctrl.setPreviousPhase(() -> {
            goToSolutionScoringPhase();
        });
        ctrl.setNextPhase(() -> {
            goToEvaluationPhase();
        });
        ctrl.updateForConfigStatus(ddService != null && ddConfig != null && ddService.isConfigurationValid(ddConfig));
        ctrl.showPhaseNode(phaseNode);
    }

    private Node ddNode = null;
    private Node prepareDuplicateDetectionPhase(DuplicateDetectionService service, DuplicateDetectionConfiguration config) {
        if (ddNode != null) {
            return ddNode;
        }
        FXMLLoader ddLoader = new FXMLLoader();
        ddLoader.setController(new DuplicateDetectionGUIController(this, service, config));
        ddLoader.setLocation(getClass().getResource("/guis/DuplicateDetectionGUI.fxml"));
        try {
            ddNode = ddLoader.<Node>load();
            return ddNode;
        } catch (IOException e) {
            // TODO
            System.out.println(e.getMessage());
            return null;
        }

    }

    // EVALUATION PHASE

    private void goToEvaluationPhase() {
        Node phaseNode = prepareEvaluationPhase();

        ctrl.removePhaseNavigation();
        ctrl.showPhaseNode(phaseNode);

        Task<Boolean> evalTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                return evaluator.evaluate();
            }
        };

        evalTask.setOnSucceeded(e -> {
            if (evalTask.getValue()) {
                goToResultsPhase(ctrl);
            }
        });

        Thread evalThread = new Thread(evalTask);
        evalThread.start();
    }

    private Node prepareEvaluationPhase() {
        // TODO proper GUI
        return new Label("Evaluating...");
    }

    // RESULTS PHASE

    private void goToResultsPhase(MainGUIController ctrl) {
        Node phaseNode = prepareResultsPhase(evaluator.getResults(), evaluator.getDupRatings());

        ctrl.removePhaseNavigation();
        ctrl.setResultExport(() -> {
            showResultExportDialog();
        });
        ctrl.showPhaseNode(phaseNode);
    }

    private Node prepareResultsPhase(Results results, DuplicateRatings dupRatings) {
        FXMLLoader resLoader = new FXMLLoader();
        resLoader.setController(new ResultsGUIController());
        resLoader.setLocation(getClass().getResource("/guis/ResultsGUI.fxml"));
        try {
            ScrollPane ret = resLoader.<ScrollPane>load();
            resLoader.<ResultsGUIController>getController().fill(results, dupRatings);
            ret.getStylesheets().add("/css/scrollpane.css");
            return ret;
        }
        catch (IOException e) {
            // TODO
            System.out.println(e.getMessage());
            return null;
        }
    }

    private void showResultExportDialog() {
        ResultExportService service = new ResultExportService();

        Stage dialog = new Stage();
        dialog.setTitle("Export results...");
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(mainWindow);

        FXMLLoader resLoader = new FXMLLoader();
        resLoader.setController(new ResultExportDialogGUIController(this, service, evaluator.getResults(), evaluator.getDupRatings()));
        resLoader.setLocation(getClass().getResource("/guis/ResultExportDialogGUI.fxml"));
        try {
            Pane pane = resLoader.<Pane>load();
            dialog.setScene(new Scene(pane));
            dialog.sizeToScene();
            dialog.show();
        }
        catch (IOException e) {
            // TODO
            System.out.println(e.getMessage());
            return;
        }
    }

}
