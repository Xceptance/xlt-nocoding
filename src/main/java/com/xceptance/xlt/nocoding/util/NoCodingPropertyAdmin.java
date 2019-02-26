package com.xceptance.xlt.nocoding.util;

import java.text.MessageFormat;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.engine.XltWebClient;

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
