package com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.duplicate;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * A storage unit, that can have duplicate items.
 * 
 * @author ckeiner
 */
public abstract class DuplicateStorage
{
    private final List<NameValuePair> items;

    public DuplicateStorage()
    {
        this(new ArrayList<NameValuePair>());
    }

    public DuplicateStorage(final List<NameValuePair> items)
    {
        this.items = items;
    }

    public void store(final String itemName, final String value)
    {
        getItems().add(new NameValuePair(itemName, value));
    }

    public String get(final String key)
    {
        return null;
    }

    public boolean remove(final String itemName)
    {
        boolean removed = false;
        for (final NameValuePair item : getItems())
        {
            if (item.getName().equals(itemName))
            {
                removed = getItems().remove(item);
            }
        }
        return removed;
    }

    public void clear()
    {
        getItems().clear();
    }

    public List<NameValuePair> getItems()
    {
        return items;
    }

}
