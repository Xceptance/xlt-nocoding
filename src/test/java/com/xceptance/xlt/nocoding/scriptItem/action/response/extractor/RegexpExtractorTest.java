package com.xceptance.xlt.nocoding.scriptItem.action.response.extractor;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.AbstractExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.RegexpExtractor;

public class RegexpExtractorTest extends ExtractorTest
{

    @Test
    public void testRegExpSelector() throws Throwable
    {
        final AbstractExtractor extractor = new RegexpExtractor(mockObjects.regexString);
        extractor.execute(context);
        final List<String> result = extractor.getResult();
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(mockObjects.regexStringExpected, result.get(0));
    }

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

}
