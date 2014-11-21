package info.androiddevice.deviceinventory.info;


import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

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

    private StructStat getFileInfo(File file) throws Exception {
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            libcore.io.Os os = libcore.io.Libcore.os;
            libcore.io.StructStat fileInfo = os.lstat(file.getAbsolutePath());
            return new StructStat(fileInfo);
        } else {
            android.system.StructStat fileInfo = android.system.Os.lstat(file.getAbsolutePath());
            return new StructStat(fileInfo);
        }
    }

    private static class StructStat {
        public final int st_uid;
        public final int st_gid;
        public final int st_mode;

        public StructStat(libcore.io.StructStat info) {
            this.st_uid = info.st_uid;
            this.st_gid = info.st_gid;
            this.st_mode = info.st_mode;
        }

        public StructStat(android.system.StructStat info) {
            this.st_uid = info.st_uid;
            this.st_gid = info.st_gid;
            this.st_mode = info.st_mode;
        }
    }
}
