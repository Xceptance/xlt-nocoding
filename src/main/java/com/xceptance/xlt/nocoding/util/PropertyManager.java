package com.xceptance.xlt.nocoding.util;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.util.DataStorage.DataStorage;

public class PropertyManager
{
    private final XltProperties properties;

    private final DataStorage globalStorage;

    public PropertyManager(final XltProperties properties, final DataStorage globalStorage)
    {
        this.properties = properties;
        this.globalStorage = globalStorage;
    }

    public XltProperties getProperties()
    {
        return properties;
    }

    public DataStorage getGlobalStorage()
    {
        return globalStorage;
    }

}
