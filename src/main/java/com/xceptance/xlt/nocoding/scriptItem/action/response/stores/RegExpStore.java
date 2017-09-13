package com.xceptance.xlt.nocoding.scriptItem.action.response.stores;

import java.util.regex.Pattern;

import org.junit.Assert;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.nocoding.util.DataStorage.DataStorage;

public class RegExpStore extends AbstractResponseStore
{

    private final DataStorage globalStorage;

    private final String regExp;

    private final int group;

    public RegExpStore(final DataStorage globalStorage, final String regExp)
    {
        this(globalStorage, regExp, 1);
    }

    public RegExpStore(final DataStorage globalStorage, final String regExp, final int group)
    {
        this.globalStorage = globalStorage;
        this.regExp = regExp;
        this.group = group;
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
        final String foundContent = Pattern.compile(this.regExp).matcher(pageContent).group(this.group);
        Assert.assertNotNull("Couldn't find anything with the regexp", foundContent);
        // this.globalStorage.store(foundContent);
    }
}
