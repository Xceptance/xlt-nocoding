package com.xceptance.xlt.nocoding.scriptItem.action.response.stores;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.openqa.selenium.InvalidArgumentException;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.nocoding.util.Context;

/**
 * Stores the item that is found via the regular expression.
 * 
 * @author ckeiner
 */
public class RegExpStore extends AbstractResponseStore
{

    /**
     * The group of the match
     */
    private String group;

    /**
     * The regular expression
     */
    private String pattern;

    public RegExpStore(final String name, final String pattern)
    {
        this(name, pattern, null);
    }

    public RegExpStore(final String name, final String pattern, final String group)
    {
        super(name);
        this.group = group;
        this.pattern = pattern;
    }

    @Override
    public void execute(final Context context) throws Exception
    {
        final WebResponse webResponse = context.getWebResponse();
        // Resolve values
        resolveValues(context);

        // Create a lightWeightPage so we can get the content
        final LightWeightPage page = new LightWeightPage(webResponse, context.getWebClient().getTimerName());
        // Get the content
        final String pageContent = page.getContent();
        String foundContent = null;
        // Compile the pattern and match it
        final Matcher matcher = Pattern.compile(getPattern()).matcher(pageContent);
        // If we found something
        if (matcher.find())
        {
            // If we have a group specified
            if (getGroup() != null)
            {
                // Extract the match
                foundContent = matcher.group(Integer.parseInt(getGroup()));
            }
            else
            {
                // Extract the match
                foundContent = matcher.group();
            }
        }
        // Assert that we found a match
        Assert.assertNotNull("Couldn't find anything with the pattern: " + getPattern(), foundContent);
        // And store the match as variable
        context.storeVariable(getVariableName(), foundContent);
    }

    /**
     * Tries to resolve all variables of non-null attributes. Variables are specified with by "${variable}".
     * 
     * @param context
     *            The propertyManager with the DataStorage to use
     * @throws InvalidArgumentException
     */
    private void resolveValues(final Context context)
    {
        String resolvedValue = context.resolveString(getPattern());
        setPattern(resolvedValue);
        if (getGroup() != null)
        {
            resolvedValue = context.resolveString(getGroup());
            setGroup(resolvedValue);
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

    public String getPattern()
    {
        return pattern;
    }

    public void setPattern(final String pattern)
    {
        this.pattern = pattern;
    }

}
