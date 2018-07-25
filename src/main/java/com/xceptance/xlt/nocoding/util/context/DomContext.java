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
import com.xceptance.xlt.nocoding.util.resolver.VariableResolver;
import com.xceptance.xlt.nocoding.util.storage.DataStorage;

/**
 * The {@link Context} used in the Dom mode of the execution. Therefore, it extends <code>Context&lt;SgmlPage&gt;</code>
 * 
 * @author ckeiner
 */
public class DomContext extends Context<SgmlPage>
{
    /**
     * Calls {@link DomContext#DomContext(XltProperties, DataStorage)}, with a new {@link DataStorage}.
     * 
     * @param xltProperties
     *            The properties to use - normally {@link XltProperties#getInstance()}
     * @see Context#Context(XltProperties)
     */
    public DomContext(final XltProperties xltProperties)
    {
        super(xltProperties);
    }

    /**
     * Creates a new {@link DomContext}, with the provided {@link DataStorage}, creates a new {@link XltWebClient} and
     * {@link VariableResolver} and finally calls {@link Context#initialize()}.
     * 
     * @param xltProperties
     *            The properties to use - normally {@link XltProperties#getInstance()}
     * @param dataStorage
     *            The {@link DataStorage} you want to use
     * @see Context#Context(XltProperties, DataStorage)
     */
    public DomContext(final XltProperties xltProperties, final DataStorage dataStorage)
    {
        super(xltProperties, dataStorage);
    }

    /**
     * Creates a new {@link DomContext} out of the old {@link DomContext}
     * 
     * @param context
     *            The Context to copy the values from
     * @see Context#Context(Context)
     */
    public DomContext(final Context<SgmlPage> context)
    {
        super(context);
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
        // If getPage is null, which happens for all requests, that only have XHR or no requests as predecessor
        else
        {
            // Check if the webRequest was xhr
            if (getWebResponse().getWebRequest().isXHR())
            {
                // Append an empty page to the result browser
                ((SessionImpl) Session.getCurrent()).getRequestHistory().add(name);
            }
            // If it wasn't an XHR, build a LightWeightPage from the WebResponse
            else
            {
                ((SessionImpl) Session.getCurrent()).getRequestHistory()
                                                    .add(new LightWeightPageImpl(getWebResponse(), name, getWebClient()));
            }
        }
    }

    /**
     * Creates a new {@link DomContext} out of the current one.
     * 
     * @return A new <code>DomContext</code> via {@link #DomContext(Context)}
     */
    @Override
    public DomContext buildNewContext()
    {
        return new DomContext(this);
    }

}
