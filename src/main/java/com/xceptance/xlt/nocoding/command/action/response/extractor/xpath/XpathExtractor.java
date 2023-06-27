package com.xceptance.xlt.nocoding.command.action.response.extractor.xpath;

import org.htmlunit.WebResponse;

import com.xceptance.xlt.nocoding.command.action.response.extractor.AbstractExtractor;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Decides which {@link XpathExtractorExecutor} is to be used and stores the result in {@link #getResult()}
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
        addResult(executor.getResult());
    }

    /**
     * Checks the type of the {@link WebResponse} and chooses the appropriate {@link XpathExtractorExecutor}
     *
     * @param context
     *            The {@link Context} with the WebResponse in it
     * @return {@link HtmlXmlXpathExtractorExecutor} or {@link JsonXpathExtractorExecutor}, depending on the content
     *         type
     */
    XpathExtractorExecutor getExecutor(final Context<?> context)
    {
        final String content = context.getWebResponse().getContentType();
        XpathExtractorExecutor executor = null;
        if (HtmlXmlXpathExtractorExecutor.HEADERCONTENTTYPES.containsKey(content))
        {
            executor = new HtmlXmlXpathExtractorExecutor(getExtractionExpression());
        }
        else if (JsonXpathExtractorExecutor.HEADERCONTENTTYPES.containsKey(content))
        {
            executor = new JsonXpathExtractorExecutor(getExtractionExpression());
        }
        else
        {
            throw new IllegalStateException("Content type not supported: " + content);
        }
        return executor;
    }

}
