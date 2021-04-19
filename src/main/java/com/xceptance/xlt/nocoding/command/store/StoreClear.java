package com.xceptance.xlt.nocoding.command.store;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.command.Command;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.storage.unit.unique.UniqueStorage;

/**
 * Command to clear the stored data all at once
 */
public class StoreClear implements Command
{
    /**
     * Executes the {@link StoreClear} by resolving the values and then storing the {@link #variableName} with its
     * {@link #value} in {@link Context#getVariables()}. If {@link #getVariableName()} is {@link Constants#STORE} and
     * {@link #getValue()} is {@link Constants#DELETE}, all variables are deleted.
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