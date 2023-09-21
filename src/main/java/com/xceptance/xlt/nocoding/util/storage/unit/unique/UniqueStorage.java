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

import java.util.HashMap;
import java.util.Map;

/**
 * A storage unit, that stores unique items in the sense of the key being unique.
 *
 * @author ckeiner
 */
public class UniqueStorage
{

    /**
     * Stores all items in a {@link Map}.
     */
    protected final Map<String, String> items;

    /**
     * Creates a new instance of {@link UniqueStorage} by setting {@link #items} to new {@link HashMap}
     */
    public UniqueStorage()
    {
        this(new HashMap<String, String>());
    }

    /**
     * Creates a new instance of {@link UniqueStorage}
     *
     * @param items
     *            The items you want
     */
    public UniqueStorage(final Map<String, String> items)
    {
        this.items = items;
    }

    /**
     * Stores itemName and value in {@link #items}
     *
     * @param itemName
     *            The itemName/key
     * @param value
     *            The value of the corresponding itemName/key
     */
    public void store(final String itemName, final String value)
    {
        getItems().put(itemName, value);
    }

    /**
     * Returns the value mapped to the specified itemName/key
     *
     * @param itemName
     *            The itemName/key you want to search for
     * @return The value mapped to the specified key
     */
    public String get(final String itemName)
    {
        return getItems().get(itemName);
    }

    /**
     * Removes the specified itemName/key with its value
     *
     * @param itemName
     *            The name of the item
     * @return
     */
    public String remove(final String itemName)
    {
        return getItems().remove(itemName);
    }

    /**
     * Deletes all items from {@link #items}
     */
    public void clear()
    {
        getItems().clear();
    }

    public Map<String, String> getItems()
    {
        return items;
    }

}
