package libcore.io;

public interface Os {
    public StructStat lstat(String path) throws ErrnoException;
}
