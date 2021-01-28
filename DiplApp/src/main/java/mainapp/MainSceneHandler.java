package mainapp;

import javafx.concurrent.Task;
import javafx.stage.Stage;
import mainapp.controllers.MainGUIController;

public class MainSceneHandler {

    private MainGUIController ctrl = null;
    private Stage mainWindow = null;
    private SceneMediator mediator = null;

    private Evaluator evaluator = null;


    public MainSceneHandler(MainGUIController ctrl, Stage mainWindow, SceneMediator mediator) {
        this.ctrl = ctrl;
        this.mainWindow = mainWindow;
        this.mediator = mediator;
    }

    public void runEvaluationCycle() {
        evaluator = new Evaluator(mediator);
        goToFileFetchingPhase();
    }


    private void goToFileFetchingPhase() {
        ctrl.prepareFileFetchingPhase(mainWindow, evaluator.getFFService(), evaluator.getFFConfig());
        ctrl.setNextPhase(() -> { goToCodeCompilationPhase(); });
        ctrl.showPreparedPhase();
    }
    
    private void goToCodeCompilationPhase() {
        ctrl.prepareCodeCompilationPhase(mainWindow, evaluator.getCCService(), evaluator.getCCConfig());
        ctrl.setNextPhase(() -> { goToSolutionScoringPhase(); });
        ctrl.showPreparedPhase();
    }

    private void goToSolutionScoringPhase() {
        ctrl.prepareSolutionScoringPhase(mainWindow, evaluator.getSSService(), evaluator.getSSConfig());
        ctrl.setNextPhase(() -> { goToDuplicateDetectionPhase(); });
        ctrl.showPreparedPhase();
    }

    private void goToDuplicateDetectionPhase() {
        ctrl.prepareDuplicateDetectionPhase(mainWindow, evaluator.getDDService(), evaluator.getDDConfig());
        ctrl.setNextPhase(() -> { goToEvaluationPhase();});
        ctrl.showPreparedPhase();
    }

    private void goToEvaluationPhase() {
        ctrl.prepareEvaluationPhase();
        ctrl.removeNextPhase();
        ctrl.showPreparedPhase();

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

    private void goToResultsPhase(MainGUIController ctrl) {
        ctrl.prepareResultsPhase(evaluator.getResults(), evaluator.getDupRatings());
        ctrl.removeNextPhase();
        ctrl.showPreparedPhase();
    }

}
