package com.xceptance.xlt.nocoding.scriptItem.action.response.stores;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.scriptItem.action.response.selector.AbstractSelector;
import com.xceptance.xlt.nocoding.scriptItem.action.response.selector.RegexpSelector;
import com.xceptance.xlt.nocoding.util.Context;

public class GroupResponseStore extends AbstractResponseStore
{
    private String group;

    public GroupResponseStore(final String variableName, final AbstractSelector selector, final String group)
    {
        super(variableName, selector);
        this.group = group;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void execute(final Context context) throws Exception
    {
        // Resolve values
        resolveValues(context);
        // Set the group for the selector
        if (selector instanceof RegexpSelector)
        {
            ((RegexpSelector) selector).setGroup(group);
        }
        // Execute the selector
        selector.execute(context);
        // Try to turn group into an Integer
        // Finally store the variable
        context.storeVariable(getVariableName(), selector.getResult().get(0));
        XltLogger.runTimeLogger.info("Added Variable: " + variableName + " : " + selector.getResult().get(0));
    }

    private void resolveValues(final Context context)
    {
        group = context.resolveString(group);
    }

}
