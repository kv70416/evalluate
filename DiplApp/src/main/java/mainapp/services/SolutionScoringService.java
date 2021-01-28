package mainapp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import mainapp.configurations.ModuleConfiguration;
import mainapp.configurations.SolutionScoringConfiguration;
import mainapp.modules.factoryinterfaces.ISolutionScoringModuleFactory;
import mainapp.modules.interfaces.ISolutionScoringModule;

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

    public List<String> getInputs(int index) {
        if (badIndex(index)) return null;
        return instances.get(index).inputSegments();
    }
    
    public boolean fetchInput(int index, String inputName, String targetInputPath) {
        if (badIndex(index)) return false;
        return instances.get(index).fetchInputFile(inputName, targetInputPath);
    }
    
    public void addScoreForSource(int index, String student, String sourceFilePath) {
        if (badIndex(index)) return;
        instances.get(index).addSourceScore(student, sourceFilePath);
    }
    
    public void addScoreForOutput(int index, String student, String inputName, String actualOutputPath) {
        if (badIndex(index)) return;
        instances.get(index).addOutputScore(student, inputName, actualOutputPath);
    }

    public void addScoreForFail(int index, String student, String inputName) {
        if (badIndex(index)) return;
        instances.get(index).addFailScore(student, inputName);
	}

    
    public double getTotalModuleScore(int index, String student) {
        if (badIndex(index)) return Double.NaN;
        return instances.get(index).solutionScore(student);
    }
    
    public Map<String, Double> getModuleScoresPerSegment(int index, String student) {
        if (badIndex(index)) return null;
        return instances.get(index).segmentScores(student);
    }

}
