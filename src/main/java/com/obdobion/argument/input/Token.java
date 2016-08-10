package com.obdobion.argument.input;

import org.apache.commons.codec.language.Metaphone;

import com.obdobion.argument.CmdLine;
import com.obdobion.argument.type.ICmdLineArg;

/**
 * <p>Token class.</p>
 *
 * @author Chris DeGreef fedupforone@gmail.com
 */
public class Token
{
    private final char commandPrefix;
    private final int  inputStartX;
    private final int  inputEndX;
    private String     value;
    private boolean    literal;
    private boolean    used;
    private boolean    charCommand;
    private boolean    wordCommand;
    private String     cachedWordCommand;

    /**
     * <p>Constructor for Token.</p>
     *
     * @param _commandPrefix a char.
     * @param _value a {@link java.lang.String} object.
     */
    public Token(final char _commandPrefix, final String _value)
    {
        this(_commandPrefix, _value, 0, _value.length() - 1, false);
    }

    /**
     * <p>Constructor for Token.</p>
     *
     * @param _commandPrefix a char.
     * @param _value a {@link java.lang.String} object.
     * @param startIndex a int.
     * @param endIndex a int.
     * @param forceLiteral a boolean.
     */
    public Token(
            final char _commandPrefix,
            final String _value,
            final int startIndex,
            final int endIndex,
            final boolean forceLiteral)
    {
        super();
        this.commandPrefix = _commandPrefix;
        this.value = _value;
        this.used = false;
        this.inputStartX = startIndex;
        this.inputEndX = endIndex - 1;

        if (forceLiteral)
            this.literal = true;
        else if (!isParserDirective())
            switch (dashes())
            {
            case 1:
                this.charCommand = true;
                break;
            case 2:
                this.wordCommand = true;
                break;
            default:
                this.literal = true;
            }
    }

    /**
     * <p>charCommand.</p>
     *
     * @return a char.
     */
    public char charCommand()
    {
        if (!isCharCommand() || isUsed())
            return 0;
        return getValue().charAt(1);
    }

    private int dashes()
    {
        if (getValue() == null || isCommand() || getValue().length() == 0)
            return 0;
        final int tokenSize = getValue().length();
        if (tokenSize > 1 && getValue().charAt(0) == commandPrefix)
        {
            if (tokenSize > 2 && getValue().charAt(1) == commandPrefix)
                return 2;
            return 1;
        }
        return 0;
    }

    /**
     * <p>Getter for the field <code>inputEndX</code>.</p>
     *
     * @return a int.
     */
    public int getInputEndX()
    {
        return inputEndX;
    }

    /**
     * <p>Getter for the field <code>inputStartX</code>.</p>
     *
     * @return a int.
     */
    public int getInputStartX()
    {
        return inputStartX;
    }

    /**
     * <p>Getter for the field <code>value</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getValue()
    {
        return value;
    }

    /**
     * <p>Getter for the field <code>wordCommand</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getWordCommand()
    {
        if (cachedWordCommand != null)
            return cachedWordCommand;

        if (!isWordCommand() || getValue().length() < 3)
            return null;
        cachedWordCommand = getValue().substring(2);
        return cachedWordCommand;
    }

    /**
     * <p>isCharCommand.</p>
     *
     * @return a boolean.
     */
    public boolean isCharCommand()
    {
        return charCommand;
    }

    /**
     * <p>isCharCommand.</p>
     *
     * @param argDef a {@link com.obdobion.argument.type.ICmdLineArg} object.
     * @return a boolean.
     */
    public boolean isCharCommand(final ICmdLineArg<?> argDef)
    {
        return argDef.getKeychar() != null && isCharCommand() && (charCommand() == argDef.getKeychar().charValue());
    }

    /**
     * <p>isCommand.</p>
     *
     * @return a boolean.
     */
    public boolean isCommand()
    {
        return isCharCommand() | isWordCommand();
    }

    /**
     * <p>isGroupEnd.</p>
     *
     * @return a boolean.
     */
    public boolean isGroupEnd()
    {
        if (getValue() == null || isCommand() || getValue().length() != 1)
            return false;
        return getValue().charAt(0) == ')' || getValue().charAt(0) == ']';
    }

