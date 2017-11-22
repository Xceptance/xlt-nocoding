package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.response.selector.SelectorParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.response.validators.ValidationModeParser;
import com.xceptance.xlt.nocoding.scriptItem.action.response.AbstractResponseItem;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Validator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.selector.AbstractSelector;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMode.AbstractValidationMode;
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
    public List<AbstractResponseItem> parse(final JsonNode node) throws IOException
    {
        // Initialize variables
        final List<AbstractResponseItem> validator = new ArrayList<AbstractResponseItem>();
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
                // The current fieldName is the name of the validation
                validationName = fieldName.next();
                String group = null;
                AbstractSelector selector = null;
                AbstractValidationMode validation = null;
                /*
                 * Substructure of a validation
                 */

                // Get the substructure (which is an Object)
                final JsonNode validationContent = current.get(validationName);
                // And get an iterator over the fieldNames
                final Iterator<String> name = validationContent.fieldNames();
                // Iterate over the fieldNames of the substructure
                while (name.hasNext())
                {
                    final String nextName = name.next();

                    if (Constants.isPermittedSelectionMode(nextName))
                    {
                        selector = new SelectorParser(nextName).parse(validationContent);
                    }
                    else if (Constants.isPermittedValidationMode(nextName))
                    {
                        validation = new ValidationModeParser(nextName).parse(validationContent);
                    }
                    else if (nextName.equals(Constants.GROUP))
                    {
                        group = ParserUtils.readValue(validationContent, nextName);
                    }
                    else
                    {
                        throw new IllegalArgumentException("Unknown Validation Item " + nextName);
                    }
                }
                validator.add(new Validator(validationName, selector, validation, group));

                XltLogger.runTimeLogger.debug("Added " + validationName + " to Validations");
            }
        }
        // Return all validations
        return validator;
    }

}
