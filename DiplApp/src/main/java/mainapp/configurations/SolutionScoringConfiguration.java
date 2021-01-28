package mainapp.configurations;

import java.util.ArrayList;
import java.util.List;
import mainapp.services.SolutionScoringService;

public class SolutionScoringConfiguration extends ModuleConfiguration {
    
    private List<Integer> selectedModuleIndices = new ArrayList<>();
    
    public int addModule(SolutionScoringService service, String moduleID) {
        int index = service.createModuleInstance(moduleID);
        selectedModuleIndices.add(index);
        return index;
    }
    
    public void removeModule(SolutionScoringService service, int index) {
        selectedModuleIndices.removeIf(i -> i.equals(index));
        service.removeModuleInstance(index);
    }
    
    public List<Integer> getSelectedModules() {
        return selectedModuleIndices;
    }
}
