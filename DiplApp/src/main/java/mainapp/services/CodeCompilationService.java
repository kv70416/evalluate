package mainapp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import mainapp.configurations.CodeCompilationConfiguration;
import mainapp.configurations.ModuleConfiguration;
import mainapp.moduleFactoryInterfaces.ICodeCompilationModuleFactory;
import mainapp.moduleInterfaces.ICodeCompilationModule;

public class CodeCompilationService extends ModuleService<ICodeCompilationModule> {
        
    public CodeCompilationService() {
        factories = new ArrayList<>();
        ServiceLoader<ICodeCompilationModuleFactory> ccLoader = ServiceLoader.load(ICodeCompilationModuleFactory.class);
        for (ICodeCompilationModuleFactory factory : ccLoader) {
            factories.add(factory);
        }
        
        instances = new ArrayList<>();
    }

    @Override
    public boolean isConfigurationValid(ModuleConfiguration config) {
        if (!(config instanceof CodeCompilationConfiguration)) {
            return false;
        }
        
        CodeCompilationConfiguration ccConfig = (CodeCompilationConfiguration)config;
        return isModuleConfigured(ccConfig.getSelectedModuleIndex());
    }
    
    
    public String getSourceFileDirPath(int index, String student) {
        if (badIndex(index)) return null;
        return instances.get(index).sourceTargetDirectoryPath(student);
    }

    public String getInputDataDirPath(int index, String student) {
        if (badIndex(index)) return null;
        return instances.get(index).inputTargetDirectoryPath(student);
    }

    public String getOutputDataPath(int index, String student) {
        if (badIndex(index)) return null;
        return instances.get(index).programOutputFilePath(student);
    }

    
    public boolean runInitialization(int index, List<String> students) {
        if (badIndex(index)) return false;
        return instances.get(index).initialize(students);
    }
    
    public boolean runCompilation(int index, String student) {
        if (badIndex(index)) return false;
        return instances.get(index).compileSource(student);
    }
    
    public boolean runExecution(int index, String student) {
        if (badIndex(index)) return false;
        return instances.get(index).runProgram(student);
    }
    
    public boolean runCleanUp(int index, List<String> students) {
        if (badIndex(index)) return false;
        return instances.get(index).cleanUp(students);
    }
}
