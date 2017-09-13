package com.xceptance.xlt.nocoding.scriptItem.action.response.stores;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;

public abstract class AbstractResponseStore
{

    public abstract void store(HtmlPage page) throws Exception;

    public abstract void store(LightWeightPage page) throws Exception;

}
