package com.xceptance.xlt.nocoding.scriptItem.action.response.stores;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;

public class CookieStore extends AbstractResponseStore
{

    private final String cookie;

    public CookieStore(final String cookie)
    {
        this.cookie = cookie;
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
