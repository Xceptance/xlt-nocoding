package com.xceptance.xlt.nocoding.util;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.api.data.GeneralDataProvider;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.engine.XltWebClient;
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

    public void initialize()
    {
        getDataStorage().loadDefaultConfig();
        configureWebClient();
    }

    public DataStorage getDataStorage()
    {
        return dataStorage;
    }

    public void setWebClient(final XltWebClient webClient)
    {
        // TODO This okay? We don't want to change our webClient afaik
        if (this.webClient == null)
        {
            this.webClient = webClient;
        }
    }

    public XltWebClient getWebClient()
    {
        return webClient;
    }

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

    public NoCodingPropertyAdmin getPropertyAdmin()
    {
        return propertyAdmin;
    }

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
     * Removes a key value pair from the configItems. If a fallback value was specified for the configItem, it is going to
     * be used from then on
     * 
     * @param key
     *            The variable name/key to the corresponding value
     */
    public void removeConfigItem(final String key)
    {
        getDataStorage().removeConfigItem(key);
    }

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

}
