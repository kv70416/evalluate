package mainapp.results;

import java.util.List;

public class Aggregation {
    public static double aggregate(List<Double> scores, AggregationType type) {
        int len = scores.size();
        if (len == 0) {
            return Double.NaN;
        }
        if (type == null) {
            if (len == 1) {
                return scores.get(0);
            }
            else {
                return Double.NaN;
            }
        }
        switch(type) {
            case SUM:
                return scores.stream().reduce(0., Double::sum);
            case MIN:
                return scores.stream().reduce(Double.POSITIVE_INFINITY, Double::min );
            case MAX:
                return scores.stream().reduce(Double.NEGATIVE_INFINITY, Double::max );
            case AVG:
                return scores.stream().reduce(0., Double::sum) / len;
            default:
                return Double.NaN;
        }
    }
}
