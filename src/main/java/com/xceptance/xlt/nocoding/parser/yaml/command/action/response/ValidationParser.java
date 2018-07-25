package com.xceptance.xlt.nocoding.parser.yaml.command.action.response;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.command.action.response.Validator;
import com.xceptance.xlt.nocoding.command.action.response.extractor.AbstractExtractor;
import com.xceptance.xlt.nocoding.command.action.response.validator.AbstractValidator;
import com.xceptance.xlt.nocoding.parser.yaml.command.action.response.extractor.ExtractorParser;
import com.xceptance.xlt.nocoding.parser.yaml.command.action.response.validator.ValidatorParser;
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
            throw new IllegalArgumentException("Expected ArrayNode after Validate, but was " + validateNode.getClass().getSimpleName());
        }
        // Initialize variables
        final List<Validator> validatorList = new ArrayList<>();

        // Get an Iterator over every validation
        final Iterator<JsonNode> iteratorOverValidations = validateNode.elements();

        // Iterate over all validations
        while (iteratorOverValidations.hasNext())
        {
            // Get the next element, therefore the ObjectNode with a single validation in it
            final JsonNode validationNode = iteratorOverValidations.next();
            final Validator validator = parseSingleValidator(validationNode);
            // Print a debug statement
            XltLogger.runTimeLogger.debug("Added " + validator.getValidationName() + " to Validations.");
            validatorList.add(validator);
        }
        // Return all validations
        return validatorList;
    }

    protected Validator parseSingleValidator(final JsonNode validation)
    {
        final String validationName = validation.fieldNames().next();
        final JsonNode validationContent = validation.get(validationName);

        // Verify the validation is an ObjectNode
        if (!(validationContent instanceof ObjectNode))
        {
            throw new IllegalArgumentException("Expected ObjectNode after the validation name, " + validationName + ", but was "
                                               + validationContent.getClass().getSimpleName());
        }

        // Get the fieldNames, that is the extraction method and validation method name
        final Iterator<String> contentKeys = validationContent.fieldNames();
        AbstractExtractor extractor = null;
        AbstractValidator validationMethod = null;
        // Iterate over all content keys
        while (contentKeys.hasNext())
        {
            // Get the next contentKey
            final String contentKey = contentKeys.next();
            // If it is an extraction, parse the extraction
            if (Constants.isPermittedExtraction(contentKey))
            {
                // Verify, that no extractor was parsed already
                if (extractor != null)
                {
                    throw new IllegalArgumentException("Cannot parse two extraction methods!");
                }
                // Parse the extractor
                XltLogger.runTimeLogger.debug("Extraction Mode is " + extractor);
                extractor = new ExtractorParser(contentKey).parse(validationContent);
            }
            // If it is a validation method, parse the validation method
            else if (Constants.isPermittedValidationMethod(contentKey))
            {
                // Verify, that an extractor was parsed already
                if (extractor == null)
                {
                    throw new IllegalArgumentException("Cannot parse validation before the extraction!");
                }
                // Verify, that no validation was parsed already
                if (validationMethod != null)
                {
                    throw new IllegalArgumentException("Cannot parse two validation methods!");
                }
                // Parse the validation method
                XltLogger.runTimeLogger.debug("Validation Method is " + extractor);
                validationMethod = new ValidatorParser(contentKey).parse(validationContent);
            }
            // If it is not Group OR Group and extractor is null, throw an error
            else if (!Constants.GROUP.equals(contentKey) || extractor == null)
            {
                throw new IllegalArgumentException("Unknown Validation Item: " + contentKey);
            }
        }

        return new Validator(validationName, extractor, validationMethod);
    }

}
