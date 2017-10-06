package com.xceptance.xlt.nocoding.scriptItem.action.response.stores;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.openqa.selenium.InvalidArgumentException;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.nocoding.util.Context;

public class RegExpStore extends AbstractResponseStore
{

    private String group;

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
    public void store(final Context context, final WebResponse webResponse) throws Exception
    {
        resolveValues(context);
        // TODO check for mode - throw error if not lightweight mode
        // if(propertyManager.mode = dom)
        // throw new IllegalStateException("Cannot use a regular expression in dom mode. Please use the lightweight mode.");
        final LightWeightPage page = new LightWeightPage(webResponse, context.getWebClient().getTimerName());
        final String pageContent = page.getContent();
        String foundContent = null;
        final Matcher matcher = Pattern.compile(getPattern()).matcher(pageContent);
        if (matcher.find())
        {
            if (getGroup() != null)
            {
                foundContent = matcher.group(Integer.parseInt(getGroup()));
            }
            else
            {
                foundContent = matcher.group();
            }
        }

        Assert.assertNotNull("Couldn't find anything with the regexp", foundContent);
        context.getDataStorage().storeVariable(getVariableName(), foundContent);
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
