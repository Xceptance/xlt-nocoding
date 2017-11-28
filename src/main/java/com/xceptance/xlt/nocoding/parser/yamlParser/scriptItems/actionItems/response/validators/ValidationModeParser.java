package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.response.validators;

import org.apache.commons.lang3.NotImplementedException;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMode.AbstractValidationMode;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMode.CountValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMode.MatchesValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMode.TextValidator;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.ParserUtils;

/**
 * Takes an identifier (which is an element of {@link JsonNode#fieldNames()}) of a {@link JsonNode} with the validation
 * mode in it and parses it to an {@link AbstractValidationMode}.
 * 
 * @author ckeiner
 */
public class ValidationModeParser
{
    /**
     * The identifier of the validation mode, should be an item in {@link Constants#PERMITTEDVALIDATIONMODE}.
     */
    final String identifier;

    public ValidationModeParser(final String identifier)
    {
        this.identifier = identifier;
    }

    /**
     * Parses the selection item in node to an {@link AbstractValidationMode}
     * 
     * @param node
     *            The node of the validation item
     * @return The specified {@link AbstractValidationMode}
     */
    public AbstractValidationMode parse(final JsonNode node)
    {
        AbstractValidationMode mode = null;
        // Get the associated value
        final String validationExpression = ParserUtils.readValue(node, identifier);
        // Build a validation mode depending on the name of the selector
        if (identifier.equals(Constants.MATCHES))
        {
            mode = new MatchesValidator(validationExpression);
        }
        else if (identifier.equals(Constants.TEXT))
        {
            mode = new TextValidator(validationExpression);
        }
        else if (identifier.equals(Constants.COUNT))
        {
            mode = new CountValidator(validationExpression);
        }
        else
        {
            throw new NotImplementedException("Permitted Validation Mode but no parsing specified: " + identifier);
        }
        // Return it
        return mode;
    }

}
