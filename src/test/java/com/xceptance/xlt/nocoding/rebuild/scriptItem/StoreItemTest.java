package com.xceptance.xlt.nocoding.rebuild.scriptItem;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.StoreItem;
import com.xceptance.xlt.nocoding.util.PropertyManager;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;

public class StoreItemTest
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
        final ScriptItem store = new StoreItem("test", "text");
        Assert.assertNull(propertyManager.getDataStorage().getVariableByKey("test"));
        store.execute(propertyManager);
        Assert.assertEquals("text", propertyManager.getDataStorage().getVariableByKey("test"));
    }

    @Test
    public void overwriteStore() throws Throwable
    {
        ScriptItem store = new StoreItem("test", "text");
        Assert.assertNull(propertyManager.getDataStorage().getVariableByKey("test"));
        store.execute(propertyManager);
        Assert.assertEquals("text", propertyManager.getDataStorage().getVariableByKey("test"));
        store = new StoreItem("test", "delete");
        store.execute(propertyManager);
        Assert.assertEquals("delete", propertyManager.getDataStorage().getVariableByKey("test"));
    }

}
