package Log;

import java.io.IOException;
import java.io.OutputStream;

public class StreamLog implements ILog{

    private final OutputStream stream;

    public StreamLog(OutputStream stream)
    {
        this.stream = stream;
    }

    @Override
    public void write(Object... objects)
    {
        for (Object o:objects) {
            try
            {
                stream.write(o.toString().concat("\n").getBytes());
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void writeFormatted(String format, Object... objects)
    {
        try
        {
            stream.write(String.format(format,objects).getBytes());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void close()
    {
        try
        {
            stream.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
