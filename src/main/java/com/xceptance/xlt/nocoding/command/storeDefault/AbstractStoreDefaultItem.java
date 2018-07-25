package com.xceptance.xlt.nocoding.command.storeDefault;

import com.xceptance.xlt.nocoding.command.Command;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.resolver.VariableResolver;
import com.xceptance.xlt.nocoding.util.storage.DataStorage;

/**
 * The abstract class for every default store item. A default store item is an item that sets the default value for
 * certain fields. These fields are specified by {@link Constants#PERMITTEDLISTITEMS}.
 *
 * @author ckeiner
 */
public abstract class AbstractStoreDefaultItem implements Command
{
    /**
     * The name of the default item
     */
    protected final String variableName;

    /**
     * The value of the default item
     */
    protected String value;

    /**
     * Creates a {@link AbstractStoreDefaultItem} that sets {@link #variableName} and {@link #value}
     *
     * @param variableName
     *            The name of the variable
     * @param value
     *            The value of the variable
     */
    public AbstractStoreDefaultItem(final String variableName, final String value)
    {
        this.variableName = variableName;
        this.value = value;
    }

    public String getVariableName()
    {
        return variableName;
    }

    public String getValue()
    {
        return value;
    }

    /**
     * Resolves {@link #value}.
     *
     * @param context
     *            The {@link Context} with the {@link VariableResolver} and {@link DataStorage}
     */
    protected void resolveValues(final Context<?> context)
    {
        value = context.resolveString(getValue());
    }

}
