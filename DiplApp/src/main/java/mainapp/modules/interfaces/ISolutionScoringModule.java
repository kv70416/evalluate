package mainapp.modules.interfaces;

import java.util.List;
import java.util.Map;

public interface ISolutionScoringModule extends IModule {
    public List<String> inputSegments();
    
    public boolean fetchInputFile(String segmentName, String inputFilePath);

    public void addSourceScore(String student, String sourceFilePath);
    public void addOutputScore(String student, String segmentName, String actualOutputPath);
    public void addFailScore(String student, String segmentName);
    
    public double solutionScore(String student);
    public Map<String, Double> segmentScores(String student);
}
