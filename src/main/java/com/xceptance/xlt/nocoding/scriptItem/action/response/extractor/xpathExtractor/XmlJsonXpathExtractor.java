package com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.xpathExtractor;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.AbstractExtractor;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Extractor for json and xml content types
 * 
 * @author ckeiner
 */
public class XmlJsonXpathExtractor extends AbstractExtractor
{
    private XPath xPath;

    private Document xmlInputSource;

    static final HashMap<String, String> HEADERCONTENTTYPES = new HashMap<>();

    static final String JSON = "json";

    static final String XML = "xml";

    static
    {
        // Json Types
        HEADERCONTENTTYPES.put("application/json", JSON);
        HEADERCONTENTTYPES.put("text/json", JSON);
        HEADERCONTENTTYPES.put("text/x-json", JSON);

        // XML Types
        HEADERCONTENTTYPES.put("text/xml", XML);
        HEADERCONTENTTYPES.put("application/xml", XML);
    }

    public XmlJsonXpathExtractor(final String extractionExpression)
    {
        super(extractionExpression);
    }

    @Override
    public void execute(final Context<?> context)
    {
        try
        {
            getResult().addAll(getByXPathFromInputSource(context));
        }
        catch (final Exception e)
        {
            throw new IllegalArgumentException("Failed to fetch Elements: " + e.getMessage(), e);
        }
    }

    private List<String> getByXPathFromInputSource(final Context<?> context)
        throws XPathExpressionException, ParserConfigurationException, SAXException, IOException
    {
        loadContentFromWebResponseIfNecessary(context);
        final NodeList nodeList = createNodeListByXPathFromInputSource(getExtractionExpression());
        final List<String> resultList = createStringListFromNodeList(nodeList);
        return resultList;
    }

    /**
     * Sets {@link #xmlInputSource} and {@link #xPath} if they are null
     * 
     * @param context
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    private void loadContentFromWebResponseIfNecessary(final Context<?> context) throws ParserConfigurationException, SAXException, IOException
    {
        if (xmlInputSource == null)
        {
            loadXMLSourceFromWebResponse(context);
        }
        if (xPath == null)
        {
            XltLogger.runTimeLogger.debug("Creating new XPath");
            final XPathFactory xpathFactory = XPathFactory.newInstance();
            xPath = xpathFactory.newXPath();
        }
    }

    private void loadXMLSourceFromWebResponse(final Context<?> context) throws ParserConfigurationException, SAXException, IOException
    {
        XltLogger.runTimeLogger.debug("Loading content from WebResponse");
        final String contentType = HEADERCONTENTTYPES.get(context.getWebResponse().getContentType());

        final String bodyContent = context.getWebResponse().getContentAsString();

        if (JSON.equals(contentType))
        {
            xmlInputSource = createXMLSourceFromJson(bodyContent);
        }
        else if (XML.equals(contentType))
        {
            xmlInputSource = createXMLSourceFromXML(bodyContent);
        }
        else
        {
            throw new IllegalArgumentException("Expected " + JSON + ", or " + XML + " but was " + contentType);
        }
    }

    private Document createXMLSourceFromJson(final String json) throws ParserConfigurationException, SAXException, IOException
    {
        XltLogger.runTimeLogger.debug("Converting Json Content to XML");
        String xmlString;
        xmlString = org.json.XML.toString(new JSONObject(json));
        xmlString = "<json>" + xmlString + "</json>";

        final Document document = createDocumentFromXmlString(xmlString);

        return document;
    }

    private Document createDocumentFromXmlString(final String xmlString) throws SAXException, IOException, ParserConfigurationException
    {
        final InputSource source = new InputSource(new StringReader(xmlString));
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        final DocumentBuilder db = dbf.newDocumentBuilder();
        final Document document = db.parse(source);
        return document;
    }

    private Document createXMLSourceFromXML(final String xmlString) throws SAXException, IOException, ParserConfigurationException
    {
        XltLogger.runTimeLogger.debug("Loading XML Content");
        final Document document = createDocumentFromXmlString(xmlString);
        return document;
    }

    private NodeList createNodeListByXPathFromInputSource(final String xPath)
    {
        XltLogger.runTimeLogger.debug("Getting Elements by XPath: " + xPath);
        NodeList list = new NodeList()
        {
            @Override
            public Node item(final int index)
            {
                return null;
            }

            @Override
            public int getLength()
            {
                return 0;
            }
        };
        try
        {
            list = (NodeList) this.xPath.compile(xPath).evaluate(xmlInputSource, XPathConstants.NODESET);
        }
        catch (final Exception e)
        {
            XltLogger.runTimeLogger.debug("Failed to get Elements: " + e.getMessage());
        }
        return list;
    }

    private List<String> createStringListFromNodeList(final NodeList nodeList)
    {
        final List<String> resultList = new ArrayList<>();

        for (int i = 0; i < nodeList.getLength(); i++)
        {
            final Node n = nodeList.item(i);
            resultList.add(n.getTextContent());
        }
        return resultList;
    }

}
