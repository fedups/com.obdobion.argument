package com.obdobion.argument;

import java.text.ParseException;

/**
 * @author Chris DeGreef
 * 
 */
public class EnumCLA extends AbstractCLA<String>
{
    public EnumCLA(final char _keychar)
    {
        super(_keychar);
    }

    public EnumCLA(final char _keychar, final String _keyword)
    {
        super(_keychar, _keyword);
    }

    public EnumCLA(final String _keyword)
    {
        super(_keyword);
    }

    @Override
    public void asDefinedType (StringBuilder sb)
    {
        sb.append(CLAFactory.TYPE_ENUM);
    }

    @Override
    public Object asEnum (String enumClassFieldName, Object[] possibleConstants) throws ParseException
    {
        String enumConstantName = getValue();

        Object selectedConstant = null;

        final StringBuilder possibleValues = new StringBuilder();
        for (int c = 0; c < possibleConstants.length; c++)
        {
            final String econst = possibleConstants[c].toString();
            if (c > 0)
                possibleValues.append(", ");
            possibleValues.append(econst);
        }

        for (int c = 0; c < possibleConstants.length; c++)
        {
            String econst = possibleConstants[c].toString().toLowerCase();
            if (econst.length() > enumConstantName.length())
                econst = econst.substring(0, enumConstantName.length());
            if (econst.equalsIgnoreCase(enumConstantName))
            {
                if (selectedConstant != null)
                    throw new ParseException("\""
                            + enumConstantName
                            + "\" is not a unique enum constant for variable \""
                            + enumClassFieldName
                            + "\" ("
                            + possibleValues.toString()
                            + ")",
                            0);
                selectedConstant = possibleConstants[c];
            }
        }
        if (selectedConstant != null)
            return selectedConstant;

        throw new ParseException("\""
                + enumConstantName
                + "\" is not a valid enum constant for variable \""
                + enumClassFieldName
                + "\" ("
                + possibleValues.toString()
                + ")",
                0);
    }

    @Override
    public String convert (final String valueStr, final boolean _caseSensitive, final Object target)
    {
        if (_caseSensitive)
            return valueStr;
        return valueStr.toLowerCase();
    }

    @Override
    public String defaultInstanceClass ()
    {
        return enumClassName;
    }

    @Override
    protected void exportCommandLineData (final StringBuilder out, final int occ)
    {
        out.append('"');
        out.append(getValue(occ).replaceAll("\"", "\\\\\""));
        out.append('"');
    }

    @Override
    protected void exportNamespaceData (final String prefix, final StringBuilder out, final int occ)
    {
        out.append(prefix);
        out.append("=");
        out.append(getValue(occ));
        out.append("\n");
    }

    @Override
    protected void exportXmlData (final StringBuilder out, final int occ)
    {
        out.append(getValue(occ));
    }
}
