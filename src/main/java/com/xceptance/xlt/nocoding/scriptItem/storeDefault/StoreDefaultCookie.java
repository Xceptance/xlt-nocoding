package com.xceptance.xlt.nocoding.scriptItem.storeDefault;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Stream;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.engine.XltWebClient;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.uniqueStorage.UniqueStorage;

/**
 * Stores a default cookie and sets it at the {@link WebClient}.
 * 
 * @author ckeiner
 */
public class StoreDefaultCookie extends StoreDefault
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
     * If {@link #getValue()} is {@link Constants#DELETE}, the list of default cookies is deleted. Else, it stores a default
     * cookie.
     * 
     * @throws MalformedURLException
     */
    @Override
    public void execute(final Context<?> context) throws MalformedURLException
    {
        // Resolve values
        super.resolveValues(context);
        // Get the appropriate storage
        final UniqueStorage storage = context.getDefaultCookies();
        // If the value is not "delete"
        if (!value.equals(Constants.DELETE))
        {
            storage.store(variableName, value);
            // Add a cookie with a non-existing url
            context.getWebClient().addCookie(variableName + "=" + value, new URL("http://thisIsno.existingDo.main"), this);
            XltLogger.runTimeLogger.debug("Added \"" + variableName.toLowerCase() + "\" with the value \"" + value
                                          + "\" to default cookies");
        }
        else
        {
            // If the variableName is Constants.COOKIES, then we delete all default cookies
            if (variableName.equals(Constants.COOKIES))
            {

                // Delete all cookies from the WebClient
                storage.getItems().forEach((key, value) -> {
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
     * @return True if the cookie was found, else false
     */
    private boolean deleteCookie(final String cookieName, final Context<?> context)
    {
        final boolean wasRemoved = false;
        final CookieManager cookieManager = context.getWebClient().getCookieManager();
        // Find the first occurence of the cookie
        final Stream<Cookie> cookieStream = cookieManager.getCookies()
                                                         .stream()
                                                         .filter(singleCookie -> singleCookie.getName().equals(cookieName));
        cookieStream.forEach((cookie) -> {
            cookieManager.removeCookie(cookie);
        });
        return wasRemoved;
    }

}
