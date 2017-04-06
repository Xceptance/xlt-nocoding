package com.xceptance.xlt.nocoding.actions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.engine.XltWebClient;
import com.xceptance.xlt.nocoding.util.MockObjects;
import com.xceptance.xlt.nocoding.util.action.validation.URLActionDataExecutableResult;
import com.xceptance.xlt.nocoding.util.action.validation.URLActionDataExecutableResultFactory;

public class XhrHtmlPageActionTest
{

    private Downloader downloader;

    private final String name = "test";

    private WebRequest request;

    private URLActionDataExecutableResultFactory factory;

    private String urlString;

    private static MockObjects mockObjects;

    @Before
    public void setup() throws MalformedURLException
    {
        mockObjects = new MockObjects();
        final XltWebClient client = new XltWebClient();
        client.setTimerName("timeName");
        urlString = mockObjects.urlStringDemoHtml;
        downloader = new Downloader(client);
        request = new WebRequest(new URL(urlString));
        factory = new URLActionDataExecutableResultFactory();
    }

    @Test
    public void testConstructor()
    {
        final XhrHtmlPageAction action = new XhrHtmlPageAction(name, request, factory);
        downloader = new Downloader((XltWebClient) action.getWebClient());
        action.setDownloader(downloader);
        Assert.assertEquals(urlString, action.getUrl().toString());
    }

    @Test
    public void testGetResult() throws Throwable
    {
        final XhrHtmlPageAction action = new XhrHtmlPageAction(name, request, factory);
        downloader = new Downloader((XltWebClient) action.getWebClient());
        action.setDownloader(downloader);
        action.run();

        final URLActionDataExecutableResult result = action.getResult();
        final List<?> list = result.getByXPath(mockObjects.xPathString);
        Assert.assertEquals(mockObjects.xpathStringExpected, list.get(0));
    }
}
