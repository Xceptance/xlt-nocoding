package com.xceptance.xlt.nocoding.util;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.api.data.GeneralDataProvider;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.engine.XltWebClient;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;
import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.StorageUnit;
import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.duplicate.CookieStorage;
import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.duplicate.DuplicateStorage;
import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.duplicate.ParameterStorage;
import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.single.SingleStorage;
import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.single.StaticUrlStorage;
import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.unique.DefaultKeyValueStorage;
import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.unique.HeaderStorage;
import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.unique.UniqueStorage;
import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.unique.VariableStorage;
import com.xceptance.xlt.nocoding.util.variableResolver.VariableResolver;

/**
 * The context for the execution. Thus, the context has methods for handling the data storage, web client, resolving
 * variables, storing the current WebResponse and managing properties.
 * 
 * @author ckeiner
 */
public class Context
{
    protected final DataStorage dataStorage;

    protected final XltWebClient webClient;

    protected final VariableResolver resolver;

    protected WebResponse webResponse;

    protected NoCodingPropertyAdmin propertyAdmin;

    protected List<StorageUnit> storages;

    /**
     * Creates a new context, sets default Values in the dataStorage and configures the webClient according to the
     * xltProperties
     * 
     * @param xltProperties
     *            The properties to use - normally XltProperties.getInstance()
     */
    public Context(final XltProperties xltProperties)
    {
        this.dataStorage = new DataStorage();
        this.propertyAdmin = new NoCodingPropertyAdmin(xltProperties);
        this.webClient = new XltWebClient();
        this.resolver = new VariableResolver(GeneralDataProvider.getInstance());
        this.storages = new ArrayList<StorageUnit>();
        initialize();
    }

    /**
     * Creates a new context out of the old context
     * 
     * @param context
     */
    public Context(final Context context)
    {
        this.dataStorage = context.getDataStorage();
        this.webClient = context.getWebClient();
        this.resolver = context.getResolver();
        this.webResponse = context.getWebResponse();
        this.propertyAdmin = context.getPropertyAdmin();
        this.storages = context.getStorages();
    }

    /**
     * Initializes the {@link Context} by loading some default items and configuring the {@link XltWebClient}
     */
    public void initialize()
    {
        initStorages();
        configureWebClient();
    }

    /**
     * Initializes all {@link StorageUnit}s to the {@link #storages}.
     */
    private void initStorages()
    {
        getStorages().add(new CookieStorage());
        getStorages().add(new ParameterStorage());
        getStorages().add(new StaticUrlStorage());
        getStorages().add(new DefaultKeyValueStorage());
        getStorages().add(new HeaderStorage());
        getStorages().add(new VariableStorage());
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

    public List<StorageUnit> getStorages()
    {
        return this.storages;
    }

    /**
     * Gets the first {@link StorageUnit} that has the specified class.
     * 
     * @param classOfUnit
     *            The class of the {@link StorageUnit}
     * @return The {@link StorageUnit} with the class classOfUnit
     */
    private StorageUnit getStorageUnit(final Class classOfUnit)
    {
        StorageUnit unit = null;
        for (final StorageUnit storageUnit : storages)
        {
            if (classOfUnit.isInstance(storageUnit))
            {
                unit = storageUnit;
                break;
            }
        }
        return unit;
    }

    /**
     * Configures the webClient as specified in the config, thus dis/enabling javascript, css, etc.
     */
    private void configureWebClient()
    {
        getPropertyAdmin().configWebClient(webClient);
    }

}
