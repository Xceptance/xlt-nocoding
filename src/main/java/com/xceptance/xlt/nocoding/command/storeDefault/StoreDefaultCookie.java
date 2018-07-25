package com.xceptance.xlt.nocoding.command.storeDefault;

import java.net.MalformedURLException;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.engine.XltWebClient;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.storage.unit.SingleStorage;

/**
 * Stores a default cookie and sets it at the {@link WebClient}.
 *
 * @author ckeiner
 */
public class StoreDefaultCookie extends AbstractStoreDefaultItem
{
    /**
     * Creates an instance of {@link StoreDefaultHeader} that sets {@link #getVariableName()} and {@link #getValue()}
     *
     * @param variableName
     *            The name of the cookie
     * @param value
     *            The corresponding default value of the cookie
     */
    public StoreDefaultCookie(final String variableName, final String value)
    {
        super(variableName, value);
    }

    /**
     * If {@link #getValue()} is {@link Constants#DELETE}, the list of default cookies is deleted. Else, it stores a
     * default cookie.
     *
     * @throws MalformedURLException
     */
    @Override
    public void execute(final Context<?> context) throws MalformedURLException
    {
        // Resolve values
        super.resolveValues(context);
        // Get the appropriate storage
        final SingleStorage storage = context.getDefaultCookies();
        // If the value is not "delete"
        if (!value.equals(Constants.DELETE))
        {
            // Parse 'variableName=value' as cookie and add it to the webclient's cookie manager
            addCookie(variableName, value, context);
        }
        else
        {
            // If the variableName is Constants.COOKIES, then we delete all default cookies
            if (variableName.equals(Constants.COOKIES))
            {

                // Delete all cookies from the WebClient
                storage.getItems().forEach((key) -> {
                    deleteCookie(key, context);
                });
                // Delete default cookies from the storage
                storage.clear();
                XltLogger.runTimeLogger.debug("Removed all default cookies");
            }
            // Else we simply delete the specified cookie
            else
            {
                deleteCookie(variableName, context);
                // Remove cookie from storage
                storage.remove(variableName);
                XltLogger.runTimeLogger.debug("Removed \"" + variableName + "\" from default cookies");
            }
        }
    }

    /**
     * Removes every default cookie from the {@link XltWebClient}.
     *
     * @param cookieName
     *            The name of the cookie
     * @param context
     *            The current {@link Context}
     */
    private void deleteCookie(final String cookieName, final Context<?> context)
    {
        final CookieManager cookieManager = context.getWebClient().getCookieManager();
        cookieManager.getCookies()
                     .stream()
                     .filter(singleCookie -> singleCookie.getName().equals(cookieName))
                     .forEach(cookieManager::removeCookie);
    }

    /**
     * Adds the cookie parsed from the passed name and definition to the webclient's cookie manager.
     *
     * @param cookieName
     *            The name of the cookie
     * @param cookieDefinition
     *            The cookie definition as per RFC 2965
     * @param context
     *            The current {@link Context}
     * @see #parseCookie(String, String)
     */
    private void addCookie(final String cookieName, final String cookieDefinition, final Context<?> context)
    {
        final Cookie cookie = parseCookie(cookieName, cookieDefinition);
        if (cookie != null)
        {
            context.getWebClient().getCookieManager().addCookie(cookie);
            context.getDefaultCookies().store(variableName);
            XltLogger.runTimeLogger.debug("Added cookie \"" + cookie.getName() + "\" with value \"" + cookie.getValue()
                                          + "\" to default cookies");
        }
    }

    /**
     * Interprets the given arguments as they would have been returned by a server as response header {@code
     * Set-Cookie: cookieName=cookieDefinition} and returns the parsed cookie if valid, and {@code null} otherwise.
     *
     * @param cookieName
     *            The name of the cookie
     * @param cookieDefinition
     *            The cookie definition as per RFC 2965
     * @return parsed cookie given name and definition are valid, {@code null} otherwise
     */
    private Cookie parseCookie(final String cookieName, final String cookieDefinition)
    {
        Cookie cookie = null;
        if (StringUtils.isNoneBlank(cookieName, cookieDefinition))
        {
            final StringTokenizer tokenizer = new StringTokenizer(cookieDefinition, ";");

            String cookieVal = null;
            String path = "/";
            String domain = "";
            String maxAge = "";
            boolean secure = false;

            while (tokenizer.hasMoreTokens())
            {
                final String token = tokenizer.nextToken().trim();
                if (cookieVal == null)
                {
                    cookieVal = token;
                    continue;
                }

                String att = token;
                String val = "";
                final int idx = token.indexOf('=');
                if (idx > -1)
                {
                    att = token.substring(0, idx).trim();
                    if (idx < token.length() - 1)
                    {
                        val = token.substring(idx + 1).trim();
                    }
                }

                // attribute value might be quoted -> remove wrapping quotes
                val = StringUtils.unwrap(val, '"');

                if (att.equalsIgnoreCase("domain"))
                {
                    final int lastDotPos = val.lastIndexOf('.');
                    // check for an embedded dot
                    if (lastDotPos > 0 && lastDotPos < val.length() - 1)
                    {
                        domain = val;
                        // append leading dot if missing
                        if (domain.charAt(0) != '.')
                        {
                            domain = "." + domain;
                        }
                    }
                }
                else if (att.equalsIgnoreCase("path"))
                {
                    path = StringUtils.defaultIfBlank(val, "/");
                    // append leading slash if missing
                    if (path.charAt(0) != '/')
                    {
                        path = "/" + path;
                    }
                }
                else if (att.equalsIgnoreCase("max-age"))
                {
                    maxAge = val;
                }
                else if (att.equalsIgnoreCase("secure"))
                {
                    secure = true;
                }
            }

            if (cookieVal != null && StringUtils.isNotBlank(domain))
            {
                int expires = -1;
                if (StringUtils.isNotBlank(maxAge))
                {
                    try
                    {
                        expires = Integer.parseInt(maxAge);
                    }
                    catch (final NumberFormatException nfe)
                    {
                        XltLogger.runTimeLogger.warn("Value '" + maxAge + "' of 'max-age' attribute for cookie '" + cookieName
                                                     + "' is invalid");
                    }
                }

                cookie = new Cookie(domain, cookieName, cookieVal, path, expires, secure);
            }
            else
            {
                final String warning = "Cookie will be ignored!";
                if (cookieVal == null)
                {
                    XltLogger.runTimeLogger.warn("No value specified! " + warning);
                }
                else
                {
                    XltLogger.runTimeLogger.warn("No domain specified! Domains must be specified for default cookies. " + warning);
                }
            }

        }

        return cookie;
    }
}
