package com.xceptance.xlt.nocoding.scriptItem.storeDefault;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * Tests {@link StoreDefaultParameter}
 * 
 * @author ckeiner
 */
public class StoreDefaultParameterTest extends StoreDefaultTest
{

    /**
     * Verifies {@link StoreDefaultParameter} can store one default parameter
     * 
     * @throws Throwable
     */
    @Test
    public void singleStore() throws Throwable
    {
        final ScriptItem store = new StoreDefaultParameter("header_1", "value");
        Assert.assertTrue(context.getDefaultParameters().get("header_1").isEmpty());
        store.execute(context);
        Assert.assertEquals("value", context.getDefaultParameters().get("header_1").get(0));
    }

    /**
     * Verifies {@link StoreDefaultParameter} can delete one default parameter
     * 
     * @throws Throwable
     */
    @Test
    public void deleteStore() throws Throwable
    {
        ScriptItem store = new StoreDefaultParameter("param_1", "value");
        Assert.assertTrue(context.getDefaultParameters().get("param_1").isEmpty());
        store.execute(context);
        Assert.assertEquals("value", context.getDefaultParameters().get("param_1").get(0));
        store = new StoreDefaultParameter("param_1", Constants.DELETE);
        store.execute(context);
        Assert.assertTrue(context.getDefaultParameters().get("param_1").isEmpty());
    }

    /**
     * Verifies {@link StoreDefaultParameter} can delete all default parameter
     * 
     * @throws Throwable
     */
    @Test
    public void deleteAllDefaultParameters() throws Throwable
    {
        final List<ScriptItem> store = new ArrayList<ScriptItem>();
        store.add(new StoreDefaultParameter("param_1", "value"));
        store.add(new StoreDefaultParameter("param_2", "value"));
        store.add(new StoreDefaultParameter("param_3", "value"));
        store.add(new StoreDefaultParameter("param_4", "value"));
        store.add(new StoreDefaultParameter("param_5", "value"));
        Assert.assertTrue(context.getDefaultParameters().get("param_1").isEmpty());
        int i = 1;
        for (final ScriptItem scriptItem : store)
        {
            scriptItem.execute(context);
            Assert.assertEquals("value", context.getDefaultParameters().get("param_" + i).get(0));
            i++;
        }
        final ScriptItem deleteIt = new StoreDefaultParameter(Constants.PARAMETERS, Constants.DELETE);
        deleteIt.execute(context);
        Assert.assertTrue(context.getDefaultParameters().get("param_1").isEmpty());
        Assert.assertTrue(context.getDefaultParameters().getItems().isEmpty());
    }

}
