package info.androiddevice.deviceinventory.info;

public class MountsProperty extends FileProperty {
    @Override
    public String getName() {
        return "mounts";
    }

    @Override
    protected String getFilename() {
        return "/proc/mounts";
    }
}
