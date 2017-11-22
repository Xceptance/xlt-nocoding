package com.xceptance.xlt.nocoding.scriptItem.action.response.selector;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class CookieSelectorTest extends SelectorTest
{

    @Test
    public void testCookieSelector() throws Throwable
    {
        final AbstractSelector selector = new CookieSelector(mockObjects.cookieName1);
        selector.execute(context);
        final List<String> result = selector.getResult();
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(mockObjects.cookieValue1, result.get(0));
    }

    @Test
    public void testCookieSelectorWithVariables() throws Throwable
    {
        final String variableName = "variable_1";
        final String storeValue = mockObjects.cookieName1;
        context.storeVariable(variableName, storeValue);
        final AbstractSelector selector = new CookieSelector("${" + variableName + "}");
        selector.execute(context);
        final List<String> result = selector.getResult();
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(mockObjects.cookieValue1, result.get(0));
    }

}
