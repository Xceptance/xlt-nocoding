package com.xceptance.xlt.nocoding.scriptItem.storeDefault;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultHeader;
import com.xceptance.xlt.nocoding.util.Constants;

public class StoreDefaultHeaderTest extends StoreDefaultTest
{
    @Test
    public void singleStore() throws Throwable
    {
        final ScriptItem store = new StoreDefaultHeader("header_1", "value");
        Assert.assertNull(context.getDefaultHeaders().get("header_1"));
        store.execute(context);
        Assert.assertEquals("value", context.getDefaultHeaders().get("header_1"));
    }

    @Test
    public void deleteStore() throws Throwable
    {
        ScriptItem store = new StoreDefaultHeader("header_1", "value");
        Assert.assertNull(context.getDefaultHeaders().get("header_1"));
        store.execute(context);
        Assert.assertEquals("value", context.getDefaultHeaders().get("header_1"));
        store = new StoreDefaultHeader("header_1", "delete");
        store.execute(context);
        Assert.assertNull(context.getDefaultHeaders().get("header_1"));
    }

    @Test
    public void deleteAllDefaultHeaders() throws Throwable
    {
        final List<ScriptItem> store = new ArrayList<ScriptItem>();
        store.add(new StoreDefaultHeader("header_1", "value"));
        store.add(new StoreDefaultHeader("header_2", "value"));
        store.add(new StoreDefaultHeader("header_3", "value"));
        store.add(new StoreDefaultHeader("header_4", "value"));
        store.add(new StoreDefaultHeader("header_5", "value"));
        Assert.assertNull(context.getDefaultHeaders().get("header_1"));
        int i = 1;
        for (final ScriptItem scriptItem : store)
        {
            scriptItem.execute(context);
            Assert.assertEquals("value", context.getDefaultHeaders().get("header_" + i));
            i++;
        }
        final ScriptItem deleteIt = new StoreDefaultHeader(Constants.HEADERS, "delete");
        deleteIt.execute(context);
        Assert.assertNull(context.getDefaultHeaders().get("header_1"));
        Assert.assertTrue(context.getDefaultHeaders().isEmpty());
    }

}
