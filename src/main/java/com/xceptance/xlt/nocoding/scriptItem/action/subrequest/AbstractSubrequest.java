package com.xceptance.xlt.nocoding.scriptItem.action.subrequest;

import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.util.Context;

public abstract class AbstractSubrequest extends AbstractActionItem
{
    protected Context context;

    @Override
    public abstract void execute(Context context) throws Throwable;

    public void setContext(final Context context)
    {
        this.context = context;
    }

    public Context getContext()
    {
        return context;
    }

}
