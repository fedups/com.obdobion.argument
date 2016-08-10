package com.obdobion.argument.type;

/**
 * <p>ByteCLA class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class ByteCLA extends AbstractCLA<Byte>
{
    /*
     * These literals represent the number that happens to be the same as its
     * index.
     */
    /** Constant <code>ByteLiteral="{null,soh,stx,etx,eot,enq,ack,bel,bs,ht"{trunked}</code> */
    static public final String[] ByteLiteral = {
            "null",
            "soh",
            "stx",
            "etx",
            "eot",
            "enq",
            "ack",
            "bel",
            "bs",
            "ht",
            "lf",
            "vt",
            "ff",
            "cr",
            "so",
            "si",
            "dle",
            "dc1",
            "dc2",
            "dc3",
            "dc4",
            "nak",
            "syn",
            "etb",
            "can",
            "em",
            "sub",
            "esc",
            "fs",
            "gs",
            "rs",
            "us",
            "sp"
    };

    /**
     * <p>asLiteral.</p>
     *
     * @param aByte a byte.
     * @return a {@link java.lang.String} object.
     */
    static public String asLiteral(final byte aByte)
    {
        if (aByte < ByteLiteral.length)
            return ByteLiteral[aByte] + "(" + (int) aByte + ")";
        return (char) aByte + "(" + (int) aByte + ")";
    }

    /**
     * <p>byteToLit.</p>
     *
     * @param number a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    static public String byteToLit(final String number)
    {
        final byte aByte = (byte) Integer.parseInt(number);

        if (aByte < ByteLiteral.length)
            return ByteLiteral[aByte];
        return "" + (int) aByte;
    }

    /** {@inheritDoc} */
    @Override
    public Byte convert(final String valueStr, final boolean _caseSensitive, final Object target)
    {
        if (valueStr.length() == 1)
        {
            if (_caseSensitive)
                return new Byte((byte) valueStr.charAt(0));
            return new Byte((byte) valueStr.toLowerCase().charAt(0));
        }
        for (int b = 0; b < ByteLiteral.length; b++)
        {
            if (ByteLiteral[b].equalsIgnoreCase(valueStr))
                return (byte) b;
        }
        int intValue = 0;
        try
        {
            intValue = Integer.parseInt(valueStr);
        } catch (final NumberFormatException e)
        {
            final StringBuilder errMsg = new StringBuilder();
            errMsg.append(valueStr);
            errMsg.append(" is not a valid byte code.  Try one of these codes or the corresponding number: ");
            for (int b = 0; b < ByteLiteral.length; b++)
            {
                errMsg.append(ByteLiteral[b].toUpperCase());
                errMsg.append("(");
                errMsg.append(b);
                errMsg.append(") ");
            }
            throw new NumberFormatException(errMsg.toString());
        }
        if (intValue > 0xff)
            throw new NumberFormatException(intValue + " is too large, max = 255");
        return new Byte((byte) intValue);
    }

    /** {@inheritDoc} */
    @Override
    public String defaultInstanceClass()
    {
        return "byte";
    }

    /** {@inheritDoc} */
    @Override
    protected void exportCommandLineData(final StringBuilder out, final int occ)
    {
        out.append(" ");
        out.append(getValue(occ));
    }

    /** {@inheritDoc} */
    @Override
    protected void exportNamespaceData(final String prefix, final StringBuilder out, final int occ)
    {
        out.append(prefix);
        out.append("=");
        out.append(getValue(occ));
        out.append("\n");
    }

    /** {@inheritDoc} */
    @Override
    protected void exportXmlData(final StringBuilder out, final int occ)
    {
        xmlEncode(getValue(occ).toString(), out);
    }

    /** {@inheritDoc} */
    @Override
    public String genericClassName()
    {
        return "java.lang.Byte";
    }

    /** {@inheritDoc} */
    @Override
    public byte[] getValueAsbyteArray()
    {
        final byte[] result = new byte[size()];

        for (int r = 0; r < size(); r++)
            result[r] = getValue(r).byteValue();

        return result;
    }

    /** {@inheritDoc} */
    @Override
    public Byte[] getValueAsByteArray()
    {
        final Byte[] result = new Byte[size()];

        for (int r = 0; r < size(); r++)
            result[r] = getValue(r);

        return result;
    }
}
