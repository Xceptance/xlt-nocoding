package com.xceptance.xlt.nocoding.util.action.validation;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.util.MockObjects;

public class URLActionDataExecutableResultTestCookies
{
    static XPathGetable xPathGetable;

    static MockObjects mockObjects;

    static HtmlPage page;

    @BeforeClass
    public static void setupBeforeClass()
    {
        mockObjects = new MockObjects();
        mockObjects.load();
        page = mockObjects.getHtmlPage();
        xPathGetable = new XPathWithHtmlPage(page);
    }

    @Test
    public void testGetCookies()
    {

        URLActionDataExecutableResult executableResult;
        executableResult = new URLActionDataExecutableResult(page.getWebResponse(), xPathGetable);
        executableResult.getCookie();
        Assert.assertEquals(executableResult.getCookie().size(), 3);
    }

    @Test
    public void testGetHttpCookies()
    {
        URLActionDataExecutableResult executableResult;
        executableResult = new URLActionDataExecutableResult(page.getWebResponse(), xPathGetable);
        Assert.assertEquals(executableResult.getCookie().size(), 3);
    }

    @Test
    public void testGetCookiesByName()
    {
        URLActionDataExecutableResult executableResult;
        executableResult = new URLActionDataExecutableResult(page.getWebResponse(), xPathGetable);
        final List<NameValuePair> cookies = executableResult.getCookieByName("session-id");
        Assert.assertEquals(1, cookies.size());

    }
}
