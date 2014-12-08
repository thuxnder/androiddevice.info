package info.androiddevice.deviceinventory.info;

import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;

import org.json.JSONArray;

import info.androiddevice.deviceinventory.Application;

public class SharedLibraryNamesProperty implements Property {
    @Override
    public Object getProperty() {
        JSONArray jsonArray = new JSONArray();

        PackageManager pm = Application.getContext().getPackageManager();
        String[] libraries = pm != null ? pm.getSystemSharedLibraryNames() : new String[0];
        for(String library: libraries) {
            jsonArray.put(library);
        }
        return jsonArray;
    }

    @Override
    public String getName() {
        return "sharedlibraries";
    }
}
