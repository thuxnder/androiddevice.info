package info.androiddevice.deviceinventory.info;


import android.annotation.TargetApi;
import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Properties;

public class JavaSystemProperty implements Property {
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public Object getProperty() {
        if(Build.VERSION.SDK_INT < 9) {
            return JSONObject.NULL;
        }
        JSONObject jsonObject = new JSONObject();
        Properties properties = System.getProperties();
        for (String propertyName : properties.stringPropertyNames()) {
            try {
                jsonObject.put(propertyName, properties.getProperty(propertyName));
            } catch (JSONException e) {
            }
        }
        return jsonObject;
    }

    @Override
    public String getName() {
        return "javasys";
    }
}
