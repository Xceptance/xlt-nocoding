package com.xceptance.xlt.nocoding.scriptItem;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.util.Context;

public class StoreItemTest
{
    public Context context;

    @Before
    public void init()
    {
        context = new Context(XltProperties.getInstance());
    }

    @Test
    public void singleStore() throws Throwable
    {
        final ScriptItem store = new StoreItem("test", "text");
        Assert.assertNull(context.getVariables().get("test"));
        store.execute(context);
        Assert.assertEquals("text", context.getVariables().get("test"));
    }

    @Test
    public void overwriteStore() throws Throwable
    {
        ScriptItem store = new StoreItem("test", "text");
        Assert.assertNull(context.getVariables().get("test"));
        store.execute(context);
        Assert.assertEquals("text", context.getVariables().get("test"));
        store = new StoreItem("test", "newValue");
        store.execute(context);
        Assert.assertEquals("newValue", context.getVariables().get("test"));
    }

    @Test
    public void resolveStore() throws Throwable
    {
        ScriptItem store = new StoreItem("var_1", "val_1");
        Assert.assertNull(context.getVariables().get("var_1"));
        store.execute(context);
        Assert.assertEquals("val_1", context.getVariables().get("var_1"));
        store = new StoreItem("var_2", "${var_1}");
        store.execute(context);
        Assert.assertEquals("val_1", context.getVariables().get("var_2"));
    }

}
