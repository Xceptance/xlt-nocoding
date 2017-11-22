package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.response.validators;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validator.AbstractValidationMode;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validator.CountValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validator.MatchesValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validator.TextValidator;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.ParserUtils;

public class ValidationModeParser
{
    final String identifier;

    public ValidationModeParser(final String identifier)
    {
        this.identifier = identifier;
    }

    public AbstractValidationMode parse(final JsonNode node)
    {
        AbstractValidationMode mode = null;

        final String validationExpression = ParserUtils.readValue(node, identifier);
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

        return mode;
    }

}
