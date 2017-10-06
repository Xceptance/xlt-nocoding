package com.xceptance.xlt.nocoding.rebuild.scriptItem;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.StoreItem;
import com.xceptance.xlt.nocoding.util.Context;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;

public class StoreItemTest
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
        final ScriptItem store = new StoreItem("test", "text");
        Assert.assertNull(context.getDataStorage().getVariableByKey("test"));
        store.execute(context);
        Assert.assertEquals("text", context.getDataStorage().getVariableByKey("test"));
    }

    @Test
    public void overwriteStore() throws Throwable
    {
        ScriptItem store = new StoreItem("test", "text");
        Assert.assertNull(context.getDataStorage().getVariableByKey("test"));
        store.execute(context);
        Assert.assertEquals("text", context.getDataStorage().getVariableByKey("test"));
        store = new StoreItem("test", "delete");
        store.execute(context);
        Assert.assertEquals("delete", context.getDataStorage().getVariableByKey("test"));
    }

}
