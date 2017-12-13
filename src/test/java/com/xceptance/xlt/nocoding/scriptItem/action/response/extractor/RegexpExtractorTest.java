package com.xceptance.xlt.nocoding.scriptItem.action.response.extractor;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.util.MockObjects;

/**
 * Tests for {@link RegexpExtractor}
 * 
 * @author ckeiner
 */
public class RegexpExtractorTest extends ExtractorTest
{

    /**
     * Verifies {@link RegexpExtractor} extracts the correct string with the pattern from {@link MockObjects#regexString}
     * 
     * @throws Throwable
     */
    @Test
    public void testRegExpSelector() throws Throwable
    {
        final AbstractExtractor extractor = new RegexpExtractor(mockObjects.regexString);
        extractor.execute(context);
        final List<String> result = extractor.getResult();
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(mockObjects.regexStringExpected, result.get(0));
    }

    /**
     * Verifies {@link RegexpExtractor} extracts the correct string with the pattern from {@link MockObjects#regexString} if
     * the pattern is a variable
     * 
     * @throws Throwable
     */
    @Test
    public void testRegExpSelectorWithVariables() throws Throwable
    {
        final String variableName = "variable_1";
        final String storeValue = mockObjects.regexString;
        context.getVariables().store(variableName, storeValue);
        final AbstractExtractor extractor = new RegexpExtractor("${" + variableName + "}");
        extractor.execute(context);
        final List<String> result = extractor.getResult();
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(mockObjects.regexStringExpected, result.get(0));
    }

    /**
     * Verifies {@link RegexpExtractor} finds nothing if the pattern results in nothing
     * 
     * @throws Throwable
     */
    @Test
    public void testRegExpSelectorNoResult() throws Throwable
    {
        final AbstractExtractor extractor = new RegexpExtractor(mockObjects.regexStringNoResult);
        extractor.execute(context);
        final List<String> result = extractor.getResult();
        Assert.assertEquals(0, result.size());
    }

}
