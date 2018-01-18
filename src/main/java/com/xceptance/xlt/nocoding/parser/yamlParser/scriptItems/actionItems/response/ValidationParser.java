package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.response;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.response.extractor.ExtractorParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.response.validators.ValidationMethodParser;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Validator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.AbstractExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.RegexpExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod.AbstractValidationMethod;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * The class for parsing validations.
 * 
 * @author ckeiner
 */
public class ValidationParser
{

    /**
     * Parses the validation items in the response block to a list of {@link Validator}.
     * 
     * @param validateNode
     *            The {@link JsonNode} with the validation item
     * @return A list of <code>Validator</code>
     */
    public List<Validator> parse(final JsonNode validateNode)
    {
        // Verify that an array was used and not an object
        if (!(validateNode instanceof ArrayNode))
        {
            throw new IllegalArgumentException("Expected ArrayNode in the validate block but was "
                                               + validateNode.getClass().getSimpleName());
        }
        // Initialize variables
        final List<Validator> validator = new ArrayList<Validator>();
        String validationName = null;

        // Get an Iterator over every validation
        final Iterator<JsonNode> iterator = validateNode.elements();

        // Iterate over the elements
        while (iterator.hasNext())
        {
            // Get the next element, therefore the ObjectNode with a single validation in it
            final JsonNode current = iterator.next();
            // Get an iterator over the fieldNames, which should only be the name of the validation
            final Iterator<String> fieldName = current.fieldNames();

            // Iterate over the fieldNames
            while (fieldName.hasNext())
            {
                // Get the next fieldName, which is also the name of the validation
                validationName = fieldName.next();
                AbstractExtractor extractor = null;
                AbstractValidationMethod validation = null;

                /*
                 * Substructure of a validation
                 */

                // Get the substructure, that is the ObjectNode with the information of the validation
                final JsonNode validationContent = current.get(validationName);
                // Verify that it is an ObjectNode
                if (!(validationContent instanceof ObjectNode))
                {
                    throw new IllegalArgumentException("Expected ObjectNode after the validation name, " + validationName + ", but was "
                                                       + validateNode.getClass().getSimpleName());
                }

                // And get an iterator over the fieldNames, that is the content of a single Validator
                final Iterator<String> name = validationContent.fieldNames();
                // Iterate over the fieldNames of the substructure
                while (name.hasNext())
                {
                    // Get the next fieldName
                    final String nextName = name.next();

                    // If it is a permitted extraction mode
                    if (Constants.isPermittedExtraction(nextName))
                    {
                        // Check that the extractor is the first item and no other extractor was defined already
                        if (validation == null && extractor == null)
                        {
                            // Parse the extractor
                            XltLogger.runTimeLogger.debug("Extraction Mode is " + extractor);
                            extractor = new ExtractorParser(nextName).parse(validationContent);
                        }
                        else
                        {
                            // Throw an error depending on why the parsing failed
                            String errorMessage = null;
                            if (validation != null)
                            {
                                errorMessage = "Extraction must be parsed before the Validation Method!";
                            }
                            else if (extractor != null)
                            {
                                errorMessage = "Cannot parse two extractors!";
                            }
                            throw new IllegalArgumentException(errorMessage);
                        }
                    }
                    // If it is a permitted validation method
                    else if (Constants.isPermittedValidationMethod(nextName))
                    {
                        // Check that the validation is the second item and no other validation was defined already
                        if (extractor != null && validation == null)
                        {
                            // Parse the validation method
                            XltLogger.runTimeLogger.debug("Validation Method is " + extractor);
                            validation = new ValidationMethodParser(nextName).parse(validationContent);
                        }
                        else
                        {
                            // Throw an error depending on why the parsing failed
                            String errorMessage = null;
                            if (extractor == null)
                            {
                                errorMessage = "Validation Method must be parsed after the extractor!";
                            }
                            else if (validation != null)
                            {
                                errorMessage = "Cannot parse two validation method!";
                            }
                            throw new IllegalArgumentException(errorMessage);
                        }
                    }
                    // If it is group
                    else if (nextName.equals(Constants.GROUP))
                    {
                        if (!(extractor instanceof RegexpExtractor))
                        {
                            throw new IllegalArgumentException("Group cannot be specified unless the extractor is a RegexpExtractor, but is "
                                                               + extractor.getClass().getSimpleName());
                        }
                    }
                    // If it is none of the above, nextName was not a permitted validation item
                    else
                    {
                        throw new IllegalArgumentException("Unknown Validation Item " + nextName);
                    }
                }
                // Add the new validator to the list of validators
                validator.add(new Validator(validationName, extractor, validation));
                // Print a debug statement
                XltLogger.runTimeLogger.debug("Added " + validationName + " to Validations");
            }
        }
        // Return all validations
        return validator;
    }

}
