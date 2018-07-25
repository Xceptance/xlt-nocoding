package com.xceptance.xlt.nocoding.command.store;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.command.Command;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.context.LightWeightContext;

/**
 * Tests {@link Store}
 *
 * @author ckeiner
 */
public class StoreTest
{
    public Context<?> context;

    /**
     * Creates a new {@link Context}
     */
    @Before
    public void init()
    {
        context = new LightWeightContext(XltProperties.getInstance());
    }

    /**
     * Verifies {@link Store} can store a single item
     *
     * @throws Throwable
     */
    @Test
    public void singleStore() throws Throwable
    {
        final Command store = new Store("test", "text");
        Assert.assertNull(context.getVariables().get("test"));
        store.execute(context);
        Assert.assertEquals("text", context.getVariables().get("test"));
    }

    /**
     * Verifies {@link Store} can overwrite a stored item
     *
     * @throws Throwable
     */
    @Test
    public void overwriteStore() throws Throwable
    {
        Command store = new Store("test", "text");
        Assert.assertNull(context.getVariables().get("test"));
        store.execute(context);
        Assert.assertEquals("text", context.getVariables().get("test"));
        store = new Store("test", "newValue");
        store.execute(context);
        Assert.assertEquals("newValue", context.getVariables().get("test"));
    }

    /**
     * Verifies {@link Store#getValue()} is resolved before storing the variable with its value
     *
     * @throws Throwable
     */
    @Test
    public void resolveStore() throws Throwable
    {
        Command store = new Store("var_1", "val_1");
        Assert.assertNull(context.getVariables().get("var_1"));
        store.execute(context);
        Assert.assertEquals("val_1", context.getVariables().get("var_1"));
        store = new Store("var_2", "${var_1}");
        store.execute(context);
        Assert.assertEquals("val_1", context.getVariables().get("var_2"));
    }

}
