package info.androiddevice.deviceinventory.info;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import info.androiddevice.deviceinventory.Application;

import static info.androiddevice.deviceinventory.info.Utils.byteToHexString;

public class PackageSigProperty implements Property {
    @Override
    public String getName() {
        return "package_signature";
    }

    @Override
    public Object getProperty() {
        try {
            JSONArray result = new JSONArray();
            PackageInfo packageInfo = Application.getContext().getPackageManager().getPackageInfo("android", PackageManager.GET_SIGNATURES);
            for(Signature item: Arrays.asList(packageInfo.signatures)) {
                MessageDigest digest = null;
                try {
                    digest = MessageDigest.getInstance("SHA-256");
                    digest.update(item.toCharsString().getBytes());
                    result.put(byteToHexString(digest.digest()));
                } catch (NoSuchAlgorithmException e) {}

            }
            return result;
        } catch (PackageManager.NameNotFoundException e) {
            return JSONObject.NULL;
        }
    }


}
