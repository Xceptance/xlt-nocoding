package com.xceptance.xlt.nocoding.util.WebAction;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.api.actions.AbstractLightWeightPageAction;
import com.xceptance.xlt.nocoding.util.ThrowingConsumer;

public class WebAction extends AbstractLightWeightPageAction
{

    // TODO We want to have the opportunity to set a html page and a lightweight page

    private final WebRequest webRequest;

    private WebResponse webResponse;

    private WebClient webClient;

    private ThrowingConsumer<WebAction> function;

    public WebAction(final String timerName, final WebRequest webRequest, final WebClient webClient,
        final ThrowingConsumer<WebAction> function)
    {
        super(timerName);
        this.webRequest = webRequest;
        this.webClient = webClient;
        this.function = function;
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

}
