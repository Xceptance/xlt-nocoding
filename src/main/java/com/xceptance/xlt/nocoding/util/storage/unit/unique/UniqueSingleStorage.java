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
package com.xceptance.xlt.nocoding.util.storage.unit.unique;

import java.util.HashSet;
import java.util.Set;

/**
 * Saves items in a {@link Set} so that there are no duplicates.
 *
 * @author ckeiner
 */
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
