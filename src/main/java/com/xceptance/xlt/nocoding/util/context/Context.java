package com.xceptance.xlt.nocoding.util.context;

import java.io.IOException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.api.data.GeneralDataProvider;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.engine.XltWebClient;
import com.xceptance.xlt.nocoding.util.NoCodingPropertyAdmin;
import com.xceptance.xlt.nocoding.util.resolver.VariableResolver;
import com.xceptance.xlt.nocoding.util.storage.DataStorage;
import com.xceptance.xlt.nocoding.util.storage.unit.DuplicateStorage;
import com.xceptance.xlt.nocoding.util.storage.unit.SingleStorage;
import com.xceptance.xlt.nocoding.util.storage.unit.unique.DefaultKeyValueStorage;
import com.xceptance.xlt.nocoding.util.storage.unit.unique.UniqueSingleStorage;
import com.xceptance.xlt.nocoding.util.storage.unit.unique.UniqueStorage;

/**
 * A template for the context during the execution. Thus, it has methods for handling the {@link DataStorage},
 * {@link XltWebClient}, resolving variables, creating a {@link WebResponse} and accessing properties.
 * 
 * @param T
 *            The type of the loaded page to load
 * @author ckeiner
 */
public abstract class Context<T>
{
    /**
     * The storage for default items, and variables
     */
    protected final DataStorage dataStorage;

    /**
     * The {@link WebClient} for sending {@link WebRequest}s
     */
    protected final XltWebClient webClient;

    /**
     * The resolver that resolves variables
     */
    protected final VariableResolver resolver;

    /**
     * The last {@link WebResponse}, that was received
     */
    protected WebResponse webResponse;

    /**
     * Provides access to the properties
     */
    protected NoCodingPropertyAdmin propertyAdmin;

    /**
     * The index of the script item
     */
    protected int actionIndex;

    /**
     * The page corresponding to the {@link #webResponse}
     */
    protected T page;

    /**
     * Creates a new {@link Context#Context(XltProperties, DataStorage)}, with a new {@link DataStorage}.
     * 
     * @param xltProperties
     *            The properties to use - normally {@link XltProperties#getInstance()}
     */
    public Context(final XltProperties xltProperties)
    {
        this(xltProperties, new DataStorage());
    }

    /**
     * Creates a new {@link Context}, with the provided {@link DataStorage}, creates a new {@link XltWebClient} and
     * {@link VariableResolver} and finally calls {@link #initialize()}.
     * 
     * @param xltProperties
     *            The properties to use - normally {@link XltProperties#getInstance()}
     * @param dataStorage
     *            The {@link DataStorage} you want to use
     */
    public Context(final XltProperties xltProperties, final DataStorage dataStorage)
    {
        this.dataStorage = dataStorage;
        this.propertyAdmin = new NoCodingPropertyAdmin(xltProperties);
        this.webClient = new XltWebClient();
        this.resolver = new VariableResolver(GeneralDataProvider.getInstance());
        initialize();
    }

    /**
     * Creates a new {@link Context} out of the old {@link Context}
     * 
     * @param context
     *            The context that should be copied
     */
    protected Context(final Context<T> context)
    {
        this.dataStorage = context.getDataStorage();
        this.webClient = context.getWebClient();
        this.resolver = context.getResolver();
        this.webResponse = context.getWebResponse();
        this.propertyAdmin = context.getPropertyAdmin();
        this.actionIndex = 0;
        this.page = context.page;

    }

    /**
     * Initializes the {@link Context} by configuring the {@link XltWebClient}
     */
    protected void initialize()
    {
        configureWebClient();
    }

    /**
     * Gets the {@link DataStorage}
     * 
     * @return
     */
    public DataStorage getDataStorage()
    {
        return dataStorage;
    }

    /**
     * @return The {@link XltWebClient} that is used in this context
     */
    public XltWebClient getWebClient()
    {
        return webClient;
    }

    /**
     * @return The {@link WebResponse} of the current request
     */
    public WebResponse getWebResponse()
    {
        return webResponse;
    }

