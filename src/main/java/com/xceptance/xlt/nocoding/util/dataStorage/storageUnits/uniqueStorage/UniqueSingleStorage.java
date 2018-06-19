package com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.uniqueStorage;

import java.util.HashSet;
import java.util.Set;

public class UniqueSingleStorage
{

    protected final Set<String> items;

    /**
     * Creates a new instance of UniqueSingleStorage and sets {@link #items} to a new {@link HashSet}
     */
    public UniqueSingleStorage()
    {
        this(new HashSet<String>());
    }

    /**
     * Creates a new instance of UniqueSingleStorage
     * 
     * @param items
     *            The items you want
     */
    public UniqueSingleStorage(final Set<String> items)
    {
        this.items = items;
    }

    /**
     * Stores item in {@link #items}
     * 
     * @param item
     *            The item you want to store
     */
    public void store(final String item)
    {
        getItems().add(item);
    }

    /**
     * Deletes all items from {@link #items}
     */
    public void clear()
    {
        getItems().clear();
    }

    /**
     * Removes the item with the name itemName.
     * 
     * @param itemName
     *            The value of the item
     * @return True if an item was removed. Otherwise, returns false
     */
    public boolean remove(final String itemName)
    {
        // Return if we removed at least one item
        return getItems().remove(itemName);
    }

    public Set<String> getItems()
    {
        return items;
    }
}
