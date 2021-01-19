package mainapp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import mainapp.configurations.DuplicateDetectionConfiguration;
import mainapp.configurations.ModuleConfiguration;
import mainapp.moduleFactoryInterfaces.IDuplicateDetectionModuleFactory;
import mainapp.moduleInterfaces.IDuplicateDetectionModule;

public class DuplicateDetectionService extends ModuleService<IDuplicateDetectionModule> {
    
    public DuplicateDetectionService() {
        factories = new ArrayList<>();
        ServiceLoader<IDuplicateDetectionModuleFactory> ddLoader = ServiceLoader.load(IDuplicateDetectionModuleFactory.class);
        for (IDuplicateDetectionModuleFactory factory : ddLoader) {
            factories.add(factory);
        }
        
        instances = new ArrayList<>();
    }

    @Override
    public boolean isConfigurationValid(ModuleConfiguration config) {
        if (!(config instanceof DuplicateDetectionConfiguration)) {
            return false;
        }
        
        DuplicateDetectionConfiguration ddConfig = (DuplicateDetectionConfiguration)config;
        List<Integer> selectedModules = ddConfig.getSelectedModules();
        for (int index : selectedModules) {
            if (!isModuleConfigured(index)) {
                return false;
            }
        }
        return !selectedModules.isEmpty();
    }
    
    
    public boolean runDuplicateDetection(int index, List<String> students, List<String> srcPaths) {
        if (badIndex(index)) return false;
        return instances.get(index).runDuplicateDetection(students, srcPaths);
    }

    public double getStudentDuplicateRating(int index, String student) {
        if (badIndex(index)) return Double.NaN;
        return instances.get(index).getStudentTotalDuplicateRating(student);
    }
    
    public Map<String, Double> getStudentComparisonRatings(int index, String student) {
        if (badIndex(index)) return null;
        return instances.get(index).getStudentDuplicateComparisons(student);
    }
    
}
