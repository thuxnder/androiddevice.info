package info.androiddevice.deviceinventory.info;

import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;

import org.json.JSONArray;

import info.androiddevice.deviceinventory.Application;

public class FeaturesProperty implements Property {
    @Override
    public Object getProperty() {
        JSONArray jsonArray = new JSONArray();

        PackageManager pm = Application.getContext().getPackageManager();
        FeatureInfo[] features = pm != null ? pm.getSystemAvailableFeatures() : new FeatureInfo[0];
        for(FeatureInfo feature: features) {
            jsonArray.put(feature.name);
        }
        return jsonArray;
    }

    @Override
    public String getName() {
        return "feature";
    }
}
