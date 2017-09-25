package com.xceptance.xlt.nocoding.scriptItem.action;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.api.util.XltException;
import com.xceptance.xlt.engine.XltWebClient;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.AbstractSubrequest;
import com.xceptance.xlt.nocoding.util.PropertyManager;
import com.xceptance.xlt.nocoding.util.webAction.WebAction;

public class DomAction extends Action
{
    private HtmlPage htmlPage;

    private final long waitingTime = 30000;

    public DomAction(final Request request, final Response response, final List<AbstractSubrequest> subrequests)
    {
        super(request, response, subrequests);
    }

    @Override
    public void execute(final PropertyManager propertyManager) throws Throwable
    {
        final List<WebRequest> requestsOfSubrequest = null;
        final WebAction action = new WebAction(this.getRequest().getName(), this.getRequest().buildWebRequest(propertyManager),
                                               requestsOfSubrequest, propertyManager.getWebClient(), (final WebAction x) -> doExecute(x));

        action.run();
    }

    public HtmlPage getHtmlPage()
    {
        return htmlPage;
    }

    public void setHtmlPage(final HtmlPage htmlPage)
    {
        this.htmlPage = htmlPage;
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

    public void doExecute(final WebAction action) throws Exception
    {
        // TODO Extract waiting time
        final long waitingTime = 30000;
        final Page result = action.getWebClient().getPage(action.getWebRequest());
        setHtmlPage(waitForPageIsComplete(result, waitingTime));

    }

}
