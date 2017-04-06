package com.xceptance.xlt.nocoding.actions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.engine.XltWebClient;
import com.xceptance.xlt.nocoding.util.MockObjects;
import com.xceptance.xlt.nocoding.util.action.validation.URLActionDataExecutableResultFactory;

public class HtmlPageActionTest
{
    private Downloader downloader;

    private final String name = "test";

    private WebRequest request;

    private String urlString;

    private URLActionDataExecutableResultFactory factory;

    private static MockObjects mockObjects;

    @Before
    public void setup() throws MalformedURLException
    {
        mockObjects = new MockObjects();
        final XltWebClient client = new XltWebClient();
        client.setTimerName("timeName");
        factory = new URLActionDataExecutableResultFactory();
        urlString = mockObjects.urlStringDemoHtml;
        downloader = new Downloader(client);
        request = new WebRequest(new URL(urlString));
    }

    @Test
    public void testConstructor() throws Throwable
    {
        final HtmlPageAction action = new HtmlPageAction(name, request, factory);
        downloader = new Downloader((XltWebClient) action.getWebClient());
        action.setDownloader(downloader);
        Assert.assertEquals(urlString, action.getUrl().toString());
    }

    @Test
    public void testGetHtmlPage() throws Throwable
    {

        final HtmlPageAction action = new HtmlPageAction(name, request, factory);
        downloader = new Downloader((XltWebClient) action.getWebClient());
        action.setDownloader(downloader);
        action.run();

        final HtmlPage page = action.getHtmlPage();
        final List<?> list = page.getByXPath(mockObjects.xPathString);
        Assert.assertFalse(list.isEmpty());
    }
}
