/*
 * Copyright (c) 2013-2023 Xceptance Software Technologies GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xceptance.xlt.nocoding.command.action.response;

import org.htmlunit.WebResponse;
import org.junit.Assert;

import com.xceptance.xlt.api.validators.HttpResponseCodeValidator;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Validates the Httpcode of a {@link WebResponse}. Httpcode can be specified or defaulted.
 *
 * @author ckeiner
 */
public class HttpCodeValidator extends AbstractResponseSubItem
{
    /**
     * The expected httpcode
     */
    private String httpcode;

    /**
     * Creates an instance of {@link HttpCodeValidator} and sets {@link #httpcode}.
     *
     * @param httpcode
     *            The expected Http Responsecode
     */
    public HttpCodeValidator(final String httpcode)
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
