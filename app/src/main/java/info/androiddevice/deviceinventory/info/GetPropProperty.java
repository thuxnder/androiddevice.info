package info.androiddevice.deviceinventory.info;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import name.unused.android.utils.systemproperties.SystemProperty;
import name.unused.android.utils.systemproperties.exception.NoSuchPropertyException;
import info.androiddevice.deviceinventory.Application;

public class GetPropProperty implements Property {
    static List<String> whitelist = new ArrayList<String>() {{
        add("ro.product.device");
        add("ro.product.manufacturer");
        add("ro.product.model");
        add("ro.product.name");
        add("ro.board.platform");
        add("ro.boot.hardware");
        add("ro.build.description");
        add("ro.build.display.id");
        add("ro.build.fingerprint");
        add("ro.build.host");
        add("ro.build.id");
        add("ro.build.product");
        add("ro.build.tags");
        add("ro.build.type");
        add("ro.build.user");
        add("ro.build.version.codename");
        add("ro.build.version.incremental");
        add("ro.build.version.release");
        add("ro.build.version.sdk");
        add("ro.product.board");
        add("ro.product.brand");
        add("ro.product.cpu.abi2");
        add("ro.product.cpu.abi");
        add("ro.revision");
    }};


    @Override
    public Object getProperty() {
        JSONObject jsonObject = new JSONObject();
        SystemProperty property = new SystemProperty(Application.getContext());
        for(String key: whitelist) {
            try {
                String value = property.getOrThrow(key);
                jsonObject.put(key, value);
            } catch (NoSuchPropertyException e) {
                try {
                    jsonObject.put(key, JSONObject.NULL);
                } catch (JSONException e1) {}
            } catch (JSONException e) {
                try {
                    jsonObject.put(key, JSONObject.NULL);
                } catch (JSONException e1) {}
            }
        }
        return jsonObject;
    }

    @Override
    public String getName() {
        return "properties";
    }

}
