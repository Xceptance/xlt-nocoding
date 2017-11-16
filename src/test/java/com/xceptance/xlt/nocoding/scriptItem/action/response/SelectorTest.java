package com.xceptance.xlt.nocoding.scriptItem.action.response;

import org.junit.Assert;
import org.junit.Test;

// TODO test all selectors
public class SelectorTest
{

    @Test
    public void testCookieSelector() throws Exception
    {
        // TODO call CookieSelector
        final String cookieHeader = "Set-Cookie:testname=testvalue;";

        final int equalSignPosition = cookieHeader.indexOf("=");
        final String cookieName = cookieHeader.substring(11, equalSignPosition);
        // and comparing it with the input name
        Assert.assertEquals("testname", cookieName);
        // Get the content of the cookie, which is until the first semicolon
        final int semicolonPosition = cookieHeader.indexOf(";");
        // Content starts after the equal sign (position+1) and ends before the semicolon
        final String cookieContent = cookieHeader.substring(equalSignPosition + 1, semicolonPosition);
        Assert.assertEquals("testvalue", cookieContent);

    }

}
