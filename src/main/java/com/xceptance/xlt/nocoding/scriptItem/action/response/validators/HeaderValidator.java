package com.xceptance.xlt.nocoding.scriptItem.action.response.validators;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.Context;

/**
 * Validates a header by searching for its name. Depending on the mode, it validates the amount of the specified header
 * or the content of the header.
 * 
 * @author ckeiner
 */
public class HeaderValidator extends AbstractValidator
{
    /**
     * The name of the header
     */
    protected String header;

    /**
     * The expected content of the header
     */
    protected String expectedContent;

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
    public HeaderValidator(final String validationName, final String validationMode, final String header)
    {
        this(validationName, validationMode, header, null, null);
    }

    /**
     * The constructors builds a validation module, that checks either the value of the specified header or if the amount of
     * the header is as specified.
     * 
     * @param variableName
     *            The name of the variable you want to store the header in
     * @param header
     *            The header you want to verify
     * @param text
     *            The text the header is supposed to have
     * @param count
     *            The amount of times the header is in the response
     */
    public HeaderValidator(final String validationName, final String validationMode, final String header, final String expectedContent,
        final String count)
    {
        super(validationName, validationMode);
        this.header = header;
        this.expectedContent = expectedContent;
        this.count = count;
    }

    @Override
    public void execute(final Context context) throws Exception
    {
        final WebResponse webResponse = context.getWebResponse();
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
                if (getText() != null && getValidationMode() != null && !getValidationMode().equals(Constants.EXISTS)
                    && !getValidationMode().equals(Constants.COUNT))
                {
                    // If the validationMode is 'Text', validate that the expectedContent equals the actual content
                    if (getValidationMode().equals(Constants.TEXT))
                    {
                        Assert.assertEquals("Value of header does not match expected value", getText(), header.getValue());
                    }
                    // If the validationMode is 'Matches', validate that the expectedContent matches the actual content
                    else if (getValidationMode().equals(Constants.MATCHES))
                    {
                        final Matcher matcher = Pattern.compile(expectedContent).matcher(header.getValue());
                        final String errorMsg = expectedContent + " did not match " + header.getValue();
                        Assert.assertTrue(errorMsg, matcher.find());
                    }
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
        else if (getCount() != null && count != Integer.parseInt(getCount()) && getValidationMode().equals(Constants.COUNT))
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
        return expectedContent;
    }

    public void setText(final String text)
    {
        this.expectedContent = text;
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
