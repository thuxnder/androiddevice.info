package info.androiddevice.deviceinventory;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Error {
    String packagename;
    String versionname;
    String versioncode;
    String model;
    String androidversion;
    String board;
    String device;
    String brand;
    String stacktrace;

    public Error(PackageInfo packageinfo, Throwable exception) {
        packagename = packageinfo.packageName;
        versionname = packageinfo.versionName;
        versioncode = Integer.toString(packageinfo.versionCode);
        model = Build.MODEL;
        androidversion = Build.VERSION.RELEASE;
        board = Build.BOARD;
        device = Build.DEVICE;
        brand = Build.BRAND;
        stacktrace = getStacktrace(exception);
    }

    private String getStacktrace(Throwable exception) {
        Throwable current = exception;
        StringBuilder result = new StringBuilder();

        do {
            result.append(current.toString() + "\n");
            for(StackTraceElement elem : current.getStackTrace()) {
                result.append(" at " + elem.toString() + "\n");
            }

            current = current.getCause();
            if(current != null) {
                result.append("\n");
                result.append("Caused by:\n");
                result.append("\n");
            }
        } while(current != null);
        return result.toString();
    }

    @Override
    public String toString() {
        return "Error{" +
                "packagename='" + packagename + '\'' +
                ", versionname='" + versionname + '\'' +
                ", versioncode='" + versioncode + '\'' +
                ", model='" + model + '\'' +
                ", androidversion='" + androidversion + '\'' +
                ", board='" + board + '\'' +
                ", device='" + device + '\'' +
                ", brand='" + brand + '\'' +
                ", stacktrace='" + stacktrace + '\'' +
                '}';
    }
}
