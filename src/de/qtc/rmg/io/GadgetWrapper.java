package de.qtc.rmg.io;

import java.nio.ByteBuffer;
import java.util.Base64;

import de.qtc.rmg.exceptions.InvalidGadgetException;
import de.qtc.rmg.utils.RMGUtils;

public class GadgetWrapper
{
    private final byte[] gadget;

    public GadgetWrapper(String gadget) throws InvalidGadgetException
    {
        this.gadget = truncate(Base64.getDecoder().decode(gadget.getBytes()));
    }

    public GadgetWrapper(byte[] gadget) throws InvalidGadgetException
    {
        this.gadget = truncate(gadget);
    }

    public byte[] getGadget()
    {
        return gadget;
    }

    private byte[] truncate(byte[] fullGadget) throws InvalidGadgetException
    {
        if (!ByteBuffer.wrap(fullGadget, 0, 4).equals(ByteBuffer.wrap(RMGUtils.hexToBytes("aced0005"))))
        {
            throw new InvalidGadgetException();
        }

        byte[] truncated = new byte[fullGadget.length - 4];
        System.arraycopy(fullGadget, 4, truncated, 0, truncated.length);

        return truncated;
    }
}
