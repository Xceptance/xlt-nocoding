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
import com.xceptance.xlt.nocoding.util.storage.unit.SingleStorage;

/**
 * Stores a default static request. This class does not use the variableName with the single exception of "Static:
 * Delete", which deletes all default static requests.
 *
 * @author ckeiner
 */
public class StoreDefaultStaticSubrequest extends AbstractStoreDefaultItem
{

    /**
     * Creates an instance of {@link StoreDefaultStaticSubrequest} that sets {@link #getVariableName()} to
     * {@link Constants#STATIC} and {@link #getValue()}
     *
     * @param value
     *            The URL of the default static request
     */
    public StoreDefaultStaticSubrequest(final String value)
    {
        // variableName isn't used, so we set it to "Static" to remain some sort of meaning
        super(Constants.STATIC, value);
    }

    /**
     * If {@link #getValue()} is {@link Constants#DELETE}, the list of default static requests is deleted. Else, it
     * stores a default static requests.
     */
    @Override
    public void execute(final Context<?> context)
    {
        // Resolve values
        super.resolveValues(context);
        // Get the appropriate storage
        final SingleStorage storage = context.getDefaultStatics();
        // If the value is not "delete"
        if (!value.equals(Constants.DELETE))
        {
            // Store the URL
            storage.store(value);
            XltLogger.runTimeLogger.debug("Added url \"" + value + "\" to default static request storage");
        }
        else
        {
            // Delete all default statics
            storage.clear();
            XltLogger.runTimeLogger.debug("Removed all default static requests");
        }
    }

}
