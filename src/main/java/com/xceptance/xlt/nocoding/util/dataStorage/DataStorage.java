package com.xceptance.xlt.nocoding.util.dataStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.scriptItem.StoreItem;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultHeader;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultItem;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultParameter;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultStatic;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * Handles all data storage related matters. This means, the class saves variables, default
 * definitions/headers/parameters/static requests.
 * 
 * @author ckeiner
 */
public class DataStorage
{
    /**
     * Variables saved by {@link StoreItem}
     */
    private final Map<String, String> variables;

    /**
     * The default items like httpcode = 200, saved by {@link StoreDefaultItem}
     */
    private final Map<String, String> configItem;

    /**
     * The default headers saved by {@link StoreDefaultHeader}
     */
    private final Map<String, String> headers;

    /**
     * The default parameters saved by {@link StoreDefaultParameter}
     */
    private final Map<String, String> parameters;

    /**
     * The urls for the default static request, saved by {@link StoreDefaultStatic}
     */
    private final List<String> staticUrls;

    /**
     * Fallback values in case certain default items get deleted
     */
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
        this.parameters = new LinkedHashMap<String, String>();
        this.staticUrls = new ArrayList<String>();
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

}
