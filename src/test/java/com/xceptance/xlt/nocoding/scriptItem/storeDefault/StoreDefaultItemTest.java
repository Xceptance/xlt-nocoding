package com.xceptance.xlt.nocoding.scriptItem.storeDefault;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultItem;
import com.xceptance.xlt.nocoding.util.Constants;

public class StoreDefaultItemTest extends StoreDefaultTest
{

    @Test
    public void singleStore() throws Throwable
    {
        final ScriptItem store = new StoreDefaultItem("test", "text");
        Assert.assertNull(context.getDataStorage().getConfigItemByKey("test"));
        store.execute(context);
        Assert.assertEquals("text", context.getDataStorage().getConfigItemByKey("test"));
    }

    @Test
    public void deleteStore() throws Throwable
    {
        ScriptItem store = new StoreDefaultItem("test", "text");
        Assert.assertNull(context.getDataStorage().getConfigItemByKey("test"));
        store.execute(context);
        Assert.assertEquals("text", context.getDataStorage().getConfigItemByKey("test"));
        store = new StoreDefaultItem("test", "delete");
        store.execute(context);
        Assert.assertNull(context.getDataStorage().getConfigItemByKey("test"));
    }

    @Test
    public void deleteDefault() throws Throwable
    {
        context.getDataStorage().loadDefaultConfig();
        ScriptItem store = new StoreDefaultItem(Constants.METHOD, Constants.METHOD_POST);
        Assert.assertEquals(Constants.METHOD_GET, context.getDataStorage().getConfigItemByKey(Constants.METHOD));
        store.execute(context);
        Assert.assertEquals(Constants.METHOD_POST, context.getDataStorage().getConfigItemByKey(Constants.METHOD));
        store = new StoreDefaultItem(Constants.METHOD, "delete");
        store.execute(context);
        Assert.assertEquals(Constants.METHOD_GET, context.getDataStorage().getConfigItemByKey(Constants.METHOD));
    }

}
