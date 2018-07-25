package com.xceptance.xlt.nocoding.command.action.response.extractor.xpath;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.command.action.response.extractor.xpath.HtmlXmlXpathExtractorExecutor;
import com.xceptance.xlt.nocoding.util.MockWebResponse;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.context.DomContext;
import com.xceptance.xlt.nocoding.util.context.LightWeightContext;

public class HtmlXmlXpathExtractorTest extends AbstractXpathExtractorExecutorTest
{

    String xmlType = "text/xml";

    String xmlContent = "<data>" + " <employee>" + "   <name>John</name>" + "   <title>Manager</title>" + " </employee>" + " <employee>"
                        + "   <name>Sara</name>" + "   <title>Clerk</title>" + " </employee>" + "</data>";

    /**
     * {@link #xmlContent} without ending data tag
     */
    String maliciousXmlContent = "<data>" + " <employee>" + "   <name>John</name>" + "   <title>Manager</title>" + " </employee>"
                                 + " <employee>" + "   <name>Sara</name>" + "   <title>Clerk</title>" + " </employee>";

    /**
     * Verifies the extractor works
     */
    @Test
    public void testXPathExtraction()
    {
        mockObjects.loadHtmlPage();
        ((DomContext) context).setPage(mockObjects.getHtmlPage());
        final HtmlXmlXpathExtractorExecutor extractorExecutor = new HtmlXmlXpathExtractorExecutor(mockObjects.xPathString);
        extractorExecutor.execute(context);
        final List<String> results = extractorExecutor.getResult();
        Assert.assertEquals(mockObjects.xpathStringExpected, results.get(0));
    }

    /**
     * Verifies you can get XML Content with Dom Mode
     * 
     * @throws IOException
     * @throws FailingHttpStatusCodeException
     */
    @Test
    public void testGetByXPathWithXMLInDomMode() throws FailingHttpStatusCodeException, IOException
    {
        final String url = "http://www.xceptance.net";
        final WebResponse xmlResponse = new MockWebResponse(xmlContent, new URL(url), xmlType);
        context.setWebResponse(xmlResponse);
        ((DomContext) context).setPage((SgmlPage) context.getWebClient().loadWebResponseInto(xmlResponse,
                                                                                             context.getWebClient().getCurrentWindow()));
        final HtmlXmlXpathExtractorExecutor xpathResponse = new HtmlXmlXpathExtractorExecutor("//title");
        xpathResponse.execute(context);
        final List<String> list = xpathResponse.getResult();
        final String tit0 = list.get(0);
        final String tit1 = list.get(1);
        Assert.assertEquals("Manager", tit0);
        Assert.assertEquals("Clerk", tit1);
    }

    /**
     * Verifies an error is thrown when the Xml Content is faulty and in Dom Mode
     * 
     * @throws IOException
     * @throws FailingHttpStatusCodeException
     */
    @Test(expected = IllegalStateException.class)
    public void testGetByXPathWithMaliciousXmlInDomMode() throws FailingHttpStatusCodeException, IOException
    {
        // final LightWeightContext context = new LightWeightContext(XltProperties.getInstance());
        final String url = "http://www.xceptance.net";
        final WebResponse maliciousXmlContentResponse = new MockWebResponse(maliciousXmlContent, new URL(url), xmlType);
        context.setWebResponse(maliciousXmlContentResponse);
        ((DomContext) context).setPage((SgmlPage) context.getWebClient().loadWebResponseInto(maliciousXmlContentResponse,
                                                                                             context.getWebClient().getCurrentWindow()));
        final HtmlXmlXpathExtractorExecutor xpathResponse = new HtmlXmlXpathExtractorExecutor("//title");
        xpathResponse.execute(context);
    }

    /**
     * Verifies you can get XML Content with Lightweight Mode
     * 
     * @throws IOException
     * @throws FailingHttpStatusCodeException
     */
    @Test
    public void testGetByXPathWithXMLInLightMode() throws FailingHttpStatusCodeException, IOException
    {
        final Context<?> context = new LightWeightContext(XltProperties.getInstance());
        final String url = "http://www.xceptance.net";
        final WebResponse xmlResponse = new MockWebResponse(xmlContent, new URL(url), xmlType);
        context.setWebResponse(xmlResponse);
        final HtmlXmlXpathExtractorExecutor xpathResponse = new HtmlXmlXpathExtractorExecutor("//title");
        xpathResponse.execute(context);
        final List<String> list = xpathResponse.getResult();
        final String tit0 = list.get(0);
        final String tit1 = list.get(1);
        Assert.assertEquals("Manager", tit0);
        Assert.assertEquals("Clerk", tit1);
    }

    /**
     * Verifies an error is thrown when the Xml Content is faulty and in Lightweight Mode
     * 
     * @throws IOException
     * @throws FailingHttpStatusCodeException
     */
    @Test(expected = IllegalStateException.class)
    public void testGetByXPathWithMaliciousXmlInLightMode() throws FailingHttpStatusCodeException, IOException
    {
        final Context<?> context = new LightWeightContext(XltProperties.getInstance());
        // final LightWeightContext context = new LightWeightContext(XltProperties.getInstance());
        final String url = "http://www.xceptance.net";
        final WebResponse maliciousXmlContentResponse = new MockWebResponse(maliciousXmlContent, new URL(url), xmlType);
        context.setWebResponse(maliciousXmlContentResponse);
        final HtmlXmlXpathExtractorExecutor xpathResponse = new HtmlXmlXpathExtractorExecutor("//title");
        xpathResponse.execute(context);
    }

}
