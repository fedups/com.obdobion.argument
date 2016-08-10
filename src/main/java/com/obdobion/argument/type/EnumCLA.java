package com.obdobion.argument.type;

import java.text.ParseException;

/**
 * <p>EnumCLA class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class EnumCLA extends StringCLA
{
    /** {@inheritDoc} */
    @Override
    public Object asEnum(final String enumClassFieldName, final Object[] possibleConstants) throws ParseException
    {
        return stringToEnumConstant(enumClassFieldName, possibleConstants, getValue());
    }

    /** {@inheritDoc} */
    @Override
    public Enum<?>[] asEnumArray(final String enumClassFieldName, final Object[] possibleConstants)
            throws ParseException
    {
        final Enum<?>[] enumArray = new Enum<?>[size()];
        for (int v = 0; v < size(); v++)
        {
            enumArray[v] = (Enum<?>) stringToEnumConstant(enumClassFieldName, possibleConstants, getValue(v));
        }
        return enumArray;
    }

    /** {@inheritDoc} */
    @Override
    public String convert(final String valueStr, final boolean _caseSensitive, final Object target)
    {
        if (_caseSensitive)
            return valueStr;
        return valueStr.toLowerCase();
    }

    /** {@inheritDoc} */
    @Override
    public String defaultInstanceClass()
    {
        return enumClassName;
    }

    /** {@inheritDoc} */
    @Override
    protected void exportCommandLineData(final StringBuilder out, final int occ)
    {
        uncompileQuoter(out, getValue(occ));
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
        out.append(getValue(occ));
    }

    /** {@inheritDoc} */
    @Override
    public String genericClassName()
    {
        return "java.lang.String";
    }

    private Object stringToEnumConstant(final String enumClassFieldName, final Object[] possibleConstants,
            final String enumConstantName) throws ParseException
    {
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
}
