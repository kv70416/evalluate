package mainapp.controllers;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import mainapp.DuplicateRatings;
import mainapp.Results;
import mainapp.DuplicateRatings.ModuleDupRating;
import mainapp.DuplicateRatings.PairwiseDupRating;
import mainapp.DuplicateRatings.StudentDupRating;
import mainapp.Results.ModuleScore;
import mainapp.Results.StudentScore;

public class ResultsGUIController {
    public VBox solutionScoresBox = null;
    public VBox duplicateRatingsBox = null;

    public void fill(Results results, DuplicateRatings ratings) {
        for (StudentScore studentScore : results.getStudentScores()) {
            FXMLLoader scoreLoader = new FXMLLoader();
            scoreLoader.setController(new ResultsScoreGUIController());
            scoreLoader.setLocation(getClass().getResource("/guis/ResultsSolutionScoreGUI.fxml"));

            try {
                VBox scoreBox = scoreLoader.<VBox>load();

                ResultsScoreGUIController ctrl = scoreLoader.<ResultsScoreGUIController>getController();
                ctrl.fill(studentScore);

                solutionScoresBox.getChildren().add(scoreBox);
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }

        }

        for (StudentDupRating studentRating : ratings.getStudentRatings()) {
            FXMLLoader ratingLoader = new FXMLLoader();
            ratingLoader.setController(new ResultsRatingGUIController());
            ratingLoader.setLocation(getClass().getResource("/guis/ResultsDuplicateRatingGUI.fxml"));

            try {
                VBox ratingBox = ratingLoader.<VBox>load();

                ResultsRatingGUIController ctrl = ratingLoader.<ResultsRatingGUIController>getController();
                ctrl.fill(studentRating);

                duplicateRatingsBox.getChildren().add(ratingBox);
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }


    public class ResultsRatingGUIController {
        public Label studentLabel = null;
        public VBox moduleRatingsBox = null;

        public void fill(StudentDupRating studentRating) {
            studentLabel.setText("Student " + studentRating.getStudent() + ":");

            for (ModuleDupRating moduleRating : studentRating.getModuleRatings()) {
                FXMLLoader modRatingLoader = new FXMLLoader();
                modRatingLoader.setController(new ResultsModRatingGUIController());
                modRatingLoader.setLocation(getClass().getResource("/guis/ResultsModuleRatingGUI.fxml"));

                try {
                    VBox modRatingBox = modRatingLoader.<VBox>load();

                    ResultsModRatingGUIController ctrl = modRatingLoader.<ResultsModRatingGUIController>getController();
                    ctrl.fill(moduleRating);

                    moduleRatingsBox.getChildren().add(modRatingBox);
                }
                catch (IOException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        }

    }

    public class ResultsModRatingGUIController {
        public Label moduleLabel = null;
        public GridPane comparisonList = null;

        public void fill(ModuleDupRating moduleRating) {
            moduleLabel.setText("Module " + moduleRating.getModuleName() + ": " + String.format("%.2f", moduleRating.getTotalRating()));

            int pos = 0;
            for (PairwiseDupRating pairwiseRating : moduleRating.getPairwiseRatings()) {
                String compStudent = pairwiseRating.getComparisonStudent();
                double compRating = pairwiseRating.getComparisonRating();

                VBox ratingPane = new VBox(new Text(String.format("%.2f", compRating)));
                VBox studentPane = new VBox(new Text(compStudent));

                ratingPane.setStyle("-fx-border-style: solid; -fx-border-color: #105B3D; -fx-border-width: 1");
                studentPane.setStyle("-fx-border-style: solid; -fx-border-color: #105B3D; -fx-border-width: 1");
                
                ratingPane.setPadding(new Insets(2., 2., 2., 2.));
                studentPane.setPadding(new Insets(2., 2., 2., 2.));

                comparisonList.add(ratingPane, 0, pos, 1, 1);
                comparisonList.add(studentPane, 1, pos, 1, 1);

                pos++;
            }
        }
    }


    public class ResultsScoreGUIController {
        public Label studentLabel = null;
        public VBox moduleScoresBox = null;

        public void fill(StudentScore studentScore) {
            studentLabel.setText("Student " + studentScore.getStudent() + ":");

            for (ModuleScore moduleScore : studentScore.getModuleScores()) {
                FXMLLoader modScoreLoader = new FXMLLoader();
                modScoreLoader.setController(new ResultsModScoreGUIController());
                modScoreLoader.setLocation(getClass().getResource("/guis/ResultsModuleScoreGUI.fxml"));
    
                try {
                    VBox modScoreBox = modScoreLoader.<VBox>load();
    
                    ResultsModScoreGUIController ctrl = modScoreLoader.<ResultsModScoreGUIController>getController();
                    ctrl.fill(moduleScore);
    
                    moduleScoresBox.getChildren().add(modScoreBox);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
    
            }
        }
    }

    public class ResultsModScoreGUIController {
        public Label moduleLabel = null;
        public GridPane segmentPane = null;

        public void fill(ModuleScore moduleScore) {
            moduleLabel.setText("Module " + moduleScore.getModuleName() + ": " + Double.toString(moduleScore.getModuleScore()));

            int pos = 0;
            for (Results.TestScore testScore : moduleScore.getTestScores()) {
                String test = testScore.getTestName();
                double score = testScore.getTestScore();

                VBox testPane = new VBox(new Text(test));
                VBox scorePane = new VBox(new Text(Double.toString(score)));

                testPane.setStyle("-fx-border-style: solid; -fx-border-color: #105B3D; -fx-border-width: 1");
                scorePane.setStyle("-fx-border-style: solid; -fx-border-color: #105B3D; -fx-border-width: 1");
                
                testPane.setPadding(new Insets(2., 2., 2., 2.));
                scorePane.setPadding(new Insets(2., 2., 2., 2.));

                segmentPane.add(testPane, pos, 0, 1, 1);
                segmentPane.add(scorePane, pos, 1, 1, 1);

                pos++;
            }
        }
    }



}
