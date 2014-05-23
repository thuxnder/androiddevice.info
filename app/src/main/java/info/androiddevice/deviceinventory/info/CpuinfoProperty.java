package info.androiddevice.deviceinventory.info;

public class CpuinfoProperty extends FileProperty {
    @Override
    public String getName() {
        return "cpuinfo";
    }

    @Override
    protected String getFilename() {
        return "/proc/cpuinfo";
    }
}
