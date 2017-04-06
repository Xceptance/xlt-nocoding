package com.xceptance.xlt.nocoding.util.action.validation;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.nocoding.util.MockObjects;
import com.xceptance.xlt.nocoding.util.MockWebResponse;

public class XPathWithParseableWebResponseTest
{
    private static MockObjects mockObjects;

    @BeforeClass
    public static void setup()
    {
        mockObjects = new MockObjects();
        mockObjects.load();
    }

    @Test
    public void testIsParseable()
    {
        final MockWebResponse mockWeResponseUnsupported = new MockWebResponse("some content", mockObjects.getUrl(), "unsupported/type");
        Assert.assertFalse(XPathWithParseableWebResponse.isWebResponseParseable(mockWeResponseUnsupported));
        final MockWebResponse mockWeResponseHtml = new MockWebResponse("some content", mockObjects.getUrl(), "text/html");
        Assert.assertTrue(XPathWithParseableWebResponse.isWebResponseParseable(mockWeResponseHtml));
        final MockWebResponse mockWeResponseHtml2 = new MockWebResponse("some content", mockObjects.getUrl(), "text/application");
        Assert.assertTrue(XPathWithParseableWebResponse.isWebResponseParseable(mockWeResponseHtml2));

    }

    @Test
    public void testGetByXPath()
    {
        final WebResponse response = mockObjects.getResponse();
        final XPathWithParseableWebResponse thing = new XPathWithParseableWebResponse(response);
        final List<String> xPathResults = thing.getByXPath(mockObjects.xPathString);
        Assert.assertEquals(mockObjects.xpathStringExpected, xPathResults.get(0));
    }

}
