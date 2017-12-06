package com.xceptance.xlt.nocoding.scriptItem.storeDefault;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.util.Constants;

public class StoreDefaultItemTest extends StoreDefaultTest
{

    @Test
    public void singleStore() throws Throwable
    {
        final ScriptItem store = new StoreDefaultItem("test", "text");
        Assert.assertNull(context.getDefaultItems().get("test"));
        store.execute(context);
        Assert.assertEquals("text", context.getDefaultItems().get("test"));
    }

    @Test
    public void deleteStore() throws Throwable
    {
        ScriptItem store = new StoreDefaultItem("test", "text");
        Assert.assertNull(context.getDefaultItems().get("test"));
        store.execute(context);
        Assert.assertEquals("text", context.getDefaultItems().get("test"));
        store = new StoreDefaultItem("test", Constants.DELETE);
        store.execute(context);
        Assert.assertNull(context.getDefaultItems().get("test"));
    }

    @Test
    public void deleteDefault() throws Throwable
    {
        ScriptItem store = new StoreDefaultItem(Constants.METHOD, Constants.METHOD_POST);
        Assert.assertEquals(Constants.METHOD_GET, context.getDefaultItems().get(Constants.METHOD));
        store.execute(context);
        Assert.assertEquals(Constants.METHOD_POST, context.getDefaultItems().get(Constants.METHOD));
        store = new StoreDefaultItem(Constants.METHOD, Constants.DELETE);
        store.execute(context);
        Assert.assertEquals(Constants.METHOD_GET, context.getDefaultItems().get(Constants.METHOD));
    }

}
