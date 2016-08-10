package com.obdobion.argument.type;

import java.text.ParseException;

/**
 * <p>CharacterCLA class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class CharacterCLA extends AbstractCLA<Character>
{
    /** {@inheritDoc} */
    @Override
    public Character convert(final String valueStr, final boolean _caseSensitive, final Object target)
            throws ParseException
    {
        if (valueStr.length() == 1)
        {
            if (_caseSensitive)
                return new Character(valueStr.charAt(0));
            return new Character(valueStr.toLowerCase().charAt(0));
        }
        throw new ParseException("invalid value for character argument: " + valueStr, 0);
    }

    /** {@inheritDoc} */
    @Override
    public String defaultInstanceClass()
    {
        return "char";
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
        return "java.lang.Character";
    }

    /** {@inheritDoc} */
    @Override
    public Character[] getValueAsCharacterArray() throws ParseException
    {
        final Character[] result = new Character[size()];

        for (int r = 0; r < size(); r++)
            result[r] = getValue(r);

        return result;
    }

    /** {@inheritDoc} */
    @Override
    public char[] getValueAscharArray() throws ParseException
    {
        final char[] result = new char[size()];

        for (int r = 0; r < size(); r++)
            result[r] = getValue(r).charValue();

        return result;
    }
}
