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
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import mainapp.results.ratings.DuplicateRatings;
import mainapp.results.ratings.PairwiseRating;
import mainapp.results.ratings.StudentRating;
import mainapp.results.scores.FailedStudentResult;
import mainapp.results.scores.FailedTestResult;
import mainapp.results.scores.Results;
import mainapp.results.scores.StudentResult;
import mainapp.results.scores.SuccessfulStudentResult;
import mainapp.results.scores.SuccessfulTestResult;
import mainapp.results.scores.TestResult;

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

        Map<String, StudentRating> studentRatings = ratings.getStudentRatings();
        students = new ArrayList<>(studentRatings.keySet());
        students.sort(null);
        for (String student : students) {
            FXMLLoader ratingLoader = new FXMLLoader();
            ratingLoader.setController(new ResultsRatingGUIController());
            ratingLoader.setLocation(getClass().getResource("/guis/ResultsDuplicateRatingGUI.fxml"));

            try {
                VBox ratingBox = ratingLoader.<VBox>load();

                ResultsRatingGUIController ctrl = ratingLoader.<ResultsRatingGUIController>getController();
                ctrl.fill(student, studentRatings.get(student));

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
                msgLabel.setStyle("-fx-font-weight: bolder;");
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
            moduleLabel.setText("Module #" + (Integer.parseInt(module) + 1) + ": " + Double.toString(moduleScore));

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

            pane.add(leftBox, 0, pos, 1, 1);
            pane.add(rightBox, 1, pos, 1, 1);

            // TODO - remember to not put it next to failed tests!
                if (pos == 0) return;
                Label info = new Label("INFO");
                info.setStyle("-fx-underline: true; -fx-text-weight: bold;");
                Tooltip tip = new Tooltip("Success!");
                tip.setStyle("-fx-font-size: 12.0;");
                info.setTooltip(tip);
                pane.add(info, 2, pos, 1, 1);
            //            
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

        public void fill(String student, StudentRating studentRating) {
            studentLabel.setText("Student " + student + ": " + String.format("%.2f", studentRating.getTotalRating()));


            List<String> modules = new ArrayList<>(studentRating.getModuleRatings().keySet());
            modules.sort(null);
            for (String module : modules) {
                FXMLLoader modRatingLoader = new FXMLLoader();
                modRatingLoader.setController(new ResultsModRatingGUIController());
                modRatingLoader.setLocation(getClass().getResource("/guis/ResultsModuleRatingGUI.fxml"));

                try {
                    VBox modRatingBox = modRatingLoader.<VBox>load();

                    ResultsModRatingGUIController ctrl = modRatingLoader.<ResultsModRatingGUIController>getController();
                    ctrl.fill(module, studentRating);

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

        public void fill(String module, StudentRating studentRating) {
            moduleLabel.setText("Module #" + (Integer.parseInt(module) + 1) + ": "
                    + String.format("%.2f", studentRating.getModuleRatings().get(module)));

            addGridRow(comparisonList, "Student", "Rating", 0);

            int pos = 1;
            List<PairwiseRating> pairwiseDupRatings = studentRating.getPairwiseRatings().get(module);
            pairwiseDupRatings.sort(Comparator.comparingDouble(pr -> ((PairwiseRating) pr).getComparisonRating()).reversed());
            for (PairwiseRating pairwiseRating : pairwiseDupRatings) {
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
