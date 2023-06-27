package com.xceptance.xlt.nocoding.command.action.response.extractor.xpath;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.htmlunit.SgmlPage;
import org.htmlunit.html.DomNode;
import org.htmlunit.xml.XmlPage;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.context.DomContext;
import com.xceptance.xlt.nocoding.util.context.LightWeightContext;
import com.xceptance.xlt.nocoding.util.context.RequestContext;

/**
 * XPath Extractor for "html/text" content type
 *
 * @author ckeiner
 */
public class HtmlXmlXpathExtractorExecutor extends XpathExtractorExecutor
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
        // Html Types
        HEADERCONTENTTYPES.put(TEXTHTML, HTML);
        HEADERCONTENTTYPES.put(TEXTAPPLICATION, HTML);

        // XML Types
        HEADERCONTENTTYPES.put("text/xml", XML);
        HEADERCONTENTTYPES.put("application/xml", XML);

    }

    /**
     * Creates a new {@link HtmlXmlXpathExtractorExecutor} for Html and Xml content types
     *
     * @param extractionExpression
     *            The {@link #extractionExpression} to use for extracting
     * @see XpathExtractorExecutor#XpathExtractorExecutor(String)
     */
    public HtmlXmlXpathExtractorExecutor(final String extractionExpression)
    {
        super(extractionExpression);
    }

    @Override
    public void execute(final Context<?> context)
    {
        // Get the Sgml page if the context is LightWeightContext
        if (context instanceof LightWeightContext)
        {
            sgmlPage = ((LightWeightContext) context).getSgmlPage();
        }
        else if (context instanceof RequestContext)
        {
            sgmlPage = ((RequestContext) context).getSgmlPage();
        }
        // Else, simply get the page
        else if (context instanceof DomContext)
        {
            sgmlPage = ((DomContext) context).getPage();
        }
        else
        {
            throw new IllegalStateException("Context must be " + LightWeightContext.class.getSimpleName() + ", "
                                            + RequestContext.class.getSimpleName() + " or " + DomContext.class.getSimpleName() + " but is "
                                            + context.getClass().getSimpleName());
        }

        // Verify, that if the sgmlPage is a XmlPage, that it has content
        if (sgmlPage instanceof XmlPage && ((XmlPage) sgmlPage).getXmlDocument() == null)
        {
            throw new IllegalStateException("Xml page doesn't have a document");
        }

        // Get the Html elements via xPath
        final List<DomNode> htmlElements = getHtmlElementListByXPath(getExtractionExpression());
        // Store each element as string in result
        for (final DomNode htmlElement : htmlElements)
        {
            final String elementAsString = getStringFromHtmlElement(htmlElement);
            XltLogger.runTimeLogger.debug("Found Element: " + elementAsString);
            addResult(elementAsString);
        }
    }

    /**
     * Gets the TextContent of a {@link DomNode}
     *
     * @param node
     *            The node from which to extract the text content
     * @return
     */
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

    /**
     * Gets all matching elements from the xPath expression from {@link #sgmlPage}
     *
     * @param xPath
     *            The xPath expression with which to extract information
     * @return A list of {@link DomNode}s that can be extracted via xPath
     */
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
