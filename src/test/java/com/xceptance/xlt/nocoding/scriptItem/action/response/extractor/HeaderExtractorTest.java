package com.xceptance.xlt.nocoding.scriptItem.action.response.extractor;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.AbstractExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.HeaderExtractor;

public class HeaderExtractorTest extends ExtractorTest
{

    @Test
    public void testHeaderSelector() throws Throwable
    {
        final AbstractExtractor extractor = new HeaderExtractor("Set-Cookie");
        extractor.execute(context);
        final List<String> result = extractor.getResult();
        Assert.assertEquals(3, result.size());
        Assert.assertEquals(mockObjects.cookieName1 + "=" + mockObjects.cookieValue1, result.get(0));
        Assert.assertEquals(mockObjects.cookieName2 + "=" + mockObjects.cookieValue2, result.get(1));
        Assert.assertEquals(mockObjects.cookieName3 + "=" + mockObjects.cookieValue3, result.get(2));
    }

    @Test
    public void testHeaderSelectorWithVariables() throws Throwable
    {
        final String variableName = "variable_1";
        final String storeValue = "Set-Cookie";
        context.getVariables().store(variableName, storeValue);
        final AbstractExtractor extractor = new HeaderExtractor("${" + variableName + "}");
        extractor.execute(context);
        final List<String> result = extractor.getResult();
        Assert.assertEquals(3, result.size());
        Assert.assertEquals(mockObjects.cookieName1 + "=" + mockObjects.cookieValue1, result.get(0));
        Assert.assertEquals(mockObjects.cookieName2 + "=" + mockObjects.cookieValue2, result.get(1));
        Assert.assertEquals(mockObjects.cookieName3 + "=" + mockObjects.cookieValue3, result.get(2));
    }
}
