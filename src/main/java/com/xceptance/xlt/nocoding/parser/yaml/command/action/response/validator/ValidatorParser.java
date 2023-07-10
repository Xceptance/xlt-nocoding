/*
 * Copyright (c) 2013-2023 Xceptance Software Technologies GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xceptance.xlt.nocoding.parser.yaml.command.action.response.validator;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.parser.ParserException;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.command.action.response.validator.AbstractValidator;
import com.xceptance.xlt.nocoding.command.action.response.validator.CountValidator;
import com.xceptance.xlt.nocoding.command.action.response.validator.MatchesValidator;
import com.xceptance.xlt.nocoding.command.action.response.validator.TextValidator;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParserUtils;
import com.xceptance.xlt.nocoding.util.Constants;

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
     * @param context
     *            The {@link Mark} of the surrounding {@link Node}/context.
     * @param node
     *            The <code>Node</code> with the validation method
     * @return An <code>AbstractValidationMethod</code> with the parsed data
     */
    public AbstractValidator parse(final Mark context, final Node node)
    {
        AbstractValidator method = null;
        // Get the associated value
        final String validationExpression = YamlParserUtils.transformScalarNodeToString(context, node);
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
                // We didn't find something fitting, so throw an Exception
                throw new ParserException("Node", context, " contains a permitted but unknown validation item", node.getStartMark());
        }

        // Return the method
        return method;
    }

}
