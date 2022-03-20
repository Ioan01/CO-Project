package Log;

import java.io.IOException;
import java.io.OutputStream;

/**
 Generic stream logger class used for writing to any type of output stream,
 such as files, standard console output, error output etc.
 */

public class StreamLog implements ILog{

    private final OutputStream stream;

    public StreamLog(OutputStream stream)
    {
        this.stream = stream;
    }


    /**
     * @param objects Writes objects to the output stream
     */
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

    /**
     * @param format Specifies the format in which the objects will be printed
     * @param objects Specifies the objects printed in the format specified
     */
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

    /**
     * Flushes the output stream, before closing it
     */
    public void close()
    {
        try
        {
            stream.flush();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            stream.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
