package com.xceptance.xlt.nocoding.util.dataStorage;

import java.util.HashMap;
import java.util.Map;

import com.xceptance.xlt.nocoding.util.Constants;

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
        this.storeConfigItem(Constants.METHOD, DefaultValue.METHOD.toString());
        this.storeConfigItem(Constants.XHR, DefaultValue.XHR.toString());
        this.storeConfigItem(Constants.ENCODEPARAMETERS, DefaultValue.ENCODEPARAMETERS.toString());
        this.storeConfigItem(Constants.ENCODEBODY, DefaultValue.ENCODEBODY.toString());
        this.storeConfigItem(Constants.HTTPCODE, DefaultValue.HTTPCODE.toString());
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
