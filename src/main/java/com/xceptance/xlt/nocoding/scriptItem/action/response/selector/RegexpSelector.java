package com.xceptance.xlt.nocoding.scriptItem.action.response.selector;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.nocoding.util.Context;

public class RegexpSelector extends AbstractSelector
{

    public RegexpSelector(final String selectionExpression)
    {
        super(selectionExpression);
    }

    @Override
    public void execute(final Context context)
    {
        final WebResponse webResponse = context.getWebResponse();
        // Resolve variables
        resolveValues(context);
        // Build a page with the content
        final LightWeightPage page = new LightWeightPage(webResponse, context.getWebClient().getTimerName());
        // Read the content
        final String pageContent = page.getContent();
        // Create a matcher object, so we can save our found matches
        final Matcher matcher = Pattern.compile(selectionExpression).matcher(pageContent);

        // If we find matches
        while (matcher.find())
        {
            addResult(matcher.group());
        }

    }

}
