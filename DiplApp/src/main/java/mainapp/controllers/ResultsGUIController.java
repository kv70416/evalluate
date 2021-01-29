package mainapp.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import mainapp.duplicateratings.DuplicateRatings;
import mainapp.results.FailedStudentResult;
import mainapp.results.FailedTestResult;
import mainapp.results.Results;
import mainapp.results.StudentResult;
import mainapp.results.SuccessfulStudentResult;
import mainapp.results.SuccessfulTestResult;
import mainapp.results.TestResult;
import mainapp.duplicateratings.DuplicateRatings.ModuleDupRating;
import mainapp.duplicateratings.DuplicateRatings.PairwiseDupRating;
import mainapp.duplicateratings.DuplicateRatings.StudentDupRating;

public class ResultsGUIController {
    public VBox solutionScoresBox = null;
    public VBox duplicateRatingsBox = null;

    public void fill(Results results, DuplicateRatings ratings) {
        Map<String, StudentResult> studentResults = results.getStudentResults();
        List<String> students = new ArrayList<>(studentResults.keySet());
        students.sort(null);
        for (String student : students) {
            FXMLLoader scoreLoader = new FXMLLoader();
            scoreLoader.setController(new ResultsScoreGUIController());
            scoreLoader.setLocation(getClass().getResource("/guis/ResultsSolutionScoreGUI.fxml"));

            try {
                VBox scoreBox = scoreLoader.<VBox>load();

                ResultsScoreGUIController ctrl = scoreLoader.<ResultsScoreGUIController>getController();
                ctrl.fill(student, studentResults.get(student));

                solutionScoresBox.getChildren().add(scoreBox);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }

        }

        List<StudentDupRating> studentRatings = ratings.getStudentRatings();
        studentRatings.sort(Comparator.comparing(sr -> ((StudentDupRating) sr).getStudent()));
        for (StudentDupRating studentRating : studentRatings) {
            FXMLLoader ratingLoader = new FXMLLoader();
            ratingLoader.setController(new ResultsRatingGUIController());
            ratingLoader.setLocation(getClass().getResource("/guis/ResultsDuplicateRatingGUI.fxml"));

            try {
                VBox ratingBox = ratingLoader.<VBox>load();

                ResultsRatingGUIController ctrl = ratingLoader.<ResultsRatingGUIController>getController();
                ctrl.fill(studentRating);

                duplicateRatingsBox.getChildren().add(ratingBox);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }


    // result scores //

    public class ResultsScoreGUIController {
        public Label studentLabel = null;
        public VBox moduleScoresBox = null;

        public void fill(String student, StudentResult result) {
            if (result instanceof SuccessfulStudentResult) {
                studentLabel.setText("Student " + student + ": " + ((SuccessfulStudentResult) result).getTotalScore());

                Map<String, Double> moduleScores = ((SuccessfulStudentResult) result).getModuleScores();
                List<String> modules = new ArrayList<>(moduleScores.keySet());
                modules.sort(null);        
                for (String module : modules) {
                    FXMLLoader modScoreLoader = new FXMLLoader();
                    modScoreLoader.setController(new ResultsModScoreGUIController());
                    modScoreLoader.setLocation(getClass().getResource("/guis/ResultsModuleScoreGUI.fxml"));

                    try {
                        VBox modScoreBox = modScoreLoader.<VBox>load();

                        ResultsModScoreGUIController ctrl = modScoreLoader.<ResultsModScoreGUIController>getController();
                        ctrl.fill((SuccessfulStudentResult) result, module);

                        moduleScoresBox.getChildren().add(modScoreBox);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }

                }
            }

            if (result instanceof FailedStudentResult) {
                studentLabel.setText("Student " + student + ": ");

                Label msgLabel = new Label(((FailedStudentResult) result).getFailMessage());
                moduleScoresBox.getChildren().add(msgLabel);
            }
        }
    }

    public class ResultsModScoreGUIController {
        public Label moduleLabel = null;
        public GridPane segmentPane = null;
        public Button expandBtn = null;

        public void fill(SuccessfulStudentResult result, String module) {
            double moduleScore = result.getModuleScores().get(module);
            moduleLabel.setText("Module " + module + ": " + Double.toString(moduleScore));

            addGridRow(segmentPane, "Test", "Score", 0);
            int pos = 1;
            
            Map<String, TestResult> testResults = result.getTestResults(module);
            List<String> tests = new ArrayList<>(testResults.keySet());
            tests.sort(null);        
            for (String test : tests) {
                TestResult testResult = testResults.get(test);

                if (testResult instanceof SuccessfulTestResult) {
                    double score = ((SuccessfulTestResult) testResult).getScore();
                    addGridRow(segmentPane, test, Double.toString(score), pos);
                }

                if (testResult instanceof FailedTestResult) {
                    String msg = ((FailedTestResult) testResult).getFailMessage();
                    addGridRow(segmentPane, test, msg, pos);
                }

                pos++;
            }
        }

        private void addGridRow(GridPane pane, String leftVal, String rightVal, int pos) {
            VBox leftBox = new VBox(new Text(leftVal));
            VBox rightBox = new VBox(new Text(rightVal));
            if (pos == 0) {
                leftBox.setStyle("-fx-border-style: solid; -fx-border-color: #105B3D; -fx-border-width: 0 1 2 0");
                rightBox.setStyle("-fx-border-style: solid; -fx-border-color: #105B3D; -fx-border-width: 0 0 2 1");
            }
            else {
                leftBox.setStyle("-fx-border-style: solid; -fx-border-color: #105B3D; -fx-border-width: 0 1 0 0");
                rightBox.setStyle("-fx-border-style: solid; -fx-border-color: #105B3D; -fx-border-width: 0 0 0 1");
            }

            leftBox.setPadding(new Insets(2., 2., 2., 2.));
            rightBox.setPadding(new Insets(2., 2., 2., 2.));

            segmentPane.add(leftBox, 0, pos, 1, 1);
            segmentPane.add(rightBox, 1, pos, 1, 1);
        }

        @FXML
        public void expandSegmentPane() {
            segmentPane.setVisible(!segmentPane.isVisible());
            segmentPane.setManaged(segmentPane.isVisible());
            expandBtn.setText(segmentPane.isVisible() ? "Collapse" : "Expand");
        }
    
    }


    // duplicate ratings //

    public class ResultsRatingGUIController {
        public Label studentLabel = null;
        public VBox moduleRatingsBox = null;

        public void fill(StudentDupRating studentRating) {
            studentLabel.setText("Student " + studentRating.getStudent() + ":");

            List<ModuleDupRating> moduleRatings = studentRating.getModuleRatings();
            moduleRatings.sort(Comparator.comparing(r -> ((ModuleDupRating) r).getModuleName()));
            for (ModuleDupRating moduleRating : moduleRatings) {
                FXMLLoader modRatingLoader = new FXMLLoader();
                modRatingLoader.setController(new ResultsModRatingGUIController());
                modRatingLoader.setLocation(getClass().getResource("/guis/ResultsModuleRatingGUI.fxml"));

                try {
                    VBox modRatingBox = modRatingLoader.<VBox>load();

                    ResultsModRatingGUIController ctrl = modRatingLoader.<ResultsModRatingGUIController>getController();
                    ctrl.fill(moduleRating);

                    moduleRatingsBox.getChildren().add(modRatingBox);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        }

    }

    public class ResultsModRatingGUIController {
        public Label moduleLabel = null;
        public GridPane comparisonList = null;
        public Button expandBtn = null;

        public void fill(ModuleDupRating moduleRating) {
            moduleLabel.setText("Module " + moduleRating.getModuleName() + ": "
                    + String.format("%.2f", moduleRating.getTotalRating()));

            addGridRow(comparisonList, "Student", "Rating", 0);

            int pos = 1;
            List<PairwiseDupRating> pairwiseDupRatings = moduleRating.getPairwiseRatings();
            pairwiseDupRatings.sort(Comparator.comparingDouble(pr -> ((PairwiseDupRating) pr).getComparisonRating()).reversed());
            for (PairwiseDupRating pairwiseRating : pairwiseDupRatings) {
                String compStudent = pairwiseRating.getComparisonStudent();
                double compRating = pairwiseRating.getComparisonRating();

                addGridRow(comparisonList, compStudent, String.format("%.2f", compRating), pos);

                pos++;
            }
        }

        private void addGridRow(GridPane pane, String leftVal, String rightVal, int pos) {
            VBox leftBox = new VBox(new Text(leftVal));
            VBox rightBox = new VBox(new Text(rightVal));
            if (pos == 0) {
                leftBox.setStyle("-fx-border-style: solid; -fx-border-color: #105B3D; -fx-border-width: 0 1 2 0");
                rightBox.setStyle("-fx-border-style: solid; -fx-border-color: #105B3D; -fx-border-width: 0 0 2 1");
            }
            else {
                leftBox.setStyle("-fx-border-style: solid; -fx-border-color: #105B3D; -fx-border-width: 0 1 0 0");
                rightBox.setStyle("-fx-border-style: solid; -fx-border-color: #105B3D; -fx-border-width: 0 0 0 1");
            }

            leftBox.setPadding(new Insets(2., 2., 2., 2.));
            rightBox.setPadding(new Insets(2., 2., 2., 2.));

            pane.add(leftBox, 0, pos, 1, 1);
            pane.add(rightBox, 1, pos, 1, 1);
        }

        @FXML
        public void expandComparisonList() {
            comparisonList.setVisible(!comparisonList.isVisible());
            comparisonList.setManaged(comparisonList.isVisible());
            expandBtn.setText(comparisonList.isVisible() ? "Collapse" : "Expand");
        }

    }

}
