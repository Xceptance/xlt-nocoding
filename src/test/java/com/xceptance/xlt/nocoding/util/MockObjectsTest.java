package com.xceptance.xlt.nocoding.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MockObjectsTest
{
    String urlString = "http://localhost:8080";

    String urlStringGoogle = "https://www.google.de";

    private MockObjects mockObjects;

    @Before
    public void setup()
    {

    }

    @Test
    public void testConstructor()
    {
        mockObjects = new MockObjects();
        Assert.assertEquals(urlString, mockObjects.getUrlString());

        mockObjects = new MockObjects(urlStringGoogle);
        Assert.assertEquals(urlStringGoogle, mockObjects.getUrlString());
    }

    @Test
    public void testLoadResponse()
    {
        mockObjects = new MockObjects();
        mockObjects.load();
        System.err.println("-------------LIGHTWEIGHT-----------------");
        System.err.println(mockObjects.getLightWeightPage().getContent().replaceAll("\\s+", ""));
        System.err.println("-------------HTMLPAGE-----------------");
        System.err.println(mockObjects.getHtmlPage().getWebResponse().getContentAsString().replaceAll("\\s+", ""));
    }

}
