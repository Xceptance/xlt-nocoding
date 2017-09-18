package com.xceptance.xlt.nocoding.util.WebAction;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.api.actions.AbstractWebAction;
import com.xceptance.xlt.nocoding.util.ThrowingConsumer;

public class WebAction extends AbstractWebAction
{
    /**
     * The request defined in the request block
     */
    private final WebRequest webRequest;

    /**
     * The requests defined in the subrequests
     */
    private final List<WebRequest> subrequests;

    /**
     * The responses to the subrequests
     */
    private final List<WebResponse> subrequestResponses;

    private WebResponse webResponse;

    private WebClient webClient;

    private ThrowingConsumer<WebAction> function;

    public WebAction(final String timerName, final WebRequest webRequest, final List<WebRequest> subrequests, final WebClient webClient,
        final ThrowingConsumer<WebAction> function)
    {
        // TODO Unfortunately we cannot set the webClient before the timer name, so there will be many webClient assigned
        super(timerName);
        this.webRequest = webRequest;
        this.subrequests = subrequests;
        this.webClient = webClient;
        this.function = function;
        this.subrequestResponses = new ArrayList<WebResponse>(this.subrequests.size());
    }

    @Override
    protected void execute() throws Exception
    {
        function.accept(this);
    }

    @Override
    protected void postValidate() throws Exception
    {
    }

    @Override
    public void preValidate() throws Exception
    {
    }

    public WebClient getWebClient()
    {
        WebClient webClient;
        if (this.webClient != null)
        {
            webClient = this.webClient;
        }
        else
        {
            webClient = super.getWebClient();
        }
        return webClient;
    }

    public void setWebClient(final WebClient webClient)
    {
        this.webClient = webClient;
    }

    public WebRequest getWebRequest()
    {
        return webRequest;
    }

    public ThrowingConsumer<WebAction> getFunction()
    {
        return function;
    }

    public void setFunction(final ThrowingConsumer<WebAction> function)
    {
        this.function = function;
    }

    public WebResponse getWebResponse()
    {
        return webResponse;
    }

    public void setWebResponse(final WebResponse webResponse)
    {
        this.webResponse = webResponse;
    }

    public List<WebRequest> getSubrequests()
    {
        return subrequests;
    }

    public List<WebResponse> getSubrequestResponses()
    {
        return subrequestResponses;
    }

}
