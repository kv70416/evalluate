package mainapp.configurations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import mainapp.results.Aggregation;
import mainapp.results.AggregationType;
import mainapp.services.SolutionScoringService;

public class SolutionScoringConfiguration extends ModuleConfiguration {
    
    private List<Integer> selectedModuleIndices = new ArrayList<>();
    private Map<String, List<Integer> > selectedPerModuleID = new HashMap<>();

    private AggregationType aggregationType = null;
    
    public int addModule(SolutionScoringService service, String moduleID) {
        int index = service.createModuleInstance(moduleID);
        selectedModuleIndices.add(index);
        if (selectedPerModuleID.get(moduleID) == null)
            selectedPerModuleID.put(moduleID, new ArrayList<>());
        selectedPerModuleID.get(moduleID).add(index);
        return index;
    }
    
    public void removeModule(SolutionScoringService service, int index) {
        selectedModuleIndices.removeIf(i -> i.equals(index));
        for (List<Integer> l : selectedPerModuleID.values()) {
            l.removeIf(i -> i.equals(index));
        }
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


	public boolean setImportedConfiguration(Map<String, String> parseSSConfiguration, SolutionScoringService service) {
        if (parseSSConfiguration == null || parseSSConfiguration.size() < 1) {
            return false;
        }

        for (String id : parseSSConfiguration.keySet()) {
            String config = parseSSConfiguration.get(id);
            JSONArray configArray = new JSONArray(config);
            if (configArray.length() < 1) {
                return false;
            }
            for (int i = 0; i < configArray.length(); i++) {
                int index = addModule(service, id);
                boolean success = service.importConfiguration(index, configArray.getString(i));
                if (!success) {
                    return false;
                }
            }
        }
        return true;
    }

	public boolean setImportedAggregation(String parseSSAggregation) {
        setAggregationType(Aggregation.stringToType(parseSSAggregation));
		return true;
	}

    public Map<String, String> exportConfiguration(SolutionScoringService service) {
        Map<String, String> ret = new HashMap<>();
        for (String id : selectedPerModuleID.keySet()) {
            List<Integer> indices = selectedPerModuleID.get(id);
            JSONArray configArray = new JSONArray();
            for (int index : indices) {
                configArray.put(service.exportConfiguration(index));
            }
            ret.put(id, configArray.toString());
        }
        return ret;
    }

    public String exportAggregation() {
        return Aggregation.typeToString(aggregationType);
    }
}
