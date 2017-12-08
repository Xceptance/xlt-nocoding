package com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.uniqueStorage;

import java.util.HashMap;
import java.util.Map;

import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.dataStorage.DefaultValues;

/**
 * A storage unit, that stores simple key value pairs for default values. However, some have a fallback value.
 * 
 * @author ckeiner
 */
public class DefaultKeyValueStorage extends UniqueStorage
{
    protected Map<String, String> fallback;

    public DefaultKeyValueStorage()
    {
        super(new HashMap<String, String>());
        fallback = new HashMap<String, String>();
        initialize();
    }

    public void initialize()
    {
        this.store(Constants.METHOD, DefaultValues.METHOD, DefaultValues.METHOD);
        this.store(Constants.XHR, DefaultValues.XHR, DefaultValues.XHR);
        this.store(Constants.ENCODEPARAMETERS, DefaultValues.ENCODEPARAMETERS, DefaultValues.ENCODEPARAMETERS);
        this.store(Constants.ENCODEBODY, DefaultValues.ENCODEBODY, DefaultValues.ENCODEBODY);
        this.store(Constants.HTTPCODE, DefaultValues.HTTPCODE, DefaultValues.HTTPCODE);

    }

    /**
     * Returns the item with the specified key. If the key cannot be found, the method searches in {@link #fallback}. If it
     * still cannot be found, the method returns null.
     * 
     * @param key
     *            The key you want to search for
     * @return Null if the key is neither in the {@link #fallback} nor in configItems, else the value corresponding to the
     *         key
     */
    @Override
    public String get(final String key)
    {
        String value = null;
        // Look for the key in the config items
        if (getItems().containsKey(key))
        {
            value = super.get(key);
        }
        // Search for the key in the default values
        else if (fallback.containsKey(key))
        {
            value = fallback.get(key);
        }
        return value;
    }

    /**
     * Stores an item with a default value of {@link #fallback}.
     * 
     * @param key
     *            The key you want to store your item with
     * @param value
     *            The value you want to store
     * @param fallback
     *            The default value of the specified key, in case the ConfigItem gets deleted
     */
    public void store(final String itemName, final String value, final String fallback)
    {
        getItems().put(itemName, value);
        if (fallback != null)
        {
            this.fallback.put(itemName, fallback);
        }
    }
}
