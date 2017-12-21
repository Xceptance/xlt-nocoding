package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.response.validators;

import org.apache.commons.lang3.NotImplementedException;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod.AbstractValidationMethod;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod.CountValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod.MatchesValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod.TextValidator;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.ParserUtils;

/**
 * Takes an identifier (which is an element of {@link JsonNode#fieldNames()}) of a {@link JsonNode} with the validation
 * method in it and parses it to an {@link AbstractValidationMethod}.
 * 
 * @author ckeiner
 */
public class ValidationMethodParser
{
    /**
     * The identifier of the validation method, must be in {@link Constants#PERMITTEDVALIDATIONMETHOD}.
     */
    final String identifier;

    public ValidationMethodParser(final String identifier)
    {
        this.identifier = identifier;
    }

    /**
     * Parses the validation method in the {@link JsonNode} to an {@link AbstractValidationMethod}
     * 
     * @param node
     *            The node of the validation method
     * @return The specified {@link AbstractValidationMethod}
     */
    public AbstractValidationMethod parse(final JsonNode node)
    {
        AbstractValidationMethod method = null;
        // Get the associated value
        final String validationExpression = ParserUtils.readValue(node, identifier);
        // Build a validation method depending on the name of the selector
        if (identifier.equals(Constants.MATCHES))
        {
            method = new MatchesValidator(validationExpression);
        }
        else if (identifier.equals(Constants.TEXT))
        {
            method = new TextValidator(validationExpression);
        }
        else if (identifier.equals(Constants.COUNT))
        {
            method = new CountValidator(validationExpression);
        }
        else
        {
            throw new NotImplementedException("Permitted Validation Method but no parsing specified: " + identifier);
        }
        // Return it
        return method;
    }

}