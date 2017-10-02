package com.xceptance.xlt.nocoding.rebuild.scriptItem;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.StoreDefault;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.PropertyManager;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;

public class StoreDefaultTest
{
    public PropertyManager propertyManager;

    @Before
    public void init()
    {
        propertyManager = new PropertyManager(XltProperties.getInstance(), new DataStorage());
    }

    @Test
    public void singleStore() throws Throwable
    {
        final ScriptItem store = new StoreDefault("test", "text");
        Assert.assertNull(propertyManager.getDataStorage().getConfigItemByKey("test"));
        store.execute(propertyManager);
        Assert.assertEquals("text", propertyManager.getDataStorage().getConfigItemByKey("test"));
    }

    @Test
    public void deleteStore() throws Throwable
    {
        ScriptItem store = new StoreDefault("test", "text");
        Assert.assertNull(propertyManager.getDataStorage().getConfigItemByKey("test"));
        store.execute(propertyManager);
        Assert.assertEquals("text", propertyManager.getDataStorage().getConfigItemByKey("test"));
        store = new StoreDefault("test", "delete");
        store.execute(propertyManager);
        Assert.assertNull(propertyManager.getDataStorage().getConfigItemByKey("test"));
    }

    @Test
    public void deleteDefault() throws Throwable
    {
        propertyManager.getDataStorage().loadDefaultConfig();
        ScriptItem store = new StoreDefault(Constants.METHOD, Constants.METHOD_POST);
        Assert.assertEquals(Constants.METHOD_GET, propertyManager.getDataStorage().getConfigItemByKey(Constants.METHOD));
        store.execute(propertyManager);
        Assert.assertEquals(Constants.METHOD_POST, propertyManager.getDataStorage().getConfigItemByKey(Constants.METHOD));
        store = new StoreDefault(Constants.METHOD, "delete");
        store.execute(propertyManager);
        Assert.assertEquals(Constants.METHOD_GET, propertyManager.getDataStorage().getConfigItemByKey(Constants.METHOD));
    }

}
