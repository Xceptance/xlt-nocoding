package com.xceptance.xlt.nocoding.command.action.response.extractor;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Matches the pattern provided via {@link #getExtractionExpression()} and applies it to the
 * {@link LightWeightPage#getContent()} which is created with the {@link Context#getWebResponse()}. Then, stores the
 * match in {@link #addResult(String)}.
 * 
 * @author ckeiner
 */
public class RegexpExtractor extends AbstractExtractor
{

    private String group;

    /**
     * Creates an instance of {@link RegexpExtractor}, sets {@link #extractionExpression} and creates an ArrayList for
     * {@link #result}.
     * 
     * @param extractionExpression
     *            The regular expression to use on the content of the {@link WebResponse}.
     */
    public RegexpExtractor(final String extractionExpression)
    {
        this(extractionExpression, null);
    }

    /**
     * Creates an instance of {@link RegexpExtractor}, sets {@link #extractionExpression} and creates an {@link ArrayList}
     * for {@link #result}.
     * 
     * @param extractionExpression
     *            The regular expression to use on the content of the {@link WebResponse}.
     * @param group
     *            The matching group to use
     */
    public RegexpExtractor(final String extractionExpression, final String group)
    {
        super(extractionExpression);
        this.group = group;
    }

    /**
     * Compiles the pattern provided via {@link #getExtractionExpression()} and applies it to the
     * {@link LightWeightPage#getContent()} that is created with {@link Context#getWebResponse()}. If {@link #group} is
     * specified, it only stores this matching group. Else, it stores every group.
     */
    @Override
    public void execute(final Context<?> context)
    {
        final WebResponse webResponse = context.getWebResponse();
        // Resolve variables
        resolveValues(context);
        // Get the content of the webResponse as string
        final String pageContent = webResponse.getContentAsString();
        // Create a matcher object, so we can save our found matches
        final Matcher matcher = Pattern.compile(extractionExpression).matcher(pageContent);

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

    /**
     * Resolves the {@link #getExtractionExpression()} and {@link #group}.
     */
    @Override
    protected void resolveValues(final Context<?> context)
    {
        super.resolveValues(context);
        if (group != null && !group.isEmpty())
        {
            group = context.resolveString(group);
        }
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
