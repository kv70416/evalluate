package mainapp.results.ratings;

public class PairwiseRating {
    private String comparisonStudentName;
    private double comparisonRating;
    
    public PairwiseRating(String student, double rating) {
        comparisonStudentName = student;
        comparisonRating = rating;
    }

    public String getComparisonStudent() {
        return comparisonStudentName;
    }

    public double getComparisonRating() {
        return comparisonRating;
    }

}
