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

        final List<AbstractValidator> validator = new ArrayList<AbstractValidator>();
        String validationName = null;

        final Iterator<JsonNode> iterator = node.elements();

        while (iterator.hasNext())
        {
            final JsonNode current = iterator.next();
            final Iterator<String> fieldName = current.fieldNames();

            while (fieldName.hasNext())
            {
                validationName = fieldName.next();
                // The substructure
                final JsonNode storeContent = current.get(validationName);
                final Iterator<String> name = storeContent.fieldNames();
                // Iterate over the content
                while (name.hasNext())
                {
                    final String leftHandExpression = name.next();
                    switch (leftHandExpression)
                    {
                        case Constants.XPATH:
                            final String xPathExpression = storeContent.get(leftHandExpression).textValue();
                            String matches = null;
                            String count = null;
                            // if we have another name, this means the optional text is specified
                            if (name.hasNext())
                            {
                                final String left = name.next();
                                if (left.equals(Constants.MATCHES))
                                {
                                    matches = storeContent.get(left).textValue();
                                }
                                else if (left.equals(Constants.COUNT))
                                {
                                    count = storeContent.get(left).textValue();
                                }
                            }
                            validator.add(new XPathValidator(validationName, xPathExpression, matches, count));

                            break;

                        case Constants.REGEXP:
                            final String pattern = storeContent.get(leftHandExpression).textValue();
                            String group = null;
                            String text = null;
                            // if we have another name, this means the optional text is specified
                            if (name.hasNext())
                            {
                                text = storeContent.get(name.next()).textValue();
                                // if we have yet another name, this is the optional group
                                if (name.hasNext())
                                {
                                    group = storeContent.get(name.next()).textValue();
                                }
                            }

                            validator.add(new RegExpValidator(validationName, pattern, text, group));
                            break;

                        case Constants.HEADER:
                            final String header = storeContent.get(leftHandExpression).textValue();
                            String textOrCountDecider = null;
                            String textOrCount = null;
                            if (name.hasNext())
                            {
                                textOrCountDecider = name.next();
                                textOrCount = storeContent.get(textOrCountDecider).textValue();
                            }
                            validator.add(new HeaderValidator(validationName, header, textOrCountDecider, textOrCount));
                            break;

                        case Constants.COOKIE:
                            final String cookieName = storeContent.get(leftHandExpression).textValue();
                            String cookieContent = null;

                            // If we have another name, it is the optional "matches" field
                            if (name.hasNext())
                            {
                                cookieContent = storeContent.get(name.next()).textValue();
                            }

                            validator.add(new CookieValidator(validationName, cookieName, cookieContent));
                            break;

                        default:
                            throw new IOException("No permitted validation item: " + leftHandExpression);
                    }
                }

                XltLogger.runTimeLogger.debug("Added " + validationName + " to Validations");
            }
        }
        // return new ArrayList<AbstractValidator>();
        return validator;
    }

}
