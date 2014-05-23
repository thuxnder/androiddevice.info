package info.androiddevice.deviceinventory.info;

import org.json.JSONException;

public class NameProperty implements Property {
    @Override
    public Object getProperty() throws JSONException {
        return "";
    }

    @Override
    public String getName() {
        return "name";
    }
}
