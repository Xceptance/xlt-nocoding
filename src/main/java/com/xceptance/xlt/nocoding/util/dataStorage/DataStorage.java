package com.xceptance.xlt.nocoding.util.dataStorage;

import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.duplicate.DuplicateStorage;
import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.single.SingleStorage;
import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.unique.DefaultKeyValueStorage;
import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.unique.UniqueStorage;

/**
 * Handles all data storage related matters. This means, the class saves variables, default
 * definitions/headers/parameters/static requests.
 * 
 * @author ckeiner
 */
public class DataStorage
{
    protected DuplicateStorage defaultCookies;

    protected DuplicateStorage defaultParameters;

    protected SingleStorage defaultStatics;

    protected UniqueStorage defaultHeaders;

    protected UniqueStorage variables;

    protected DefaultKeyValueStorage defaultItems;

    public DataStorage()
    {
        this.defaultCookies = new DuplicateStorage();
        this.defaultParameters = new DuplicateStorage();
        this.defaultStatics = new SingleStorage();
        this.defaultHeaders = new UniqueStorage();
        this.variables = new UniqueStorage();
        this.defaultItems = new DefaultKeyValueStorage();
    }

    public DuplicateStorage getDefaultCookies()
    {
        return defaultCookies;
    }

    public DuplicateStorage getDefaultParameters()
    {
        return defaultParameters;
    }

    public SingleStorage getDefaultStatics()
    {
        return defaultStatics;
    }

    public UniqueStorage getDefaultHeaders()
    {
        return defaultHeaders;
    }

    public UniqueStorage getVariables()
    {
        return variables;
    }

    public DefaultKeyValueStorage getDefaultItems()
    {
        return defaultItems;
    }

}
