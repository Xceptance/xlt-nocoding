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
package com.xceptance.xlt.nocoding.command.action.response.extractor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlunit.util.NameValuePair;

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
     * The optional regular expression to extract only a part of a header value.
     */
    private String regex;

    /**
     * The optional matching group index to use when extracting only a part of a header value.
     */
    private String group;

    /**
     * Creates an instance of {@link HeaderExtractor}, sets {@link #extractionExpression} and creates an ArrayList for
     * {@link #result}.
     *
     * @param extractionExpression
     *            The name of the header
     */
    public HeaderExtractor(final String extractionExpression)
    {
        this(extractionExpression, null, null);
    }

    /**
     * Creates an instance of {@link HeaderExtractor}, sets {@link #extractionExpression} and creates an ArrayList for
     * {@link #result}.
     *
     * @param extractionExpression
     *            The name of the header
     * @param regex
     *            The regular expression to extract only a part of a header value (maybe <code>null</code>)
     * @param group
     *            The matching group index to use when extracting only a part of a header value (maybe
     *            <code>null</code>)
     */
    public HeaderExtractor(final String extractionExpression, final String regex, final String group)
    {
        super(extractionExpression);
        this.regex = regex;
        this.group = group;
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

        // Create a reusable Pattern object from the regex if available
        final Pattern pattern = regex == null ? null : Pattern.compile(regex);

        // Get all headers
        final List<NameValuePair> headers = context.getWebResponse().getResponseHeaders();
        // For each header,
        for (final NameValuePair header : headers)
        {
            // Search for the header name
            if (header.getName().equals(getExtractionExpression()))
            {
                final String headerValue = header.getValue();

                if (pattern != null)
                {
                    // Extract only a part of the header value
                    final Matcher matcher = pattern.matcher(headerValue);

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
                    // Simply add the complete header value to the result list
                    addResult(headerValue);
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
