package com.xceptance.xlt.nocoding.command.action.response.extractor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * The optional regular expression to extract only a part of a cookie value.
     */
    private String regex;

    /**
     * The optional matching group index to use when extracting only a part of a cookie value.
     */
    private String group;

    /**
     * Creates an instance of {@link CookieExtractor}, sets {@link #extractionExpression} and creates an ArrayList for
     * {@link #result}.
     *
     * @param extractionExpression
     *            The name of the cookie
     */
    public CookieExtractor(final String extractionExpression)
    {
        this(extractionExpression, null, null);
    }

    /**
     * Creates an instance of {@link CookieExtractor}, sets {@link #extractionExpression} and creates an ArrayList for
     * {@link #result}.
     *
     * @param extractionExpression
     *            The name of the cookie
     * @param regex
     *            The regular expression to extract only a part of a cookie value (maybe <code>null</code>)
     * @param group
     *            The matching group index to use when extracting only a part of a cookie value (maybe
     *            <code>null</code>)
     */
    public CookieExtractor(final String extractionExpression, final String regex, final String group)
    {
        super(extractionExpression);
        this.regex = regex;
        this.group = group;
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

        // Create a reusable Pattern object from the regex if available
        final Pattern pattern = regex == null ? null : Pattern.compile(regex);

        final List<NameValuePair> headers = context.getWebResponse().getResponseHeaders();
        // For each header,
        for (final NameValuePair header : headers)
        {
            // Search for the Set-Cookie header
            if (header.getName().equalsIgnoreCase("Set-Cookie"))
            {
                // Get the cookieName by looking for the separating character
                final int equalSignPosition = header.getValue().indexOf("=");
                // Get the cookieName
                String cookieName = header.getValue().substring(0, equalSignPosition);
                // Remove possible whitespace at the beginning
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
                    // Remove possible whitespace at the beginning or end
                    cookieContent = cookieContent.trim();

                    if (pattern != null)
                    {
                        // Extract only a part of the cookie value
                        final Matcher matcher = pattern.matcher(cookieContent);

                        // If we don't have a group, add all matches
                        if (group == null)
                        {
                            // If we find matches
                            while (matcher.find())
                            {
                                addResult(matcher.group());
                            }
                        }
                        // Else, simply add the group
                        else
                        {
                            if (matcher.find())
                            {
                                addResult(matcher.group(Integer.parseInt(group)));
                            }
                        }
                    }
                    else
                    {
                        // Add it to the results
                        addResult(cookieContent);
                    }
                }
            }
        }
    }

    /**
     * Resolves the {@link #getExtractionExpression()} as well as {@link #regex} and {@link #group}.
     */
    @Override
    protected void resolveValues(final Context<?> context)
    {
        super.resolveValues(context);

        if (regex != null && !regex.isEmpty())
        {
            regex = context.resolveString(regex);
        }

        if (group != null && !group.isEmpty())
        {
            group = context.resolveString(group);
        }
    }

    public String getRegex()
    {
        return regex;
    }

    public String getGroup()
    {
        return group;
    }
}
