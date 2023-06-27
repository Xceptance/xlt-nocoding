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
package com.xceptance.xlt.nocoding.util;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.engine.XltWebClient;

import java.text.MessageFormat;

/**
 * Handles all property related matters.
 *
 * @author ckeiner
 */
public class NoCodingPropertyAdmin
{
    private final XltProperties xltProperties;

    public static final String REDIRECTENABLED = "com.xceptance.xlt.nocoding.redirect";

    public static final String FILENAME = "com.xceptance.xlt.nocoding.filename";

    public static final String DIRECTORY = "com.xceptance.xlt.nocoding.directory";

    public static final String USERAGENTUID = "userAgent.UID";

    public static final String DOWNLOADTHREADS = "com.xceptance.xlt.staticContent.downloadThreads";

    public static final String MODE = "com.xceptance.xlt.nocoding.mode";

    public static final String LIGHTWEIGHT = "light";

    public static final String DOM = "dom";

    public static final String REQUEST = "request";

    public static final String MODE_DEFAULT = REQUEST;

    public static final String DIRECTORY_DEFAULT = "./config/data/";

    public static final String DEFAULT_HTTP_CODE_VALIDATION = "com.xceptance.xlt.nocoding.defaultHttpCodeValidation";

    public NoCodingPropertyAdmin(final XltProperties xltProperties)
    {
        this.xltProperties = xltProperties;
    }

    /**
     * Sets whether redirects should be enabled or not.
     *
     * @param webClient
     *            The {@link XltWebClient} on which to set this property.
     */
    public void configWebClient(final XltWebClient webClient)
    {
        setRedirectEnabled(webClient);
    }

    private void setRedirectEnabled(final XltWebClient webClient)
    {
        final String property = getPropertyByKey(REDIRECTENABLED, "true");

        if (property.equalsIgnoreCase("true") || property.equalsIgnoreCase("false"))
        {
            final boolean bool = Boolean.valueOf(property);
            webClient.getOptions().setRedirectEnabled(bool);
            XltLogger.runTimeLogger.debug(getConfigWebClient("Redirect", String.valueOf(bool)));
        }
        else
        {
            throw new IllegalArgumentException(getIllegalPropertyValue(property, REDIRECTENABLED));
        }
    }

    public String getPropertyByKey(final String key)
    {
        return xltProperties.getProperty(key);
    }

    public String getPropertyByKey(final String key, final String defaultValue)
    {
        return xltProperties.getProperty(key, defaultValue);
    }

    public int getPropertyByKey(final String key, final int defaultValue)
    {
        return xltProperties.getProperty(key, defaultValue);
    }

    public boolean getPropertyByKey(final String key, final boolean defaultValue)
    {
        return xltProperties.getProperty(key, defaultValue);
    }

    public long getPropertyByKey(final String key, final long defaultValue)
    {
        return xltProperties.getProperty(key, defaultValue);
    }

    public XltProperties getProperties()
    {
        return xltProperties;
    }

    private String getIllegalPropertyValue(final String value, final String property)
    {
        final String message = MessageFormat.format("Illegal value: \"{0}\" for Property: \"{1}\"", value, property);
        return message;
    }

    private String getConfigWebClient(final String option, final String value)
    {
        final String message = MessageFormat.format("Config WebClient: \"{0}\" = \"{1}\"", option, value);
        return message;
    }
}
