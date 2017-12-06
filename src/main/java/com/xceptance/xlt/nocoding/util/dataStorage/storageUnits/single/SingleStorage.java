package com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.single;

import java.util.ArrayList;
import java.util.List;

import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.StorageUnit;

/**
 * A storage unit, that stores a {@link String} in a {@link List}.
 * 
 * @author ckeiner
 */
public abstract class SingleStorage implements StorageUnit
{
    private final List<String> items;

    public SingleStorage()
    {
        this(new ArrayList<String>());
    }

    public SingleStorage(final List<String> items)
    {
        this.items = items;
    }

    public void store(final String item)
    {
        getItems().add(item);
    }

    public boolean remove(final String itemName)
    {
        boolean removed = false;
        for (final String item : getItems())
        {
            if (item.equals(itemName))
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

    public List<String> getItems()
    {
        return items;
    }

}
