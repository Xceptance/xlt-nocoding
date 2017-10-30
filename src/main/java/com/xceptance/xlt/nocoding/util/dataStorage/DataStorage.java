package com.xceptance.xlt.nocoding.util.dataStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.scriptItem.StoreItem;
import com.xceptance.xlt.nocoding.util.Constants;

public class DataStorage
{
    private final Map<String, String> variables;

    private final Map<String, String> configItem;

    private final Map<String, String> headers;

    private final Map<String, String> parameters;

    private final List<StoreItem> storeItems;

    private final List<String> staticUrls;

    private final Map<String, String> fallback;

    public DataStorage()
    {
        this(new HashMap<String, String>(), new HashMap<String, String>());
    }

    public DataStorage(final Map<String, String> variables, final Map<String, String> configItem)
    {
        this(variables, configItem, new HashMap<String, String>());
    }

    public DataStorage(final Map<String, String> variables, final Map<String, String> configItem, final Map<String, String> fallback)
    {
        this.variables = variables;
        this.configItem = configItem;
        this.fallback = fallback;
        this.headers = new HashMap<String, String>();
        this.parameters = new HashMap<String, String>();
        this.staticUrls = new ArrayList<String>();
        this.storeItems = new ArrayList<StoreItem>();
    }

    /**
     * Stores a variable with the specified key
     * 
     * @param key
     *            The key you want to store your item with
     * @param value
     *            The value you want to store
     */
    public void storeVariable(final String key, final String value)
    {
        this.variables.put(key, value);
    }

    /**
     * Stores a configuration item with the default value null
     * 
     * @param key
     *            The key you want to store your item with
     * @param value
     *            The value you want to store
     */
    public void storeConfigItem(final String key, final String value)
    {
        storeConfigItem(key, value, null);
    }

    /**
     * Stores a configuration item with a default value of fallback (in the case that a default value gets deleted)
     * 
     * @param key
     *            The key you want to store your item with
     * @param value
     *            The value you want to store
     * @param fallback
     *            The default value of the specified key, in case the ConfigItem gets deleted
     */
    public void storeConfigItem(final String key, final String value, final String fallback)
    {
        this.configItem.put(key, value);
        if (fallback != null)
        {
            this.fallback.put(key, fallback);
        }
    }

    /**
     * Returns the variable with the specified key
     * 
     * @param key
     *            The key the stored item has
     * @return The value associated with the specified key
     */
    public String getVariableByKey(final String key)
    {
        return this.variables.get(key);
    }

    /**
     * Returns the config item with the specified key. If the key cannot be found, the method searches in the default
     * values. If it still cannot be found, the method returns null.
     * 
     * @param key
     *            The key you want to search for
     * @return The value of the key; Null if the key is neither in the default values nor in configItems
     */
    public String getConfigItemByKey(final String key)
    {
        String value = null;
        // Look for the key in the config items
        if (getConfigItem().containsKey(key))
        {
            value = getConfigItem().get(key);
        }
        // Search for the key in the default values
        else if (fallback.containsKey(key))
        {
            value = fallback.get(key);
        }
        return value;
    }

    /**
     * Returns all variables as string
     * 
     * @return All variables as string
     */
    public String getAllVariables()
    {
        return variables.toString();
    }

    /**
     * Returns all ConfigItems as string
     * 
     * @return All ConfigItems as string
     */
    public String getAllConfigItems()
    {
        return configItem.toString();
    }

    /**
     * Places some default values in the ConfigItems
     */
    public void loadDefaultConfig()
    {
        // this.configItem = DefaultValue.defaultValues;
        this.storeConfigItem(Constants.METHOD, DefaultValue.METHOD, DefaultValue.METHOD);
        this.storeConfigItem(Constants.XHR, DefaultValue.XHR, DefaultValue.XHR);
        this.storeConfigItem(Constants.ENCODEPARAMETERS, DefaultValue.ENCODEPARAMETERS, DefaultValue.ENCODEPARAMETERS);
        this.storeConfigItem(Constants.ENCODEBODY, DefaultValue.ENCODEBODY, DefaultValue.ENCODEBODY);
        this.storeConfigItem(Constants.HTTPCODE, DefaultValue.HTTPCODE, DefaultValue.HTTPCODE);
    }

