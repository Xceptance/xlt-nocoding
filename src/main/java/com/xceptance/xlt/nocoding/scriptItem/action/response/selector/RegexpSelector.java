package com.xceptance.xlt.nocoding.scriptItem.action.response.selector;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.nocoding.util.Context;

public class RegexpSelector extends AbstractSelector
{

    private String group;

    public RegexpSelector(final String selectionExpression)
    {
        this(selectionExpression, null);
    }

    public RegexpSelector(final String selectionExpression, final String group)
    {
        super(selectionExpression);
        this.group = group;
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

        // If we don't have a group, add all matches
        if (this.group == null)
        {
            // If we find matches
            while (matcher.find())
            {
                addResult(matcher.group());
            }
        }
        // Else, simply add the group
        else
        {
            if (matcher.find())
            {
                addResult(matcher.group(Integer.parseInt(this.group)));
            }
        }
    }

    @Override
    protected void resolveValues(final Context context)
    {
        super.resolveValues(context);
        group = context.resolveString(group);
    }

    public String getGroup()
    {
        return group;
    }

    public void setGroup(final String group)
    {
        this.group = group;
    }

}
