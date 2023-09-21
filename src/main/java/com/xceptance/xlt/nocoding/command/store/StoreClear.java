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
package com.xceptance.xlt.nocoding.command.store;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.command.Command;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.storage.unit.unique.UniqueStorage;

/**
 * Command to clear the stored data all at once
 */
public class StoreClear implements Command
{
    /**
     * Fixes the compiler complains, but we don't use that at all
     */
    private static final long serialVersionUID = 1L;

    /**
     * Executes the {@link StoreClear} command.
     */
    @Override
    public void execute(final Context<?> context)
    {
        // Get the appropriate storage
        final UniqueStorage storage = context.getVariables();

        storage.clear();
        XltLogger.runTimeLogger.info("Removed all Variables");
    }
}