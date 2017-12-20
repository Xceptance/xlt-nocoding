package com.xceptance.xlt.nocoding.util;

import java.text.MessageFormat;

import org.apache.commons.codec.binary.Base64;

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

    public static final String JAVASCRIPTENABLED = "com.xceptance.xlt.javaScriptEnabled";

    public static final String CSSENABLED = "com.xceptance.xlt.cssEnabled";

    public static final String LOADSTATICCONTENT = "com.xceptance.xlt.loadStaticContent";

    public static final String USERNAMEAUTH = "com.xceptance.xlt.auth.userName";

    public static final String PASSWORDAUTH = "com.xceptance.xlt.auth.password";

    public static final String REDIRECTENABLED = "com.xceptance.xlt.nocoding.redirect";

    public static final String FILENAME = "com.xceptance.xlt.nocoding.filename";

    public static final String DIRECTORY = "com.xceptance.xlt.nocoding.directory";

    public static final String USERAGENTUID = "userAgent.UID";

    public static final String DOWNLOADTHREADS = "com.xceptance.xlt.staticContent.downloadThreads";

    public static final String TLSVERSION = "com.xceptance.xlt.nocoding.TLSVersion";

    public static final String MODE = "com.xceptance.xlt.nocoding.mode";

    public static final String LIGHTWEIGHT = "light";

    public static final String DOM = "dom";

    public NoCodingPropertyAdmin(final XltProperties xltProperties)
    {
        this.xltProperties = xltProperties;
    }

    public void configWebClient(final XltWebClient webClient)
    {
        setJavaScriptEnabled(webClient);
        setCssEnabled(webClient);
        setLoadStaticContent(webClient);
        setCredentials(webClient);
        setRedirectEnabled(webClient);
        setTlsVersion(webClient);
    }

    private void setTlsVersion(final XltWebClient webClient)
    {
        final String tlsVersion = getPropertyByKey(TLSVERSION);
        if (tlsVersion != null)
        {
            webClient.getOptions().setSSLClientProtocols(new String[]
                {
                  tlsVersion
                });
            XltLogger.runTimeLogger.debug(getConfigWebClient("TLSVersion", tlsVersion));
        }
    }

    private void setJavaScriptEnabled(final XltWebClient webClient)
    {
        final String property = getPropertyByKey(JAVASCRIPTENABLED, "true");
        if (property.equalsIgnoreCase("true") || property.equalsIgnoreCase("false"))
        {
            final boolean bool = Boolean.valueOf(property);
            webClient.getOptions().setJavaScriptEnabled(bool);
            XltLogger.runTimeLogger.debug(getConfigWebClient("JavaScriptEnabled", String.valueOf(bool)));
        }
        else
        {
            throw new IllegalArgumentException(getIllegalPropertyValue(property, JAVASCRIPTENABLED));
        }
    }

    private void setCssEnabled(final XltWebClient webClient)
    {
        final String property = getPropertyByKey(CSSENABLED, "true");

        if (property.equalsIgnoreCase("true") || property.equalsIgnoreCase("false"))
        {
            final boolean bool = Boolean.valueOf(property);
            webClient.getOptions().setCssEnabled(bool);
            XltLogger.runTimeLogger.debug(getConfigWebClient("CssEnabled", String.valueOf(bool)));
        }
        else
        {
            throw new IllegalArgumentException(getIllegalPropertyValue(property, CSSENABLED));
        }
    }

    private void setLoadStaticContent(final XltWebClient webClient)
    {
        final String property = getPropertyByKey(LOADSTATICCONTENT, "true");

        if (property.equalsIgnoreCase("true") || property.equalsIgnoreCase("false"))
        {
            final boolean bool = Boolean.valueOf(property);
            webClient.setLoadStaticContent(bool);
            XltLogger.runTimeLogger.debug(getConfigWebClient("LoadStaticContent", String.valueOf(bool)));
        }
        else
        {
            throw new IllegalArgumentException(getIllegalPropertyValue(property, LOADSTATICCONTENT));
        }
    }

    private void setCredentials(final XltWebClient webClient)
    {
        final String name = getAuthName();
        final String password = getAuthPassword();
        if (name != null)
        {
            final String userPass = name + ":" + password;
            final String userPassBase64 = Base64.encodeBase64String(userPass.getBytes());
            webClient.addRequestHeader("Authorization", "Basic " + userPassBase64);
            XltLogger.runTimeLogger.debug(getConfigWebClient("Credentials", userPass));
        }
    }

    private String getAuthName()
    {
        final String property = getPropertyByKey(USERNAMEAUTH);
        return property;
    }

    private String getAuthPassword()
    {
        final String property = getPropertyByKey(PASSWORDAUTH);
        return property;
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
