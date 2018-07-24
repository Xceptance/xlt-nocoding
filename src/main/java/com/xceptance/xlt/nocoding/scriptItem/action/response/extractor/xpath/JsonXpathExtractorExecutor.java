package com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.xpath;

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

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Xpath Extractor for json content types
 * 
 * @author ckeiner
 */
public class JsonXpathExtractorExecutor extends XpathExtractorExecutor
{
    private XPath xPath;

    private Document xmlInputSource;

    static final HashMap<String, String> HEADERCONTENTTYPES = new HashMap<>();

    static final String JSON = "json";

    static
    {
        // Json Types
        HEADERCONTENTTYPES.put("application/json", JSON);
        HEADERCONTENTTYPES.put("text/json", JSON);
        HEADERCONTENTTYPES.put("text/x-json", JSON);
    }

    /**
     * Creates a new {@link JsonXpathExtractorExecutor} for Html and Xml content types
     * 
     * @param extractionExpression
     *            The {@link #extractionExpression} to use for extracting
     * @see XpathExtractorExecutor#XpathExtractorExecutor(String)
     */
    public JsonXpathExtractorExecutor(final String extractionExpression)
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

    /**
     * Creates a {@link Document} from the {@link WebResponse}, then extracts a {@link NodeList} via the xpath expression,
     * and finally creates a list of Strings out of it
     * 
     * @param context
     * @return A list of strings that match the xpath expression
     */
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
     */
    private void loadContentFromWebResponseIfNecessary(final Context<?> context)
        throws ParserConfigurationException, SAXException, IOException
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

    /**
     * Sets {@link #xmlInputSource}. How the {@link Document} is created, depends on the content type.
     * 
     * @param context
     */
    private void loadXMLSourceFromWebResponse(final Context<?> context) throws ParserConfigurationException, SAXException, IOException
    {
        XltLogger.runTimeLogger.debug("Loading content from WebResponse");
        final String contentType = HEADERCONTENTTYPES.get(context.getWebResponse().getContentType());

        final String bodyContent = context.getWebResponse().getContentAsString();

        if (JSON.equals(contentType))
        {
            xmlInputSource = createXMLSourceFromJson(bodyContent);
        }
        else
        {
            throw new IllegalArgumentException("Expected " + JSON + ", " + " but was " + contentType);
        }
    }

    /**
     * Parses Json to Xml and creates a {@link Document} out of it.
     * 
     * @param json
     *            The String with the Json content
     * @return The Json Content as Document
     */
    private Document createXMLSourceFromJson(final String json) throws ParserConfigurationException, SAXException, IOException
    {
        XltLogger.runTimeLogger.debug("Converting Json Content to XML");
        // Convert Json to Xml
        String xmlString = org.json.XML.toString(new JSONObject(json));
        // Add json tags to the xmlString
        xmlString = "<json>" + xmlString + "</json>";

        // Create the document from the xml string
        final Document document = createDocumentFromXmlString(xmlString);
        // Return the document
        return document;
    }

    /**
     * Creates a {@link Document} from the xml string
     * 
     * @param xmlString
     *            The String with the xml content
     * @return The xml string as Document
     */
    private Document createDocumentFromXmlString(final String xmlString) throws SAXException, IOException, ParserConfigurationException
    {
        final InputSource source = new InputSource(new StringReader(xmlString));
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        final DocumentBuilder db = dbf.newDocumentBuilder();
        final Document document = db.parse(source);
        return document;
    }

    /**
     * Extracts a {@link NodeList} from {@link #xmlInputSource} via the supplied xPath expression
     * 
     * @param xPath
     *            The xPath expression with which to extract the nodes
     * @return A list of nodes that match the xPath expression
     */
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

    /**
     * Gets the text content from the {@link NodeList}.
     * 
     * @param nodeList
     *            The <code>NodeList</code> from which to extract the text content
     * @return A list of String with the extracted text content
     */
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
