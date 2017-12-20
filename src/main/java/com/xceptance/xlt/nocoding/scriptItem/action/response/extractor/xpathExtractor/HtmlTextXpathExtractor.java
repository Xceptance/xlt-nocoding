package com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.xpathExtractor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.AbstractExtractor;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.context.DomContext;
import com.xceptance.xlt.nocoding.util.context.LightWeightContext;

public class HtmlTextXpathExtractor extends AbstractExtractor
{
    private SgmlPage htmlPage;

    /**
     * Contains all the supported mime types. Supported mime types so far:
     * <ul>
     * <li>Html:
     * <ul>
     * <li>text/html
     * <li>text/application
     * </ul>
     * </ul>
     */
    static final Map<String, String> HEADERCONTENTTYPES = new HashMap<>();

    static final String HTML = "html";

    static final String TEXTHTML = "text/html";

    static final String TEXTAPPLICATION = "text/application";

    static
    {
        HEADERCONTENTTYPES.put(TEXTHTML, HTML);
        HEADERCONTENTTYPES.put(TEXTAPPLICATION, HTML);
    }

    public HtmlTextXpathExtractor(final String extractionExpression)
    {
        super(extractionExpression);
    }

    @Override
    public void execute(final Context context)
    {
        if (context instanceof LightWeightContext)
        {
            htmlPage = ((LightWeightContext) context).getSgmlPage(context.getWebResponse());
        }
        else if (context instanceof DomContext)
        {
            htmlPage = context.getSgmlPage();
        }
        else
        {
            throw new IllegalStateException("Context must be " + LightWeightContext.class.getSimpleName() + " or "
                                            + DomContext.class.getSimpleName() + " but is " + context.getClass().getSimpleName());
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
            final List<DomNode> htmlElements = htmlPage.getByXPath(xPath);

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
