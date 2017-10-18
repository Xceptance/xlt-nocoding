package com.xceptance.xlt.nocoding.scriptItem.action;

import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.api.util.XltException;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.engine.XltWebClient;
import com.xceptance.xlt.nocoding.util.Context;
import com.xceptance.xlt.nocoding.util.webAction.WebAction;

// TODO
public class DomAction extends Action
{
    private HtmlPage htmlPage;

    private final long waitingTime = 30000;

    public DomAction(final Request request, final List<AbstractActionItem> actionItems)
    {
        super(request, actionItems);
    }

    @Override
    public void execute(final Context context) throws Throwable
    {
        final WebAction action = new WebAction(getRequest().getName(), context, getRequest(), getActionItems(),
                                               (final WebAction webAction) -> {
                                                   try
                                                   {
                                                       doExecute(webAction);
                                                   }
                                                   catch (final Throwable e)
                                                   {
                                                       XltLogger.runTimeLogger.error("Execution Step failed");
                                                       e.printStackTrace();
                                                       throw new Exception(e);
                                                   }
                                               });

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

    /**
     * This method uses an action as parameter and defines how to execute a WebAction. This is used in a lambda method in
     * the execute-Method.
     * 
     * @param action
     * @throws Exception
     */
    public void doExecute(final WebAction action) throws Throwable
    {
        final Context context = action.getContext();
        final List<AbstractActionItem> actionItems = action.getActionItems();
        action.getRequest().execute(context);

        if (actionItems != null && !actionItems.isEmpty())
        {
            for (final AbstractActionItem actionItem : actionItems)
            {
                actionItem.execute(context);
            }
        }
    }

}
