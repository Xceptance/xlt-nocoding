package com.xceptance.xlt.nocoding.scriptItem.action.response.stores;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.nocoding.util.DataStorage.DataStorage;

public abstract class AbstractResponseStore
{
    protected final String variableName;

    protected DataStorage globalStorage;

    public AbstractResponseStore(final String variableName)
    {
        this.variableName = variableName;
    }

    public abstract void store(HtmlPage page) throws Exception;

    public abstract void store(LightWeightPage page) throws Exception;

    public DataStorage getGlobalStorage()
    {
        return globalStorage;
    }

    public void setGlobalStorage(final DataStorage globalStorage)
    {
        this.globalStorage = globalStorage;
    }

    public String getVariableName()
    {
        return variableName;
    }

}
