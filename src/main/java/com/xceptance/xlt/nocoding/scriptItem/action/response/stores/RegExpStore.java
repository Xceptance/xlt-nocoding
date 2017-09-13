package com.xceptance.xlt.nocoding.scriptItem.action.response.stores;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;

public class RegExpStore extends AbstractResponseStore
{

    // private final String regExp;

    private final int group;

    private final Pattern pattern;

    public RegExpStore(final String name, final String regExp)
    {
        this(name, regExp, 1);
    }

    public RegExpStore(final String name, final String regExp, final int group)
    {
        super(name);
        this.group = group;
        pattern = Pattern.compile(regExp);
    }

    @Override
    public void store(final HtmlPage page) throws Exception
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void store(final LightWeightPage page) throws Exception
    {
        final String pageContent = page.getContent();
        String foundContent = null;
        final Matcher matcher = this.pattern.matcher(pageContent);
        if (matcher.find())
        {
            foundContent = matcher.group(this.group);
        }

        Assert.assertNotNull("Couldn't find anything with the regexp", foundContent);
        this.globalStorage.storeDefault(this.getVariableName(), foundContent);
    }
}
