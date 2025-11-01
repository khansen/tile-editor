/*
*
*    Copyright (C) 2003 Kent Hansen.
*
*    This file is part of Tile Manipulator.
*
*    Tile Manipulator is free software; you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation; either version 2 of the License, or
*    (at your option) any later version.
*
*    Tile Manipulator is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*/

package tm.utils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class XMLParser {

    private static Document parseInternal(org.xml.sax.InputSource source, boolean validating)
        throws SAXException, SAXParseException, ParserConfigurationException, IOException {
        Document document = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(validating);
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Custom EntityResolver for DTDs inside JAR
            builder.setEntityResolver(new EntityResolver() {
                @Override
                public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                    if (systemId != null && systemId.endsWith("tmspec.dtd")) {
                        // Try to load tmspec.dtd from classpath
                        InputStream dtdStream = XMLParser.class.getClassLoader().getResourceAsStream("tmspec.dtd");
                        if (dtdStream != null) {
                            InputSource is = new InputSource(dtdStream);
                            is.setPublicId(publicId);
                            is.setSystemId(systemId);
                            return is;
                        }
                    }
                    // Default behavior
                    return null;
                }
            });

            builder.setErrorHandler(
                new org.xml.sax.ErrorHandler() {
                    @Override
                    public void fatalError(SAXParseException exception) throws SAXException {}
                    @Override
                    public void error(SAXParseException e) throws SAXParseException { throw e; }
                    @Override
                    public void warning(SAXParseException err) throws SAXParseException {
                        System.out.println("** Warning"
                            + ", line " + err.getLineNumber()
                            + ", uri " + err.getSystemId());
                        System.out.println("   " + err.getMessage());
                    }
                }
            );
            document = builder.parse(source);
        } catch (SAXParseException spe) {
            throw spe;
        } catch (SAXException sxe) {
            throw sxe;
        } catch (ParserConfigurationException pce) {
            throw pce;
        } catch (IOException ioe) {
            throw ioe;
        }
        return document;
    }

    public static Document parse(File file)
        throws SAXException, SAXParseException, ParserConfigurationException, IOException {
        return parseInternal(new org.xml.sax.InputSource(file.getAbsolutePath()), true);
    }

    public static Document parse(InputStream in)
        throws SAXException, SAXParseException, ParserConfigurationException, IOException {
        return parseInternal(new org.xml.sax.InputSource(in), false);
    }

    public static String getNodeValue(Node n) {
        String value = "";
        if (n != null) {
            NodeList children = n.getChildNodes();
            for(int j=0; j<children.getLength(); j++ ) {
                Node child = children.item(j);
                if(child.getNodeType() == Node.TEXT_NODE) {
                    value = value + child.getNodeValue();
                }
            }
        }
        return value;
    }   // getNodeValue()

}