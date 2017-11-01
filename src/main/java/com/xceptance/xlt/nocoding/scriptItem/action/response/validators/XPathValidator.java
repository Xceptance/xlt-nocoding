package com.xceptance.xlt.nocoding.scriptItem.action.response.validators;

import com.xceptance.xlt.nocoding.util.Context;

public class XPathValidator extends AbstractValidator
{

    private final String xPathExpression;

    private final String text;

    private final String count;

    public XPathValidator(final String validationName, final String xPathExpression)
    {
        this(validationName, xPathExpression, null, null);
    }

    public XPathValidator(final String validationName, final String xPathExpression, final String text, final String count)
    {
        super(validationName);
        this.xPathExpression = xPathExpression;
        this.text = text;
        this.count = count;
    }

    @Override
    public void execute(final Context context) throws Exception
    {
        // TODO Auto-generated method stub

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
