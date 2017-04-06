package com.xceptance.xlt.nocoding.util.action.execution;

import java.net.URL;

import com.xceptance.xlt.nocoding.api.URLTestCase;
import com.xceptance.xlt.nocoding.util.action.data.URLActionData;
import com.xceptance.xlt.nocoding.util.action.validation.URLActionDataExecutableResult;

/**
 * A simple interface for the execution of a http request. <br>
 * the response must be wrapped with a {@link URLActionDataExecutableResult}.
 *
 * @author matthias mitterreiter
 */
public interface URLActionDataExecutionable
{
    /**
     * @return the {@link URLActionDataExecutableResult response} of the request.
     */
    public URLActionDataExecutableResult getResult();

    /**
     * executes an {@link URLActionData}, which means it fires the resulting request and creates the
     * {@link URLActionDataExecutableResult}.
     */
    public void executeAction();

    /**
     * same as above with the urlTestCase as a parameter to handle subsequent xhr within the action
     */
    public void executeAction(URLTestCase urlTestCase);

    /**
     * Adds {@link URLActionData} of type STATIC, which will be executes automatically.
     *
     * @param url
     */
    public void addStaticRequest(final URL url);

    /**
     * @return the request url.
     */
    public URL getUrl();

}
