package mainapp.configurations;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

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

	public boolean setImportedConfiguration(Map<String, String> parseCCConfiguration, CodeCompilationService service) {
        if (parseCCConfiguration == null || parseCCConfiguration.size() != 1) {
            return false;
        }
        String modID = parseCCConfiguration.keySet().iterator().next();
        String config = parseCCConfiguration.get(modID);
        JSONArray configArray = new JSONArray(config);
        if (configArray.length() != 1) {
            return false;
        }

        selectModule(service, modID);

        return service.importConfiguration(selectedModuleIndex, configArray.getString(0));
	}
    
    public Map<String, String> exportConfiguration(CodeCompilationService service) {
        Map<String, String> ret = new HashMap<>();
        JSONArray configArray = new JSONArray();
        configArray.put(service.exportConfiguration(selectedModuleIndex));
        ret.put(selectedModuleID, configArray.toString());
        return ret;
    }
}
