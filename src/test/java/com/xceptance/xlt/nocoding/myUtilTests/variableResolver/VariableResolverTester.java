package com.xceptance.xlt.nocoding.myUtilTests.variableResolver;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.nocoding.util.PropertyManager;
import com.xceptance.xlt.nocoding.util.DataStorage.DataStorage;
import com.xceptance.xlt.nocoding.util.variableResolver.VariableResolver;

public class VariableResolverTester
{

    public PropertyManager propertyManager;

    @Before
    public void init()
    {
        this.propertyManager = new PropertyManager(null, new DataStorage());
        final DataStorage dataStorage = propertyManager.getDataStorage();
        dataStorage.storeVariable("host", "https://localhost:8443/posters/");
        dataStorage.storeVariable("blub", "s");
    }

    @Test
    public void test()
    {
        try
        {
            String resolved;
            // resolved = VariableResolver.resolveString("${host}", propertyManager);
            // Assert.assertEquals("https://localhost:8443/posters/", resolved);
            // resolved = VariableResolver.resolveString("${host}${host}", propertyManager);
            // Assert.assertEquals("https://localhost:8443/posters/https://localhost:8443/posters/", resolved);
            //
            // resolved = VariableResolver.resolveString("${ho${blub}t}", propertyManager);
            // Assert.assertEquals("https://localhost:8443/posters/", resolved);
            // resolved = VariableResolver.resolveString("${ho${blub}t}${host}", propertyManager);
            // Assert.assertEquals("https://localhost:8443/posters/https://localhost:8443/posters/", resolved);
            //
            // resolved = VariableResolver.resolveString("${ho${blub}t}${host}", propertyManager);
            // Assert.assertEquals("https://localhost:8443/posters/https://localhost:8443/posters/", resolved);
            //
            // resolved = VariableResolver.resolveString("${RANDOM.Email()}", propertyManager);
            // Assert.assertNotNull(resolved);
            resolved = VariableResolver.resolveString("${DATA.getEmail()}", propertyManager);
            Assert.assertNotNull(resolved);
            System.out.println(resolved);

            // resolved = VariableResolver.resolveString("${NOW.toString()}", propertyManager);
            // Assert.assertNotNull(resolved);
            // System.out.println(resolved);
            // resolved = VariableResolver.resolveString("${NOW.toString()}", propertyManager);
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
