package com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.xpathExtractor;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.AbstractExtractor;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Decides which XpathExtractor is to be used and stores the result in {@link #getResult()}
 * 
 * @author ckeiner
 */
public class XpathExtractor extends AbstractExtractor
{
    public XpathExtractor(final String extractionExpression)
    {
        super(extractionExpression);
    }

    @Override
    public void execute(final Context<?> context)
    {
        final XpathExtractorExecutor executor = getExecutor(context);
        executor.execute(context);
        result.addAll(executor.getResult());
    }

    /**
     * Checks the type of the {@link WebResponse} and chooses the appropriate {@link XpathExtractorExecutor}
     * 
     * @param context
     *            The {@link Context} with the WebResponse in it
     * @return {@link HtmlXmlXpathExtractor} or {@link JsonXpathExtractor}, depending on the content type
     */
    XpathExtractorExecutor getExecutor(final Context<?> context)
    {
        final String content = context.getWebResponse().getContentType();
        XpathExtractorExecutor executor = null;
        if (HtmlXmlXpathExtractor.HEADERCONTENTTYPES.containsKey(content))
        {
            executor = new HtmlXmlXpathExtractor(getExtractionExpression());
        }
        else if (JsonXpathExtractor.HEADERCONTENTTYPES.containsKey(content))
        {
            executor = new JsonXpathExtractor(getExtractionExpression());
        }
        else
        {
            throw new IllegalStateException("Content type not supported: " + content);
        }
        return executor;
    }

}
