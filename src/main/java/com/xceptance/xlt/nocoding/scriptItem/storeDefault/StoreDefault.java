package com.xceptance.xlt.nocoding.scriptItem.storeDefault;

import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.Context;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;
import com.xceptance.xlt.nocoding.util.variableResolver.VariableResolver;

/**
 * The abstract class for every default store item. A default store item is an item that sets the default value for
 * certain fields. These fields are specified by {@link Constants#PERMITTEDLISTITEMS}. However, extending this list
 * means, one must also extend the parser, and where the new default value is to be set.
 * 
 * @author ckeiner
 */
public abstract class StoreDefault implements ScriptItem
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
     * Creates a {@link StoreDefault} that sets {@link #variableName} and {@link #value}
     * 
     * @param variableName
     * @param value
     */
    public StoreDefault(final String variableName, final String value)
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
     * Resolves the {@link #value}.
     * 
     * @param context
     *            The {@link Context} with the {@link VariableResolver} and {@link DataStorage}
     */
    protected void resolveValues(final Context context)
    {
        value = context.resolveString(getValue());
    }

}
