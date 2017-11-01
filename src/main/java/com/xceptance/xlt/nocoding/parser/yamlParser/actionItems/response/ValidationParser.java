package com.xceptance.xlt.nocoding.parser.yamlParser.actionItems.response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.AbstractValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.CookieValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.HeaderValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.RegExpValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.XPathValidator;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.ParserUtils;

public class ValidationParser
{

    /**
     * Parses the validation items in the response block to List<AbstractValidator>
     * 
     * @param node
     *            The node the item starts at
     * @return
     * @throws IOException
     */
    public List<AbstractValidator> parse(final JsonNode node) throws IOException
    {
        // Initialize variables
        final List<AbstractValidator> validator = new ArrayList<AbstractValidator>();
        String validationName = null;

        // Get an iterator over the elements
        final Iterator<JsonNode> iterator = node.elements();

        // Iterate over the elements
        while (iterator.hasNext())
        {
            final JsonNode current = iterator.next();
            // Get the fieldNames
            final Iterator<String> fieldName = current.fieldNames();

            // Iterate over the fieldNames
            while (fieldName.hasNext())
            {
                // The current fieldName ist the name of the validation
                validationName = fieldName.next();
                // Get the substructure (which is an Object)
                final JsonNode storeContent = current.get(validationName);
                // And get an iterator over the fieldNames
                final Iterator<String> name = storeContent.fieldNames();
                // Iterate over the fieldNames of the substructure
                while (name.hasNext())
                {
                    // Get the left hand expression
                    final String leftHandExpression = name.next();
                    switch (leftHandExpression)
                    {
                        case Constants.XPATH:
                            // Get the xPath Expression
                            final String xPathExpression = ParserUtils.readValue(storeContent, leftHandExpression);
                            String matches = null;
                            String count = null;
                            // if we have another fieldName, an optional attribute is specified
                            if (name.hasNext())
                            {
                                // Get the left hand expression of the second fieldName
                                final String left = name.next();
                                if (left.equals(Constants.MATCHES))
                                {
                                    matches = ParserUtils.readValue(storeContent, left);
                                }
                                else if (left.equals(Constants.COUNT))
                                {
                                    count = ParserUtils.readValue(storeContent, left);
                                }
                                else
                                {
                                    throw new IllegalArgumentException("Unknown validation item: " + left);
                                }
                            }
                            // Add the validator to the validations
                            validator.add(new XPathValidator(validationName, xPathExpression, matches, count));

                            break;

                        case Constants.REGEXP:
                            // Extract the RegExp pattern
                            final String pattern = ParserUtils.readValue(storeContent, leftHandExpression);
                            String group = null;
                            String text = null;
                            // if we have another fieldName, the optional text is specified
                            if (name.hasNext())
                            {
                                String nextName = name.next();
                                // TODO Text vs Matches
                                if (nextName.equals(Constants.TEXT) || nextName.equals(Constants.MATCHES))
                                {
                                    text = ParserUtils.readValue(storeContent, nextName);
                                    // if we have yet another fieldName, the optional group is specified
                                    if (name.hasNext())
                                    {
                                        nextName = name.next();
                                        if (nextName.equals(Constants.GROUP))
                                        {
                                            group = ParserUtils.readValue(storeContent, nextName);
                                        }
                                        else
                                        {
                                            throw new IllegalArgumentException("Unknown validation item: " + nextName);
                                        }
                                    }
                                }
                                else
                                {
                                    throw new IllegalArgumentException("Unknown validation item: " + nextName);
                                }
                            }

                            // Add the validator to the validations
                            validator.add(new RegExpValidator(validationName, pattern, text, group));
                            break;

                        case Constants.HEADER:
                            // Extract the name of the header
                            final String header = ParserUtils.readValue(storeContent, leftHandExpression);
                            String headerText = null;
                            String headerCount = null;
                            // If we have another fieldName, an optional attribute is specified
                            if (name.hasNext())
                            {
                                final String textOrCount = name.next();
                                if (textOrCount.equals(Constants.TEXT))
                                {
                                    headerText = ParserUtils.readValue(storeContent, textOrCount);
                                }
                                else if (textOrCount.equals(Constants.COUNT))
                                {
                                    headerCount = ParserUtils.readValue(storeContent, textOrCount);
                                }
                                else
                                {
                                    throw new IllegalArgumentException("Unknown validation item: " + textOrCount);
                                }
                            }
                            // Add the validator to the validations
                            validator.add(new HeaderValidator(validationName, header, headerText, headerCount));
                            break;

                        case Constants.COOKIE:
                            final String cookieName = ParserUtils.readValue(storeContent, leftHandExpression);
                            String cookieContent = null;

                            // If we have another name, the optional "matches" field is specified
                            if (name.hasNext())
                            {
                                final String nextCookieContent = name.next();
                                // TODO Which is the correct one? Specification vs Examples
                                if (nextCookieContent.equals(Constants.MATCHES) || nextCookieContent.equals(Constants.TEXT))
                                {
                                    cookieContent = ParserUtils.readValue(storeContent, nextCookieContent);
                                }
                                else
                                {
                                    throw new IllegalArgumentException("Unknown validation item: " + nextCookieContent);
                                }
                            }

                            // Add the validator to the validations
                            validator.add(new CookieValidator(validationName, cookieName, cookieContent));
                            break;

                        default:
                            throw new IOException("No permitted validation item: " + leftHandExpression);
                    }
                }

                XltLogger.runTimeLogger.debug("Added " + validationName + " to Validations");
            }
        }
        // Return all validations
        return validator;
    }

}
