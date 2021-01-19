package mainapp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import mainapp.configurations.FileFetchingConfiguration;
import mainapp.configurations.ModuleConfiguration;
import mainapp.moduleFactoryInterfaces.IFileFetchingModuleFactory;
import mainapp.moduleInterfaces.IFileFetchingModule;

public class FileFetchingService extends ModuleService<IFileFetchingModule> {
        
    public FileFetchingService() {
        factories = new ArrayList<>();
        ServiceLoader<IFileFetchingModuleFactory> ffLoader = ServiceLoader.load(IFileFetchingModuleFactory.class);
        for (IFileFetchingModuleFactory factory : ffLoader) {
            factories.add(factory);
            
            // TODO remove
            System.out.println(factory.moduleName());
        }
        
        instances = new ArrayList<>();
    }

    @Override
    public boolean isConfigurationValid(ModuleConfiguration config) {
        if (!(config instanceof FileFetchingConfiguration)) {
            return false;
        }
        
        FileFetchingConfiguration ffConfig = (FileFetchingConfiguration)config;
        return isModuleConfigured(ffConfig.getSelectedModuleIndex());
    }
    
    public boolean runFetchForModule(int index, String student, String targetSourcePath) {
        if (badIndex(index)) return false;
        return instances.get(index).fetchSourceFiles(student, targetSourcePath);
    }
    
    public List<String> getStudentsFromModule(int index) {
        if (badIndex(index)) return null;
        return instances.get(index).getAllStudents();
    }
    
}
