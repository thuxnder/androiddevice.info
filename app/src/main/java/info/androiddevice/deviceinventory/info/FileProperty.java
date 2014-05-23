package info.androiddevice.deviceinventory.info;


import org.json.JSONObject;

import static info.androiddevice.deviceinventory.info.Utils.readFile;

public abstract class FileProperty implements Property {

    @Override
    public Object getProperty() {
        try {
           return readFile(getFilename());
        } catch (Exception e) {
            return JSONObject.NULL;
        }
    }

    protected abstract String getFilename();
}
