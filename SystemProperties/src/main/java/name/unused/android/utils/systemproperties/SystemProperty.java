package name.unused.android.utils.systemproperties;

import android.content.Context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import name.unused.android.utils.systemproperties.exception.NoSuchPropertyException;

public class SystemProperty {
    private final Context mContext;

    public SystemProperty(Context mContext) {
        this.mContext = mContext;
    }

    public String getOrThrow(String key) throws NoSuchPropertyException {
        try {
            ClassLoader classLoader = mContext.getClassLoader();
            Class SystemProperties = classLoader.loadClass("android.os.SystemProperties");
            Method methodGet = SystemProperties.getMethod("get", String.class);
            return (String) methodGet.invoke(SystemProperties, key);
        } catch (ClassNotFoundException e) {
            throw new NoSuchPropertyException(e);
        } catch (NoSuchMethodException e) {
            throw new NoSuchPropertyException(e);
        } catch (InvocationTargetException e) {
            throw new NoSuchPropertyException(e);
        } catch (IllegalAccessException e) {
            throw new NoSuchPropertyException(e);
        }
    }

    public String get(String key) {
        try {
            return getOrThrow(key);
        } catch (NoSuchPropertyException e) {
            return null;
        }
    }

}
