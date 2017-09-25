package com.xceptance.xlt.nocoding.util;

import com.xceptance.xlt.api.data.GeneralDataProvider;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.engine.XltWebClient;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;
import com.xceptance.xlt.nocoding.util.dataStorage.DefaultValue;
import com.xceptance.xlt.nocoding.util.variableResolver.VariableResolver;

public class PropertyManager
{
    private final XltProperties properties;

    private final DataStorage dataStorage;

    private XltWebClient webClient;

    private final VariableResolver resolver;

    public PropertyManager(final XltProperties properties, final DataStorage dataStorage)
    {
        this.properties = properties;
        this.dataStorage = dataStorage;
        this.webClient = new XltWebClient();
        this.resolver = new VariableResolver(GeneralDataProvider.getInstance());
        // TODO in Config auslagern!
        webClient.getOptions().setRedirectEnabled(false);
    }

    public PropertyManager(final XltProperties properties, final DataStorage dataStorage, final DefaultValue defaultValues)
    {
        this.properties = properties;
        this.dataStorage = dataStorage;
        this.webClient = new XltWebClient();
        this.resolver = new VariableResolver(GeneralDataProvider.getInstance());
        // TODO in Config auslagern!
        webClient.getOptions().setRedirectEnabled(false);
    }

    public XltProperties getProperties()
    {
        return properties;
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

    public String resolveString(final String toResolve)
    {
        String resolvedString = null;
        if (toResolve != null)
        {
            resolvedString = resolver.resolveString(toResolve, this);
        }
        return resolvedString;
    }

}
