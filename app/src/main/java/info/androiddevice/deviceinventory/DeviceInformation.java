package info.androiddevice.deviceinventory;


import org.json.JSONException;
import org.json.JSONObject;

import info.androiddevice.deviceinventory.info.DirProperty;
import info.androiddevice.deviceinventory.info.NameProperty;
import info.androiddevice.deviceinventory.info.OtacertsProperty;
import info.androiddevice.deviceinventory.info.PackageSigProperty;
import info.androiddevice.deviceinventory.info.CpuinfoProperty;
import info.androiddevice.deviceinventory.info.DisplayProperty;
import info.androiddevice.deviceinventory.info.EnvironmentProperty;
import info.androiddevice.deviceinventory.info.FeaturesProperty;
import info.androiddevice.deviceinventory.info.GetPropProperty;
import info.androiddevice.deviceinventory.info.JavaSystemProperty;
import info.androiddevice.deviceinventory.info.MeminfoProperty;
import info.androiddevice.deviceinventory.info.MountsProperty;
import info.androiddevice.deviceinventory.info.Property;
import info.androiddevice.deviceinventory.info.UsbProperty;
import info.androiddevice.deviceinventory.info.VersionProperty;

public class DeviceInformation {
    private static DeviceInformation singleton = null;

    public static DeviceInformation getInstance() {
        if(singleton==null)
            singleton = new DeviceInformation();
        return singleton;
    }

    private DeviceInformation() {
    }

    public JSONObject getDeviceInformation() {
        JSONObject jsonObject = new JSONObject();
        addProperty(jsonObject, new MeminfoProperty());
        addProperty(jsonObject, new MountsProperty());
        addProperty(jsonObject, new CpuinfoProperty());
        addProperty(jsonObject, new EnvironmentProperty());
        addProperty(jsonObject, new JavaSystemProperty());
        addProperty(jsonObject, new FeaturesProperty());
        addProperty(jsonObject, new DisplayProperty());
        addProperty(jsonObject, new UsbProperty());
        addProperty(jsonObject, new GetPropProperty());
        addProperty(jsonObject, new PackageSigProperty());
        addProperty(jsonObject, new OtacertsProperty());
        addProperty(jsonObject, new DirProperty());
        addProperty(jsonObject, new VersionProperty());
        addProperty(jsonObject, new NameProperty());

        return jsonObject;
    }

    private void addProperty(JSONObject jsonObject, Property property) {
        if(jsonObject.has(property.getName())) {
            throw new RuntimeException("property already exists");
        }
        try {
            jsonObject.put(property.getName(), property.getProperty());
        } catch (JSONException e) {
        //} catch (Exception e) {
            try {
                jsonObject.put(property.getName(), JSONObject.NULL);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

    }
}
