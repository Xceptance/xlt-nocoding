package com.xceptance.xlt.nocoding.scriptItem.action.response.extractor;

import java.util.List;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Extracts the specified cookie from the headers located in {@link Context#getWebResponse()}. The cookie is extracted
 * via name provided by {@link #getExtractionExpression()}. The value of the cookie is then accessible with
 * {@link #getResult()}.
 * 
 * @author ckeiner
 */
public class CookieExtractor extends AbstractExtractor
{

    /**
     * Creates an instance of {@link CookieExtractor}, sets {@link #extractionExpression} and creates an ArrayList for
     * {@link #result}.
     * 
     * @param extractionExpression
     *            The name of the cookie
     */
    public CookieExtractor(final String extractionExpression)
    {
        super(extractionExpression);
    }

    /**
     * Iterates over the headers in {@link Context#getWebResponse()} and extracts the cookie with the name provided by
     * {@link #getExtractionExpression()}. Finally, it stores the value in the results via {@link #addResult(String)}.
     * 
     * @param context
     *            The {@link Context} to use
     */
    @Override
    public void execute(final Context<?> context)
    {
        // Resolve variables
        resolveValues(context);

        final List<NameValuePair> headers = context.getWebResponse().getResponseHeaders();
        // For each header,
        for (final NameValuePair header : headers)
        {
            // Search for the Set-Cookie header
            if (header.getName().equals("Set-Cookie"))
            {
                // Get the cookieName by looking for the separating character
                final int equalSignPosition = header.getValue().indexOf("=");
                // Get the cookieName
                String cookieName = header.getValue().substring(0, equalSignPosition);
                // Remove possible whitespaces at the beginning
                cookieName = cookieName.trim();
                // and compare it with the extractionExpression
                if (cookieName.equals(getExtractionExpression()))
                {
                    String cookieContent = null;
                    // Get the end position of the cookie content, which is at the first semicolon
                    final int semicolonPosition = header.getValue().indexOf(";");
                    // If the cookie does not have a semicolon, the content ends with the end of the string
                    if (semicolonPosition < 0)
                    {
                        // The content starts after the equal sign position and ends at the end of the string
                        cookieContent = header.getValue().substring(equalSignPosition + 1, header.getValue().length());
                    }
                    else
                    {
                        // Content starts after the equal sign position and ends before the semicolon
                        cookieContent = header.getValue().substring(equalSignPosition + 1, semicolonPosition);
                    }
                    // Remove possible whitespaces at the beginning or end
                    cookieContent = cookieContent.trim();
                    // Add it to the results
                    addResult(cookieContent);
                }
            }
        }

    }

}
