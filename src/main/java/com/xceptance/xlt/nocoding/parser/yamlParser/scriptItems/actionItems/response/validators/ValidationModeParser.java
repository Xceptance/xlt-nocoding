package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.response.validators;

import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.AbstractValidationMode;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.CountValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.ExistsValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.MatchesValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.TextValidator;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.ParserUtils;

public class ValidationModeParser
{
    final Iterator<String> iterator;

    public ValidationModeParser(final Iterator<String> iterator)
    {
        this.iterator = iterator;
    }

    public AbstractValidationMode parse(final JsonNode node)
    {
        AbstractValidationMode mode = null;

        if (iterator.hasNext())
        {
            final String nextExpression = iterator.next();
            final String validationExpression = ParserUtils.readValue(node, nextExpression);
            if (nextExpression.equals(Constants.MATCHES))
            {
                String group = null;
                if (iterator.hasNext())
                {
                    group = ParserUtils.readValue(node, iterator.next());
                }
                mode = new MatchesValidator(validationExpression, group);
            }
            else if (nextExpression.equals(Constants.TEXT))
            {
                String group = null;
                if (iterator.hasNext())
                {
                    group = ParserUtils.readValue(node, iterator.next());
                }
                mode = new TextValidator(validationExpression, group);
            }
            else if (nextExpression.equals(Constants.COUNT))
            {
                if (iterator.hasNext())
                {
                    throw new IllegalArgumentException("Unexpected item " + iterator.next());
                }
                mode = new CountValidator(validationExpression);
            }
        }

        else
        {
            mode = new ExistsValidator();
        }

        return mode;
    }

}
