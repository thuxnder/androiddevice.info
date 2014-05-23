package info.androiddevice.deviceinventory.info;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static info.androiddevice.deviceinventory.info.Utils.readFile;

public class UsbProperty implements Property {
    private final List<String> keys = new ArrayList<String>() {{
        add("iManufacturer");
        add("iProduct");
        add("idProduct");
        add("idVendor");
    }};
    @Override
    public Object getProperty() {
        JSONObject jsonObject = new JSONObject();
        for(String key:keys) {
            try {
                String deviceFolder = "/sys/devices/virtual/android_usb/android0/";
                String value = readFile(new File(deviceFolder, key));
                jsonObject.put(key, value.trim());
            } catch (Exception e) {
                try {
                    jsonObject.put(key, JSONObject.NULL);
                } catch (JSONException e1) {}
            }
        }
        return jsonObject;
    }

    @Override
    public String getName() {
        return "usb";
    }
}
