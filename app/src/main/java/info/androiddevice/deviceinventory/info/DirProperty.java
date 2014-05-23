package info.androiddevice.deviceinventory.info;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import libcore.io.Libcore;
import libcore.io.Os;
import libcore.io.StructStat;

import static info.androiddevice.deviceinventory.info.Utils.readFile;

public class DirProperty implements Property {
    private static final String[] directories = new String[] {"/dev/socket", "/system/bin", "/system/xbin"};

    @Override
    public Object getProperty() {
        JSONObject jsonDirectories = new JSONObject();
        for(String directory: directories) {
            try {
                jsonDirectories.put(directory, getDirListing(new File(directory)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonDirectories;
    }

    @Override
    public String getName() {
        return "directories";
    }


    private JSONArray getDirListing(File directory) {
        try {
            JSONArray result = new JSONArray();
            if(!directory.isDirectory()) {
                return result;
            }
            File[] files = directory.listFiles();
            if(files == null) {
                return result;
            }
            for(File file: files) {
                try {
                    JSONObject jsonFile = new JSONObject();
                    jsonFile.put("pathname", file.getName());
                    StructStat info = getFileInfo(file);
                    jsonFile.put("st_mode", info.st_mode);
                    jsonFile.put("st_uid", info.st_uid);
                    jsonFile.put("st_gid", info.st_gid);
                    result.put(jsonFile);
                } catch(Exception e) {
                    continue;
                }
            }
           return result;
        } catch (Exception e) {
            return new JSONArray();
        }
    }

    private StructStat getFileInfo(File file) throws Exception, IllegalAccessError {
        Os os = Libcore.os;
        StructStat fileInfo = os.lstat(file.getAbsolutePath());
        return fileInfo;
    }
}
