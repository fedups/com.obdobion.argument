package com.obdobion.argument.input;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.obdobion.argument.ICmdLineArg;
import com.obdobion.argument.Token;

/**
 * @author Chris DeGreef
 * 
 */
public class XmlParser extends AbstractInputParser implements IParserInput
{
    final static private String rootTag   = "cmdline";
    final static private String nonameTag = "noname";
    final static private String delimAttr = "delim";

    static public IParserInput getInstance (final File file) throws IOException
    {
        final XmlParser parser = new XmlParser();
        parser.input = new FileInputStream(file);
        return parser;
    }

    static public IParserInput getInstance (final InputStream input)
    {
        final XmlParser parser = new XmlParser();
        parser.input = input;
        return parser;
    }

    static public IParserInput getInstance (final String input)
    {
        final InputStream is = new ByteArrayInputStream(input.getBytes());
        final XmlParser parser = new XmlParser();
        parser.input = is;
        return parser;
    }

    static protected String quote (final String value)
    {
        final StringBuilder out = new StringBuilder();
        out.append(value.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
        return out.toString();
    }

    static public String unparseTokens (final List<ICmdLineArg<?>> args)
    {
        final StringBuilder out = new StringBuilder();
        out.append("<").append(rootTag).append(">");
        unparseTokens(args, out);
        out.append("</").append(rootTag).append(">");
        return out.toString();
    }

    static public void unparseTokens (final List<ICmdLineArg<?>> args, final StringBuilder out)
    {
        final Iterator<ICmdLineArg<?>> aIter = args.iterator();
        while (aIter.hasNext())
        {
            final ICmdLineArg<?> arg = aIter.next();
            if (arg.isParsed())
            {
                arg.exportXml(out);
            }
        }
    }

    InputStream input;
    final char  commandPrefix = '-';

    private XmlParser()
    {
        super();
    }

    public String substring (int inclusiveStart, int exclusiveEnd)
    {
        return "";
    }

    public Token[] parseTokens ()
    {
        final List<Token> out = new ArrayList<>();
        final List<NodeOc> depth = new ArrayList<>();
        final List<NodeOc> line = new ArrayList<>();
        final NodeOc closeGroup = new NodeOc();
        closeGroup.key = CLOSE_GROUP;
        closeGroup.occurrance = -1;

        final SAXParserFactory factory = SAXParserFactory.newInstance();
        try
        {
            final SAXParser parser = factory.newSAXParser();
            parser.parse(input, new DefaultHandler()
            {
                StringBuilder value;
                int           nodeCounter = -1;
                String        delim       = null;

                @Override
                public void characters (final char[] ch, final int start, final int length) throws SAXException
                {
                    for (int c = start; c < start + length; c++)
                    {
                        /*
                         * Extra spaces between tags end up here. I guess this
                         * could be comments too. Who cares, ignore it. The
                         * value gets assigned when a tag actually starts.
                         */
                        if (value != null)
                            value.append(ch[c]);
                    }
                }

                @Override
                public void endDocument () throws SAXException
                {
                    for (int d = 1; d < depth.size(); d++)
                    {
                        line.add(closeGroup);
                    }
                }

                @Override
                public void endElement (final String uri, final String localName, final String name)
                        throws SAXException
                {
                    if (rootTag.equals(name))
                        return;
                    if (value != null)
                    {
                        if (delim == null)
                            buildTokens(commandPrefix, out, depth, line, value.toString());
                        else
                        {
                            final String[] parts = value.toString().split(delim);
                            for (final String part : parts)
                                buildTokens(commandPrefix, out, depth, line, part);
                        }
                        value = null;
                    }
                    line.remove(line.size() - 1);
                }

                @Override
                public void startDocument () throws SAXException
                {
                    super.startDocument();
                }

                @Override
                public void startElement (final String uri, final String localName, final String name,
                        final Attributes attributes) throws SAXException
                {
                    if (rootTag.equals(name))
                        return;

                    value = new StringBuilder();
                    {
                        final NodeOc newNode = new NodeOc();
                        if (nonameTag.equalsIgnoreCase(name))
                            newNode.key = "";
                        else
                            newNode.key = name;
                        newNode.occurrance = nodeCounter++;
                        line.add(newNode);
                    }
                    for (int a = 0; a < attributes.getLength(); a++)
                    {
                        if (delimAttr.equalsIgnoreCase(attributes.getQName(a)))
                        {
                            delim = attributes.getValue(a);
                            continue;
                        }

                        final NodeOc aNode = new NodeOc();
                        if (nonameTag.equalsIgnoreCase(attributes.getQName(a)))
                            aNode.key = "";
                        else
                            aNode.key = attributes.getQName(a);
                        aNode.occurrance = nodeCounter++;
                        line.add(aNode);

                        buildTokens(commandPrefix, out, depth, line, attributes.getValue(a));

                        line.remove(aNode);
                    }
                }
            });

            for (int d = 1; d < depth.size(); d++)
                out.add(new Token(commandPrefix, CLOSE_GROUP, 0, 0, false));

            return out.toArray(new Token[out.size()]);
        } catch (final ParserConfigurationException e)
        {
            e.printStackTrace();
            return null;
        } catch (final SAXException e)
        {
            e.printStackTrace();
            return null;
        } catch (final IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
