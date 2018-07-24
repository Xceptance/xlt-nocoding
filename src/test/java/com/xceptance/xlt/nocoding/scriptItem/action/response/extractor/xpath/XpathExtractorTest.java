package com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.xpath;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.ExtractorTest;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.xpath.XpathExtractor;
import com.xceptance.xlt.nocoding.util.MockWebResponse;

public class XpathExtractorTest extends ExtractorTest
{

    /**
     * Verifies an unsupported type cannot be parsed
     */
    @Test(expected = IllegalStateException.class)
    public void testUnsupportedType()
    {
        mockObjects.load();
        final MockWebResponse mockWeResponseUnsupported = new MockWebResponse("some content", mockObjects.getUrl(), "unsupported/type");
        context.setWebResponse(mockWeResponseUnsupported);
        new XpathExtractor("").getExecutor(context);
    }

    /**
     * Verifies a supported type can be parsed
     */
    @Test
    public void testSupportedType()
    {
        final MockWebResponse mockWeResponseHtml = new MockWebResponse("some content", mockObjects.getUrl(), "text/html");
        context.setWebResponse(mockWeResponseHtml);
        new XpathExtractor("").getExecutor(context);
        final MockWebResponse mockWeResponseHtml2 = new MockWebResponse("some content", mockObjects.getUrl(), "text/application");
        context.setWebResponse(mockWeResponseHtml2);
        new XpathExtractor("").getExecutor(context);
    }

    /**
     * Verifies XpathExtractor decides on a specific XpathExtractor and gets the result
     */
    @Test
    public void testGetByXPath()
    {
        context.setWebResponse(mockObjects.getResponse());
        final XpathExtractor extractor = new XpathExtractor(mockObjects.xPathString);
        extractor.execute(context);
        final List<String> xPathResults = extractor.getResult();
        Assert.assertEquals(mockObjects.xpathStringExpected, xPathResults.get(0));
    }

}
