package com.xceptance.xlt.nocoding.scriptItem.storeDefault;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * Tests {@link StoreDefaultStatic}
 * 
 * @author ckeiner
 */
public class StoreDefaultStaticTest extends StoreDefaultTest
{

    /**
     * Verifies {@link StoreDefaultStatic} can store one default static url
     * 
     * @throws Throwable
     */
    @Test
    public void singleStore() throws Throwable
    {
        final String url = "http://www.xceptance.net";
        final ScriptItem store = new StoreDefaultStatic(url);
        Assert.assertTrue(context.getDefaultStatics().getItems().isEmpty());
        store.execute(context);
        Assert.assertTrue(context.getDefaultStatics().getItems().contains(url));
    }

    /**
     * Verifies {@link StoreDefaultStatic} can delete one default static url
     * 
     * @throws Throwable
     */
    @Test
    public void deleteStore() throws Throwable
    {
        final String url = "http://www.xceptance.net";
        ScriptItem store = new StoreDefaultStatic(url);
        Assert.assertTrue(context.getDefaultStatics().getItems().isEmpty());
        store.execute(context);
        Assert.assertTrue(context.getDefaultStatics().getItems().contains(url));

        store = new StoreDefaultStatic(Constants.DELETE);
        store.execute(context);
        Assert.assertTrue(context.getDefaultStatics().getItems().isEmpty());
    }

    /**
     * Verifies {@link StoreDefaultStatic} can delete all default static url
     * 
     * @throws Throwable
     */
    @Test
    public void deleteAllDefaultHeaders() throws Throwable
    {
        final String url = "http://www.xceptance.net";
        final List<ScriptItem> store = new ArrayList<ScriptItem>();
        store.add(new StoreDefaultStatic(url));
        store.add(new StoreDefaultStatic(url));
        store.add(new StoreDefaultStatic(url));
        store.add(new StoreDefaultStatic(url));
        store.add(new StoreDefaultStatic(url));
        Assert.assertTrue(context.getDefaultStatics().getItems().isEmpty());
        for (final ScriptItem scriptItem : store)
        {
            scriptItem.execute(context);
            Assert.assertTrue(context.getDefaultStatics().getItems().contains(url));
        }
        Assert.assertEquals(context.getDefaultStatics().getItems().size(), 5);
        final ScriptItem deleteIt = new StoreDefaultStatic(Constants.DELETE);
        deleteIt.execute(context);
        Assert.assertTrue(context.getDefaultStatics().getItems().isEmpty());
    }

}
