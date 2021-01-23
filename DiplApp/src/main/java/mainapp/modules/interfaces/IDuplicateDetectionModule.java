package mainapp.modules.interfaces;

import java.util.List;
import java.util.Map;

public interface IDuplicateDetectionModule extends IModule {
    public boolean runDuplicateDetection(List<String> students, List<String> sourceDirPaths);
    public double getStudentTotalDuplicateRating(String student);
    public Map<String, Double> getStudentDuplicateComparisons(String student);
}
