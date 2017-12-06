package com.xceptance.xlt.nocoding.scriptItem.action.response.selector;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class RegexpSelectorTest extends SelectorTest
{

    @Test
    public void testRegExpSelector() throws Throwable
    {
        final AbstractSelector selector = new RegexpSelector(mockObjects.regexString);
        selector.execute(context);
        final List<String> result = selector.getResult();
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(mockObjects.regexStringExpected, result.get(0));
    }

    @Test
    public void testRegExpSelectorWithVariables() throws Throwable
    {
        final String variableName = "variable_1";
        final String storeValue = mockObjects.regexString;
        context.getVariables().store(variableName, storeValue);
        final AbstractSelector selector = new RegexpSelector("${" + variableName + "}");
        selector.execute(context);
        final List<String> result = selector.getResult();
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(mockObjects.regexStringExpected, result.get(0));
    }

}
