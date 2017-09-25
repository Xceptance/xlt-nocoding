package com.xceptance.xlt.nocoding.util.dataStorage;

import java.util.HashMap;
import java.util.Map;

public class DataStorage
{
    private final Map<String, String> variables;

    // TODO this probably needs to be changed depending on meeting at September 14th
    private Map<String, String> configItem;

    public DataStorage()
    {
        this.variables = new HashMap<String, String>();
        this.configItem = new HashMap<String, String>();
    }

    public DataStorage(final Map<String, String> variables, final Map<String, String> configItem)
    {
        this.variables = variables;
        this.configItem = configItem;
    }

    public void storeVariable(final String key, final String value)
    {
        this.variables.put(key, value);
    }

    public void storeConfigItem(final String key, final String value)
    {
        this.configItem.put(key, value);
    }

    public String getVariableByKey(final String key)
    {
        return this.variables.get(key);
    }

    public String getConfigItemByKey(final String key)
    {
        return this.configItem.get(key);
    }

    public String getAllVariables()
    {
        return variables.toString();
    }

    public String getAllConfigItems()
    {
        return configItem.toString();
    }

    public void loadDefaultConfig()
    {
        this.configItem = DefaultValue.defaultValues;
        this.storeConfigItem("httpmethod", DefaultValue.HTTPMETHOD.toString());
        this.storeConfigItem("isXhr", DefaultValue.IS_XHR.toString());
        this.storeConfigItem("encodeParameters", DefaultValue.ENCODE_PARAMETERS.toString());
        this.storeConfigItem("encodeBody", DefaultValue.ENCODE_PARAMETERS.toString());
        this.storeConfigItem("httpcode", DefaultValue.HTTPCODE.toString());
    }

    public Map<String, String> getVariables()
    {
        return variables;
    }

    public Map<String, String> getConfigItem()
    {
        return configItem;
    }

    public String searchFor(final String resolvedTarget)
    {
        String found = "";
        // TODO config raus
        if (variables.containsKey(resolvedTarget))
        {
            found = variables.get(resolvedTarget);
        }
        else
        {
            found = null;
        }
        return found;
    }

}
