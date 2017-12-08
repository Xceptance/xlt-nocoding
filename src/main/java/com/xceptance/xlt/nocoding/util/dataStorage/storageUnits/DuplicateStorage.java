package com.xceptance.xlt.nocoding.util.dataStorage.storageUnits;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * A storage unit, that can have duplicate items.
 * 
 * @author ckeiner
 */
public class DuplicateStorage
{
    /**
     * Stores all items in a {@link List}<{@link NameValuePair}>
     */
    private final List<NameValuePair> items;

    /**
     * Creates a new instance of {@link DuplicateStorage} and sets {@link #items} to a new {@link ArrayList}
     */
    public DuplicateStorage()
    {
        this(new ArrayList<NameValuePair>());
    }

    /**
     * Creates a new instance of {@link DuplicateStorage}
     * 
     * @param items
     *            The items you want
     */
    public DuplicateStorage(final List<NameValuePair> items)
    {
        this.items = items;
    }

    /**
     * Stores itemName and value in {@link #items}
     * 
     * @param itemName
     *            The name of the {@link NameValuePair}
     * @param value
     *            The value of the {@link NameValuePair}
     */
    public void store(final String itemName, final String value)
    {
        getItems().add(new NameValuePair(itemName, value));
    }

    /**
     * Return all {@link NameValuePair} with the specified itemName as name
     * 
     * @param itemName
     *            The name of the {@link NameValuePair}
     * @return The value of the {@link NameValuePair} with the specified itemName as name. Returns an emtpy list if no
     *         {@link NameValuePair} was found
     */
    public List<String> get(final String itemName)
    {
        final List<String> value = new ArrayList<String>();
        // Iterate over all items
        for (final NameValuePair item : getItems())
        {
            // If the name equals itemName
            if (item.getName().equals(itemName))
            {
                // Save the value
                value.add(item.getValue());
            }
        }
        return value;
    }

    /**
     * Removes all {@link NameValuePair} with the name itemName
     * 
     * @param itemName
     *            The name of the {@link NameValuePair}
     * @return True if an item was removed. Otherwise, returns falls
     */
    public boolean remove(final String itemName)
    {
        boolean removed = false;
        // Get the maximum amount of removable items
        int amountRemovableItems = getItems().size();
        // Iterate over all elements
        for (final NameValuePair item : getItems())
        {
            // If the name equals itemName
            if (item.getName().equals(itemName))
            {
                // Remove the item
                removed = getItems().remove(item);
                // Decrement the amount of removable items
                amountRemovableItems--;
                // If we reached zero, there isn't an element left. Therefore, we want to quit the loop.
                if (amountRemovableItems == 0)
                {
                    break;
                }
            }
        }
        // Return if we removed at least one item
        return removed;
    }

    /**
     * Deletes all items from {@link #items}
     */
    public void clear()
    {
        getItems().clear();
    }

    public List<NameValuePair> getItems()
    {
        return items;
    }

}
