package com.xceptance.xlt.nocoding.util;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.engine.XltWebClient;
import com.xceptance.xlt.nocoding.util.DataStorage.DataStorage;

public class PropertyManager
{
    private final XltProperties properties;

    private final DataStorage globalStorage;

    private final XltWebClient webClient;

    public PropertyManager(final XltProperties properties, final DataStorage globalStorage, final XltWebClient webClient)
    {
        this.properties = properties;
        this.globalStorage = globalStorage;
        this.webClient = webClient;
    }

    public XltProperties getProperties()
    {
        return properties;
    }

    public DataStorage getGlobalStorage()
    {
        return globalStorage;
    }

    public XltWebClient getWebClient()
    {
        return webClient;
    }

}
