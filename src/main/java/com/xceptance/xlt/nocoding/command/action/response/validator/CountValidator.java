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

import org.junit.Assert;

import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Validates that {@link #getExpressionToValidate()} has the size of the Integer value of {@link #count}.
 *
 * @author ckeiner
 */
public class CountValidator extends AbstractValidator
{
    /**
     * The expected size of {@link #getExpressionToValidate()} as String
     */
    private String count;

    /**
     * Creates an instance of {@link CountValidator}, that sets {@link #count}.
     *
     * @param count
     *            The expected size of {@link #getExpressionToValidate()} as String
     */
    public CountValidator(final String count)
    {
        this.count = count;
    }

    /**
     * Resolves values, parses {@link #count} to an Integer and finally asserts that {@link #getExpressionToValidate()}
     * has a size of the Integer value of <code>value</code>.
     */
    @Override
    public void execute(final Context<?> context)
    {
        // Resolve values
        resolveValues(context);
        // Transform count to an Integer
        final Integer count = Integer.parseInt(this.count);
        // Verify the result list/expressionToValidate is not null
        if (getExpressionToValidate() == null)
        {
            throw new IllegalStateException("Result list is null");
        }
        // Assert that the amount of results is the same as count
        Assert.assertTrue("Expected " + this.count + " matches but found " + getExpressionToValidate().size() + " matches",
                          count.equals(getExpressionToValidate().size()));
    }

    /**
     * Resolves {@link #count}.
     */
    @Override
    protected void resolveValues(final Context<?> context)
    {
        count = context.resolveString(count);
    }

}
