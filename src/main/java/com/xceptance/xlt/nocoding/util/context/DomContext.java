package com.xceptance.xlt.nocoding.util.context;

import java.io.IOException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
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
 * The {@link Context} used in the Dom mode of the execution. Therefore, it extends <code>Context&lt;SgmlPage&gt;</code>
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
    public DomContext(final Context<SgmlPage> context)
    {
        super(context);
    }

    /**
     * Creates a new {@link DomContext}, with the provided {@link DataStorage} and configures the {@link XltWebClient}
     * according to the {@link XltProperties}
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
     * Creates a new {@link DomContext}, with a new {@link DataStorage} and configures the {@link XltWebClient} according to
     * the {@link XltProperties}
     * 
     * @param xltProperties
     *            The properties to use - normally {@link XltProperties#getInstance()}
     */
    public DomContext(final XltProperties xltProperties)
    {
        super(xltProperties);
    }

    /**
     * Gets the {@link SgmlPage}
     * 
     * @return
     */
    @Override
    public SgmlPage getPage()
    {
        return page;
    }

    /**
     * Sets the {@link SgmlPage}
     * 
     * @param sgmlPage
     */
    @Override
    public void setPage(final SgmlPage sgmlPage)
    {
        this.page = sgmlPage;
    }

    /**
     * Loads the {@link WebResponse} corresponding to the {@link WebRequest}. <br>
     * If {@link WebRequest#isXHR()} is <code>false</code>, it loads the {@link SgmlPage} and sets the WebResponse.<br>
     * If <code>WebRequest.isXHR()</code> is <code>true</code>, it only sets the WebResponse.
     * 
     * @param webRequest
     *            The request to send
     * @throws IOException
     * @throws FailingHttpStatusCodeException
     */
    @Override
    public void loadWebResponse(final WebRequest webRequest) throws FailingHttpStatusCodeException, IOException
    {
        // If the webRequest is not a Xhr
        if (!webRequest.isXHR())
        {
            // Load and set page
            setPage(this.getWebClient().getPage(webRequest));
            // Set webResponse
            setWebResponse(getPage().getWebResponse());
        }
        // If the webRequest is a Xhr
        else
        {
            // Loads the page, but only saves the webResponse
            setWebResponse(this.getWebClient().getPage(webRequest).getWebResponse());
        }
    }

    /**
     * Appends {@link #getPage()} to the result browser, if it is an instance of {@link HtmlPage}. <br>
     * Otherwise, it creates a new {@link LightWeightPage} and appends it.
     * 
     * @throws IOException
     * @throws FailingHttpStatusCodeException
     */
    @Override
    public void appendToResultBrowser() throws FailingHttpStatusCodeException, IOException
    {
        final String name = getWebClient().getTimerName();
        if (getPage() != null)
        {
            if (getPage() instanceof HtmlPage)
            {
                ((SessionImpl) Session.getCurrent()).getRequestHistory().add(name, (HtmlPage) getPage());
            }
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
