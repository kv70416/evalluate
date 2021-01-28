package mainapp.modules.interfaces;

import java.util.Map;

public interface IDuplicateDetectionModule extends IModule {
    public boolean runDuplicateDetection(Map<String, String> studentSourceDirPaths);
    public double getStudentTotalDuplicateRating(String student);
    public Map<String, Double> getStudentDuplicateComparisons(String student);
}
