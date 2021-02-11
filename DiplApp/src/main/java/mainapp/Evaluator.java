package mainapp;

import java.util.ArrayList;
import java.util.HashMap;
import mainapp.configurations.SolutionScoringConfiguration;
import mainapp.results.ratings.DuplicateRatings;
import mainapp.results.scores.Results;
import mainapp.configurations.FileFetchingConfiguration;
import mainapp.configurations.CodeCompilationConfiguration;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import mainapp.configurations.DuplicateDetectionConfiguration;
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
    
    private SceneMediator mediator = null;
    
    public Evaluator(SceneMediator mediator) {
        ffService = new FileFetchingService();
        ccService = new CodeCompilationService();
        ssService = new SolutionScoringService();
        ddService = new DuplicateDetectionService();
        
        ffConfig = new FileFetchingConfiguration();
        ccConfig = new CodeCompilationConfiguration();
        ssConfig = new SolutionScoringConfiguration();
        ddConfig = new DuplicateDetectionConfiguration();

        this.mediator = mediator;
    }


    public FileFetchingService getFFService() {
        return ffService;
    }

    public CodeCompilationService getCCService() {
        return ccService;
    }

    public SolutionScoringService getSSService() {
        return ssService;
    }

    public DuplicateDetectionService getDDService() {
        return ddService;
    }


    public FileFetchingConfiguration getFFConfig() {
        return ffConfig;
    }

    public CodeCompilationConfiguration getCCConfig() {
        return ccConfig;
    }

    public SolutionScoringConfiguration getSSConfig() {
        return ssConfig;
    }

    public DuplicateDetectionConfiguration getDDConfig() {
        return ddConfig;
    }


    public Results getResults() {
        return results;
    }

    public DuplicateRatings getDupRatings() {
        return dupRatings;
    }



    public boolean evaluate() {
        System.out.println("EVALUATING.......");
        
        results = new Results();
        dupRatings = new DuplicateRatings();
        
        List<String> allStudents = listStudents();
        if (allStudents == null || allStudents.isEmpty()) {
            listError();
            return false;
        }

        StudentClassification students = new StudentClassification(allStudents);
        
        boolean initSuccess = performInitializations(students);
        if (!initSuccess) {
            initError(students);
            return false;
        }

        Map<String, String> allStudentSourcePaths = fetchSource(students);

        detectDuplicates(students, allStudentSourcePaths); // TODO successful vs unsuccessful modules
        
        performScoring(students, allStudentSourcePaths);

        recordScores(students);


        boolean cleanupSuccess = performCleanups(students);
        if (!cleanupSuccess) {
            cleanupError();
        }

        return true;
    }


    private class StudentClassification {
        private List<String> okStudents = null;
        private List<String> noSourceStudents = null;
        private List<String> compilationFailedStudents = null;

        private StudentClassification(List<String> students) {
            okStudents = new ArrayList<String>(students);
            noSourceStudents = new ArrayList<>();
            compilationFailedStudents = new ArrayList<>();
        }

        public List<String> getCurrentOkStudents() {
            return new ArrayList<>(okStudents);
        }

        public List<String> getCurrentNoSourceStudents() {
            return new ArrayList<>(noSourceStudents);
        }

        public List<String> getCurrentCompilationFailedStudents() {
            return new ArrayList<>(compilationFailedStudents);
        }

        public List<String> getAllStudents() {
            List<String> ret = new ArrayList<>();
            ret.addAll(okStudents);
            ret.addAll(noSourceStudents);
            ret.addAll(compilationFailedStudents);
            return ret;
        }

        public List<String> getAllStudentsWithSource() {
            List<String> ret = new ArrayList<>();
            ret.addAll(okStudents);
            ret.addAll(compilationFailedStudents);
            return ret;
        }

        public void moveToNoSource(String student) {
            if (okStudents.contains(student)) {
                okStudents.remove(student);
                noSourceStudents.add(student);
            }
        }

        public void moveToCompilationFailed(String student) {
            if (okStudents.contains(student)) {
                okStudents.remove(student);
                compilationFailedStudents.add(student);
            }
        }
    }


    private List<String> listStudents() {
        List<String> students = ffService.getStudentsFromModule(ffConfig.getSelectedModuleIndex());
        return students;
    }

    private void listError() {
        Platform.runLater(() -> {
            ErrorWindow ew = new ErrorWindow("No student solutions found.");
            ew.setActionOnClose(() -> { mediator.showTitleScene(); });
            mediator.showErrorWindow(ew);
        });
    }
    

    private boolean performInitializations(StudentClassification students) {
        boolean initSuccess = ccService.runInitialization(ccConfig.getSelectedModuleIndex(), students.getAllStudents());
        return initSuccess;
    }

    private void initError(StudentClassification students) {
        Platform.runLater(() -> {
            ErrorWindow ew = new ErrorWindow("Failed to initialize the compilation module.");
            ew.setActionOnClose(() -> {
                performCleanups(students);
                mediator.showTitleScene();
            });
            mediator.showErrorWindow(ew);
        });
    }


    private Map<String, String> fetchSource(StudentClassification students) {
        List<String> studentList = students.getCurrentOkStudents();
        Map<String, String> allSourcePaths = new HashMap<>();
        for (String student : studentList) {
            String targetSourcePath = ccService.getSourceFileDirPath(ccConfig.getSelectedModuleIndex(), student);
            if (targetSourcePath == null) {
                students.moveToNoSource(student);
                continue;
            }

            boolean fetchSuccess = ffService.runFetchForModule(ffConfig.getSelectedModuleIndex(), student, targetSourcePath);
            if (!fetchSuccess) {
                students.moveToNoSource(student);
                continue;
            }

            allSourcePaths.put(student, targetSourcePath);
        }
        return allSourcePaths;
    }


    private List<Integer> detectDuplicates(StudentClassification students, Map<String, String> allSourcePaths) {
        List<String> studentList = students.getAllStudentsWithSource();

        List<Integer> ddModuleIndices = ddConfig.getSelectedModules();
        List<Integer> successfulModules = new ArrayList<>();
        for (int ddIndex : ddModuleIndices) {

            boolean dupDetectionSuccess = ddService.runDuplicateDetection(ddIndex, allSourcePaths);
            if (!dupDetectionSuccess) {
                continue;
            }

            for (String student : studentList) {
                String moduleNo = Integer.toString(ddIndex); // TODO
                
                double dupRating = ddService.getStudentDuplicateRating(ddIndex, student);
                Map<String, Double> pairwiseDupRatings = ddService.getStudentComparisonRatings(ddIndex, student);
                
                dupRatings.setStudentModuleRating(student, moduleNo, dupRating);
                if (pairwiseDupRatings != null && !pairwiseDupRatings.isEmpty()) {
                    dupRatings.setModuleComparisonRating(student, moduleNo, pairwiseDupRatings);
                }
                dupRatings.calculateTotalRating(student, ddConfig.getAggregationType());
            }

            successfulModules.add(ddIndex);
        }
        return successfulModules;
    }


    private void performScoring(StudentClassification students, Map<String, String> allSourcePaths) {
        List<String> studentList = students.getCurrentOkStudents();
        for (String student : studentList) {
            scoreSource(student, allSourcePaths.get(student));
            
            boolean success = compileSource(student);

            if (success) {
                runAndScoreTests(student);
            }
            else {
                students.moveToCompilationFailed(student);
            }
        }
    }

    private void scoreSource(String student, String targetSourcePath) {
        List<Integer> ssModuleIndices = ssConfig.getSelectedModules();
        for (int ssIndex : ssModuleIndices) {
            ssService.addScoreForSource(ssIndex, student, targetSourcePath);
        }
    }

    private boolean compileSource(String student) {
        boolean compilationSuccess = ccService.runCompilation(ccConfig.getSelectedModuleIndex(), student);
        return compilationSuccess;
    }

    private void runAndScoreTests(String student) {
        List<Integer> ssModuleIndices = ssConfig.getSelectedModules();
        for (int ssIndex : ssModuleIndices) {
            List<String> inputs = ssService.getInputs(ssIndex);
            for (String input : inputs) {
                String targetInputPath = ccService.getInputDataDirPath(ccConfig.getSelectedModuleIndex(), student);
                if (targetInputPath == null) {
                    ssService.addScoreForFail(ssIndex, student, input);
                    results.setStudentModuleTestFail(student, Integer.toString(ssIndex), input, "Input fetch failed.");
                    continue;
                }

                boolean fetchSuccess = ssService.fetchInput(ssIndex, input, targetInputPath);
                if (!fetchSuccess) {
                    ssService.addScoreForFail(ssIndex, student, input);
                    results.setStudentModuleTestFail(student, Integer.toString(ssIndex), input, "Input fetch failed.");
                    continue;
                }

                boolean runSuccess = ccService.runExecution(ccConfig.getSelectedModuleIndex(), student);
                if (!runSuccess) {
                    ssService.addScoreForFail(ssIndex, student, input);
                    results.setStudentModuleTestFail(student, Integer.toString(ssIndex), input, "Execution failed.");
                    continue;
                }

                String actualOutputPath = ccService.getOutputDataPath(ccConfig.getSelectedModuleIndex(), student);
                if (actualOutputPath == null) {
                    ssService.addScoreForFail(ssIndex, student, input);
                    results.setStudentModuleTestFail(student, Integer.toString(ssIndex), input, "Output reading failed.");
                    continue;
                }

                ssService.addScoreForOutput(ssIndex, student, input, actualOutputPath);
            }
        }
    }


    private void recordScores(StudentClassification students) {
        List<String> okStudents = students.getCurrentOkStudents();
        for (String student : okStudents) {
            recordStudentScores(student);
        }

        List<String> noSourceStudents = students.getCurrentNoSourceStudents();
        for (String student : noSourceStudents) {
            results.setStudentFail(student, "Source fetch failed.");
        }
        
        List<String> compilationFailedStudents = students.getCurrentCompilationFailedStudents();
        for (String student : compilationFailedStudents) {
            results.setStudentFail(student, "Compilation failed.");
        }
    }

    private void recordStudentScores(String student) {
        List<Integer> ssModuleIndices = ssConfig.getSelectedModules();
        
        for (int ssIndex : ssModuleIndices) {
            String moduleNo = Integer.toString(ssIndex); // TODO actual name
            double totalScore = ssService.getTotalModuleScore(ssIndex, student);
            Map<String, Double> testScores = ssService.getModuleScoresPerSegment(ssIndex, student);
            
            results.setStudentModuleScore(student, moduleNo, totalScore);
            results.setStudentModuleTestScores(student, moduleNo, testScores);
        }

        results.calculateTotalScore(student, ssConfig.getAggregationType());
    }


    private boolean performCleanups(StudentClassification students) {
        boolean cleanupSuccess = ccService.runCleanUp(ccConfig.getSelectedModuleIndex(), students.getAllStudents());
        return cleanupSuccess;
    }

    private void cleanupError() {
        Platform.runLater(() -> {
            ErrorWindow ew = new ErrorWindow("Module cleanups failed. Further actions may be needed to run evaluation successfully in the future.");
            mediator.showErrorWindow(ew);
        });
    }

}
