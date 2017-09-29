package com.xceptance.xlt.nocoding.rebuild.util.propertyManager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.PropertyManager;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;
import com.xceptance.xlt.nocoding.util.dataStorage.DefaultValue;

public class DataStorageTest
{
    private PropertyManager propertyManager;

    /**
     * Instantiate the fields and set answers to localhost/posters/ and localhost/posters/POST
     */
    @Before
    public void init()
    {
        this.propertyManager = new PropertyManager(XltProperties.getInstance(), new DataStorage());
    }

    @Test
    public void oneKey()
    {
        final String key = "test";
        final String value = "t";
        propertyManager.getDataStorage().storeVariable(key, value);
        propertyManager.getDataStorage().storeConfigItem(key, value);

        Assert.assertEquals("t", propertyManager.getDataStorage().getVariableByKey(key));
        Assert.assertEquals("t", propertyManager.getDataStorage().getConfigItemByKey(key));
    }

    @Test
    public void storageNotShared()
    {
        final String key = "test";
        final String value = "t";
        propertyManager.getDataStorage().storeVariable(key, value);

        Assert.assertEquals("t", propertyManager.getDataStorage().getVariableByKey(key));
        Assert.assertEquals(null, propertyManager.getDataStorage().getConfigItemByKey(key));

        final String key2 = "testing";
        final String value2 = "testing";
        propertyManager.getDataStorage().storeConfigItem(key2, value2);

        Assert.assertEquals(null, propertyManager.getDataStorage().getVariableByKey(key2));
        Assert.assertEquals("testing", propertyManager.getDataStorage().getConfigItemByKey(key2));
    }

    @Test
    public void sameKey()
    {
        String key = "test";
        String value = "t";
        propertyManager.getDataStorage().storeVariable(key, value);
        propertyManager.getDataStorage().storeConfigItem(key, value);

        Assert.assertEquals("t", propertyManager.getDataStorage().getVariableByKey(key));
        Assert.assertEquals("t", propertyManager.getDataStorage().getConfigItemByKey(key));

        key = "test";
        value = "e";
        propertyManager.getDataStorage().storeVariable(key, value);
        propertyManager.getDataStorage().storeConfigItem(key, value);

        Assert.assertEquals("e", propertyManager.getDataStorage().getVariableByKey(key));
        Assert.assertEquals("e", propertyManager.getDataStorage().getConfigItemByKey(key));
        Assert.assertNotEquals("t", propertyManager.getDataStorage().getVariableByKey(key));
        Assert.assertNotEquals("t", propertyManager.getDataStorage().getConfigItemByKey(key));

        key = "test";
        value = "s";
        propertyManager.getDataStorage().storeVariable(key, value);
        propertyManager.getDataStorage().storeConfigItem(key, value);

        Assert.assertEquals("s", propertyManager.getDataStorage().getVariableByKey(key));
        Assert.assertEquals("s", propertyManager.getDataStorage().getConfigItemByKey(key));
        Assert.assertNotEquals("t", propertyManager.getDataStorage().getVariableByKey(key));
        Assert.assertNotEquals("t", propertyManager.getDataStorage().getConfigItemByKey(key));
        Assert.assertNotEquals("e", propertyManager.getDataStorage().getVariableByKey(key));
        Assert.assertNotEquals("e", propertyManager.getDataStorage().getConfigItemByKey(key));
    }

    @Test
    public void noResult()
    {
        final String key = "test";

        Assert.assertEquals(null, propertyManager.getDataStorage().getVariableByKey(key));
        Assert.assertEquals(null, propertyManager.getDataStorage().getConfigItemByKey(key));

    }

    @Test
    public void deleteConfigItem()
    {
        // TODO set a new default value, then delete it, forcing it to be the default value
        final String key = "test";
        final String value = "t";

        propertyManager.getDataStorage().storeConfigItem(key, value);
        Assert.assertEquals(value, propertyManager.getDataStorage().getConfigItemByKey(key));
        propertyManager.getDataStorage().removeConfigItem(key);
        Assert.assertNotEquals(value, propertyManager.getDataStorage().getConfigItemByKey(key));
        Assert.assertEquals(null, propertyManager.getDataStorage().getConfigItemByKey(key));

        final String fallback = "est";
        propertyManager.getDataStorage().storeConfigItem(key, value, fallback);
        Assert.assertEquals(value, propertyManager.getDataStorage().getConfigItemByKey(key));
        propertyManager.getDataStorage().removeConfigItem(key);
        Assert.assertEquals(fallback, propertyManager.getDataStorage().getConfigItemByKey(key));

    }

