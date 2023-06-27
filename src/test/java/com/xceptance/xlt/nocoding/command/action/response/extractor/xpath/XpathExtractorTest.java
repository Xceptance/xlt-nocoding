package com.xceptance.xlt.nocoding.command.action.response.extractor.xpath;

import java.io.IOException;
import java.util.List;

import org.htmlunit.FailingHttpStatusCodeException;
import org.htmlunit.Page;
import org.htmlunit.html.HtmlPage;
import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.command.action.response.extractor.ExtractorTest;
import com.xceptance.xlt.nocoding.util.MockWebResponse;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.context.DomContext;

public class XpathExtractorTest extends ExtractorTest
{

    public XpathExtractorTest(final Context<?> context)
    {
        super(context);
    }

    /**
     * Verifies an unsupported type cannot be parsed
     */
    @Test(expected = IllegalStateException.class)
    public void testUnsupportedType()
    {
        mockObjects.load();
        final MockWebResponse mockWeResponseUnsupported = new MockWebResponse("some content", mockObjects.getUrl(), "unsupported/type");
        context.setWebResponse(mockWeResponseUnsupported);
        new XpathExtractor("").getExecutor(context);
    }

    /**
     * Verifies a supported type can be parsed
     */
    @Test
    public void testSupportedType()
    {
        final MockWebResponse mockWeResponseHtml = new MockWebResponse("some content", mockObjects.getUrl(), "text/html");
        context.setWebResponse(mockWeResponseHtml);
        new XpathExtractor("").getExecutor(context);
        final MockWebResponse mockWeResponseHtml2 = new MockWebResponse("some content", mockObjects.getUrl(), "text/application");
        context.setWebResponse(mockWeResponseHtml2);
        new XpathExtractor("").getExecutor(context);
    }

    /**
     * Verifies XpathExtractor decides on a specific XpathExtractor and gets the result
     * 
     * @throws IOException
     * @throws FailingHttpStatusCodeException
     */
    @Test
    public void testGetByXPath() throws FailingHttpStatusCodeException, IOException
    {
        context.setWebResponse(mockObjects.getResponse());
        // If the context is Dom, then we additionally have to set a page
        if (context instanceof DomContext)
        {
            final Page page = context.getWebClient()
                                     .loadWebResponseInto(context.getWebResponse(), context.getWebClient().getCurrentWindow());
            if (page.isHtmlPage())
            {
                ((DomContext) context).setPage((HtmlPage) page);
            }
        }
        final XpathExtractor extractor = new XpathExtractor(mockObjects.xPathString);
        extractor.execute(context);
        final List<String> xPathResults = extractor.getResult();
        Assert.assertEquals(mockObjects.xpathStringExpected, xPathResults.get(0));
    }

}
