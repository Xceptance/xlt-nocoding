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
        final AbstractExtractor extractor = getExtractor(context);
        extractor.execute(context);
        result.addAll(extractor.getResult());
    }

    /**
     * Checks the type of the {@link WebResponse} and chooses the appropriate XPathExtractor
     * 
     * @param context
     *            The {@link Context} with the WebResponse in it
     * @return {@link HtmlTextXpathExtractor} or {@link XmlJsonXpathExtractor}, depending on the content type
     */
    AbstractExtractor getExtractor(final Context<?> context)
    {
        final String content = context.getWebResponse().getContentType();
        AbstractExtractor extractor = null;
        if (HtmlTextXpathExtractor.HEADERCONTENTTYPES.containsKey(content))
        {
            extractor = new HtmlTextXpathExtractor(getExtractionExpression());
        }
        else if (XmlJsonXpathExtractor.HEADERCONTENTTYPES.containsKey(content))
        {
            extractor = new XmlJsonXpathExtractor(getExtractionExpression());
        }
        else
        {
            throw new IllegalStateException("Content type not supported: " + content);
        }
        return extractor;
    }

}
