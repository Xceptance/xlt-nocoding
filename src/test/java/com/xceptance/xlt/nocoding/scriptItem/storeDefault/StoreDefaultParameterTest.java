package com.xceptance.xlt.nocoding.scriptItem.storeDefault;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultParameter;
import com.xceptance.xlt.nocoding.util.Constants;

public class StoreDefaultParameterTest extends StoreDefaultTest
{

    @Test
    public void singleStore() throws Throwable
    {
        final ScriptItem store = new StoreDefaultParameter("header_1", "value");
        Assert.assertNull(context.getDefaultParameters().get("header_1"));
        store.execute(context);
        Assert.assertEquals("value", context.getDefaultParameters().get("header_1"));
    }

    @Test
    public void deleteStore() throws Throwable
    {
        ScriptItem store = new StoreDefaultParameter("header_1", "value");
        Assert.assertNull(context.getDefaultParameters().get("header_1"));
        store.execute(context);
        Assert.assertEquals("value", context.getDefaultParameters().get("header_1"));
        store = new StoreDefaultParameter("header_1", "delete");
        store.execute(context);
        Assert.assertNull(context.getDefaultParameters().get("header_1"));
    }

    @Test
    public void deleteAllDefaultHeaders() throws Throwable
    {
        final List<ScriptItem> store = new ArrayList<ScriptItem>();
        store.add(new StoreDefaultParameter("header_1", "value"));
        store.add(new StoreDefaultParameter("header_2", "value"));
        store.add(new StoreDefaultParameter("header_3", "value"));
        store.add(new StoreDefaultParameter("header_4", "value"));
        store.add(new StoreDefaultParameter("header_5", "value"));
        Assert.assertNull(context.getDefaultParameters().get("header_1"));
        int i = 1;
        for (final ScriptItem scriptItem : store)
        {
            scriptItem.execute(context);
            Assert.assertEquals("value", context.getDefaultParameters().get("header_" + i));
            i++;
        }
        final ScriptItem deleteIt = new StoreDefaultParameter(Constants.PARAMETERS, "delete");
        deleteIt.execute(context);
        Assert.assertNull(context.getDefaultParameters().get("header_1"));
        Assert.assertTrue(context.getDefaultParameters().isEmpty());
    }

}
