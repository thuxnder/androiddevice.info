package info.androiddevice.deviceinventory.info;

public class MeminfoProperty extends FileProperty {
    @Override
    public String getName() {
        return "meminfo";
    }

    @Override
    protected String getFilename() {
        return "/proc/meminfo";
    }
}
