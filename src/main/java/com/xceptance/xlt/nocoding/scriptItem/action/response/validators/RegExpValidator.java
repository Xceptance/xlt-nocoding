package com.xceptance.xlt.nocoding.scriptItem.action.response.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.openqa.selenium.InvalidArgumentException;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.Context;

/**
 * Validates the content of the {@link WebResponse} by matching it with a pattern. If specified, the group is used as
 * matching group and the found content of the regular expression is used.
 * 
 * @author ckeiner
 */
public class RegExpValidator extends AbstractValidator
{
    private String pattern;

    private String expectedContent;

    private String group;

    public RegExpValidator(final String validationName, final String validationMode, final String pattern)
    {
        this(validationName, validationMode, pattern, null, null);
    }

    public RegExpValidator(final String validationName, final String validationMode, final String pattern, final String expectedContent)
    {
        this(validationName, validationMode, pattern, expectedContent, null);
    }

    public RegExpValidator(final String validationName, final String validationMode, final String pattern, final String expectedContent,
        final String group)
    {
        super(validationName, validationMode);
        this.pattern = pattern;
        this.expectedContent = expectedContent;
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

    public String getExpectedContent()
    {
        return expectedContent;
    }

    public void setExpectedContent(final String expectedContent)
    {
        this.expectedContent = expectedContent;
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
    public void execute(final Context context) throws Exception
    {
        final WebResponse webResponse = context.getWebResponse();
        XltLogger.runTimeLogger.debug("Starting Validation of " + getValidationName());
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
            if (getExpectedContent() != null && getValidationMode() != null && !getValidationMode().equals(Constants.EXISTS))
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
                    // If the validationMode is 'Text', verify the expectedContent equals the actual content
                    if (getValidationMode().equals(Constants.TEXT))
                    {
                        Assert.assertTrue("Found content does not equal matcher", getExpectedContent().equals(matchingString));
                    }
                    // If the validationMode is 'Matches', verify the expectedContent matches the actual content
                    else if (getValidationMode().equals(Constants.MATCHES))
                    {
                        final Matcher matcherMode = Pattern.compile(expectedContent).matcher(matchingString);
                        final String errorMsg = expectedContent + " did not match " + matchingString;
                        Assert.assertTrue(errorMsg, matcherMode.find());
                    }
                }
                // otherwise throw an error
                else
                {
                    throw new AssertionError("Matched with null at " + getValidationName());
                    // if null is matched, then we most likely don't want it to be matched
                }
            }
            // If we didn't specify a text, we don't need to do anything else
        }
        // If we did not find any matches, throw an error
        else
        {
            throw new AssertionError("Did not find a match at " + getValidationName());
        }

        XltLogger.runTimeLogger.debug("Finished Validation of " + getValidationName());
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
        if (getExpectedContent() != null)
        {
            resolvedValue = context.resolveString(getExpectedContent());
            setExpectedContent(resolvedValue);
        }

        // Resolve count
        if (getGroup() != null)
        {
            resolvedValue = context.resolveString(getGroup());
            setGroup(resolvedValue);
        }

    }

}
