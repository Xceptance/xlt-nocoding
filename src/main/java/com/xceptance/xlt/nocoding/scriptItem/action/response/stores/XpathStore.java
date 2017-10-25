package com.xceptance.xlt.nocoding.scriptItem.action.response.stores;

import com.gargoylesoftware.htmlunit.WebResponse;
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
    public void store(final Context context, final WebResponse webResponse) throws Exception
    {
        // TODO check for mode - throw error if not dom mode
        // if(propertyManager.mode = lightweight)
        // throw new IllegalStateException("Cannot use an xpath expression in the lightweight mode. Please use the dom mode.");

        // TODO xPath Magic
    }

    public String getxPathExpression()
    {
        return xpath;
    }

}
