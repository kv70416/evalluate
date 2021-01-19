package mainapp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import mainapp.configurations.ModuleConfiguration;
import mainapp.configurations.SolutionScoringConfiguration;
import mainapp.moduleFactoryInterfaces.ISolutionScoringModuleFactory;
import mainapp.moduleInterfaces.ISolutionScoringModule;

public class SolutionScoringService extends ModuleService<ISolutionScoringModule> {
    
    public SolutionScoringService() {
        factories = new ArrayList<>();
        ServiceLoader<ISolutionScoringModuleFactory> ssLoader = ServiceLoader.load(ISolutionScoringModuleFactory.class);
        for (ISolutionScoringModuleFactory factory : ssLoader) {
            factories.add(factory);
        }
        
        instances = new ArrayList<>();
    }

    @Override
    public boolean isConfigurationValid(ModuleConfiguration config) {
        if (!(config instanceof SolutionScoringConfiguration)) {
            return false;
        }
        
        SolutionScoringConfiguration ssConfig = (SolutionScoringConfiguration)config;
        List<Integer> selectedModules = ssConfig.getSelectedModules();
        for (int index : selectedModules) {
            if (!isModuleConfigured(index)) {
                return false;
            }
        }
        return !selectedModules.isEmpty();
    }

    public int numberOfInputs(int index) {
        if (badIndex(index)) return -1;
        return instances.get(index).numberOfSegments();
    }
    
    public List<String> inputNames(int index) {
        if (badIndex(index)) return null;
        return instances.get(index).segmentNames();
    }
    
    public boolean fetchInput(int index, int inputNumber, String targetInputPath) {
        if (badIndex(index)) return false;
        return instances.get(index).fetchInputFile(inputNumber, targetInputPath);
    }
    
    public void addScoreForSource(int index, String student, String sourceFilePath) {
        if (badIndex(index)) return;
        instances.get(index).addSourceScore(student, sourceFilePath);
    }
    
    public void addScoreForOutput(int index, String student, int inputNumber, String actualOutputPath) {
        if (badIndex(index)) return;
        instances.get(index).addOutputScore(student, inputNumber, actualOutputPath);
    }
    
    public double getTotalModuleScore(int index, String student) {
        if (badIndex(index)) return Double.NaN;
        return instances.get(index).scoreSolution(student);
    }
    
    public List<Double> getModuleScoresPerSegment(int index, String student) {
        if (badIndex(index)) return null;
        return instances.get(index).scorePerSegment(student);
    }
}
