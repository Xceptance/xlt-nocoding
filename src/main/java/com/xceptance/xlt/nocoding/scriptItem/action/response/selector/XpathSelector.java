package com.xceptance.xlt.nocoding.scriptItem.action.response.selector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.util.Context;

public class XpathSelector extends AbstractSelector
{
    private SgmlPage htmlPage;

    private WebResponse webResponse;

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

    // TODO reicht eigentlich wenn htmlunit instance of sgmlpage stimmt oder so
    static final String HTML = "html";

    static final String TEXTHTML = "text/html";

    static final String TEXTAPPLICATION = "text/application";

    static
    {
        HEADERCONTENTTYPES.put(TEXTHTML, HTML);
        HEADERCONTENTTYPES.put(TEXTAPPLICATION, HTML);
    }

    public XpathSelector(final String selectionExpression)
    {
        super(selectionExpression);
    }

    @Override
    public void execute(final Context context)
    {
        throw new NotImplementedException("Not yet implemented!");

        // webResponse = context.getWebResponse();
        // if (isParsable(webResponse))
        // {
        // // TODO Do this in the context, don't forget to delete when next action
        // if (htmlPage == null)
        // {
        // try
        // {
        // final Page page = context.getWebClient().loadWebResponseInto(context.getWebResponse(),
        // context.getWebClient().getCurrentWindow());
        // if (page instanceof SgmlPage)
        // {
        // htmlPage = (SgmlPage) page;
        // }
        // }
        // catch (FailingHttpStatusCodeException | IOException e)
        // {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        //
        // // htmlPage = new HtmlPage(webResponse, context.getWebClient().loadWebResponseInto(webResponse, webWindow));
        // }
        // final List<DomNode> htmlElements = getHtmlElementListByXPath(getSelectionExpression());
        // for (final DomNode htmlElement : htmlElements)
        // {
        // final String elementAsString = getStringFromHtmlElement(htmlElement);
        // XltLogger.runTimeLogger.debug("Found Element: " + elementAsString);
        // addResult(elementAsString);
        // }
        // }

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

    private boolean isParsable(final WebResponse webResponse)
    {
        final String contentType = webResponse.getContentType();
        return HEADERCONTENTTYPES.containsKey(contentType);
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
