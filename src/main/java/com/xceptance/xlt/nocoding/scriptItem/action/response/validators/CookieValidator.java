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
 * Validates a cookie by searching for the Set-Cookie header and verifying its name. If it is specified, it also
 * validates the content of the cookie.
 * 
 * @author ckeiner
 */
public class CookieValidator extends AbstractValidator
{
    protected String cookie;

    protected String expectedContent;

    /**
     * Builds a validation module that verifies the specified cookie exists
     * 
     * @param validationName
     *            The name of the validation
     * @param cookie
     *            The name of the cookie
     */
    public CookieValidator(final String validationName, final String cookie)
    {
        this(validationName, Constants.EXISTS, cookie, null);
    }

    /**
     * Builds a validation module that verifies the specified cookie exists, with the specified content (if the content is
     * specified)
     * 
     * @param validationName
     *            The name of the validation
     * @param validationMode
     *            The mode of the validation, should be {@link Constants#TEXT} or {@link Constants#MATCHES}
     * @param cookie
     *            The name of the cookie
     * @param expectedContent
     *            The expected content of the cookie
     */
    public CookieValidator(final String validationName, final String validationMode, final String cookie, final String expectedContent)
    {
        super(validationName, validationMode);
        this.cookie = cookie;
        this.expectedContent = expectedContent;
    }

    /**
     * Resolves the values and validates a cookie in the {@link WebResponse} of the {@link Context}.
     */
    @Override
    public void execute(final Context context) throws Exception
    {
        final WebResponse webResponse = context.getWebResponse();
        // Resolve variables
        resolveValues(context);

        // If true, this throws an Exception if no cookie is found
        boolean throwException = true;
        // Get all headers
        final List<NameValuePair> headers = webResponse.getResponseHeaders();
        // For each header,
        for (final NameValuePair header : headers)
        {
            // Search for the Set-Cookie header
            if (header.getName().equals("Set-Cookie"))
            {
                // And verify if this is the correct cookie by
                // grabbing the cookie name
                final int equalSignPosition = header.getValue().indexOf("=");
                final String cookieName = header.getValue().substring(0, equalSignPosition);
                // and comparing it with the input name
                if (cookieName.equals(cookie))
                {
                    // Finally check if the text attribute is specified and the validationMode is specified and is not "Exists"
                    if (expectedContent != null && getValidationMode() != null && !getValidationMode().equals(Constants.EXISTS))
                    {
                        // Get the content of the cookie, which is until the first semicolon
                        final int semicolonPosition = header.getValue().indexOf(";");
                        // Content starts after the equal sign (position+1) and ends before the semicolon
                        final String cookieContent = header.getValue().substring(cookie.length() + 1, semicolonPosition);
                        // If the validationMode is 'Text', assert that both are equal
                        if (getValidationMode().equals(Constants.TEXT))
                        {
                            Assert.assertEquals("Content did not match", expectedContent, cookieContent);
                        }
                        // If the validationMode is matches, assert that the expectedCsontent matches the actual content
                        else if (getValidationMode().equals(Constants.MATCHES))
                        {
                            final Matcher matcher = Pattern.compile(expectedContent).matcher(cookieContent);
                            final String errorMsg = expectedContent + " did not match " + cookieContent;
                            Assert.assertTrue(errorMsg, matcher.find());
                        }
                    }
                    // At last, set throwException to false, so we know, that we found our specified cookie.
                    throwException = false;
                    break;
                }
            }
        }

        if (throwException)
        {
            throw new Exception("Did not find specified cookie");
        }

    }

    private void resolveValues(final Context context)
    {
        String resolvedValue = context.resolveString(cookie);
        cookie = resolvedValue;
        if (expectedContent != null)
        {
            resolvedValue = context.resolveString(expectedContent);
            expectedContent = resolvedValue;
        }

    }

}
