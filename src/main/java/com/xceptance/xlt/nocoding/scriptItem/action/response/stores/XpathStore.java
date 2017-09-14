package com.xceptance.xlt.nocoding.scriptItem.action.response.stores;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;

public class XpathStore extends AbstractResponseStore
{

    private final String xpath;

    public XpathStore(final String variableName, final String xpath)
    {
        super(variableName);
        this.xpath = xpath;
    }

    @Override
    public void store(final HtmlPage page) throws Exception
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void store(final LightWeightPage page) throws Exception
    {
        // TODO Auto-generated method stub

    }

}
