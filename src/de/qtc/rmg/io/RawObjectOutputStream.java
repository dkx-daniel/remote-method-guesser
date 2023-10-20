package de.qtc.rmg.io;

import java.io.DataOutput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.qtc.rmg.internal.ExceptionHandler;

/**
 * Wrapper class for an ObjectOutputStream. Allows to perform raw byte operations on the underlying
 * output stream.
 *
 * @author Tobias Neitzel (@qtc_de)
 */
public class RawObjectOutputStream {

    private DataOutput bout;
    private OutputStream outStream;
    private Method setBlockDataMode;

    /**
     * Wraps an ObjectOutputStream into an RawObjectOutputStream. The underlying OutputStream object is made
     * accessible via reflection. This underlying OutputStream can then be used to perform raw byte operations.
     *
     * @param out OutputStream to wrap around
     */
    public RawObjectOutputStream(ObjectOutput out)
    {
        try {
            Field boutField = ObjectOutputStream.class.getDeclaredField("bout");
            boutField.setAccessible(true);
            bout = (DataOutput)boutField.get(out);

            Field outputStreamField = null;
            Class<?>[] classes = ObjectOutputStream.class.getDeclaredClasses();
            for(Class<?> c : classes) {
                if(c.getCanonicalName().endsWith("BlockDataOutputStream")) {
                    outputStreamField = c.getDeclaredField("out");
                    outputStreamField.setAccessible(true);

                    setBlockDataMode = c.getDeclaredMethod("setBlockDataMode", new Class<?>[] {boolean.class});
                    setBlockDataMode.setAccessible(true);
                }
            }

            outStream = (OutputStream)outputStreamField.get(bout);

        } catch (Exception e) {
            ExceptionHandler.unexpectedException(e, "creation", "of MaliciousOutputStream", true);
        }
    }

    /**
     * Write raw byte to the underlying output stream.
     *
     * @param content byte to write
     * @throws IOException
     */
    public void writeRaw(byte content) throws IOException
    {
        outStream.write(content);
    }

    /**
     * Write raw bytes to the underlying output stream.
     *
     * @param content bytes to write
     * @throws IOException
     */
    public void writeRawObject(byte[] content) throws IOException
    {
        try
        {
            setBlockDataMode.invoke(bout, false);
            outStream.write(content);
            setBlockDataMode.invoke(bout, true);
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
