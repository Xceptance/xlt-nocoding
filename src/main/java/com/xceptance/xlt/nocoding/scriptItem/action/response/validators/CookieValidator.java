package com.xceptance.xlt.nocoding.scriptItem.action.response.validators;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.nocoding.util.PropertyManager;

public class CookieValidator extends AbstractValidator
{
    protected final String cookie;

    protected final String matches;

    public CookieValidator(final String validationName, final String cookie)
    {
        this(validationName, cookie, null);
    }

    public CookieValidator(final String validationName, final String cookie, final String matches)
    {
        super(validationName);
        this.cookie = cookie;
        this.matches = matches;
    }

    @Override
    public void validate(final PropertyManager propertyManager, final WebResponse webResponse) throws Exception
    {
        // TODO Auto-generated method stub

    }

}