    /**
     * <p>isGroupStart.</p>
     *
     * @return a boolean.
     */
    public boolean isGroupStart()
    {
        if (getValue() == null || isCommand() || getValue().length() != 1)
            return false;
        return getValue().charAt(0) == '(' || getValue().charAt(0) == '[';
    }

    /**
     * <p>isIncludeFile.</p>
     *
     * @return a boolean.
     */
    public boolean isIncludeFile()
    {
        if (value != null && value.length() > 0 && CmdLine.INCLUDE_FILE_PREFIX.charAt(0) == value.charAt(0))
            return true;
        return false;
    }

    /**
     * <p>isLiteral.</p>
     *
     * @return a boolean.
     */
    public boolean isLiteral()
    {
        return literal;
    }

    /**
     * <p>isParserDirective.</p>
     *
     * @return a boolean.
     */
    public boolean isParserDirective()
    {
        if (getValue() == null || isCommand() || getValue().length() == 0)
        {
            return false;
        }
        return getValue().charAt(0) == '_';
    }

    /**
     * <p>isUsed.</p>
     *
     * @return a boolean.
     */
    public boolean isUsed()
    {
        return used;
    }

    /**
     * <p>isWordCommand.</p>
     *
     * @return a boolean.
     */
    public boolean isWordCommand()
    {
        return wordCommand;
    }

    /**
     * <p>isWordCommand.</p>
     *
     * @param argDef a {@link com.obdobion.argument.type.ICmdLineArg} object.
     * @return a boolean.
     */
    public boolean isWordCommand(final ICmdLineArg<?> argDef)
    {
        if (argDef.getKeyword() == null || isUsed() || !isWordCommand() || getValue().length() < 3)
            return false;
        if (getWordCommand().length() == argDef.getKeyword().length())
        {
            if (getWordCommand().equalsIgnoreCase(argDef.getKeyword()))
                return true;
        } else if (getWordCommand().length() < argDef.getKeyword().length())
        {
            if (argDef.getKeyword().substring(0, getWordCommand().length()).equalsIgnoreCase(getWordCommand()))
                return true;
            if (argDef.isCamelCapsAllowed())
                /*
                 * Check for camel caps
                 */
                if (argDef.getCamelCaps() != null)
                    if (argDef.getCamelCaps().equalsIgnoreCase(getWordCommand()))
                        return true;
            if (argDef.isMetaphoneAllowed())
                /*
                 * Check for metaphone
                 */
                if (argDef.getMetaphone() != null)
                    if (new Metaphone().metaphone(getWordCommand()).equals(argDef.getMetaphone()))
                        return true;
        }
        return false;
    }

    /**
     * <p>remainderValue.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String remainderValue()
    {
        if (isCharCommand())
        {
            if (getValue().length() == 1)
                return null;
            return getValue().substring(1);
        }
        if (isWordCommand())
            return null;
        return getValue();
    }

    /**
     * <p>removeCharCommand.</p>
     */
    public void removeCharCommand()
    {
        if (!isCharCommand())
            return;
        if (getValue() == null)
            return;
        if (getValue().length() < 2)
            return;
        if (getValue().length() == 2)
        {
            setValue(null);
            setUsed(true);
            return;
        }
        setValue("-" + getValue().substring(2));
    }

    /**
     * <p>Setter for the field <code>literal</code>.</p>
     *
     * @param _literal a boolean.
     */
    protected void setLiteral(final boolean _literal)
    {
        this.literal = _literal;
    }

    /**
     * <p>Setter for the field <code>used</code>.</p>
     *
     * @param _used a boolean.
     */
    public void setUsed(final boolean _used)
    {
        this.used = _used;
    }

    /**
     * <p>Setter for the field <code>value</code>.</p>
     *
     * @param _value a {@link java.lang.String} object.
     */
    protected void setValue(final String _value)
    {
        this.value = _value;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        if (isUsed())
            return getValue() + "[" + inputStartX + "," + inputEndX + "] used";
        return getValue() + "[" + inputStartX + "," + inputEndX + "]";
    }
}
