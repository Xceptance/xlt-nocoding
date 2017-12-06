package com.xceptance.xlt.nocoding.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.StorageUnit;
import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.duplicate.CookieStorage;
import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.duplicate.ParameterStorage;
import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.single.StaticUrlStorage;
import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.unique.DefaultKeyValueStorage;
import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.unique.HeaderStorage;
import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.unique.VariableStorage;

public class ContextTest
{
    Context context;

    @Before
    public void init()
    {
        context = new Context(XltProperties.getInstance());
    }

    @Test
    public void testGetStorageUnit()
    {
        StorageUnit storageUnit = null;
        storageUnit = context.getStorageUnit(CookieStorage.class);
        Assert.assertTrue(storageUnit instanceof CookieStorage);
        storageUnit = context.getStorageUnit(ParameterStorage.class);
        Assert.assertTrue(storageUnit instanceof ParameterStorage);
        storageUnit = context.getStorageUnit(StaticUrlStorage.class);
        Assert.assertTrue(storageUnit instanceof StaticUrlStorage);
        storageUnit = context.getStorageUnit(DefaultKeyValueStorage.class);
        Assert.assertTrue(storageUnit instanceof DefaultKeyValueStorage);
        storageUnit = context.getStorageUnit(HeaderStorage.class);
        Assert.assertTrue(storageUnit instanceof HeaderStorage);
        storageUnit = context.getStorageUnit(VariableStorage.class);
        Assert.assertTrue(storageUnit instanceof VariableStorage);
    }
}
