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
package com.xceptance.xlt.nocoding.command.storeDefault;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.storage.unit.unique.DefaultKeyValueStorage;

/**
 * Stores a default item that is a simple name-value pair.
 *
 * @author ckeiner
 */
public class StoreDefaultValue extends AbstractStoreDefaultItem
{

    /**
     * Creates an instance of {@link StoreDefaultValue} that sets {@link #getVariableName()} and {@link #getValue()}
     *
     * @param variableName
     *            The name of the default item
     * @param value
     *            The default value of the default item
     */
    public StoreDefaultValue(final String variableName, final String value)
    {
        super(variableName, value);
    }

    /**
     * If {@link #getValue()} is {@link Constants#DELETE}, the default item with the name {@link #getVariableName()} is
     * deleted. Else, it stores a default item.
     */
    @Override
    public void execute(final Context<?> context)
    {
        // Resolve values
        super.resolveValues(context);
        // Get the appropriate storage
        final DefaultKeyValueStorage storage = context.getDefaultItems();
        // If the value is not Constants.DELETE
        if (!value.equals(Constants.DELETE))
        {
            // Store the item in ConfigItems
            storage.store(variableName, value);
            XltLogger.runTimeLogger.debug("Added \"" + variableName + "\" with the value \"" + value + "\" to default storage");
        }
        else
        {
            // Delete the specified ConfigItem
            storage.remove(variableName);
            XltLogger.runTimeLogger.debug("Removed \"" + variableName + "\" from default storage");
        }
    }

}
