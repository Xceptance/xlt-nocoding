package com.xceptance.xlt.nocoding.rebuild.util.context;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.Context;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;
import com.xceptance.xlt.nocoding.util.dataStorage.DefaultValue;

public class DataStorageTest
{
    private Context context;

    /**
     * Instantiate the fields and set answers to localhost/posters/ and localhost/posters/POST
     */
    @Before
    public void init()
    {
        this.context = new Context(XltProperties.getInstance(), new DataStorage());
    }

    @Test
    public void oneKey()
    {
        final String key = "test";
        final String value = "t";
        context.getDataStorage().storeVariable(key, value);
        context.getDataStorage().storeConfigItem(key, value);

        Assert.assertEquals("t", context.getDataStorage().getVariableByKey(key));
        Assert.assertEquals("t", context.getDataStorage().getConfigItemByKey(key));
    }

    @Test
    public void storageNotShared()
    {
        final String key = "test";
        final String value = "t";
        context.getDataStorage().storeVariable(key, value);

        Assert.assertEquals("t", context.getDataStorage().getVariableByKey(key));
        Assert.assertEquals(null, context.getDataStorage().getConfigItemByKey(key));

        final String key2 = "testing";
        final String value2 = "testing";
        context.getDataStorage().storeConfigItem(key2, value2);

        Assert.assertEquals(null, context.getDataStorage().getVariableByKey(key2));
        Assert.assertEquals("testing", context.getDataStorage().getConfigItemByKey(key2));
    }

    @Test
    public void sameKey()
    {
        String key = "test";
        String value = "t";
        context.getDataStorage().storeVariable(key, value);
        context.getDataStorage().storeConfigItem(key, value);

        Assert.assertEquals("t", context.getDataStorage().getVariableByKey(key));
        Assert.assertEquals("t", context.getDataStorage().getConfigItemByKey(key));

        key = "test";
        value = "e";
        context.getDataStorage().storeVariable(key, value);
        context.getDataStorage().storeConfigItem(key, value);

        Assert.assertEquals("e", context.getDataStorage().getVariableByKey(key));
        Assert.assertEquals("e", context.getDataStorage().getConfigItemByKey(key));
        Assert.assertNotEquals("t", context.getDataStorage().getVariableByKey(key));
        Assert.assertNotEquals("t", context.getDataStorage().getConfigItemByKey(key));

        key = "test";
        value = "s";
        context.getDataStorage().storeVariable(key, value);
        context.getDataStorage().storeConfigItem(key, value);

        Assert.assertEquals("s", context.getDataStorage().getVariableByKey(key));
        Assert.assertEquals("s", context.getDataStorage().getConfigItemByKey(key));
        Assert.assertNotEquals("t", context.getDataStorage().getVariableByKey(key));
        Assert.assertNotEquals("t", context.getDataStorage().getConfigItemByKey(key));
        Assert.assertNotEquals("e", context.getDataStorage().getVariableByKey(key));
        Assert.assertNotEquals("e", context.getDataStorage().getConfigItemByKey(key));
    }

    @Test
    public void noResult()
    {
        final String key = "test";

        Assert.assertEquals(null, context.getDataStorage().getVariableByKey(key));
        Assert.assertEquals(null, context.getDataStorage().getConfigItemByKey(key));

    }

    @Test
    public void deleteConfigItem()
    {
        // TODO set a new default value, then delete it, forcing it to be the default value
        final String key = "test";
        final String value = "t";

        context.getDataStorage().storeConfigItem(key, value);
        Assert.assertEquals(value, context.getDataStorage().getConfigItemByKey(key));
        context.getDataStorage().removeConfigItem(key);
        Assert.assertNotEquals(value, context.getDataStorage().getConfigItemByKey(key));
        Assert.assertEquals(null, context.getDataStorage().getConfigItemByKey(key));

        final String fallback = "est";
        context.getDataStorage().storeConfigItem(key, value, fallback);
        Assert.assertEquals(value, context.getDataStorage().getConfigItemByKey(key));
        context.getDataStorage().removeConfigItem(key);
        Assert.assertEquals(fallback, context.getDataStorage().getConfigItemByKey(key));

    }

