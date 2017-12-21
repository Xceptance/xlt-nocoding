package com.xceptance.xlt.nocoding.util.context;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.api.engine.Session;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.engine.LightWeightPageImpl;
import com.xceptance.xlt.engine.SessionImpl;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;

public class DomContext extends Context
{

    public DomContext(final Context context)
    {
        super(context);
    }

    public DomContext(final XltProperties xltProperties, final DataStorage dataStorage)
    {
        super(xltProperties, dataStorage);
    }

    public DomContext(final XltProperties xltProperties)
    {
        super(xltProperties);
    }

    @Override
    public LightWeightPage getLightWeightPage()
    {
        throw new IllegalStateException("Cannot get LightWeightPage in DomMode");
    }

    @Override
    public void setLightWeightPage(final LightWeightPage lightWeightPage)
    {
        throw new IllegalStateException("Cannot set LightWeightPage in DomMode");
    }

    @Override
    public SgmlPage getSgmlPage()
    {
        return sgmlPage;
    }

    @Override
    public void setSgmlPage(final SgmlPage sgmlPage)
    {
        this.sgmlPage = sgmlPage;
    }

    @Override
    public void loadWebResponse(final WebRequest webRequest) throws Exception
    {
        if (!webRequest.isXHR())
        {
            setSgmlPage(this.getWebClient().getPage(webRequest));
            setWebResponse(getSgmlPage().getWebResponse());
        }
        else
        {
            webResponse = this.getWebClient().getPage(webRequest).getWebResponse();
        }
    }

    @Override
    public void appendToResultBrowser() throws Exception
    {
        final String name = getWebClient().getTimerName();
        if (getSgmlPage() instanceof HtmlPage)
        {
            ((SessionImpl) Session.getCurrent()).getRequestHistory().add(name, (HtmlPage) sgmlPage);
        }
        else
        {
            ((SessionImpl) Session.getCurrent()).getRequestHistory().add(new LightWeightPageImpl(getWebResponse(), name, getWebClient()));

        }
    }

    @Override
    public Context buildNewContext()
    {
        return new DomContext(this);
    }

}
