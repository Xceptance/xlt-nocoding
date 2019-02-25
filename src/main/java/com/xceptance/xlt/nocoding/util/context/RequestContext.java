package com.xceptance.xlt.nocoding.util.context;

import java.io.IOException;

import org.apache.commons.lang3.NotImplementedException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import com.xceptance.xlt.api.engine.Session;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.engine.SessionImpl;
import com.xceptance.xlt.engine.XltWebClient;
import com.xceptance.xlt.nocoding.util.resolver.VariableResolver;
import com.xceptance.xlt.nocoding.util.storage.DataStorage;

/**
 * The {@link Context} used in the Request mode of the execution. Since it doesn't do anything special, it's generic
 * type is {@link Void}.
 *
 * @author ckeiner
 */
public class RequestContext extends Context<Void>
{

    /**
     * The SgmlPage
     */
    protected SgmlPage sgmlPage;

    /**
     * Calls {@link RequestContext#LightWeightContext(XltProperties, DataStorage)}, with a new {@link DataStorage}.
     *
     * @param xltProperties
     *            The properties to use - normally {@link XltProperties#getInstance()}
     * @see Context#Context(XltProperties)
     */
    public RequestContext(final XltProperties xltProperties)
    {
        super(xltProperties);
    }

    /**
     * Creates a new {@link RequestContext}, with the provided {@link DataStorage}, creates a new {@link XltWebClient}
     * and {@link VariableResolver} and finally calls {@link Context#initialize()}.
     *
     * @param xltProperties
     *            The properties to use - normally {@link XltProperties#getInstance()}
     * @param dataStorage
     *            The {@link DataStorage} you want to use
     * @see Context#Context(XltProperties, DataStorage)
     */
    public RequestContext(final XltProperties xltProperties, final DataStorage dataStorage)
    {
        super(xltProperties, dataStorage);
    }

    /**
     * Creates a new {@link RequestContext} out of the old {@link RequestContext}
     *
     * @param context
     *            The Context to copy the values from
     * @see Context#Context(Context)
     */
    public RequestContext(final Context<Void> context)
    {
        super(context);
    }

    /**
     * Gets the {@link SgmlPage} if it isn't <code>null</code>. Else, it builds the SgmlPage from the
     * {@link WebResponse}.
     *
     * @return
     */
    public SgmlPage getSgmlPage()
    {
        // If the sgmlPage is null and therefore wasn't loaded already
        if (sgmlPage == null)
        {
            // Generate a new sgmlPage
            XltLogger.runTimeLogger.debug("Generating new SgmlPage...");
            Page page;
            try
            {
                // Load the WebResponse into the window of the web client
                page = getWebClient().loadWebResponseInto(getWebResponse(), getWebClient().getCurrentWindow());
                // If the built page is an instance of SgmlPage
                if (page instanceof SgmlPage)
                {
                    if (page instanceof XmlPage && ((XmlPage) page).getXmlDocument() == null)
                    {
                        throw new IllegalStateException("Faulty WebResponse, the page doesn't have child nodes.");
                    }
                    // Set the sgmlPage
                    setSgmlPage((SgmlPage) page);
                    XltLogger.runTimeLogger.debug("SgmlPage built");
                }
            }
            catch (FailingHttpStatusCodeException | IOException e)
            {
                throw new IllegalStateException("Cannot convert WebResponse to SgmlPage.");
            }
        }
        // Return the sgmlPage
        return sgmlPage;
    }

    public void setSgmlPage(final SgmlPage sgmlPage)
    {
        this.sgmlPage = sgmlPage;
    }

    /**
     * Loads the {@link WebResponse} corresponding to the {@link WebRequest}.<br>
     *
     * @param webRequest
     *            The webRequest that should be send
     * @throws IOException
     * @throws FailingHttpStatusCodeException
     */
    @Override
    public void loadWebResponse(final WebRequest webRequest) throws FailingHttpStatusCodeException, IOException
    {
        // Reset the sgmlPage
        setSgmlPage(null);

        setWebResponse(getWebClient().loadWebResponse(webRequest));
    }

    /**
     * Appends the {@link #getPage()} to the result browser, if it is not <code>null</code>.<br>
     * Otherwise, it creates a new {@link LightWeightPage}.
     *
     * @throws IOException
     * @throws FailingHttpStatusCodeException
     */
    @Override
    public void appendToResultBrowser() throws FailingHttpStatusCodeException, IOException
    {
        ((SessionImpl) Session.getCurrent()).getRequestHistory().add(getWebClient().getTimerName());
    }

    /**
     * Creates a new {@link RequestContext} out of the current one.
     *
     * @return A new <code>LightWeightContext</code> via {@link #LightWeightContext(Context)}
     */
    @Override
    public Context<Void> buildNewContext()
    {
        return new RequestContext(this);
    }

    /**
     * Gets the page.
     *
     * @return The page corresponding to the {@link #webResponse}
     */
    @Override
    public Void getPage()
    {
        return null;
    };

    /**
     * Sets the page.
     *
     * @param page
     *            The page corresponding to the {@link #webResponse}
     */
    @Override
    public void setPage(final Void page)
    {
        throw new NotImplementedException("This method cannot be called.");
    };

}
