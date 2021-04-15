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
     * {@link #value} in {@link Context#getVariables()}.
     * If {@link #getVariableName()} is {@link Constants#STORE} and {@link #getValue()} is {@link Constants#DELETE},
     * all variables are deleted.
     */
    @Override
    public void execute(final Context<?> context)
    {
        // Resolve values
        resolveValues(context);
        // Get the appropriate storage
        final UniqueStorage storage = context.getVariables();
        // If the variable is not "Store" and the value is not "Delete"
        if (!variableName.equals(Constants.STORE) && !value.equals(Constants.DELETE))
        {
            // Store the variable
            storage.store(variableName, value);
            XltLogger.runTimeLogger.info("Added Variable: " + variableName + " : " + value);
        }
        else {
            storage.clear();
            XltLogger.runTimeLogger.info("Removed all Variables");
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