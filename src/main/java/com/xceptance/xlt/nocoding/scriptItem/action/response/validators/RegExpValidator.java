package com.xceptance.xlt.nocoding.scriptItem.action.response.validators;

import java.util.regex.Pattern;

import org.junit.Assert;
import org.openqa.selenium.InvalidArgumentException;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.nocoding.util.PropertyManager;

public class RegExpValidator extends AbstractValidator
{
    private String pattern;

    private String text;

    private String group;

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
    public void validate(final PropertyManager propertyManager, final WebResponse webResponse) throws Exception
    {
        resolveValues(propertyManager);
        final LightWeightPage page = new LightWeightPage(webResponse, propertyManager.getWebClient().getTimerName());
        final String pageContent = page.getContent();
        if (getText() == null)
        {
            Assert.assertTrue("Pattern did not match content", Pattern.compile(getPattern()).matcher(pageContent).find());
        }
        else
        {
            final String matchingString;
            if (group != null)
            {
                matchingString = Pattern.compile(getPattern()).matcher(pageContent).group(Integer.parseInt(getGroup()));
            }
            else
            {
                matchingString = Pattern.compile(getPattern()).matcher(pageContent).group();
            }
            final int solution = getText().compareTo(matchingString);
            Assert.assertTrue("Found content does not equal matcher", solution == 0);
        }
    }

    /**
     * Tries to resolve all variables of non-null attributes. Variables are specified with by "${variable}".
     * 
     * @param propertyManager
     *            The propertyManager with the DataStorage to use
     * @throws InvalidArgumentException
     */
    private void resolveValues(final PropertyManager propertyManager) throws InvalidArgumentException
    {
        // Resolve name
        String resolvedValue = propertyManager.resolveString(getPattern());
        setPattern(resolvedValue);

        // Resolve text
        if (getText() != null)
        {
            resolvedValue = propertyManager.resolveString(getText());
            setText(resolvedValue);
        }

        // Resolve count
        if (getGroup() != null)
        {
            resolvedValue = propertyManager.resolveString(getGroup());
            setGroup(resolvedValue);
        }

    }

}