    /**
     * Sets {@link #webResponse}
     * 
     * @param webResponse
     */
    public void setWebResponse(final WebResponse webResponse)
    {
        this.webResponse = webResponse;
    }

    /**
     * Gets the {@link VariableResolver}
     * 
     * @return
     */
    public VariableResolver getResolver()
    {
        return resolver;
    }

    /**
     * @return The {@link NoCodingPropertyAdmin} that handles property related issues.
     */
    public NoCodingPropertyAdmin getPropertyAdmin()
    {
        return propertyAdmin;
    }

    /**
     * Gets the index of the last action
     * 
     * @return
     */
    public int getActionIndex()
    {
        return actionIndex;
    }

    /**
     * Sets the index as new {@link #actionIndex}
     * 
     * @param actionIndex
     *            The index to set {@link #actionIndex} to
     */
    public void setActionIndex(final int actionIndex)
    {
        this.actionIndex = actionIndex;
    }

    /**
     * Gets the page.
     * 
     * @return The page corresponding to the {@link #webResponse}
     */
    public T getPage()
    {
        return page;
    };

    /**
     * Sets the page.
     * 
     * @param page
     *            The page corresponding to the {@link #webResponse}
     */
    public void setPage(final T page)
    {
        this.page = page;
    };

    /*
     * Data Storage
     */

    public SingleStorage getDefaultCookies()
    {
        return getDataStorage().getDefaultCookies();
    }

    public DuplicateStorage getDefaultParameters()
    {
        return getDataStorage().getDefaultParameters();
    }

    public SingleStorage getDefaultStatics()
    {
        return getDataStorage().getDefaultStatics();
    }

    public UniqueSingleStorage getDefaultHeaders()
    {
        return getDataStorage().getDefaultHeaders();
    }

    public UniqueStorage getVariables()
    {
        return getDataStorage().getVariables();
    }

    public DefaultKeyValueStorage getDefaultItems()
    {
        return getDataStorage().getDefaultItems();
    }

    /*
     * Resolver
     */

    /**
     * Resolves every variable of a specified string. If a variable is specified and no value for it can be found, the
     * variable doesn't get resolved
     * 
     * @param toResolve
     *            The string which might have variables
     * @return A string with the resolved variables
     */
    public String resolveString(final String toResolve)
    {
        String resolvedString = null;
        if (toResolve != null)
        {
            resolvedString = resolver.resolveString(toResolve, this);
        }
        return resolvedString;
    }

    /*
     * Property Methods
     */

    public String getPropertyByKey(final String key)
    {
        return getPropertyAdmin().getPropertyByKey(key);
    }

    public String getPropertyByKey(final String key, final String defaultValue)
    {
        return getPropertyAdmin().getPropertyByKey(key, defaultValue);
    }

    public int getPropertyByKey(final String key, final int defaultValue)
    {
        return getPropertyAdmin().getPropertyByKey(key, defaultValue);
    }

    public boolean getPropertyByKey(final String key, final boolean defaultValue)
    {
        return getPropertyAdmin().getPropertyByKey(key, defaultValue);
    }

    public long getPropertyByKey(final String key, final long defaultValue)
    {
        return getPropertyAdmin().getPropertyByKey(key, defaultValue);
    }

    /**
     * Configures the webClient as specified in the properties, thus dis-/enabling javascript, css, etc.
     */
    private void configureWebClient()
    {
        getPropertyAdmin().configWebClient(webClient);
    }

    /**
     * Defines how the {@link WebResponse} is loaded
     * 
     * @param webRequest
     *            The {@link WebRequest} for the <code>WebResponse</code>
     * @throws IOException
     * @throws FailingHttpStatusCodeException
     */
    public abstract void loadWebResponse(final WebRequest webRequest) throws FailingHttpStatusCodeException, IOException;

    /**
     * Appends the {@link #webResponse} to the Xlt Result Browser
     * 
     * @throws IOException
     * @throws FailingHttpStatusCodeException
     */
    public abstract void appendToResultBrowser() throws FailingHttpStatusCodeException, IOException;

    /**
     * Creates a new Context out of the current one
     * 
     * @return Context with the type T
     */
    public abstract Context<T> buildNewContext();

}
