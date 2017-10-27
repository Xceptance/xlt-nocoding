package com.xceptance.xlt.nocoding.rebuild.scriptItem;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefault;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.Context;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;

public class StoreDefaultTest
{
    public Context context;

    @Before
    public void init()
    {
        context = new Context(XltProperties.getInstance(), new DataStorage());
    }

    @Test
    public void singleStore() throws Throwable
    {
        final ScriptItem store = new StoreDefault("test", "text");
        Assert.assertNull(context.getDataStorage().getConfigItemByKey("test"));
        store.execute(context);
        Assert.assertEquals("text", context.getDataStorage().getConfigItemByKey("test"));
    }

    @Test
    public void deleteStore() throws Throwable
    {
        ScriptItem store = new StoreDefault("test", "text");
        Assert.assertNull(context.getDataStorage().getConfigItemByKey("test"));
        store.execute(context);
        Assert.assertEquals("text", context.getDataStorage().getConfigItemByKey("test"));
        store = new StoreDefault("test", "delete");
        store.execute(context);
        Assert.assertNull(context.getDataStorage().getConfigItemByKey("test"));
    }

    @Test
    public void deleteDefault() throws Throwable
    {
        context.getDataStorage().loadDefaultConfig();
        ScriptItem store = new StoreDefault(Constants.METHOD, Constants.METHOD_POST);
        Assert.assertEquals(Constants.METHOD_GET, context.getDataStorage().getConfigItemByKey(Constants.METHOD));
        store.execute(context);
        Assert.assertEquals(Constants.METHOD_POST, context.getDataStorage().getConfigItemByKey(Constants.METHOD));
        store = new StoreDefault(Constants.METHOD, "delete");
        store.execute(context);
        Assert.assertEquals(Constants.METHOD_GET, context.getDataStorage().getConfigItemByKey(Constants.METHOD));
    }

}