    /**
     * Return variables as Map<String, String>
     * 
     * @return Variables as Map<String, String>
     */
    public Map<String, String> getVariables()
    {
        return variables;
    }

    /**
     * Return ConfigItems as Map<String, String>
     * 
     * @return ConfigItems as Map<String, String>
     */
    public Map<String, String> getConfigItem()
    {
        return configItem;
    }

    /**
     * Return fallback/default values as Map<String, String>
     * 
     * @return fallback/default values as Map<String, String>
     */
    public Map<String, String> getFallback()
    {
        return fallback;
    }

    public Map<String, String> getHeaders()
    {
        return headers;
    }

    public Map<String, String> getParameters()
    {
        return parameters;
    }

    public List<String> getStaticUrls()
    {
        return staticUrls;
    }

    public List<StoreItem> getStoreItems()
    {
        return storeItems;
    }

    /**
     * Removes the specified config item and returns it
     * 
     * @param key
     *            The key for the config item
     * @return The value of the removed config item
     */
    public String removeConfigItem(final String key)
    {
        return getConfigItem().remove(key);
    }

    /**
     * Adds a header to the default headers
     * 
     * @param header
     *            The header as {@link NameValuePair}
     */
    public void addDefaultHeader(final String key, final String value)
    {
        getHeaders().put(key, value);
    }

    /**
     * Adds a parameter to the default parameters
     * 
     * @param parameter
     *            The parameter as {@link NameValuePair}
     */
    public void addDefaultParameter(final String key, final String value)
    {
        getParameters().put(key, value);
    }

    /**
     * Adds a url to the default static urls
     * 
     * @param url
     *            The url as {@link String}
     */
    public void addDefaultStatic(final String url)
    {
        getStaticUrls().add(url);
    }

    /**
     * Adds a {@link StoreItem} to the default {@link StoreItem}-List
     * 
     * @param variableName
     *            The name of the variable
     * @param value
     *            The value of the variable
     */
    public void addDefaultStoreItem(final String variableName, final String value)
    {
        getStoreItems().add(new StoreItem(variableName, value));
    }

    /**
     * Deletes all default headers
     */
    public void deleteDefaultHeader()
    {
        getHeaders().clear();
    }

    /**
     * Deletes all default parameters
     */
    public void deleteDefaultParameter()
    {
        getParameters().clear();
    }

    /**
     * Deletes all default static request urls
     */
    public void deleteDefaultStatic()
    {
        getStaticUrls().clear();
    }

    /**
     * Deletes all default {@link StoreItem}
     */
    public void deleteDefaultStoreItem()
    {
        getStoreItems().clear();
    }

    /**
     * Deletes a single default headers
     */
    public void deleteDefaultHeader(final String header)
    {
        getHeaders().remove(header);
    }

    /**
     * Deletes a single default parameters
     */
    public void deleteDefaultParameter(final String parameter)
    {
        getParameters().remove(parameter);
    }

    /**
     * Deletes a single default static request urls
     */
    public void deleteDefaultStatic(final String url)
    {
        getStaticUrls().remove(getStaticUrls().indexOf(url));
    }

    /**
     * Deletes a single default {@link StoreItem}
     */
    public void deleteDefaultStoreItem(final String variableName)
    {
        StoreItem toRemove = null;
        for (final StoreItem storeItem : getStoreItems())
        {
            if (storeItem.getVariableName().equals(variableName))
            {
                toRemove = storeItem;
            }
        }
        if (toRemove != null)
        {
            getStoreItems().remove(getStoreItems().indexOf(toRemove));
        }
    }

}
