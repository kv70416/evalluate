package mainapp.configurations;

import java.util.ArrayList;
import java.util.List;
import mainapp.services.DuplicateDetectionService;

public class DuplicateDetectionConfiguration extends ModuleConfiguration {
    
    private List<Integer> selectedModuleIndices = new ArrayList<>();
    
    public int addModule(DuplicateDetectionService service, String moduleID) {
        int index = service.createModuleInstance(moduleID);
        selectedModuleIndices.add(index);
        
        return index;
    }
    
    public void removeModule(DuplicateDetectionService service, int index) {
        selectedModuleIndices.removeIf(i -> i.equals(index));
        service.removeModuleInstance(index);
    }
    
    public List<Integer> getSelectedModules() {
        return selectedModuleIndices;
    }
}
