package com.xceptance.xlt.nocoding.util.storage.unit.unique;

import java.util.HashMap;
import java.util.Map;

import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.storage.DefaultValues;

/**
 * A storage unit, that stores simple key value pairs for default values. However, some have a fallback value provided
 * by {@link DefaultValues}.
 * 
 * @author ckeiner
 */
public class DefaultKeyValueStorage extends UniqueStorage
{
    /**
     * If a key cannot be found in {@link #getItems()} (returns null), the key is looked up in {@link #fallback}. Therefore,
     * if a key is deleted in {@link #getItems()}, some keys still have a default value.
     */
    protected Map<String, String> fallback;

    /**
     * Creates an instance of {@link DefaultKeyValueStorage}, that sets {@link #getItems()} and {@link #fallback} to a new
     * {@link HashMap}. Then, initializes {@link #getItems()} and {@link #fallback} with default values from
     * {@link DefaultValues}.
     */
    public DefaultKeyValueStorage()
    {
        super(new HashMap<String, String>());
        fallback = new HashMap<String, String>();
        initialize();
    }

    /**
     * Stores standard default values provided by {@link DefaultValues} and saves them as fallback values.
     */
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
     * @return Null if the key is neither in the {@link #fallback} nor in {@link #getItems()}, else the value corresponding
     *         to the key
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
        // Search for the key in the fallback
        else if (fallback.containsKey(key))
        {
            value = fallback.get(key);
        }
        return value;
    }

    /**
     * Stores an item with a default value of {@link #fallback}.
     * 
     * @param itemName
     *            The key/name you want to store your item with
     * @param value
     *            The value you want to store
     * @param fallback
     *            The default value of the specified key, in case the key gets deleted in {@link #getItems()}
     */
    public void store(final String itemName, final String value, final String fallback)
    {
        // Store the item
        getItems().put(itemName, value);
        // If fallback is not null
        if (fallback != null)
        {
            // put it in the fallback list
            this.fallback.put(itemName, fallback);
        }
    }
}
