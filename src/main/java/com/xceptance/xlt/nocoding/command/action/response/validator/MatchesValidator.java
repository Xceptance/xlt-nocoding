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
package com.xceptance.xlt.nocoding.command.action.response.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;

import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Validates that the first result in {@link #getExpressionToValidate()} matches the {@link Pattern} provided by
 * {@link #validationExpression}.
 *
 * @author ckeiner
 */
public class MatchesValidator extends AbstractValidator
{

    /**
     * The {@link Pattern} to match {@link #getExpressionToValidate()} against as String
     */
    private String validationExpression;

    /**
     * Creates an instance of {@link MatchesValidator} that sets {@link #validationExpression}
     *
     * @param validationExpression
     *            The {@link Pattern} to match {@link #getExpressionToValidate()} against as String
     */
    public MatchesValidator(final String validationExpression)
    {
        this.validationExpression = validationExpression;
    }

    /**
     * Resolves values, verifies {@link #getExpressionToValidate()} is neither null nor empty, and matches the first
     * result of {@link #getExpressionToValidate()} against the {@link Pattern} provided by
     * {@link #validationExpression}. Finally, it verifies there is at least one result.
     */
    @Override
    public void execute(final Context<?> context)
    {
        // Resolve values
        resolveValues(context);
        // Assert we have a result list and its has elements in it
        Assert.assertNotNull("Result list is null", getExpressionToValidate());
        Assert.assertFalse("Result list is empty", getExpressionToValidate().isEmpty());
        // Get the expression we want
        final String expressionToValidate = getExpressionToValidate().get(0);
        // Assert that the expression is not null
        Assert.assertNotNull(expressionToValidate);
        // Build a matcher from the fields
        final Matcher matcher = Pattern.compile(validationExpression).matcher(expressionToValidate);
        // Verify a match was found
        Assert.assertTrue(validationExpression + " did not match " + expressionToValidate, matcher.find());
    }

    /**
     * Resolves {@link #validationExpression}
     */
    @Override
    protected void resolveValues(final Context<?> context)
    {
        validationExpression = context.resolveString(validationExpression);
    }

    public String getValidationExpression()
    {
        return validationExpression;
    }

}
