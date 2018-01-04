package com.xceptance.xlt.nocoding.util.context;

import java.io.IOException;
import java.util.Map;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.api.engine.Session;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.engine.LightWeightPageImpl;
import com.xceptance.xlt.engine.SessionImpl;
import com.xceptance.xlt.engine.XltWebClient;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;

/**
 * The {@link Context} used in the LightWeight mode of the execution.
 * 
 * @author ckeiner
 */
public class LightWeightContext extends Context<LightWeightPage>
{
    /**
     * The SgmlPage
     */
    protected SgmlPage sgmlPage;

    /**
     * Creates a new {@link LightWeightContext} out of the old {@link LightWeightContext}
     * 
     * @param context
     */
    public LightWeightContext(final Context<?> context)
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
    public LightWeightContext(final XltProperties xltProperties, final DataStorage dataStorage)
    {
        super(xltProperties, dataStorage);
    }

    /**
     * Creates a new {@link LightWeightContext}, sets default Values in the {@link DataStorage} and configures the
     * {@link XltWebClient} according to the {@link XltProperties}
     * 
     * @param xltProperties
     *            The properties to use - normally {@link XltProperties#getInstance()}
     */
    public LightWeightContext(final XltProperties xltProperties)
    {
        super(xltProperties);
    }

    /**
     * Gets the sgmlPage if it is not null. Else, it builds the sgmlPage from the {@link WebResponse}.
     * 
     * @return {@link #sgmlPage} - The {@link Map} that maps {@link WebResponse} to {@link SgmlPage}
     */
    public SgmlPage getSgmlPage()
    {
        if (sgmlPage == null)
        {
            XltLogger.runTimeLogger.debug("Generating new SgmlPage...");
            Page page;
            try
            {
                page = getWebClient().loadWebResponseInto(getWebResponse(), getWebClient().getCurrentWindow());
                if (page instanceof SgmlPage)
                {
                    sgmlPage = (SgmlPage) page;
                    XltLogger.runTimeLogger.debug("SgmlPage built");
                }
            }
            catch (FailingHttpStatusCodeException | IOException e)
            {
                throw new IllegalStateException("Cannot convert WebResponse to SgmlPage.");
            }
        }
        return sgmlPage;
    }

    public void setSgmlPage(final SgmlPage sgmlPage)
    {
        this.sgmlPage = sgmlPage;
    }

    /**
     * @return The current {@link LightWeightPage}
     */
    @Override
    public LightWeightPage getPage()
    {
        return page;
    }

    /**
     * Sets the {@link LightWeightPage}
     */
    @Override
    public void setPage(final LightWeightPage lightWeightPage)
    {
        this.page = lightWeightPage;
    }

    /**
     * Loads the {@link WebResponse} corresponding to the {@link WebRequest}. <br>
     * If {@link WebRequest#isXHR()} is <code>false</code>, it loads the {@link WebResponse} via
     * {@link XltWebClient#getLightWeightPage(WebRequest)}. <br>
     * If {@link WebRequest#isXHR()} is <code>true</code>, it loads the {@link WebResponse} via
     * {@link XltWebClient#loadWebResponse(WebRequest)}.
     * 
     * @param webResponse
     * @throws IOException
     * @throws FailingHttpStatusCodeException
     */
    @Override
    public void loadWebResponse(final WebRequest webRequest) throws FailingHttpStatusCodeException, IOException
    {
        setSgmlPage(null);
        if (!webRequest.isXHR())
        {
            this.setPage(this.getWebClient().getLightWeightPage(webRequest));
            setWebResponse(this.getPage().getWebResponse());
        }
        else
        {
            setWebResponse(this.getWebClient().loadWebResponse(webRequest));
        }
    }

    /**
     * Appends the {@link #getLightWeightPage()} to the result browser, if {@link #getLightWeightPage()} is not
     * <code>null</code>. <br>
     * Else creates a new {@link LightWeightPage}.
     */
    @Override
    public void appendToResultBrowser() throws Exception
    {
        final String name = getWebClient().getTimerName();
        if (getPage() != null)
        {
            ((SessionImpl) Session.getCurrent()).getRequestHistory().add(getPage());
        }
        else
        {
            ((SessionImpl) Session.getCurrent()).getRequestHistory().add(new LightWeightPageImpl(getWebResponse(), name, getWebClient()));

        }
    }

    /**
     * @return {@link #LightWeightContext(Context)}
     */
    @Override
    public Context<?> buildNewContext()
    {
        return new LightWeightContext(this);
    }

}
