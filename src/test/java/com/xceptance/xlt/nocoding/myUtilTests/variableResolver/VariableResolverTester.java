package com.xceptance.xlt.nocoding.myUtilTests.variableResolver;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.xceptance.xlt.nocoding.util.PropertyManager;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;
import com.xceptance.xlt.nocoding.util.variableResolver.VariableResolver;

public class VariableResolverTester
{

    public PropertyManager propertyManager;

    public VariableResolver interpreter;

    @Before
    public void init()
    {
        this.propertyManager = new PropertyManager(null, new DataStorage());
        interpreter = new VariableResolver();
    }

    @Test
    public void variableBehindVariable()
    {
        propertyManager.getDataStorage().storeVariable("bub", "${bub}");
        final String resolved = interpreter.resolveString("${bub}", propertyManager);
        Assert.assertEquals("${bub}", resolved);
    }

    @Test
    public void unfinishedDeclaration()
    {
        propertyManager.getDataStorage().storeVariable("test", "t");
        final String resolved = interpreter.resolveString("${tes${test}", propertyManager);
        Assert.assertEquals("${test", resolved);
    }

    @Test
    public void normalParam()
    {
        propertyManager.getDataStorage().storeVariable("host", "https://localhost:8443/posters/");
        final String resolved = interpreter.resolveString("${host}", propertyManager);
        Assert.assertEquals("https://localhost:8443/posters/", resolved);
    }

    @Test
    public void twoNormalParam()
    {
        propertyManager.getDataStorage().storeVariable("host", "https://localhost:8443/posters/");
        final String resolved = interpreter.resolveString("${host}${host}", propertyManager);
        Assert.assertEquals("https://localhost:8443/posters/https://localhost:8443/posters/", resolved);
    }

    @Test
    public void paramInParam()
    {
        propertyManager.getDataStorage().storeVariable("host", "https://localhost:8443/posters/");
        propertyManager.getDataStorage().storeVariable("blub", "s");
        final String resolved = interpreter.resolveString("${ho${blub}t}", propertyManager);
        Assert.assertEquals("https://localhost:8443/posters/", resolved);
    }

    @Test
    public void paramInParamWithAnotherParam()
    {
        propertyManager.getDataStorage().storeVariable("host", "https://localhost:8443/posters/");
        propertyManager.getDataStorage().storeVariable("blub", "s");
        final String resolved = interpreter.resolveString("${ho${blub}t}${host}", propertyManager);
        Assert.assertEquals("https://localhost:8443/posters/https://localhost:8443/posters/", resolved);
    }

    @Test
    public void testBeanShellNow()
    {
        String resolved = interpreter.resolveString("${NOW.toString()}", propertyManager);
        Assert.assertNotNull(resolved);
        resolved = interpreter.resolveString("${NOW.toString()}", propertyManager);
        Assert.assertNotNull(resolved);
    }

    @Test
    public void testBeanShellRandom()
    {
        final String resolved = interpreter.resolveString("${RANDOM.Email()}", propertyManager);
        Assert.assertNotNull(resolved);
    }

    @Ignore
    @Test
    public void testBeanShellData()
    {
        final String resolved = interpreter.resolveString("${DATA.getEmail()}", propertyManager);
        Assert.assertNotNull(resolved);
    }

}
