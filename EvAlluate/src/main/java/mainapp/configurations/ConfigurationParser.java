package mainapp.configurations;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConfigurationParser {
    public static Map<String, String> parseFFConfiguration(String config) {
        return parseModConfiguration(config, "f");
    }

    public static Map<String, String> parseCCConfiguration(String config) {
        return parseModConfiguration(config, "c");
    }

    public static Map<String, String> parseSSConfiguration(String config) {
        return parseModConfiguration(config, "s");
    }

    public static Map<String, String> parseDDConfiguration(String config) {
        return parseModConfiguration(config, "d");
    }

    public static String parseSSAggregation(String config) {
        try {
            return new JSONObject(config).getString("sa");
        }
        catch (JSONException e) {
            return null;
        }
    }

    public static String parseDDAggregation(String config) {
        try {
            return new JSONObject(config).getString("da");
        }
        catch (JSONException e) {
            return null;
        }
    }

    private static Map<String, String> parseModConfiguration(String config, String key) {
        try {
            JSONObject jsonConfig = new JSONObject(config);
            JSONArray modArray = jsonConfig.getJSONArray(key);
            Map<String, String> ret = new HashMap<>();
            for (int i = 0; i < modArray.length(); i++) {
                String modID = modArray.getJSONObject(i).getString("i");
                String modCStr = modArray.getJSONObject(i).getString("c");
                ret.put(modID, modCStr);
            }
            return ret;
        }
        catch (JSONException e) {
            return null;
        }
    }

	public static String buildConfigurationString(Map<String, String> ffC, Map<String, String> ccC,
			Map<String, String> ssC, Map<String, String> ddC, String ssA, String ddA) {
		JSONObject configObject = new JSONObject();

        configObject.put("f", buildModConfigArray(ffC));
        configObject.put("c", buildModConfigArray(ccC));
        configObject.put("s", buildModConfigArray(ssC));
        configObject.put("d", buildModConfigArray(ddC));

        configObject.put("sa", ssA);
        configObject.put("da", ddA);

        return configObject.toString();
    }

    private static JSONArray buildModConfigArray(Map<String, String> c) {
        JSONArray modArray = new JSONArray();
        for (String id : c.keySet()) {
            JSONObject modObject = new JSONObject();
            modObject.put("i", id);
            modObject.put("c", c.get(id));
            modArray.put(modObject);
        }
        return modArray;
    } 
}
