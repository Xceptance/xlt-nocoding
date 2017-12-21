package com.xceptance.xlt.nocoding.util.context;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.api.data.GeneralDataProvider;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.engine.XltWebClient;
import com.xceptance.xlt.nocoding.util.NoCodingPropertyAdmin;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;
import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.DuplicateStorage;
import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.SingleStorage;
import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.uniqueStorage.DefaultKeyValueStorage;
import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.uniqueStorage.UniqueStorage;
import com.xceptance.xlt.nocoding.util.variableResolver.VariableResolver;

/**
 * The context for the execution. Thus, the context has methods for handling the {@link DataStorage},
 * {@link XltWebClient}, resolving variables, creating the current {@link WebResponse} and accessing properties.
 * 
 * @author ckeiner
 */
public abstract class Context
{
    protected final DataStorage dataStorage;

    protected final XltWebClient webClient;

    protected final VariableResolver resolver;

    protected WebResponse webResponse;

    protected NoCodingPropertyAdmin propertyAdmin;

    /**
     * LightWeightPage
     */
    protected LightWeightPage lightWeightPage;

    /**
     * LightWeightPage
     */
    protected SgmlPage sgmlPage;

    /**
     * Creates a new {@link Context}, sets default Values in the {@link DataStorage} and configures the {@link XltWebClient}
     * according to the {@link XltProperties}
     * 
     * @param xltProperties
     *            The properties to use - normally {@link XltProperties#getInstance()}
     */
    public Context(final XltProperties xltProperties)
    {
        this(xltProperties, new DataStorage());
    }

    /**
     * Creates a new {@link Context}, sets default Values in the {@link DataStorage} and configures the {@link XltWebClient}
     * according to the {@link XltProperties}
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
     */
    protected Context(final Context context)
    {
        this.dataStorage = context.getDataStorage();
        this.webClient = context.getWebClient();
        this.resolver = context.getResolver();
        this.webResponse = context.getWebResponse();
        this.propertyAdmin = context.getPropertyAdmin();
    }

    /**
     * Initializes the {@link Context} by loading some default items and configuring the {@link XltWebClient}
     */
    public void initialize()
    {
        configureWebClient();
    }

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

    public abstract LightWeightPage getLightWeightPage();

    public abstract void setLightWeightPage(final LightWeightPage lightWeightPage);

    public abstract SgmlPage getSgmlPage();

    public abstract void setSgmlPage(final SgmlPage sgmlPage);

    /*
     * Data Storage
     */

    public DuplicateStorage getDefaultCookies()
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

    public UniqueStorage getDefaultHeaders()
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
     * Configures the webClient as specified in the config, thus dis-/enabling javascript, css, etc.
     */
    private void configureWebClient()
    {
        getPropertyAdmin().configWebClient(webClient);
    }

    /**
     * Defines how the {@link WebResponse} is loaded
     * 
     * @param webRequest
     *            The {@link WebRequest} for the {@link WebResponse}
     * @throws Exception
     */
    public abstract void loadWebResponse(final WebRequest webRequest) throws Exception;

    /**
     * Defines how the {@link #webResponse} is appended to the Xlt Result Browser
     * 
     * @throws Exception
     */
    public abstract void appendToResultBrowser() throws Exception;

    /**
     * Defines how a new {@link Context} gets created out of the old one
     * 
     * @return
     */
    public abstract Context buildNewContext();

}
