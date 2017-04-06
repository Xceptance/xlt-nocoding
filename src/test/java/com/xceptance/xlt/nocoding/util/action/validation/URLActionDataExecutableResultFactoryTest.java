package com.xceptance.xlt.nocoding.util.action.validation;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.xceptance.xlt.nocoding.util.MockObjects;

public class URLActionDataExecutableResultFactoryTest
{
    private static URLActionDataExecutableResultFactory resultFactory;

    private static MockObjects mockObjects;

    @BeforeClass
    public static void setup()
    {
        resultFactory = new URLActionDataExecutableResultFactory();
        mockObjects = new MockObjects();
        mockObjects.load();
    }

    @Test
    public void testGetResultWithHtmlPage()
    {
        final URLActionDataExecutableResult result = resultFactory.getResult(mockObjects.getHtmlPage());
        final List<String> something = result.getByXPath(mockObjects.xPathString);
        Assert.assertEquals(something.get(0), mockObjects.xpathStringExpected);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetResultWithLightWeightPage()
    {
        final URLActionDataExecutableResult result = resultFactory.getResult(mockObjects.getLightWeightPage());
        @SuppressWarnings("unused")
        final List<String> something = result.getByXPath(mockObjects.xPathString);
    }

    @Test
    public void testGetResultWithWebResponse()
    {
        final URLActionDataExecutableResult result = resultFactory.getResult(mockObjects.getResponse());
        final List<String> something = result.getByXPath(mockObjects.xPathString);
        Assert.assertEquals(something.get(0), mockObjects.xpathStringExpected);
    }

}
