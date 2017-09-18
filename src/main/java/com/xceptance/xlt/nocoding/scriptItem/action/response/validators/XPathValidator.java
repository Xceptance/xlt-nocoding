package com.xceptance.xlt.nocoding.scriptItem.action.response.validators;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.nocoding.util.PropertyManager;

public class XPathValidator extends AbstractValidator
{

    private final String xPathExpression;

    private final String text;

    private final Integer count;

    public XPathValidator(final String validationName, final String xPathExpression)
    {
        this(validationName, xPathExpression, null, null);
    }

    public XPathValidator(final String validationName, final String xPathExpression, final String text)
    {
        this(validationName, xPathExpression, text, null);
    }

    public XPathValidator(final String validationName, final String xPathExpression, final Integer count)
    {
        this(validationName, xPathExpression, null, count);
    }

    private XPathValidator(final String validationName, final String xPathExpression, final String text, final Integer count)
    {
        super(validationName);
        this.xPathExpression = xPathExpression;
        this.text = text;
        this.count = count;
    }

    @Override
    public void validate(final PropertyManager propertyManager, final WebResponse webResponse) throws Exception
    {
        // TODO Auto-generated method stub

    }

}
