/*
 * Copyright (c) 2013-2023 Xceptance Software Technologies GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xceptance.xlt.nocoding.util.storage.unit;

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
     * @return True if an item was removed. Otherwise, returns false
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
