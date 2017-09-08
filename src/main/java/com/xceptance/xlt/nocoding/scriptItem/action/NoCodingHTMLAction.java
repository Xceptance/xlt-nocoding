package com.xceptance.xlt.nocoding.scriptItem.action;

import java.net.URL;
import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.api.actions.AbstractWebAction;
import com.xceptance.xlt.api.engine.Session;
import com.xceptance.xlt.api.util.XltException;
import com.xceptance.xlt.engine.SessionImpl;
import com.xceptance.xlt.engine.XltWebClient;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.AbstractSubrequest;

public class NoCodingHTMLAction extends NoCodingAction
{
    private HtmlPage htmlPage;

    private final long waitingTime = 30000;

    public NoCodingHTMLAction(final AbstractWebAction previousAction, final String timerName, final Request request,
        final Response response, final List<AbstractSubrequest> subrequests)
    {
        super(previousAction, timerName, request, response, subrequests);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void preValidate() throws Exception
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected void execute() throws Exception
    {
        // Fires a standard request
        final URL url = new URL(this.request.getUrl());
        final WebRequest webRequest = createWebRequestSettings(url, this.request.getMethod(), EMPTY_PARAMETER_LIST);
        final Page result = getWebClient().getPage(webRequest);

        setHtmlPage(waitForPageIsComplete(result, waitingTime));

        // Validate response
        this.response.validate(getHtmlPage());
        dumpPage(getHtmlPage());
    }

    @Override
    protected void postValidate() throws Exception
    {
        // TODO Auto-generated method stub
    }

    private void dumpPage(final HtmlPage htmlPage)
    {
        if (htmlPage != null)
        {
            final String timerName = ((XltWebClient) getWebClient()).getTimerName();
            ((SessionImpl) Session.getCurrent()).getRequestHistory().add(timerName, htmlPage);
        }
    }

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

    public HtmlPage getHtmlPage()
    {
        return htmlPage;
    }

    public void setHtmlPage(final HtmlPage htmlPage)
    {
        this.htmlPage = htmlPage;
    }

}
