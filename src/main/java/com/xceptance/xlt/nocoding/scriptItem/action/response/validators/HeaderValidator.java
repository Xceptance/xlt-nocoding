package com.xceptance.xlt.nocoding.scriptItem.action.response.validators;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.nocoding.util.PropertyManager;

public class HeaderValidator extends AbstractValidator
{

    protected final String header;

    protected final String text;

    protected final Integer count;

    private HeaderValidator(final String validationName, final String header)
    {
        this(validationName, header, null, null);
    }

    private HeaderValidator(final String validationName, final String header, final String text)
    {
        this(validationName, header, text, null);
    }

    private HeaderValidator(final String validationName, final String header, final Integer count)
    {
        this(validationName, header, null, count);
    }

    private HeaderValidator(final String validationName, final String header, final String text, final Integer count)
    {
        super(validationName);
        this.header = header;
        this.text = text;
        this.count = count;
    }

    @Override
    public void validate(final PropertyManager propertyManager, final WebResponse webResponse) throws Exception
    {
        // TODO Auto-generated method stub

    }

}
