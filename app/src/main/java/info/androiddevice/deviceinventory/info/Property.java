package info.androiddevice.deviceinventory.info;

import org.json.JSONException;

public interface Property {
    public Object getProperty() throws JSONException;
    public String getName();
}
