package com.xceptance.xlt.nocoding.scriptItem.action.response.extractor;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.AbstractExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.CookieExtractor;

public class CookieExtractorTest extends ExtractorTest
{

    @Test
    public void testCookieSelector() throws Throwable
    {
        final AbstractExtractor extractor = new CookieExtractor(mockObjects.cookieName1);
        extractor.execute(context);
        final List<String> result = extractor.getResult();
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(mockObjects.cookieValue1, result.get(0));
    }

    @Test
    public void testCookieSelectorWithVariables() throws Throwable
    {
        final String variableName = "variable_1";
        final String storeValue = mockObjects.cookieName1;
        context.getVariables().store(variableName, storeValue);
        final AbstractExtractor extractor = new CookieExtractor("${" + variableName + "}");
        extractor.execute(context);
        final List<String> result = extractor.getResult();
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(mockObjects.cookieValue1, result.get(0));
    }

}
