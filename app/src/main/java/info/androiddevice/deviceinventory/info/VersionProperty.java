package info.androiddevice.deviceinventory.info;

import org.json.JSONException;

public class VersionProperty implements Property {
    @Override
    public Object getProperty() throws JSONException {
        return 1;
    }

    @Override
    public String getName() {
        return "version";
    }
}
