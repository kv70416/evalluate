package mainapp.services;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import javafx.stage.Stage;
import mainapp.configurations.ModuleConfiguration;
import mainapp.moduleInterfaces.IModule;
import mainapp.moduleFactoryInterfaces.IModuleFactory;

public abstract class ModuleService<IModuleType extends IModule> {
    
    protected List<IModuleFactory<IModuleType>> factories = null;

    protected List<IModuleType> instances = null;
    
    public class ModuleInformation {
        private final String moduleID;
        private final String moduleName;
        private final String moduleDescription;
        
        public ModuleInformation(String id, String name, String desc) {
            moduleID = id;
            moduleName = name;
            moduleDescription = desc;
        }
        
        public String getID() { return moduleID; }
        public String getName() { return moduleName; }
        public String getDescription() { return moduleDescription; }
    }
    
    
    public List<ModuleInformation> getAllModuleInfo() {
        List<ModuleInformation> ret = new ArrayList<>();
        for (IModuleFactory<IModuleType> factory : factories) {
            ModuleInformation moduleInfo = new ModuleInformation(
                factory.moduleID(),
                factory.moduleName(),
                factory.moduleDescription()
            );
            ret.add(moduleInfo);
        }
        return ret;
    }
    
    public String getModuleName(String moduleID) {
        for (IModuleFactory<IModuleType> factory : factories) {
            if (factory.moduleID().equals(moduleID)) {
                return factory.moduleName();
            }
        }
        return null;
    }
    
    public int createModuleInstance(String moduleID) {
        for (IModuleFactory<IModuleType> factory : factories) {
            if (factory.moduleID().equals(moduleID)) {
                IModuleType mod = factory.newModuleInstance();
                
                for (int i = 0; i < instances.size(); i++) {
                    if (instances.get(i) == null) {
                        instances.set(i, mod);
                        return i;
                    }
                }
                
                instances.add(mod);
                return instances.size() - 1;
            }
        }
        return -1;
    }
    
    public void removeModuleInstance(int index) {
        instances.set(index, null);
    }
    
    
    
    public Node getModuleGUI(int index, Stage mainWindow, Runnable mainSceneRefresh) {
        if (badIndex(index)) return null;
        return instances.get(index).moduleGUI(mainWindow, mainSceneRefresh);
    }
    
    public boolean isModuleConfigured(int index) {
        if (badIndex(index)) return false;
        return instances.get(index).isConfigured();
    }
    
    public abstract boolean isConfigurationValid(ModuleConfiguration config);
    
    
    protected boolean badIndex(int index) {
        return index < 0 || index >= instances.size();
    }
}
