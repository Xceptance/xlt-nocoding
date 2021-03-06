package com.xceptance.xlt.nocoding.command.store;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.command.AbstractContextTest;
import com.xceptance.xlt.nocoding.command.Command;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Tests {@link Store}
 *
 * @author ckeiner
 */
public class StoreTest extends AbstractContextTest
{

    public StoreTest(final Context<?> context)
    {
        super(context);
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

    /**
     * Verifies clearing of entire store with Clear command
     *
     * @throws Throwable
     */
    @Test
    public void clearStore() throws Throwable
    {
        final Command store = new Store("var_1", "val_1");
        store.execute(context);
        Assert.assertEquals("val_1", context.getVariables().get("var_1"));

        final Command clear = new StoreClear();
        clear.execute(context);
        Assert.assertNull(context.getVariables().get("var_1"));
    }
}