    @Test
    public void loadDefaultValues()
    {
        context.getDataStorage().loadDefaultConfig();

        Assert.assertEquals(DefaultValue.METHOD, context.getDataStorage().getConfigItemByKey(Constants.METHOD));
        Assert.assertEquals(DefaultValue.HTTPCODE, context.getDataStorage().getConfigItemByKey(Constants.HTTPCODE));
        Assert.assertEquals(DefaultValue.ENCODEBODY, context.getDataStorage().getConfigItemByKey(Constants.ENCODEBODY));
        Assert.assertEquals(DefaultValue.ENCODEPARAMETERS, context.getDataStorage().getConfigItemByKey(Constants.ENCODEPARAMETERS));
        Assert.assertEquals(DefaultValue.XHR, context.getDataStorage().getConfigItemByKey(Constants.XHR));

    }

    @Test
    public void overwriteDefaultValues()
    {
        final String method = HttpMethod.POST.toString();
        final String httpcode = "303";
        final String encodebody = "true";
        final String encodeparameters = "true";
        final String xhr = "true";

        context.getDataStorage().loadDefaultConfig();
        Assert.assertEquals(DefaultValue.METHOD, context.getDataStorage().getConfigItemByKey(Constants.METHOD));
        Assert.assertEquals(DefaultValue.HTTPCODE, context.getDataStorage().getConfigItemByKey(Constants.HTTPCODE));
        Assert.assertEquals(DefaultValue.ENCODEBODY, context.getDataStorage().getConfigItemByKey(Constants.ENCODEBODY));
        Assert.assertEquals(DefaultValue.ENCODEPARAMETERS, context.getDataStorage().getConfigItemByKey(Constants.ENCODEPARAMETERS));
        Assert.assertEquals(DefaultValue.XHR, context.getDataStorage().getConfigItemByKey(Constants.XHR));

        context.getDataStorage().storeConfigItem(Constants.METHOD, method);
        context.getDataStorage().storeConfigItem(Constants.HTTPCODE, httpcode);
        context.getDataStorage().storeConfigItem(Constants.ENCODEBODY, encodebody);
        context.getDataStorage().storeConfigItem(Constants.ENCODEPARAMETERS, encodeparameters);
        context.getDataStorage().storeConfigItem(Constants.XHR, xhr);

        Assert.assertEquals(method, context.getDataStorage().getConfigItemByKey(Constants.METHOD));
        Assert.assertEquals(httpcode, context.getDataStorage().getConfigItemByKey(Constants.HTTPCODE));
        Assert.assertEquals(encodebody, context.getDataStorage().getConfigItemByKey(Constants.ENCODEBODY));
        Assert.assertEquals(encodeparameters, context.getDataStorage().getConfigItemByKey(Constants.ENCODEPARAMETERS));
        Assert.assertEquals(xhr, context.getDataStorage().getConfigItemByKey(Constants.XHR));

        context.getDataStorage().removeConfigItem(Constants.METHOD);
        context.getDataStorage().removeConfigItem(Constants.HTTPCODE);
        context.getDataStorage().removeConfigItem(Constants.ENCODEBODY);
        context.getDataStorage().removeConfigItem(Constants.ENCODEPARAMETERS);
        context.getDataStorage().removeConfigItem(Constants.XHR);

        Assert.assertEquals(DefaultValue.METHOD, context.getDataStorage().getConfigItemByKey(Constants.METHOD));
        Assert.assertEquals(DefaultValue.HTTPCODE, context.getDataStorage().getConfigItemByKey(Constants.HTTPCODE));
        Assert.assertEquals(DefaultValue.ENCODEBODY, context.getDataStorage().getConfigItemByKey(Constants.ENCODEBODY));
        Assert.assertEquals(DefaultValue.ENCODEPARAMETERS, context.getDataStorage().getConfigItemByKey(Constants.ENCODEPARAMETERS));
        Assert.assertEquals(DefaultValue.XHR, context.getDataStorage().getConfigItemByKey(Constants.XHR));

    }

}
