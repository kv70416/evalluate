package mainapp.results.ratings;

import java.util.HashMap;
import java.util.Map;

import mainapp.results.AggregationType;

public class DuplicateRatings {
    private Map<String, StudentRating> studentRatings;

    public DuplicateRatings() {
        studentRatings = new HashMap<>();
    }


    public Map<String, StudentRating> getStudentRatings() {
        return studentRatings;
    }


    public void calculateTotalRating(String student, AggregationType type) {
        StudentRating rating = studentRatings.get(student);
        if (rating == null) return;
        rating.calculateTotalRating(type);
    }

    public void setStudentModuleRating(String student, String module, double rating) {
        StudentRating studentRating = studentRatings.get(student);
        if (studentRating == null) {
            studentRating = new StudentRating();
            studentRatings.put(student, studentRating);
        }
        studentRating.setModuleRating(module, rating);
    }

    public void setModuleComparisonRating(String student, String module, Map<String, Double> ratings) {
        StudentRating studentRating = studentRatings.get(student);
        if (studentRating == null) {
            studentRating = new StudentRating();
            studentRatings.put(student, studentRating);
        }
        for (String student2 : ratings.keySet()) {
            double rating = ratings.get(student2);
            studentRating.addPairwiseRating(module, student2, rating);
        }
    }
}
