package mainapp.configurations;

import java.util.ArrayList;
import java.util.List;
import mainapp.services.DuplicateDetectionService;

public class DuplicateDetectionConfiguration extends ModuleConfiguration {
    
    private List<Integer> selectedModuleIndices = new ArrayList<>();
    
    public int addModule(DuplicateDetectionService service, String moduleID) {
        int index = service.createModuleInstance(moduleID);
        selectedModuleIndices.add(index);
        
        // TODO remove
        System.out.println("Added mod index: " + index);
        
        return index;
    }
    
    public void removeModule(DuplicateDetectionService service, int index) {
        selectedModuleIndices.removeIf(i -> i.equals(index));
        service.removeModuleInstance(index);
        
        // TODO remove
        System.out.println("Removed mod index: " + index);
    }
    
    public List<Integer> getSelectedModules() {
        return selectedModuleIndices;
    }
}
