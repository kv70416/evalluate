package mainapp.moduleInterfaces;

import java.util.List;

public interface ISolutionScoringModule extends IModule {
    public int numberOfSegments();
    public List<String> segmentNames();
    
    public boolean fetchInputFile(int inputNumber, String inputFilePath);
    public void addSourceScore(String student, String sourceFilePath);
    public void addOutputScore(String student, int inputNumber, String actualOutputPath);
    
    public double scoreSolution(String student);
    public List<Double> scorePerSegment(String student);
}
