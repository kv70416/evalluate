package mainapp.configurations;

import java.util.ArrayList;
import java.util.List;

import mainapp.results.AggregationType;
import mainapp.services.SolutionScoringService;

public class SolutionScoringConfiguration extends ModuleConfiguration {
    
    private List<Integer> selectedModuleIndices = new ArrayList<>();

    private AggregationType aggregationType = null;
    
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

    public void setAggregationType(AggregationType aggregationType) {
        this.aggregationType = aggregationType;
    }

    public AggregationType getAggregationType() {
        return aggregationType;
    }
}
