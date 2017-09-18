package com.xceptance.xlt.nocoding.scriptItem.action.response.validators;

import java.util.regex.Pattern;

import org.junit.Assert;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.nocoding.util.PropertyManager;

public class RegExpValidator extends AbstractValidator
{
    final String pattern;

    final String text;

    final Integer count;

    public RegExpValidator(final String validationName, final String pattern)
    {
        this(validationName, pattern, null, null);
    }

    public RegExpValidator(final String validationName, final String pattern, final String text)
    {
        this(validationName, pattern, text, null);
    }

    public RegExpValidator(final String validationName, final String pattern, final String text, final Integer count)
    {
        super(validationName);
        this.pattern = pattern;
        this.text = text;
        this.count = count;
    }

    @Override
    public void validate(final PropertyManager propertyManager, final WebResponse webResponse) throws Exception
    {
        final LightWeightPage page = new LightWeightPage(webResponse, propertyManager.getWebClient().getTimerName());
        final String pageContent = page.getContent();
        if (text == null)
        {
            Assert.assertTrue("Pattern did not match", Pattern.compile(pattern).matcher(pageContent).find());
        }
        else
        {
            // TODO Matching Group hinzufügen!
            final String matchingString = Pattern.compile(pattern).matcher(pageContent).group();
            final int solution = text.compareTo(matchingString);
            Assert.assertTrue("Found content does not equal matcher", solution == 0);
        }
    }

}
