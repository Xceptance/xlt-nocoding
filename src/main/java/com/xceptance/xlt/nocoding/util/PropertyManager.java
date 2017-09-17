package com.xceptance.xlt.nocoding.util;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.engine.XltWebClient;
import com.xceptance.xlt.nocoding.util.DataStorage.DataStorage;

public class PropertyManager
{
    private final XltProperties properties;

    private final DataStorage globalStorage;

    private XltWebClient webClient;

    public PropertyManager(final XltProperties properties, final DataStorage globalStorage)
    {
        this.properties = properties;
        this.globalStorage = globalStorage;
        this.webClient = new XltWebClient();
    }

    public XltProperties getProperties()
    {
        return properties;
    }

    public DataStorage getGlobalStorage()
    {
        return globalStorage;
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

}
