package mainapp.configurations;

import mainapp.services.CodeCompilationService;

public class CodeCompilationConfiguration extends ModuleConfiguration {
    
    private int selectedModuleIndex = -1;
    
    private String selectedModuleID = null;

    public void selectModule(CodeCompilationService service, String moduleID) {
        if (selectedModuleIndex != -1) {
            service.removeModuleInstance(selectedModuleIndex);
        }
        
        selectedModuleIndex = service.createModuleInstance(moduleID);
        
        selectedModuleID = moduleID;
    }
    
    public int getSelectedModuleIndex() {
        return selectedModuleIndex;
    }
    
    public String getSelectedModuleID() {
        return selectedModuleID;
    }
    
}
