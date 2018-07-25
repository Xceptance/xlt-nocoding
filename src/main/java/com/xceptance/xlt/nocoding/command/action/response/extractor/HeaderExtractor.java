package com.xceptance.xlt.nocoding.command.action.response.extractor;

import java.util.List;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Extracts all headers with the name provided by {@link #getExtractionExpression()}. Headers are located in
 * {@link Context#getWebResponse()}.
 *
 * @author ckeiner
 */
public class HeaderExtractor extends AbstractExtractor
{

    /**
     * Creates an instance of {@link HeaderExtractor}, sets {@link #extractionExpression} and creates an ArrayList for
     * {@link #result}.
     *
     * @param extractionExpression
     *            The name of the header
     */
    public HeaderExtractor(final String extractionExpression)
    {
        super(extractionExpression);
    }

    /**
     * Iterates over all headers in {@link Context#getWebResponse()} and stores every header with the name
     * {@link #getExtractionExpression()} via {@link #addResult(String)}.
     */
    @Override
    public void execute(final Context<?> context)
    {
        // Resolve variables
        resolveValues(context);

        // Get all headers
        final List<NameValuePair> headers = context.getWebResponse().getResponseHeaders();
        // For each header,
        for (final NameValuePair header : headers)
        {
            // Search for the header name
            if (header.getName().equals(getExtractionExpression()))
            {
                // Add the value to the result list
                addResult(header.getValue());
            }
        }
    }

}
