package com.xceptance.xlt.nocoding.util.WebAction;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.api.actions.AbstractLightWeightPageAction;
import com.xceptance.xlt.engine.XltWebClient;

public class LightWeightPageAction extends AbstractLightWeightPageAction
{

    private final WebRequest webRequest;

    // TODO WebClient noch übergeben maybe
    public LightWeightPageAction(final AbstractLightWeightPageAction previousAction, final String timerName, final WebRequest webRequest)
    {
        super(previousAction, timerName);
        this.webRequest = webRequest;
        // TODO Auto-generated constructor stub
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

}
