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

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Tests {@link CountValidator}
 *
 * @author ckeiner
 */
public class CountValidatorTest extends ValidationMethodTest
{

    public CountValidatorTest(final Context<?> context)
    {
        super(context);
    }

    /**
     * Verifies the {@link CountValidator} validates the correct amount of results
     *
     * @throws Exception
     */
    @Test
    public void testCountValidator() throws Exception
    {
        final List<String> result = new ArrayList<>();
        result.add("test");
        AbstractValidator method = new CountValidator("1");
        method.setExpressionToValidate(result);
        method.execute(context);

        result.add("test_2");
        method = new CountValidator("2");
        method.setExpressionToValidate(result);
        method.execute(context);
    }

    /**
     * Verifies the {@link CountValidator} validates the correct amount of results. The expected amount of results is
     * hidden behind a variable
     *
     * @throws Exception
     */
    @Test
    public void testCountValidatorWithVariables() throws Exception
    {
        final String variableName = "count";
        final String value = "1";
        context.getVariables().store(variableName, value);
        final List<String> result = new ArrayList<>();
        result.add("test");
        final AbstractValidator method = new CountValidator("${" + variableName + "}");
        method.setExpressionToValidate(result);
        method.execute(context);
    }

    /**
     * Verifies {@link CountValidator} throws an {@link AssertionError} if the wrong amount of items is specified
     *
     * @throws Exception
     */
    @Test
    public void testCountValidatorWrongCount() throws Exception
    {
        final String expected = "0";
        final List<String> result = new ArrayList<>();
        result.add("test");
        final AbstractValidator method = new CountValidator(expected);
        method.setExpressionToValidate(result);
        try
        {
            method.execute(context);
            Assert.assertTrue(false);
        }
        catch (final AssertionError e)
        {
            Assert.assertEquals("Expected " + expected + " matches but found " + result.size() + " matches", e.getMessage());
        }
    }

    /**
     * Verifies {@link CountValidator} throws an error if it cannot convert to a number
     *
     * @throws Exception
     */
    @Test(expected = NumberFormatException.class)
    public void testCountValidatorNotConvertibleToInt() throws Exception
    {
        final List<String> result = new ArrayList<>();
        result.add("test");
        final AbstractValidator method = new CountValidator("wrong");
        method.setExpressionToValidate(result);
        method.execute(context);
    }

    /**
     * Verifies {@link CountValidator} throws an error if there is no expression to validate
     *
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void testCountValidatorNull() throws Exception
    {
        final AbstractValidator method = new CountValidator("1");
        method.setExpressionToValidate(null);
        method.execute(context);
    }

    /**
     * Verifies {@link CountValidator} can validate that there is no item in the result list
     *
     * @throws Exception
     */
    @Test
    public void testCountValidatorEmptyList() throws Exception
    {
        final AbstractValidator method = new CountValidator("0");
        method.setExpressionToValidate(new ArrayList<String>());
        method.execute(context);
    }

}
