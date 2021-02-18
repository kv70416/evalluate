package mainapp.results.ratings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mainapp.results.Aggregation;
import mainapp.results.AggregationType;

public class StudentRating {
    private double totalRating = Double.NaN;
    private Map<String, Double> moduleRatings = null;
    private Map<String, List<PairwiseRating> > pairwiseRatings = null;

    public StudentRating() {
        moduleRatings = new HashMap<>();
        pairwiseRatings = new HashMap<>();
    }

    public double getTotalRating() {
        return totalRating;
    }

    public Map<String, Double> getModuleRatings() {
        return moduleRatings;
    }

    public Map<String, List<PairwiseRating>> getPairwiseRatings() {
        return pairwiseRatings;
    }


    public void setModuleRating(String module, double rating) {
        moduleRatings.put(module, rating);
    }

    public void addPairwiseRating(String module, String compStudent, double rating) {
        List<PairwiseRating> pairwiseList = pairwiseRatings.get(module);
        if (pairwiseList == null) {
            pairwiseRatings.put(module, new ArrayList<>());
            pairwiseList = pairwiseRatings.get(module);
        }
        pairwiseList.removeIf(pr -> pr.getComparisonStudent() == compStudent);
        pairwiseList.add(new PairwiseRating(compStudent, rating));
    }

    public void calculateTotalRating(AggregationType type) {
        totalRating = Aggregation.aggregate(new ArrayList<>(moduleRatings.values()), type);
    }

}
