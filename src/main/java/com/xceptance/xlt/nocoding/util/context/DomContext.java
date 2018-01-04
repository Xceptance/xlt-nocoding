package com.xceptance.xlt.nocoding.util.context;

import java.io.IOException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.api.engine.Session;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.engine.LightWeightPageImpl;
import com.xceptance.xlt.engine.SessionImpl;
import com.xceptance.xlt.engine.XltWebClient;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;

/**
 * The {@link Context} used in the Dom mode of the execution.
 * 
 * @author ckeiner
 */
public class DomContext extends Context<SgmlPage>
{

    /**
     * Creates a new {@link DomContext} out of the old {@link DomContext}
     * 
     * @param context
     */
    public DomContext(final Context<?> context)
    {
        super(context);
    }

    /**
     * Creates a new {@link LightWeightContext}, sets default Values in the {@link DataStorage} and configures the
     * {@link XltWebClient} according to the {@link XltProperties}
     * 
     * @param xltProperties
     *            The properties to use - normally {@link XltProperties#getInstance()}
     * @param dataStorage
     *            The {@link DataStorage} you want to use
     */
    public DomContext(final XltProperties xltProperties, final DataStorage dataStorage)
    {
        super(xltProperties, dataStorage);
    }

    /**
     * Creates a new {@link DomContext}, sets default Values in the {@link DataStorage} and configures the
     * {@link XltWebClient} according to the {@link XltProperties}
     * 
     * @param xltProperties
     *            The properties to use - normally {@link XltProperties#getInstance()}
     */
    public DomContext(final XltProperties xltProperties)
    {
        super(xltProperties);
    }

    /**
     * @return The current {@link SgmlPage}
     */
    @Override
    public SgmlPage getPage()
    {
        return page;
    }

    /**
     * Sets the {@link SgmlPage}
     */
    @Override
    public void setPage(final SgmlPage sgmlPage)
    {
        this.page = sgmlPage;
    }

    /**
     * Loads the {@link WebResponse} corresponding to the {@link WebRequest}. <br>
     * If {@link WebRequest#isXHR()} is <code>false</code>, it loads the {@link WebResponse} via
     * {@link XltWebClient#getPage(WebRequest)}. <br>
     * If {@link WebRequest#isXHR()} is <code>true</code>, it loads the {@link WebResponse} via
     * {@link XltWebClient#getPage(WebRequest)} and {@link Page#getWebResponse()}.
     * 
     * @param webResponse
     * @throws IOException
     * @throws FailingHttpStatusCodeException
     */
    @Override
    public void loadWebResponse(final WebRequest webRequest) throws Exception
    {
        if (!webRequest.isXHR())
        {
            setPage(this.getWebClient().getPage(webRequest));
            setWebResponse(getPage().getWebResponse());
        }
        else
        {
            setWebResponse(this.getWebClient().getPage(webRequest).getWebResponse());
        }
    }

    /**
     * Appends {@link #getSgmlPage()} to the result browser, if {@link #getSgmlPage()} is not <code>null</code>. <br>
     * Else creates a new {@link LightWeightPage} and appends it.
     */
    @Override
    public void appendToResultBrowser() throws Exception
    {
        final String name = getWebClient().getTimerName();
        if (getPage() instanceof HtmlPage)
        {
            ((SessionImpl) Session.getCurrent()).getRequestHistory().add(name, (HtmlPage) getPage());
        }
        else
        {
            ((SessionImpl) Session.getCurrent()).getRequestHistory().add(new LightWeightPageImpl(getWebResponse(), name, getWebClient()));

        }
    }

    /**
     * @return {@link #DomContext(Context)}
     */
    @Override
    public DomContext buildNewContext()
    {
        return new DomContext(this);
    }

}
