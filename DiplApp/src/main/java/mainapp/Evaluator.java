package mainapp;

import java.util.ArrayList;
import java.util.HashMap;
import mainapp.configurations.SolutionScoringConfiguration;
import mainapp.configurations.FileFetchingConfiguration;
import mainapp.configurations.CodeCompilationConfiguration;
import java.util.List;
import java.util.Map;
import javafx.concurrent.Task;
import javafx.stage.Stage;
import mainapp.configurations.DuplicateDetectionConfiguration;
import mainapp.controllers.MainGUIController;
import mainapp.services.CodeCompilationService;
import mainapp.services.DuplicateDetectionService;
import mainapp.services.FileFetchingService;
import mainapp.services.SolutionScoringService;

public class Evaluator {
    
    private FileFetchingService ffService = null;
    private CodeCompilationService ccService = null;
    private SolutionScoringService ssService = null;
    private DuplicateDetectionService ddService = null;
    
    private FileFetchingConfiguration ffConfig = null;
    private CodeCompilationConfiguration ccConfig = null;
    private SolutionScoringConfiguration ssConfig = null;
    private DuplicateDetectionConfiguration ddConfig = null;
    
    private Results results = null;
    private DuplicateRatings dupRatings = null;
    
    
    public Evaluator() {
        ffService = new FileFetchingService();
        ccService = new CodeCompilationService();
        ssService = new SolutionScoringService();
        ddService = new DuplicateDetectionService();
        
        ffConfig = new FileFetchingConfiguration();
        ccConfig = new CodeCompilationConfiguration();
        ssConfig = new SolutionScoringConfiguration();
        ddConfig = new DuplicateDetectionConfiguration();
    }
    
    public void runEvaluationCycle(MainGUIController mainController, Stage mainWindow) {
        mainController.showFileFecthingPhase(mainWindow, ffService, ffConfig, () -> {
            mainController.showCodeCompilationPhase(mainWindow, ccService, ccConfig, () -> {
                mainController.showSolutionScoringPhase(mainWindow, ssService, ssConfig, () -> {
                    mainController.showDuplicateDetectionPhase(mainWindow, ddService, ddConfig, () -> {
                        mainController.showEvaluationPhase();

                        Task<Void> evalTask = new Task<>() {
                            @Override
                            protected Void call() throws Exception {

                                try {

                                evaluate();

                                }
                                catch (Exception e) {
                                    System.out.println(e.getClass());
                                    System.out.println(e.getMessage());
                                    e.printStackTrace();
                                }

                                return null;
                            }
                        };

                        evalTask.setOnSucceeded(e -> {
                            mainController.showResultsPhase(results, dupRatings);
                        });

                        Thread evalThread = new Thread(evalTask);
                        evalThread.start();
                    });
                });
            });
        });
    }
    
    
    private void evaluate() {
        System.out.println("EVALUATING.......");
        
        results = new Results();
        dupRatings = new DuplicateRatings();
        
        // get students from FFM
        List<String> students = ffService.getStudentsFromModule(ffConfig.getSelectedModuleIndex());
        
        // CCM initialization
        ccService.runInitialization(ccConfig.getSelectedModuleIndex(), students);
        
        // TODO successful init check
        
        // list for gathering source paths for DDM
        List<String> allStudentSourcePaths = new ArrayList<>();
        
        for (String student : students) {

            // get target source location from CCM
            String targetSourcePath = ccService.getSourceFileDirPath(ccConfig.getSelectedModuleIndex(), student);

            // fetch source via FFM to target source location
            ffService.runFetchForModule(ffConfig.getSelectedModuleIndex(), student, targetSourcePath);
            
            // save the source path for DDM
            allStudentSourcePaths.add(targetSourcePath);
            
        }
        
        // get all duplicate detection modules
        List<Integer> ddModuleIndices = ddConfig.getSelectedModules();
        
        for (int ddIndex : ddModuleIndices) {
            
            // perform duplicate detection
            ddService.runDuplicateDetection(ddIndex, students, allStudentSourcePaths);

            for (String student : students) {
                
                String moduleNo = Integer.toString(ddIndex); // TODO
                
                double dupRating = ddService.getStudentDuplicateRating(ddIndex, student);
                Map<String, Double> pairwiseDupRatings = ddService.getStudentComparisonRatings(ddIndex, student);
                
                dupRatings.setStudentDuplicateRating(student, moduleNo, dupRating);
                dupRatings.setStudentPairwiseDuplicateRatings(student, moduleNo, pairwiseDupRatings);

            }
        }
        
        // get all scoring modules
        List<Integer> ssModuleIndices = ssConfig.getSelectedModules();
                        
        for (String student : students) {
            
            // get target source location from CCM
            String targetSourcePath = ccService.getSourceFileDirPath(ccConfig.getSelectedModuleIndex(), student);
            
            for (int ssIndex : ssModuleIndices) {
                
                // score the solution source files
                ssService.addScoreForSource(ssIndex, student, targetSourcePath);
                
            }
            
            // compile source via CCM
            boolean compilationSuccess = ccService.runCompilation(ccConfig.getSelectedModuleIndex(), student);
            
            if (!compilationSuccess) {
                System.out.println("Compilation for " + student + " failed.");
                continue;
            }
            
            for (int ssIndex : ssModuleIndices) {
                
                // get all tests
                int numberOfInputs = ssService.numberOfInputs(ssIndex);

                for (int inputNo = 1; inputNo <= numberOfInputs; inputNo++) {
                
                    System.out.println("Running case " + Integer.toString(inputNo));
                    
                    // get location to place input data in from CCM
                    String targetInputPath = ccService.getInputDataDirPath(ccConfig.getSelectedModuleIndex(), student);

                    // fetch input data via SSM
                    ssService.fetchInput(ssIndex, inputNo, targetInputPath);

                    // execute program via CCM
                    ccService.runExecution(ccConfig.getSelectedModuleIndex(), student);

                    // get location of execution output data from CCM
                    String actualOutputPath = ccService.getOutputDataPath(ccConfig.getSelectedModuleIndex(), student);

                    // score the solution output via SSM
                    ssService.addScoreForOutput(ssIndex, student, inputNo, actualOutputPath);
                
                }
                
                // record scores
                String moduleNo = Integer.toString(ssIndex); // TODO actual name
                double totalScore = ssService.getTotalModuleScore(ssIndex, student);
                List<String> testNames = ssService.inputNames(ssIndex);
                List<Double> testScores = ssService.getModuleScoresPerSegment(ssIndex, student);
                
                results.setStudentModuleScore(student, moduleNo, totalScore);
                results.setStudentTestScores(student, moduleNo, testNames, testScores);
                
                // TODO remove
                System.out.println("Score for student " + student + " from module " + moduleNo + ": " + Double.toString(totalScore));
            
            }
        
            results.calculateTotalScore(student);
        
        }
        
        ccService.runCleanUp(ccConfig.getSelectedModuleIndex(), students);

    }
    
    public class EvaluationResults {
        public Map<String, Double> totalScorePerModule;
        public Map<String, List<Double>> scorePerModuleTest;
        
        private EvaluationResults() {
            totalScorePerModule = new HashMap<>();
            scorePerModuleTest = new HashMap<>();
        }
    }
}
