package mainapp.configurations;

import mainapp.services.FileFetchingService;

public class FileFetchingConfiguration extends ModuleConfiguration {
    
    private int selectedModuleIndex = -1;
    
    private String selectedModuleID = null;
    
    public void selectModule(FileFetchingService service, String moduleID) {
        if (selectedModuleIndex != -1) {
            service.removeModuleInstance(selectedModuleIndex);
        }
        
        selectedModuleIndex = service.createModuleInstance(moduleID);
        
        selectedModuleID = moduleID;
        
        // TODO remove
        System.out.println("Mod index: " + selectedModuleIndex);
    }
    
    public int getSelectedModuleIndex() {
        return selectedModuleIndex;
    }
    
    public String getSelectedModuleID() {
        return selectedModuleID;
    }
    
}
