package com.xceptance.xlt.nocoding.util;

import java.util.Map;

import com.xceptance.xlt.api.util.XltProperties;

public class PropertyManager
{
    private final XltProperties properties;

    private Map<String, String> storedValues;

    public PropertyManager(final XltProperties properties)
    {
        this.properties = properties;
    }

}
