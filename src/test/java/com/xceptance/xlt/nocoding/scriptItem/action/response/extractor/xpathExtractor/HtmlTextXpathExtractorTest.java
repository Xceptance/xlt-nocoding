package com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.xpathExtractor;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.ExtractorTest;
import com.xceptance.xlt.nocoding.util.context.DomContext;

public class HtmlTextXpathExtractorTest extends ExtractorTest
{

    /**
     * Verifies the extractor works
     */
    @Test
    public void testXPathExtraction()
    {
        context = new DomContext(context);
        mockObjects.loadHtmlPage();
        context.setSgmlPage(mockObjects.getHtmlPage());
        final HtmlTextXpathExtractor xpathExtractor = new HtmlTextXpathExtractor(mockObjects.xPathString);
        xpathExtractor.execute(context);
        final List<String> results = xpathExtractor.getResult();
        Assert.assertEquals(mockObjects.xpathStringExpected, results.get(0));
    }

}
