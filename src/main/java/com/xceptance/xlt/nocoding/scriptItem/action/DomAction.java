package com.xceptance.xlt.nocoding.scriptItem.action;

import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.AbstractSubrequest;
import com.xceptance.xlt.nocoding.util.PropertyManager;
import com.xceptance.xlt.nocoding.util.WebAction.HtmlPageAction;

public class DomAction extends Action
{
    private HtmlPage htmlPage;

    private final long waitingTime = 30000;

    public DomAction(final Request request, final Response response, final List<AbstractSubrequest> subrequests)
    {
        super(request, response, subrequests);
    }

    @Override
    public void execute(final PropertyManager propertyManager) throws Exception
    {
        final HtmlPageAction action = new HtmlPageAction(null, this.getRequest().getName(), this.getRequest().buildWebRequest());
        this.htmlPage = action.getHtmlPage();
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
