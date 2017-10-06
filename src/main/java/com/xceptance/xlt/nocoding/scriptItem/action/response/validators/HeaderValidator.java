package com.xceptance.xlt.nocoding.scriptItem.action.response.validators;

import java.util.List;

import org.junit.Assert;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.Context;

public class HeaderValidator extends AbstractValidator
{
    /**
     * The name of the header
     */
    protected String header;

    /**
     * The text in the header
     */
    protected String text;

    /**
     * The amount of times the header is in the response
     */
    protected String count;

    /**
     * The constructor builds a validation module, that simply checks if the specified header can be found
     * 
     * @param validationName
     *            The name of the validation
     * @param header
     *            The name of the header
     */
    public HeaderValidator(final String validationName, final String header)
    {
        this(validationName, header, null, null);
    }

    /**
     * The constructors builds a validation module, that checks either the value of the specified header or if the amount of
     * the header is as specified.
     * 
     * @param variableName
     *            The name of the variable you want to store the header in
     * @param header
     *            The header you want to verify
     * @param isTextOrCount
     *            Decides if we have count or text as final argument, needed for text and count to be mutually exclusive
     * @param textOrCount
     *            The value of the text attribute OR the count attribute
     */
    public HeaderValidator(final String validationName, final String header, final String isTextOrCount, final String textOrCount)
    {
        super(validationName);
        this.header = header;
        if (isTextOrCount == null)
        {
            // We don't want to do anything here
        }
        else if (isTextOrCount.equals(Constants.COUNT))
        {
            this.count = textOrCount;
        }
        else if (isTextOrCount.equals(Constants.TEXT))
        {
            this.text = textOrCount;
        }
        else
        {
            throw new IllegalArgumentException("Third argument must be Constant.Count or Constants.Text");
        }
    }

    @Override
    public void validate(final Context context, final WebResponse webResponse) throws Exception
    {
        // Resolve variables
        resolveValues(context);

        // If true, this throws an Exception if no header is found
        boolean throwException = true;
        // The number of times the header was found
        int count = 0;
        // Get all headers
        final List<NameValuePair> headers = webResponse.getResponseHeaders();
        // For each header,
        for (final NameValuePair header : headers)
        {
            // Search for the header name
            if (header.getName().equals(getHeader()))
            {
                // Increment the amount of found headers
                count++;
                // If the header is found, verify the value if specified
                if (getText() != null)
                {
                    Assert.assertEquals("Value of header does not match expected value", header.getValue(), getText());
                }
                // At last, set throwException to false, so we know, that we found our specified header.
                throwException = false;
            }
        }

        // If the specified header wasn't found, we need to throw an exception to the user
        if (throwException)
        {
            throw new Exception("Did not find specified header");
        }
        // if we did find the header, then we want to assert that the count (if specified) is correct
        else if (getCount() != null && count != Integer.parseInt(getCount()))
        {
            throw new Exception("Amount of found headers does not equal expected count");
        }
    }

    private void resolveValues(final Context context)
    {
        // Resolve header name
        String resolvedValue = context.resolveString(getHeader());
        setHeader(resolvedValue);
        // Resolve text if specified
        if (getText() != null)
        {
            resolvedValue = context.resolveString(getText());
            setText(resolvedValue);
        }
        // Resolve count if specified
        if (getCount() != null)
        {
            resolvedValue = context.resolveString(getCount());
            setCount(resolvedValue);
        }
    }

    public String getHeader()
    {
        return header;
    }

    public void setHeader(final String header)
    {
        this.header = header;
    }

    public String getText()
    {
        return text;
    }

    public void setText(final String text)
    {
        this.text = text;
    }

    public String getCount()
    {
        return count;
    }

    public void setCount(final String count)
    {
        this.count = count;
    }

}
