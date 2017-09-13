package com.xceptance.xlt.nocoding.util.WebAction;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.api.actions.AbstractHtmlPageAction;
import com.xceptance.xlt.api.util.XltException;
import com.xceptance.xlt.engine.XltWebClient;

public class HtmlPageAction extends AbstractHtmlPageAction
{
    private final WebRequest webRequest;

    public HtmlPageAction(final AbstractHtmlPageAction previousAction, final String timerName, final WebRequest webRequest)
    {
        super(previousAction, timerName);
        this.webRequest = webRequest;
    }

    @Override
    protected void execute() throws Exception
    {
        // TODO Extract waiting time
        final long waitingTime = 30000;
        final Page result = getWebClient().getPage(webRequest);
        setHtmlPage(waitForPageIsComplete(result, waitingTime));

    }

    @Override
    protected void postValidate() throws Exception
    {
    }

    @Override
    public void preValidate() throws Exception
    {
    }

    // TODO Ask if i can just copy this method or if there will be a loadByWebRequest in XLT
    private HtmlPage waitForPageIsComplete(final Page page, final long waitingTime)
    {
        // wait for any JavaScript background thread to finish
        if (page instanceof SgmlPage)
        {
            final XltWebClient webClient = (XltWebClient) ((SgmlPage) page).getWebClient();
            webClient.waitForBackgroundThreads(page.getEnclosingWindow().getTopWindow().getEnclosedPage(), waitingTime);
        }

        // something might have changed, including a reload via location
        final Page enclosedPage = page.getEnclosingWindow().getTopWindow().getEnclosedPage();
        final HtmlPage newHtmlPage;

        // check whether the server indeed returned HTML content
        if (enclosedPage instanceof HtmlPage)
        {
            // yes
            newHtmlPage = (HtmlPage) enclosedPage;

            // check for any new static content to load
            ((XltWebClient) newHtmlPage.getWebClient()).loadNewStaticContent(newHtmlPage);
        }
        else
        {
            // no, the server returned unexpected content (e.g. plain text in a 404 message)
            throw new XltException("The server response could not be parsed as HTML.");
        }

        // Feature #471: API: Make the network data available for validation
        // collectAndSetNetworkData();

        return newHtmlPage;
    }

}
