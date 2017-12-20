package com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.xpathExtractor;

import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.AbstractExtractor;
import com.xceptance.xlt.nocoding.util.Context;

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
    public void execute(final Context context)
    {
        // throw new NotImplementedException(this.getClass().getSimpleName() + " not yet implemented!");
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
        extractor.execute(context);
        result.addAll(extractor.getResult());
    }

}
