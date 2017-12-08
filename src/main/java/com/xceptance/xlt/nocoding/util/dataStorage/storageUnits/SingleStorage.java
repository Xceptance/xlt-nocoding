package com.xceptance.xlt.nocoding.util.dataStorage.storageUnits;

import java.util.ArrayList;
import java.util.List;

/**
 * A storage unit, that stores a {@link String} in a {@link List}.
 * 
 * @author ckeiner
 */
public class SingleStorage
{
    /**
     * Stores all items in a {@link List}<{@link String}>
     */
    private final List<String> items;

    /**
     * Creates a new instance of {@link SingleStorage} and sets {@link #items} to a new {@link ArrayList}
     */
    public SingleStorage()
    {
        this(new ArrayList<String>());
    }

    /**
     * Creates a new instance of {@link SingleStorage}
     * 
     * @param items
     *            The items you want
     */
    public SingleStorage(final List<String> items)
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
     * Removes all {@link String} with the value of itemName
     * 
     * @param itemName
     *            The value of the item
     * @return True if an item was removed. Otherwise, returns falls
     */
    public boolean remove(final String itemName)
    {
        boolean removed = false;
        // Get the maximum amount of removable items
        int amountRemovableItems = getItems().size();
        // Iterate over all elements
        for (final String item : getItems())
        {
            // If the name equals itemName
            if (item.equals(itemName))
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

    public List<String> getItems()
    {
        return items;
    }

}
