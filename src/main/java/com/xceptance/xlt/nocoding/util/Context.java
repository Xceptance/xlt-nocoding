package com.xceptance.xlt.nocoding.util;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.api.data.GeneralDataProvider;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.engine.XltWebClient;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;
import com.xceptance.xlt.nocoding.util.variableResolver.VariableResolver;

public class Context
{
    protected final XltProperties xltProperties;

    protected final DataStorage dataStorage;

    protected XltWebClient webClient;

    protected final VariableResolver resolver;

    protected WebResponse webResponse;

    public Context(final XltProperties xltProperties, final DataStorage dataStorage)
    {
        this.xltProperties = xltProperties;
        this.dataStorage = dataStorage;
        this.webClient = new XltWebClient();
        this.resolver = new VariableResolver(GeneralDataProvider.getInstance());
        // TODO in Config auslagern! -> Damit funktioniert außerdem nicht mehr der EasyTestcase
        webClient.getOptions().setRedirectEnabled(false);
    }

    public Context(final Context context)
    {
        this.xltProperties = context.getProperties();
        this.dataStorage = context.getDataStorage();
        this.webClient = context.getWebClient();
        this.resolver = context.getResolver();
        // TODO in Config auslagern! -> Damit funktioniert außerdem nicht mehr der EasyTestcase
        webClient.getOptions().setRedirectEnabled(false);
    }

    public XltProperties getProperties()
    {
        return xltProperties;
    }

    public DataStorage getDataStorage()
    {
        return dataStorage;
    }

    public void setWebClient(final XltWebClient webClient)
    {
        // TODO This okay? We don't want to change our webClient afaik
        if (this.webClient == null)
        {
            this.webClient = webClient;
        }
    }

    public XltWebClient getWebClient()
    {
        return webClient;
    }

    public WebResponse getWebResponse()
    {
        return webResponse;
    }

    public void setWebResponse(final WebResponse webResponse)
    {
        this.webResponse = webResponse;
    }

    public VariableResolver getResolver()
    {
        return resolver;
    }

    public String resolveString(final String toResolve)
    {
        String resolvedString = null;
        if (toResolve != null)
        {
            resolvedString = resolver.resolveString(toResolve, this);
        }
        return resolvedString;
    }

    public String getPropertyByKey(final String key)
    {
        return xltProperties.getProperty(key);
    }

    public String getPropertyByKey(final String key, final String defaultValue)
    {
        return xltProperties.getProperty(key, defaultValue);
    }

    public int getPropertyByKey(final String key, final int defaultValue)
    {
        return xltProperties.getProperty(key, defaultValue);
    }

    public boolean getPropertyByKey(final String key, final boolean defaultValue)
    {
        return xltProperties.getProperty(key, defaultValue);
    }

    public long getPropertyByKey(final String key, final long defaultValue)
    {
        return xltProperties.getProperty(key, defaultValue);
    }

    public void configureWebClient()
    {
        new WebClientConfigurator(xltProperties).configWebClient(webClient);
    }

}
