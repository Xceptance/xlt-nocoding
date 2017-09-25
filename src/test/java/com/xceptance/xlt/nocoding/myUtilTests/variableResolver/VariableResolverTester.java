package com.xceptance.xlt.nocoding.myUtilTests.variableResolver;

import org.junit.Assert;
import org.junit.Before;
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
        final DataStorage dataStorage = propertyManager.getDataStorage();
        dataStorage.storeVariable("host", "https://localhost:8443/posters/");
        dataStorage.storeVariable("blub", "s");
        interpreter = new VariableResolver();
    }

    @Test
    public void test()
    {
        try
        {
            String resolved;
            resolved = interpreter.resolveString("${host}", propertyManager);
            Assert.assertEquals("https://localhost:8443/posters/", resolved);
            resolved = interpreter.resolveString("${host}${host}", propertyManager);
            Assert.assertEquals("https://localhost:8443/posters/https://localhost:8443/posters/", resolved);

            resolved = interpreter.resolveString("${ho${blub}t}", propertyManager);
            Assert.assertEquals("https://localhost:8443/posters/", resolved);
            resolved = interpreter.resolveString("${ho${blub}t}${host}", propertyManager);
            Assert.assertEquals("https://localhost:8443/posters/https://localhost:8443/posters/", resolved);

            resolved = interpreter.resolveString("${ho${blub}t}${host}", propertyManager);
            Assert.assertEquals("https://localhost:8443/posters/https://localhost:8443/posters/", resolved);

            resolved = interpreter.resolveString("${NOW.toString()}", propertyManager);
            Assert.assertNotNull(resolved);
            System.out.println(resolved);
            resolved = interpreter.resolveString("${NOW.toString()}", propertyManager);
            Assert.assertNotNull(resolved);
            System.out.println(resolved);

            resolved = interpreter.resolveString("${RANDOM.Email()}", propertyManager);
            Assert.assertNotNull(resolved);
            // resolved = interpreter.resolveString("${DATA.getEmail()}", propertyManager);
            // Assert.assertNotNull(resolved);
            // System.out.println(resolved);
        }
        catch (final Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
