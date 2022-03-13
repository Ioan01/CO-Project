package Log;

public interface ILog {
    void write(Object... objects);

    void writeFormatted(String format,Object... objects);
}
