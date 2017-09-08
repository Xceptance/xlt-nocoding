package com.xceptance.xlt.nocoding.scriptItem.action.response;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;

public class XPathValidator extends AbstractValidator
{
    public final static byte EXISTS = 0;

    public final static byte MATCHES = 1;

    public final static byte COUNT = 2;

    private final String xPathExpression;

    private final String matcher;

    private final int count;

    private final byte mode;

    public XPathValidator(final String xPathExpression, final byte mode)
    {
        this(xPathExpression, "", -1, mode);
    }

    public XPathValidator(final String xPathExpression, final String matcher, final byte mode)
    {
        this(xPathExpression, matcher, -1, mode);
    }

    public XPathValidator(final String xPathExpression, final int count, final byte mode)
    {
        this(xPathExpression, "", count, mode);
    }

    public XPathValidator(final String xPathExpression, final String matcher, final int count, final byte mode)
    {
        this.xPathExpression = xPathExpression;
        this.matcher = matcher;
        this.count = count;
        this.mode = mode;
    }

    @Override
    public void validate(final HtmlPage page) throws Exception
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void validate(final LightWeightPage page) throws Exception
    {
        // TODO Auto-generated method stub

    }

}
