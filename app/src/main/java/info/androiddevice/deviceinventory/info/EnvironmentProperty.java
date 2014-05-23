package info.androiddevice.deviceinventory.info;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class EnvironmentProperty implements Property {
    @Override
    public Object getProperty() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        for(Map.Entry<String, String> env:System.getenv().entrySet()) {
            try {
                jsonObject.put(env.getKey(), env.getValue());
            } catch (JSONException e1) {}
        }
        return jsonObject;
    }

    @Override
    public String getName() {
        return "env";
    }
}
