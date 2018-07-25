package com.xceptance.xlt.nocoding.scriptItem.storeDefault;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * Tests {@link StoreDefaultValue}
 * 
 * @author ckeiner
 */
public class StoreDefaultValueTest extends AbstractStoreDefaultTest
{

    /**
     * Verifies {@link StoreDefaultValue} can store one default item
     * 
     * @throws Throwable
     */
    @Test
    public void singleStore() throws Throwable
    {
        final ScriptItem store = new StoreDefaultValue("test", "text");
        Assert.assertNull(context.getDefaultItems().get("test"));
        store.execute(context);
        Assert.assertEquals("text", context.getDefaultItems().get("test"));
    }

    /**
     * Verifies {@link StoreDefaultValue} can delete one default item
     * 
     * @throws Throwable
     */
    @Test
    public void deleteStore() throws Throwable
    {
        ScriptItem store = new StoreDefaultValue("test", "text");
        Assert.assertNull(context.getDefaultItems().get("test"));
        store.execute(context);
        Assert.assertEquals("text", context.getDefaultItems().get("test"));
        store = new StoreDefaultValue("test", Constants.DELETE);
        store.execute(context);
        Assert.assertNull(context.getDefaultItems().get("test"));
    }

    /**
     * Verifies {@link StoreDefaultValue} can delete a default item. However, the fallback value is still there
     * 
     * @throws Throwable
     */
    @Test
    public void deleteDefaultWithFallback() throws Throwable
    {
        ScriptItem store = new StoreDefaultValue(Constants.METHOD, Constants.METHOD_POST);
        Assert.assertEquals(Constants.METHOD_GET, context.getDefaultItems().get(Constants.METHOD));
        store.execute(context);
        Assert.assertEquals(Constants.METHOD_POST, context.getDefaultItems().get(Constants.METHOD));
        store = new StoreDefaultValue(Constants.METHOD, Constants.DELETE);
        store.execute(context);
        Assert.assertEquals(Constants.METHOD_GET, context.getDefaultItems().get(Constants.METHOD));
    }

}
