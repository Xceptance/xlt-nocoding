package com.xceptance.xlt.nocoding.scriptItem.action.response.validators;

import com.xceptance.xlt.nocoding.util.Context;

public class XPathValidator extends AbstractValidator
{

    private final String xPathExpression;

    private final String text;

    private final String count;

    public XPathValidator(final String validationName, final String validationMode, final String xPathExpression)
    {
        this(validationName, validationMode, xPathExpression, null, null);
    }

    public XPathValidator(final String validationName, final String validationMode, final String xPathExpression, final String text,
        final String count)
    {
        super(validationName, validationMode);
        this.xPathExpression = xPathExpression;
        this.text = text;
        this.count = count;
    }

    @Override
    public void execute(final Context context) throws Exception
    {
        // TODO Fill execute

    }

    public String getxPathExpression()
    {
        return xPathExpression;
    }

    public String getText()
    {
        return text;
    }

    public String getCount()
    {
        return count;
    }

}
