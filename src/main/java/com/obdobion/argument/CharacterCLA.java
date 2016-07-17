package com.obdobion.argument;

import java.text.ParseException;

/**
 * @author Chris DeGreef
 *
 */
public class CharacterCLA extends AbstractCLA<Character>
{

    public CharacterCLA(final char _keychar)
    {
        super(_keychar);
    }

    public CharacterCLA(final char _keychar, final String _keyword)
    {
        super(_keychar, _keyword);
    }

    public CharacterCLA(final String _keyword)
    {
        super(_keyword);
    }

    @Override
    public void asDefinedType(final StringBuilder sb)
    {
        sb.append(CLAFactory.TYPE_CHAR);
    }

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

    @Override
    public String defaultInstanceClass()
    {
        return "char";
    }

    @Override
    protected void exportCommandLineData(final StringBuilder out, final int occ)
    {
        out.append(" ");
        out.append(getValue(occ));
    }

    @Override
    protected void exportNamespaceData(final String prefix, final StringBuilder out, final int occ)
    {
        out.append(prefix);
        out.append("=");
        out.append(getValue(occ));
        out.append("\n");
    }

    @Override
    protected void exportXmlData(final StringBuilder out, final int occ)
    {
        xmlEncode(getValue(occ).toString(), out);
    }

    public String genericClassName()
    {
        return "java.lang.Character";
    }

    @Override
    public Character[] getValueAsCharacterArray() throws ParseException
    {
        final Character[] result = new Character[size()];

        for (int r = 0; r < size(); r++)
            result[r] = getValue(r);

        return result;
    }

    @Override
    public char[] getValueAscharArray() throws ParseException
    {
        final char[] result = new char[size()];

        for (int r = 0; r < size(); r++)
            result[r] = getValue(r).charValue();

        return result;
    }
}
