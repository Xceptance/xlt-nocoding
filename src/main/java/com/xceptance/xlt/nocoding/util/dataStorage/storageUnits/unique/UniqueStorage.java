package com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.unique;

import java.util.HashMap;
import java.util.Map;

import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.StorageUnit;

/**
 * A storage unit, that stores unique items in the sense of the key being unique.
 * 
 * @author ckeiner
 */
public abstract class UniqueStorage implements StorageUnit
{

    private final Map<String, String> items;

    public UniqueStorage()
    {
        this(new HashMap<String, String>());
    }

    public UniqueStorage(final Map<String, String> items)
    {
        this.items = items;
    }

    public void store(final String itemName, final String value)
    {
        getItems().put(itemName, value);
    }

    public String get(final String key)
    {
        return getItems().get(key);
    }

    public String remove(final String itemName)
    {
        return getItems().remove(itemName);
    }

    public void clear()
    {
        getItems().clear();
    }

    public Map<String, String> getItems()
    {
        return items;
    }

}
