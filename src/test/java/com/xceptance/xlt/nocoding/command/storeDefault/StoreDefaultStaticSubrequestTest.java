package com.xceptance.xlt.nocoding.command.storeDefault;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.command.Command;
import com.xceptance.xlt.nocoding.command.storeDefault.StoreDefaultStaticSubrequest;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * Tests {@link StoreDefaultStaticSubrequest}
 * 
 * @author ckeiner
 */
public class StoreDefaultStaticSubrequestTest extends AbstractStoreDefaultTest
{

    /**
     * Verifies {@link StoreDefaultStaticSubrequest} can store one default static url
     * 
     * @throws Throwable
     */
    @Test
    public void singleStore() throws Throwable
    {
        final String url = "http://www.xceptance.net";
        final Command store = new StoreDefaultStaticSubrequest(url);
        Assert.assertTrue(context.getDefaultStatics().getItems().isEmpty());
        store.execute(context);
        Assert.assertTrue(context.getDefaultStatics().getItems().contains(url));
    }

    /**
     * Verifies {@link StoreDefaultStaticSubrequest} can delete one default static url
     * 
     * @throws Throwable
     */
    @Test
    public void deleteStore() throws Throwable
    {
        final String url = "http://www.xceptance.net";
        Command store = new StoreDefaultStaticSubrequest(url);
        Assert.assertTrue(context.getDefaultStatics().getItems().isEmpty());
        store.execute(context);
        Assert.assertTrue(context.getDefaultStatics().getItems().contains(url));

        store = new StoreDefaultStaticSubrequest(Constants.DELETE);
        store.execute(context);
        Assert.assertTrue(context.getDefaultStatics().getItems().isEmpty());
    }

    /**
     * Verifies {@link StoreDefaultStaticSubrequest} can delete all default static url
     * 
     * @throws Throwable
     */
    @Test
    public void deleteAllDefaultHeaders() throws Throwable
    {
        final String url = "http://www.xceptance.net";
        final List<Command> store = new ArrayList<Command>();
        store.add(new StoreDefaultStaticSubrequest(url));
        store.add(new StoreDefaultStaticSubrequest(url));
        store.add(new StoreDefaultStaticSubrequest(url));
        store.add(new StoreDefaultStaticSubrequest(url));
        store.add(new StoreDefaultStaticSubrequest(url));
        Assert.assertTrue(context.getDefaultStatics().getItems().isEmpty());
        for (final Command scriptItem : store)
        {
            scriptItem.execute(context);
            Assert.assertTrue(context.getDefaultStatics().getItems().contains(url));
        }
        Assert.assertEquals(context.getDefaultStatics().getItems().size(), 5);
        final Command deleteIt = new StoreDefaultStaticSubrequest(Constants.DELETE);
        deleteIt.execute(context);
        Assert.assertTrue(context.getDefaultStatics().getItems().isEmpty());
    }

}
