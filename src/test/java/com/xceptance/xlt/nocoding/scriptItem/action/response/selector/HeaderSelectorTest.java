package com.xceptance.xlt.nocoding.scriptItem.action.response.selector;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class HeaderSelectorTest extends SelectorTest
{

    @Test
    public void testHeaderSelector() throws Throwable
    {
        final AbstractSelector selector = new HeaderSelector("Set-Cookie");
        selector.execute(context);
        final List<String> result = selector.getResult();
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
        final AbstractSelector selector = new HeaderSelector("${" + variableName + "}");
        selector.execute(context);
        final List<String> result = selector.getResult();
        Assert.assertEquals(3, result.size());
        Assert.assertEquals(mockObjects.cookieName1 + "=" + mockObjects.cookieValue1, result.get(0));
        Assert.assertEquals(mockObjects.cookieName2 + "=" + mockObjects.cookieValue2, result.get(1));
        Assert.assertEquals(mockObjects.cookieName3 + "=" + mockObjects.cookieValue3, result.get(2));
    }
}
