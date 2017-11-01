package com.xceptance.xlt.nocoding.util;

import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.api.data.GeneralDataProvider;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.engine.XltWebClient;
import com.xceptance.xlt.nocoding.scriptItem.StoreItem;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;
import com.xceptance.xlt.nocoding.util.variableResolver.VariableResolver;

/**
 * The context for the execution. Thus, the context has methods for handling the data storage, web client, resolving
 * variables, storing the current WebResponse and managing properties
 * 
 * @author ckeiner
 */
public class Context
{
    protected final DataStorage dataStorage;

    protected XltWebClient webClient;

    protected final VariableResolver resolver;

    protected WebResponse webResponse;

    protected NoCodingPropertyAdmin propertyAdmin;

    /**
     * Creates a new context, sets default Values in the dataStorage and configures the webClient according to the
     * xltProperties
     * 
     * @param xltProperties
     *            The properties to use - normally XltProperties.getInstance()
     * @param dataStorage
     *            The data storage to use
     */
    public Context(final XltProperties xltProperties, final DataStorage dataStorage)
    {
        this.propertyAdmin = new NoCodingPropertyAdmin(xltProperties);
        this.dataStorage = dataStorage;
        this.webClient = new XltWebClient();
        this.resolver = new VariableResolver(GeneralDataProvider.getInstance());
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
    }

    /**
     * Initializes the {@link Context} by loading some default items and configuring the {@link XltWebClient}
     */
    public void initialize()
    {
        loadDefaultConfig();
        configureWebClient();
    }

    /**
     * @return The {@link DataStorage} that has all variables, configItems/defaultItems in this context.
     */
    public DataStorage getDataStorage()
    {
        return dataStorage;
    }

    /**
     * Sets a new {@link XltWebClient} if no {@link XltWebClient} is set yet
     * 
     * @param webClient
     *            The {@link XltWebClient} you want to attach.
     */
    public void setWebClient(final XltWebClient webClient)
    {
        if (this.webClient == null)
        {
            this.webClient = webClient;
        }
        else
        {
            XltLogger.runTimeLogger.warn("WebClient already attached. Therefore, no new WebClient is set.");
        }
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
     * Data Storage Methods
     */

    /**
     * Stores the key value pair as variable in the data storage
     * 
     * @param key
     *            The variable name/key with which one accesses the value
     * @param value
     *            The value of the variable
     */
    public void storeVariable(final String key, final String value)
    {
        getDataStorage().storeVariable(key, value);
    }

    /**
     * Stores a key value pair as configItem, thus as "default" item.
     * 
     * @param key
     *            The variable name/key with which one accesses the value
     * @param value
     *            The value of the variable
     */
    public void storeConfigItem(final String key, final String value)
    {
        getDataStorage().storeConfigItem(key, value);
    }

    /**
     * Stores a key value pair as configItem, thus as "default" item. If the configItem gets deleted, the fallback value is
     * the set for the configItem
     * 
     * @param key
     *            The variable name/key with which one accesses the value
     * @param value
     *            The value of the variable
     * @param fallback
     *            The value that is used when the key gets deleted
     */
    public void storeConfigItem(final String key, final String value, final String fallback)
    {
        getDataStorage().storeConfigItem(key, value, fallback);
    }

    /**
     * Stores a key value pair as default header
     * 
     * @param header
     *            The name of the header
     * @param value
     *            The value of the header
     */
    public void storeDefaultHeader(final String header, final String value)
    {
        getDataStorage().addDefaultHeader(header, value);
    }

    /**
     * Stores a key value pair as default parameter
     * 
     * @param parameter
     *            The name of the parameter
     * @param value
     *            The value of the parameter
     */
    public void storeDefaultParameter(final String parameter, final String value)
    {
        getDataStorage().addDefaultParameter(parameter, value);
    }

    /**
     * Stores a url as default static request
     * 
     * @param url
     *            The url of the static request as {@link String}
     */
    public void storeDefaultStatic(final String url)
    {
        getDataStorage().addDefaultStatic(url);
    }

    /**
     * Stores a variable definition as default {@link StoreItem}
     * 
     * @param variableName
     *            The name of the variable
     * @param value
     *            The value of the variable
     */
    public void storeDefaultStoreItem(final String variableName, final String value)
    {
        getDataStorage().addDefaultStoreItem(variableName, value);
    }

    /**
     * Removes a key value pair from the configItems. If a fallback value was specified for the configItem, it is going to
     * be used from then on
     * 
     * @param key
     *            The variable name/key to the corresponding value
     */
    public void deleteConfigItem(final String key)
    {
        getDataStorage().removeConfigItem(key);
    }

    /**
     * Removes the specified default header
     * 
     * @param header
     *            The name of the header
     */
    public void deleteDefaultHeader(final String header)
    {
        getDataStorage().deleteDefaultHeader(header);
    }

    /**
     * Removes the specified default parameter
     * 
     * @param parameter
     *            The name of the parameter
     */
    public void deleteDefaultParameter(final String parameter)
    {
        getDataStorage().deleteDefaultParameter(parameter);
    }

    /**
     * Remov es a url out of the default static requests
     * 
     * @param url
     *            The url of the static request as {@link String}
     */
    public void deleteDefaultStatic(final String url)
    {
        getDataStorage().deleteDefaultStatic(url);
    }

    /**
     * Removes the specified variable from the default {@link StoreItem}
     * 
     * @param variableName
     *            The name of the variable
     */
    public void deleteDefaultStoreItem(final String variableName)
    {
        getDataStorage().deleteDefaultStoreItem(variableName);
    }

    /**
     * Removes all default header
     */
    public void deleteDefaultHeader()
    {
        getDataStorage().deleteDefaultHeader();
    }

    /**
     * Removes all default parameter
     * 
     * @param parameter
     *            The name of the parameter
     */
    public void deleteDefaultParameter()
    {
        getDataStorage().deleteDefaultParameter();
    }

    /**
     * Deletes all static requests
     */
    public void deleteDefaultStatic()
    {
        getDataStorage().deleteDefaultStatic();
    }

    /**
     * Deletes all default {@link StoreItem}
     */
    public void deleteDefaultStoreItem()
    {
        getDataStorage().deleteDefaultStoreItem();
    }

    /*
     * Data Storage Getter
     */

    /**
     * Returns the config item with the specified key. If the key cannot be found, the method checks if a fallback is
     * provided. If it still cannot be found, the method returns null.
     * 
     * @param key
     *            The key you want to search for
     * @return String - The value of the key; Null if the key is neither in the default values nor in configItems
     */
    public String getConfigItemByKey(final String key)
    {
        return getDataStorage().getConfigItemByKey(key);
    }

    public Map<String, String> getDefaultHeaders()
    {
        return getDataStorage().getHeaders();
    }

    public Map<String, String> getDefaultParameters()
    {
        return getDataStorage().getParameters();
    }

    /**
     * Deletes all static requests
     */
    public List<String> getDefaultStatic()
    {
        return getDataStorage().getStaticUrls();
    }

    /**
     * Deletes all default {@link StoreItem}
     */
    public List<StoreItem> getDefaultStoreItem()
    {
        return getDataStorage().getStoreItems();
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
     * Configures the webClient as specified in the config, thus dis/enabling javascript, css, etc.
     */
    private void configureWebClient()
    {
        getPropertyAdmin().configWebClient(webClient);
    }

    /**
     * Loads the default definitions
     */
    private void loadDefaultConfig()
    {
        getDataStorage().loadDefaultConfig();
    }

}
