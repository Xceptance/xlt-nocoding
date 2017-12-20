package com.xceptance.xlt.nocoding.scriptItem.action.response;

import org.junit.Assert;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.api.htmlunit.LightWeightPage;
import com.xceptance.xlt.api.validators.HttpResponseCodeValidator;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Validates the Httpcode of a {@link WebResponse}. Httpcode can be specified or is defaulted.
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
     * Creates an instance of {@link HttpcodeValidator} that sets {@link #httpcode}.
     * 
     * @param httpcode
     *            The expected httpcode
     */
    public HttpcodeValidator(final String httpcode)
    {
        this.httpcode = httpcode;
    }

    /**
     * Resolves values, fills in default data and then builds a {@link LightWeightPage} which is validated with the
     * {@link HttpResponseCodeValidator}.
     */
    @Override
    public void execute(final Context context) throws Exception
    {
        fillDefaultData(context);
        resolveValues(context);
        // TODO test
        Assert.assertEquals(Integer.parseInt(this.httpcode), context.getWebResponse().getStatusCode());
    }

    /**
     * Uses the default data for httpcode if {@link #httpcode} is null or empty
     */
    @Override
    protected void fillDefaultData(final Context context)
    {
        if (httpcode == null || httpcode.isEmpty())
        {
            httpcode = context.getDefaultItems().get(Constants.HTTPCODE);
        }
    }

    /**
     * Resolves httpcode
     * 
     * @param context
     */
    private void resolveValues(final Context context)
    {
        httpcode = context.resolveString(httpcode);
    }

    public String getHttpcode()
    {
        return httpcode;
    }

}
