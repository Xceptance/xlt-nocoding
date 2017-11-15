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
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.AbstractValidationMode;

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

                /*
                 * Substructure of a validation
                 */

                // Get the substructure (which is an Object)
                final JsonNode validationContent = current.get(validationName);
                // And get an iterator over the fieldNames
                final Iterator<String> name = validationContent.fieldNames();
                // Iterate over the fieldNames of the substructure

                final AbstractSelector selector = new SelectorParser(name).parse(validationContent);
                final AbstractValidationMode validation = new ValidationModeParser(name).parse(validationContent);
                validator.add(new Validator(validationName, selector, validation));

                XltLogger.runTimeLogger.debug("Added " + validationName + " to Validations");
            }
        }
        // Return all validations
        return validator;
    }

}
