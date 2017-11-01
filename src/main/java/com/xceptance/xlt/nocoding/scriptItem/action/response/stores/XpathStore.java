package com.xceptance.xlt.nocoding.scriptItem.action.response.stores;

import com.xceptance.xlt.nocoding.util.Context;

public class XpathStore extends AbstractResponseStore
{

    private final String xpath;

    public XpathStore(final String variableName, final String xpath)
    {
        super(variableName);
        this.xpath = xpath;
    }

    @Override
    public void execute(final Context context) throws Exception
    {
        // TODO check for mode - throw error if not dom mode
        // if(propertyManager.mode = lightweight)
        // throw new IllegalStateException("Cannot use an xpath expression in the lightweight mode. Please use the dom mode.");

    }

    public String getxPathExpression()
    {
        return xpath;
    }

}
