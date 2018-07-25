package com.xceptance.xlt.nocoding.parser.yaml.scriptItems.action.response.validator;

import org.apache.commons.lang3.NotImplementedException;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validator.AbstractValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validator.CountValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validator.MatchesValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validator.TextValidator;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.ParserUtils;

/**
 * Class for parsing the validation method. Takes an identifier (which is an element of {@link JsonNode#fieldNames()})
 * of a {@link JsonNode} with the validation method and parses it to an {@link AbstractValidator}.
 * 
 * @author ckeiner
 */
public class ValidatorParser
{
    /**
     * The identifier of the validation method, must be in {@link Constants#PERMITTEDVALIDATIONMETHOD}.
     */
    final String identifier;

    public ValidatorParser(final String identifier)
    {
        this.identifier = identifier;
    }

    /**
     * Parses the validation method in the {@link JsonNode} to an {@link AbstractValidator}.
     * 
     * @param node
     *            The <code>JsonNode</code> with the validation method
     * @return An <code>AbstractValidationMethod</code> with the parsed data
     */
    public AbstractValidator parse(final JsonNode node)
    {
        AbstractValidator method = null;
        // Get the associated value
        final String validationExpression = ParserUtils.readValue(node, identifier);
        // Build a validation method depending on the name of the selector
        switch (identifier)
        {
            case Constants.MATCHES:
                method = new MatchesValidator(validationExpression);
                break;

            case Constants.TEXT:
                method = new TextValidator(validationExpression);
                break;

            case Constants.COUNT:
                method = new CountValidator(validationExpression);
                break;

            default:
                throw new NotImplementedException("Permitted Validation Method but no parsing specified: " + identifier);
        }

        // Return the method
        return method;
    }

}
