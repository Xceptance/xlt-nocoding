package com.xceptance.xlt.nocoding.scriptItem.action.response.stores;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.nocoding.util.PropertyManager;

public class RegExpStore extends AbstractResponseStore
{

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
    public void store(final PropertyManager propertyManager, final WebResponse webResponse) throws Exception
    {
        // TODO check for mode - throw error if not light weigth mode
        // if(propertyManager.mode = dom)
        // throw new IllegalStateException("Cannot use a regular expression in dom mode. Please use the lightweight mode.");
        final LightWeightPage page = new LightWeightPage(webResponse, propertyManager.getWebClient().getTimerName());
        final String pageContent = page.getContent();
        String foundContent = null;
        final Matcher matcher = this.pattern.matcher(pageContent);
        if (matcher.find())
        {
            foundContent = matcher.group(this.group);
        }

        Assert.assertNotNull("Couldn't find anything with the regexp", foundContent);
        propertyManager.getDataStorage().storeVariable(getVariableName(), foundContent);
    }
}
