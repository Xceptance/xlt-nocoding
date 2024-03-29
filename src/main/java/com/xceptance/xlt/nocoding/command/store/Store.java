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
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.resolver.VariableResolver;
import com.xceptance.xlt.nocoding.util.storage.DataStorage;
import com.xceptance.xlt.nocoding.util.storage.unit.unique.UniqueStorage;

/**
 * A StoreItem consist of a {@link #variableName} and a {@link #value}, which gets stored in the {@link DataStorage} in
 * {@link Context#getDataStorage()}. However, the value gets resolved before it is stored.
 */
public class Store implements Command
{
    /**
     * Fixes the compiler complains, but we don't use that at all
     */
    private static final long serialVersionUID = 1L;

    /**
     * The name of the variable
     */
    private final String variableName;

    /**
     * The value of the variable
     */
    private String value;

    /**
     * Creates an instance of {@link Store}, that sets {@link #variableName} and {@link #value}
     *
     * @param variableName
     * @param value
     */
    public Store(final String variableName, final String value)
    {
        this.variableName = variableName;
        this.value = value;
    }

    /**
     * Executes the {@link Store} by resolving the values and then storing the {@link #variableName} with its
     * {@link #value} in {@link Context#getVariables()}. If {@link #getVariableName()} is {@link Constants#STORE} and
     * {@link #getValue()} is {@link Constants#DELETE}, all variables are deleted.
     */
    @Override
    public void execute(final Context<?> context)
    {
        // Resolve values
        resolveValues(context);

        // Get the appropriate storage
        final UniqueStorage storage = context.getVariables();

        // If the variable is "Store" and the value is "Delete"
        // remove all storage data
        if (Constants.STORE.equals(variableName) && Constants.DELETE.equals(value))
        {
            storage.clear();
            XltLogger.runTimeLogger.info("Removed all Variables");
        }
        else
        {
            // Store the variable
            storage.store(variableName, value);
            XltLogger.runTimeLogger.info("Added Variable: " + variableName + " : " + value);
        }
    }

    public String getVariableName()
    {
        return variableName;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(final String value)
    {
        this.value = value;
    }

    /**
     * Resolves {@link #value}.
     *
     * @param context
     *            The {@link Context} with the {@link VariableResolver} and {@link DataStorage}.
     */
    public void resolveValues(final Context<?> context)
    {
        final String resolvedValue = context.resolveString(getValue());
        setValue(resolvedValue);
    }

}