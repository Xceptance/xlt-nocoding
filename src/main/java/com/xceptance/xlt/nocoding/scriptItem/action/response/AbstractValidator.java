package com.xceptance.xlt.nocoding.scriptItem.action.response;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;

public abstract class AbstractValidator
{

    public abstract void validate(HtmlPage page) throws Exception;

    public abstract void validate(LightWeightPage page) throws Exception;

}
