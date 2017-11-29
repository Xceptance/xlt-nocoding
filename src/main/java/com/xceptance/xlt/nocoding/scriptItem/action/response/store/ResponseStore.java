package com.xceptance.xlt.nocoding.scriptItem.action.response.store;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.scriptItem.action.response.selector.AbstractSelector;
import com.xceptance.xlt.nocoding.scriptItem.action.response.selector.RegexpSelector;
import com.xceptance.xlt.nocoding.util.Context;

/**
 * Takes an {@link AbstractSelector} and stores the result via {@link Context#storeVariable(String, String)}.
 * 
 * @author ckeiner
 */
public class ResponseStore extends AbstractResponseStore
{
    /**
     * The matching group for the {@link RegexpSelector}
     */
    private String group;

    /**
     * Creates an instance of {@link ResponseStore}, that stores the result from {@link AbstractSelector} under the name
     * #variableName.
     * 
     * @param variableName
     *            The name of the variable
     * @param selector
     *            The selector to use for the value
     */
    public ResponseStore(final String variableName, final AbstractSelector selector)
    {
        this(variableName, selector, null);
    }

    /**
     * Creates an instance of {@link ResponseStore}, that stores the result from {@link AbstractSelector} under the name
     * variableName.
     * 
     * @param variableName
     *            The name of the variable
     * @param selector
     *            The selector to use for the value, should be {@link RegexpSelector}
     * @param group
     *            The matching group of the regular expression
     */
    public ResponseStore(final String variableName, final AbstractSelector selector, final String group)
    {
        super(variableName, selector);
        this.group = group;
    }

    /**
     * Resolves values, sets group of the {@link RegexpSelector} if {@link #group} is specified, then stores the result via
     * {@link Context#storeVariable(String, String)}.
     * 
     * @param context
     *            The {@link Context} to use
     */
    @Override
    public void execute(final Context context) throws Exception
    {
        // Resolve values
        resolveValues(context);
        // Set the group for the selector if specified
        if (group != null)
        {
            if (selector instanceof RegexpSelector)
            {
                ((RegexpSelector) selector).setGroup(group);
            }
            // If group is specified but the selector is not a RegexpSelector, throw an error
            else
            {
                throw new IllegalArgumentException("Group specified but selector is " + selector.getClass().getName()
                                                   + " and not a RegexpSelector");
            }
        }
        // Execute the selector
        selector.execute(context);
        // Store the solution
        context.storeVariable(getVariableName(), selector.getResult().get(0));
        XltLogger.runTimeLogger.info("Added Variable: " + variableName + " : " + selector.getResult().get(0));
    }

    /**
     * Resolves {@link #group}.
     * 
     * @param context
     */
    private void resolveValues(final Context context)
    {
        if (group != null && !group.isEmpty())
        {
            group = context.resolveString(group);
        }
    }

}
