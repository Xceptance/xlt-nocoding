package com.xceptance.xlt.nocoding.scriptItem.action.response.selector;

import org.apache.commons.lang3.NotImplementedException;

import com.xceptance.xlt.nocoding.util.Context;

public class XpathSelector extends AbstractSelector
{

    public XpathSelector(final String selectionExpression)
    {
        super(selectionExpression);
    }

    @Override
    public void execute(final Context context)
    {
        throw new NotImplementedException("Not yet implemented!");
    }

}
