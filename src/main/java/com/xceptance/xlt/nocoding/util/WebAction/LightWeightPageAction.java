package com.xceptance.xlt.nocoding.util.WebAction;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.api.actions.AbstractLightWeightPageAction;
import com.xceptance.xlt.engine.XltWebClient;

public class LightWeightPageAction extends AbstractLightWeightPageAction
{

    private final WebRequest webRequest;

    private WebClient webClient;

    public LightWeightPageAction(final AbstractLightWeightPageAction previousAction, final String timerName, final WebRequest webRequest)
    {
        super(previousAction, timerName);
        this.webRequest = webRequest;
        this.webClient = getWebClient();
    }

    public LightWeightPageAction(final AbstractLightWeightPageAction previousAction, final String timerName, final WebRequest webRequest,
        final WebClient webClient)
    {
        super(previousAction, timerName);
        this.webRequest = webRequest;
        this.webClient = webClient;
    }

    @Override
    protected void execute() throws Exception
    {
        setLightWeightPage(((XltWebClient) getWebClient()).getLightWeightPage(webRequest));
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

}
