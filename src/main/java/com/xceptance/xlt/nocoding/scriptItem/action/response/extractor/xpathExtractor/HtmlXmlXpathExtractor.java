package com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.xpathExtractor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.context.DomContext;
import com.xceptance.xlt.nocoding.util.context.LightWeightContext;

/**
 * XPath Extractor for "html/text" content type
 * 
 * @author ckeiner
 */
public class HtmlXmlXpathExtractor extends XpathExtractorExecutor
{

    private SgmlPage sgmlPage;

    /**
     * Contains all the supported types. Supported types so far:
     * <ul>
     * <li>Html:
     * <ul>
     * <li>text/html
     * <li>text/application
     * </ul>
     * <li>Xml:
     * <ul>
     * <li>text/xml
     * <li>application/xml
     * </ul>
     * </ul>
     */
    static final Map<String, String> HEADERCONTENTTYPES = new HashMap<>();

    static final String HTML = "html";

    static final String TEXTHTML = "text/html";

    static final String TEXTAPPLICATION = "text/application";

    static final String XML = "xml";

    static
    {
        HEADERCONTENTTYPES.put(TEXTHTML, HTML);
        HEADERCONTENTTYPES.put(TEXTAPPLICATION, HTML);

        // XML Types
        HEADERCONTENTTYPES.put("text/xml", XML);
        HEADERCONTENTTYPES.put("application/xml", XML);

    }

    public HtmlXmlXpathExtractor(final String extractionExpression)
    {
        super(extractionExpression);
    }

    @Override
    public void execute(final Context<?> context)
    {
        if (context instanceof LightWeightContext)
        {
            sgmlPage = ((LightWeightContext) context).getSgmlPage();
        }
        else if (context instanceof DomContext)
        {
            sgmlPage = ((DomContext) context).getPage();
        }
        else
        {
            throw new IllegalStateException("Context must be " + LightWeightContext.class.getSimpleName() + " or "
                                            + DomContext.class.getSimpleName() + " but is " + context.getClass().getSimpleName());
        }

        if (sgmlPage instanceof XmlPage && ((XmlPage) sgmlPage).getXmlDocument() == null)
        {
            throw new IllegalStateException("Xml page doesn't have a document");
        }

        final List<DomNode> htmlElements = getHtmlElementListByXPath(getExtractionExpression());
        for (final DomNode htmlElement : htmlElements)
        {
            final String elementAsString = getStringFromHtmlElement(htmlElement);
            XltLogger.runTimeLogger.debug("Found Element: " + elementAsString);
            addResult(elementAsString);
        }
    }

    private String getStringFromHtmlElement(final DomNode node)
    {
        try
        {
            final String elementAsString = node.getTextContent();
            return elementAsString;
        }
        catch (final Exception e)
        {
            throw new IllegalArgumentException("Failed to parse DomNode: " + node.getLocalName() + " of type: " + node.getNodeType()
                                               + " to String because: " + e.getMessage());
        }
    }

    private List<DomNode> getHtmlElementListByXPath(final String xPath)
    {
        XltLogger.runTimeLogger.debug("Getting Elements by XPath: " + xPath);

        try
        {
            final List<DomNode> htmlElements = sgmlPage.getByXPath(xPath);

            if (htmlElements.isEmpty())
            {
                XltLogger.runTimeLogger.debug("No Elements found!, XPath: " + xPath);
            }

            return htmlElements;
        }
        catch (final Exception e)
        {
            throw new IllegalArgumentException("Failed to get Elements by XPath: " + xPath + ", Because: " + e.getMessage());
        }
    }

}