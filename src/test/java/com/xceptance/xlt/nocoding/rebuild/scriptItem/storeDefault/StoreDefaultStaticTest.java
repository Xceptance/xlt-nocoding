package com.xceptance.xlt.nocoding.rebuild.scriptItem.storeDefault;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultParameter;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultStatic;
import com.xceptance.xlt.nocoding.util.Constants;

public class StoreDefaultStaticTest extends StoreDefaultTest
{

    @Test
    public void singleStore() throws Throwable
    {
        final String url = "http://www.xceptance.net";
        final ScriptItem store = new StoreDefaultStatic("url", url);
        Assert.assertTrue(context.getDefaultStatic().isEmpty());
        store.execute(context);
        Assert.assertTrue(context.getDefaultStatic().contains(url));
    }

    @Test
    public void deleteStore() throws Throwable
    {
        final String url = "http://www.xceptance.net";
        ScriptItem store = new StoreDefaultStatic("url", url);
        Assert.assertTrue(context.getDefaultStatic().isEmpty());
        store.execute(context);
        Assert.assertTrue(context.getDefaultStatic().contains(url));

        store = new StoreDefaultStatic(url, "delete");
        store.execute(context);
        Assert.assertTrue(context.getDefaultStatic().isEmpty());
    }

    @Test
    public void deleteAllDefaultHeaders() throws Throwable
    {
        final String url = "http://www.xceptance.net";
        final List<ScriptItem> store = new ArrayList<ScriptItem>();
        store.add(new StoreDefaultStatic("url", url));
        store.add(new StoreDefaultStatic("url", url));
        store.add(new StoreDefaultStatic("url", url));
        store.add(new StoreDefaultStatic("url", url));
        store.add(new StoreDefaultStatic("url", url));
        Assert.assertNull(context.getDefaultParameters().get("header_1"));
        int i = 0;
        for (final ScriptItem scriptItem : store)
        {
            scriptItem.execute(context);
            Assert.assertEquals(url, context.getDefaultStatic().get(i));
            i++;
        }
        final ScriptItem deleteIt = new StoreDefaultParameter(Constants.STATIC, "delete");
        deleteIt.execute(context);
        Assert.assertTrue(context.getDefaultParameters().isEmpty());
    }

}