    @Test
    public void loadDefaultValues()
    {
        propertyManager.getDataStorage().loadDefaultConfig();

        Assert.assertEquals(DefaultValue.METHOD, propertyManager.getDataStorage().getConfigItemByKey(Constants.METHOD));
        Assert.assertEquals(DefaultValue.HTTPCODE, propertyManager.getDataStorage().getConfigItemByKey(Constants.HTTPCODE));
        Assert.assertEquals(DefaultValue.ENCODEBODY, propertyManager.getDataStorage().getConfigItemByKey(Constants.ENCODEBODY));
        Assert.assertEquals(DefaultValue.ENCODEPARAMETERS, propertyManager.getDataStorage().getConfigItemByKey(Constants.ENCODEPARAMETERS));
        Assert.assertEquals(DefaultValue.XHR, propertyManager.getDataStorage().getConfigItemByKey(Constants.XHR));

    }

    @Test
    public void overwriteDefaultValues()
    {
        final String method = HttpMethod.POST.toString();
        final String httpcode = "303";
        final String encodebody = "true";
        final String encodeparameters = "true";
        final String xhr = "true";

        propertyManager.getDataStorage().loadDefaultConfig();
        Assert.assertEquals(DefaultValue.METHOD, propertyManager.getDataStorage().getConfigItemByKey(Constants.METHOD));
        Assert.assertEquals(DefaultValue.HTTPCODE, propertyManager.getDataStorage().getConfigItemByKey(Constants.HTTPCODE));
        Assert.assertEquals(DefaultValue.ENCODEBODY, propertyManager.getDataStorage().getConfigItemByKey(Constants.ENCODEBODY));
        Assert.assertEquals(DefaultValue.ENCODEPARAMETERS, propertyManager.getDataStorage().getConfigItemByKey(Constants.ENCODEPARAMETERS));
        Assert.assertEquals(DefaultValue.XHR, propertyManager.getDataStorage().getConfigItemByKey(Constants.XHR));

        propertyManager.getDataStorage().storeConfigItem(Constants.METHOD, method);
        propertyManager.getDataStorage().storeConfigItem(Constants.HTTPCODE, httpcode);
        propertyManager.getDataStorage().storeConfigItem(Constants.ENCODEBODY, encodebody);
        propertyManager.getDataStorage().storeConfigItem(Constants.ENCODEPARAMETERS, encodeparameters);
        propertyManager.getDataStorage().storeConfigItem(Constants.XHR, xhr);

        Assert.assertEquals(method, propertyManager.getDataStorage().getConfigItemByKey(Constants.METHOD));
        Assert.assertEquals(httpcode, propertyManager.getDataStorage().getConfigItemByKey(Constants.HTTPCODE));
        Assert.assertEquals(encodebody, propertyManager.getDataStorage().getConfigItemByKey(Constants.ENCODEBODY));
        Assert.assertEquals(encodeparameters, propertyManager.getDataStorage().getConfigItemByKey(Constants.ENCODEPARAMETERS));
        Assert.assertEquals(xhr, propertyManager.getDataStorage().getConfigItemByKey(Constants.XHR));

        propertyManager.getDataStorage().removeConfigItem(Constants.METHOD);
        propertyManager.getDataStorage().removeConfigItem(Constants.HTTPCODE);
        propertyManager.getDataStorage().removeConfigItem(Constants.ENCODEBODY);
        propertyManager.getDataStorage().removeConfigItem(Constants.ENCODEPARAMETERS);
        propertyManager.getDataStorage().removeConfigItem(Constants.XHR);

        Assert.assertEquals(DefaultValue.METHOD, propertyManager.getDataStorage().getConfigItemByKey(Constants.METHOD));
        Assert.assertEquals(DefaultValue.HTTPCODE, propertyManager.getDataStorage().getConfigItemByKey(Constants.HTTPCODE));
        Assert.assertEquals(DefaultValue.ENCODEBODY, propertyManager.getDataStorage().getConfigItemByKey(Constants.ENCODEBODY));
        Assert.assertEquals(DefaultValue.ENCODEPARAMETERS, propertyManager.getDataStorage().getConfigItemByKey(Constants.ENCODEPARAMETERS));
        Assert.assertEquals(DefaultValue.XHR, propertyManager.getDataStorage().getConfigItemByKey(Constants.XHR));

    }

}
