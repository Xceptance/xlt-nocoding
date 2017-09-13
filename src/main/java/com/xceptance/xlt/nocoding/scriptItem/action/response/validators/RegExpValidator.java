package com.xceptance.xlt.nocoding.scriptItem.action.response.validators;

import java.util.regex.Pattern;

import org.junit.Assert;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;

public class RegExpValidator extends AbstractValidator
{
    final String pattern;

    final String matcherString;

    public RegExpValidator(final String pattern)
    {
        this(pattern, null);
    }

    public RegExpValidator(final String pattern, final String matcherString)
    {
        this.pattern = pattern;
        this.matcherString = matcherString;
    }

    @Override
    public void validate(final HtmlPage page) throws Exception
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void validate(final LightWeightPage page) throws Exception
    {
        final String pageContent = page.getContent();
        if (matcherString == null)
        {
            Assert.assertTrue("Pattern did not match", Pattern.compile(pattern).matcher(pageContent).find());
        }
        else
        {
            final String matchingString = Pattern.compile(pattern).matcher(pageContent).group(1);
            final int solution = matcherString.compareTo(matchingString);
            Assert.assertTrue("Found content does not equal matcher", solution == 0);
        }
    }

}
