package com.xceptance.xlt.nocoding.scriptItem.action.response.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.openqa.selenium.InvalidArgumentException;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.nocoding.util.Context;

public class RegExpValidator extends AbstractValidator
{
    private String pattern;

    private String text;

    private String group;

    public RegExpValidator(final String validationName, final String pattern)
    {
        this(validationName, pattern, null, null);
    }

    public RegExpValidator(final String validationName, final String pattern, final String text)
    {
        this(validationName, pattern, text, null);
    }

    public RegExpValidator(final String validationName, final String pattern, final String text, final String group)
    {
        super(validationName);
        this.pattern = pattern;
        this.text = text;
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

    public String getText()
    {
        return text;
    }

    public void setText(final String text)
    {
        this.text = text;
    }

    public String getGroup()
    {
        return group;
    }

    public void setGroup(final String group)
    {
        this.group = group;
    }

    @Override
    public void validate(final Context context, final WebResponse webResponse) throws Exception
    {
        // Resolve variables
        resolveValues(context);
        // Build a page with the content
        final LightWeightPage page = new LightWeightPage(webResponse, context.getWebClient().getTimerName());
        // Read the content
        final String pageContent = page.getContent();
        // Create a matcher object, so we can save our found matches
        final Matcher matcher = Pattern.compile(getPattern()).matcher(pageContent);
        // Set the matchingString to null
        String matchingString = null;

        // If we find matches
        if (matcher.find())
        {
            // and did specify text, we want to verify, that we have that text in a group
            if (getText() != null)
            {
                // If we specified a group, save the match of that group
                if (group != null)
                {
                    matchingString = matcher.group(Integer.parseInt(getGroup()));
                }
                // otherwise we simply look at group()
                else
                {
                    matchingString = matcher.group(0);
                }
                // If we did get a match that is not null, verify it equals the text field
                if (matchingString != null)
                {
                    Assert.assertTrue("Found content does not equal matcher", getText().equals(matchingString));
                }
                // otherwise throw an error
                else
                {
                    throw new AssertionError("Matched with null");
                    // if null is matched, then we most likely don't want it to be matched
                }
            }
            // If we didn't specify a text, we don't need to do anything else
        }
        // If we did not find any matches, throw an error
        else
        {
            throw new AssertionError("Did not find a match");
        }
    }

    /**
     * Tries to resolve all variables of non-null attributes. Variables are specified with by "${variable}".
     * 
     * @param context
     *            The propertyManager with the DataStorage to use
     * @throws InvalidArgumentException
     */
    private void resolveValues(final Context context) throws InvalidArgumentException
    {
        // Resolve name
        String resolvedValue = context.resolveString(getPattern());
        setPattern(resolvedValue);

        // Resolve text
        if (getText() != null)
        {
            resolvedValue = context.resolveString(getText());
            setText(resolvedValue);
        }

        // Resolve count
        if (getGroup() != null)
        {
            resolvedValue = context.resolveString(getGroup());
            setGroup(resolvedValue);
        }

    }

}
