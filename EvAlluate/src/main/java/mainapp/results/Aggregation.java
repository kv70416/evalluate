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

    public static String typeToString(AggregationType type) {
        if (type == null) return null;
        switch(type) {
            case SUM:
                return "sum";
            case MIN:
                return "min";
            case MAX:
                return "max";
            case AVG:
                return "avg";
            default:
                return null;
        }
    }

    public static AggregationType stringToType(String typeStr) {
        if (typeStr == null) return null;
        switch(typeStr) {
            case "sum":
                return AggregationType.SUM;
            case "min":
                return AggregationType.MIN;
            case "max":
                return AggregationType.MAX;
            case "avg":
                return AggregationType.AVG;
            default:
                return null;
        }
    }
}
