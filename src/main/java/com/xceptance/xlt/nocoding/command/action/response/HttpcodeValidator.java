package com.xceptance.xlt.nocoding.command.action.response;

import org.junit.Assert;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.api.validators.HttpResponseCodeValidator;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Validates the Httpcode of a {@link WebResponse}. Httpcode can be specified or defaulted.
 *
 * @author ckeiner
 */
public class HttpcodeValidator extends AbstractResponseItem
{
    /**
     * The expected httpcode
     */
    private String httpcode;

    /**
     * Creates an instance of {@link HttpcodeValidator} and sets {@link #httpcode}.
     *
     * @param httpcode
     *            The expected Http Responsecode
     */
    public HttpcodeValidator(final String httpcode)
    {
        this.httpcode = httpcode;
    }

    /**
     * Resolves values, fills in default data and validates {@link WebResponse#getStatusCode()} equals {@link #httpcode}
     * {@link HttpResponseCodeValidator}.
     */
    @Override
    public void execute(final Context<?> context)
    {
        fillDefaultData(context);
        resolveValues(context);
        Assert.assertEquals(Integer.parseInt(httpcode), context.getWebResponse().getStatusCode());
    }

    /**
     * Uses the default value for httpcode if {@link #httpcode} is null or empty
     */
    protected void fillDefaultData(final Context<?> context)
    {
        if (httpcode == null || httpcode.isEmpty())
        {
            httpcode = context.getDefaultItems().get(Constants.HTTPCODE);
        }
    }

    /**
     * Resolves {@link #httpcode}
     *
     * @param context
     */
    private void resolveValues(final Context<?> context)
    {
        httpcode = context.resolveString(httpcode);
    }

    public String getHttpcode()
    {
        return httpcode;
    }

}
