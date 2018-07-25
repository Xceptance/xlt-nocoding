package com.xceptance.xlt.nocoding.command.action.response.extractor;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.util.MockObjects;

/**
 * Tests for {@link CookieExtractor}
 *
 * @author ckeiner
 */
public class CookieExtractorTest extends ExtractorTest
{

    /**
     * Verifies the {@link CookieExtractor} extracts the cookie from {@link MockObjects}
     *
     * @throws Throwable
     */
    @Test
    public void testCookieExtractor() throws Throwable
    {
        final AbstractExtractor extractor = new CookieExtractor(mockObjects.cookieName1);
        extractor.execute(context);
        final List<String> result = extractor.getResult();
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(mockObjects.cookieValue1, result.get(0));
    }

    /**
     * Verifies the {@link CookieExtractor} can extract a cookie when its a variable
     *
     * @throws Throwable
     */
    @Test
    public void testCookieExtractorWithVariables() throws Throwable
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

    /**
     * Verifies the {@link CookieExtractor} finds nothing if the name of the cookie is not found
     *
     * @throws Throwable
     */
    @Test
    public void testCookieExtractorNoResult() throws Throwable
    {
        final AbstractExtractor extractor = new CookieExtractor(mockObjects.nonExistentCookie);
        extractor.execute(context);
        final List<String> result = extractor.getResult();
        Assert.assertEquals(0, result.size());
    }

}
