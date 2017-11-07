package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.response;

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
                String validationMode = Constants.EXISTS;
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
                                validationMode = name.next();
                                if (validationMode.equals(Constants.MATCHES) || validationMode.equals(Constants.TEXT))
                                {
                                    matches = ParserUtils.readValue(storeContent, validationMode);

                                }
                                else if (validationMode.equals(Constants.COUNT))
                                {
                                    count = ParserUtils.readValue(storeContent, validationMode);
                                }
                                else
                                {
                                    throw new IllegalArgumentException("Unknown validation item: " + validationMode);
                                }
                            }
                            // Add the validator to the validations
                            validator.add(new XPathValidator(validationName, validationMode, xPathExpression, matches, count));

                            break;

                        case Constants.REGEXP:
                            // Extract the RegExp pattern
                            final String pattern = ParserUtils.readValue(storeContent, leftHandExpression);
                            String group = null;
                            String text = null;
                            // if we have another fieldName, the optional text is specified
                            if (name.hasNext())
                            {
                                validationMode = name.next();
                                // TODO [Meeting] Text vs Matches
                                if (validationMode.equals(Constants.TEXT) || validationMode.equals(Constants.MATCHES))
                                {
                                    text = ParserUtils.readValue(storeContent, validationMode);
                                    // if we have yet another fieldName, the optional group is specified
                                    if (name.hasNext())
                                    {
                                        final String nextName = name.next();
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
                                    throw new IllegalArgumentException("Unknown validation item: " + validationMode);
                                }
                            }

                            // Add the validator to the validations
                            validator.add(new RegExpValidator(validationName, validationMode, pattern, text, group));
                            break;

                        case Constants.HEADER:
                            // Extract the name of the header
                            final String header = ParserUtils.readValue(storeContent, leftHandExpression);
                            String headerText = null;
                            String headerCount = null;
                            // If we have another fieldName, an optional attribute is specified
                            if (name.hasNext())
                            {
                                validationMode = name.next();
                                if (validationMode.equals(Constants.TEXT))
                                {
                                    headerText = ParserUtils.readValue(storeContent, validationMode);
                                }
                                else if (validationMode.equals(Constants.COUNT))
                                {
                                    headerCount = ParserUtils.readValue(storeContent, validationMode);
                                }
                                else
                                {
                                    throw new IllegalArgumentException("Unknown validation item: " + validationMode);
                                }
                            }
                            // Add the validator to the validations
                            validator.add(new HeaderValidator(validationName, validationMode, header, headerText, headerCount));
                            break;

                        case Constants.COOKIE:
                            final String cookieName = ParserUtils.readValue(storeContent, leftHandExpression);
                            String cookieContent = null;

                            // If we have another name, the optional "matches" field is specified
                            if (name.hasNext())
                            {
                                validationMode = name.next();
                                // TODO [Meeting] Which is the correct one? Specification vs Examples
                                if (validationMode.equals(Constants.MATCHES) || validationMode.equals(Constants.TEXT))
                                {
                                    cookieContent = ParserUtils.readValue(storeContent, validationMode);
                                }
                                else
                                {
                                    throw new IllegalArgumentException("Unknown validation item: " + validationMode);
                                }
                            }

                            // Add the validator to the validations
                            validator.add(new CookieValidator(validationName, validationMode, cookieName, cookieContent));
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
