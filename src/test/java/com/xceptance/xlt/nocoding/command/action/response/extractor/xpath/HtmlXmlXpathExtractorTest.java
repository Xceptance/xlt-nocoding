package com.xceptance.xlt.nocoding.command.action.response.extractor.xpath;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.htmlunit.FailingHttpStatusCodeException;
import org.htmlunit.SgmlPage;
import org.htmlunit.WebResponse;
import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.util.MockWebResponse;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.context.DomContext;
import com.xceptance.xlt.nocoding.util.context.LightWeightContext;
import com.xceptance.xlt.nocoding.util.context.RequestContext;

public class HtmlXmlXpathExtractorTest extends AbstractXpathExtractorExecutorTest
{

    public HtmlXmlXpathExtractorTest(final Context<?> context)
    {
        super(context);
    }

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
        if (context instanceof DomContext)
        {
            ((DomContext) context).setPage(mockObjects.getHtmlPage());
        }
        else if (context instanceof LightWeightContext)
        {
            ((LightWeightContext) context).setSgmlPage(mockObjects.getHtmlPage());
        }
        else if (context instanceof LightWeightContext)
        {
            ((RequestContext) context).setSgmlPage(mockObjects.getHtmlPage());
        }
        final HtmlXmlXpathExtractorExecutor extractorExecutor = new HtmlXmlXpathExtractorExecutor(mockObjects.xPathString);
        extractorExecutor.execute(context);
        final List<String> results = extractorExecutor.getResult();
        Assert.assertEquals(mockObjects.xpathStringExpected, results.get(0));
    }

    /**
     * Verifies you can get XML Content.
     *
     * @throws IOException
     * @throws FailingHttpStatusCodeException
     */
    @Test
    public void testGetByXPathWithXML() throws FailingHttpStatusCodeException, IOException
    {
        final String url = "http://www.xceptance.net";
        final WebResponse xmlResponse = new MockWebResponse(xmlContent, new URL(url), xmlType);
        context.setWebResponse(xmlResponse);
        if (context instanceof DomContext)
        {
            ((DomContext) context).setPage((SgmlPage) context.getWebClient()
                                                             .loadWebResponseInto(xmlResponse, context.getWebClient().getCurrentWindow()));
        }

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
    public void testGetByXPathWithMaliciousXml() throws FailingHttpStatusCodeException, IOException
    {
        // final LightWeightContext context = new LightWeightContext(XltProperties.getInstance());
        final String url = "http://www.xceptance.net";
        final WebResponse maliciousXmlContentResponse = new MockWebResponse(maliciousXmlContent, new URL(url), xmlType);
        context.setWebResponse(maliciousXmlContentResponse);
        if (context instanceof DomContext)
        {
            ((DomContext) context).setPage((SgmlPage) context.getWebClient()
                                                             .loadWebResponseInto(maliciousXmlContentResponse,
                                                                                  context.getWebClient().getCurrentWindow()));
        }
        final HtmlXmlXpathExtractorExecutor xpathResponse = new HtmlXmlXpathExtractorExecutor("//title");
        xpathResponse.execute(context);
    }

}
