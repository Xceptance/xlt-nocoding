package com.xceptance.xlt.nocoding.scriptItem;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;
import com.xceptance.xlt.nocoding.util.variableResolver.VariableResolver;

/**
 * A StoreItem consist of a {@link #variableName} and a {@link #value}, which gets stored in the {@link DataStorage} in
 * {@link Context#getDataStorage()}. However, the value first gets resolved with {@link Context#resolveString(String)}.
 */
public class StoreItem implements ScriptItem
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
     * Creates an instance of {@link StoreItem}, that sets {@link #variableName} and {@link #value}
     * 
     * @param variableName
     * @param value
     */
    public StoreItem(final String variableName, final String value)
    {
        this.variableName = variableName;
        this.value = value;
    }

    /**
     * Executes the {@link StoreItem} by resolving values and then storing the {@link #variableName} and resolved
     * {@link #value} via {@link Context#storeVariable(String, String)}.
     */
    @Override
    public void execute(final Context<?> context)
    {
        // Resolve values
        resolveValues(context);
        // Store the variable
        context.getVariables().store(variableName, value);
        XltLogger.runTimeLogger.info("Added Variable: " + variableName + " : " + value);
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
     * Resolves the {@link #value}.
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
