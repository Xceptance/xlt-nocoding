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

import org.junit.Test;

import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Tests {@link TextValidator}
 *
 * @author ckeiner
 */
public class TextValidatorTest extends ValidationMethodTest
{

    public TextValidatorTest(final Context<?> context)
    {
        super(context);
    }

    /**
     * Verifies {@link TextValidator} validates the content of the first result
     *
     * @throws Exception
     */
    @Test
    public void testTextValidator() throws Exception
    {
        final List<String> result = new ArrayList<>();
        result.add("test");
        final AbstractValidator method = new TextValidator("test");
        method.setExpressionToValidate(result);
        method.execute(context);
    }

    /**
     * Verifies {@link TextValidator} validates the content of the first result. The expected content is hidden behind a
     * variable
     *
     * @throws Exception
     */
    @Test
    public void testTextValidatorWithVariables() throws Exception
    {
        final String variableName = "text";
        final String value = "test";
        context.getVariables().store(variableName, value);
        final List<String> result = new ArrayList<>();
        result.add(value);
        final AbstractValidator method = new TextValidator("${" + variableName + "}");
        method.setExpressionToValidate(result);
        method.execute(context);
    }

    /**
     * Verifies {@link TextValidator} throws an {@link AssertionError} if the content and the expected content are not
     * equal
     *
     * @throws Exception
     */
    @Test(expected = AssertionError.class)
    public void testTextValidatorWrongText() throws Exception
    {
        final List<String> result = new ArrayList<>();
        result.add("test");
        final AbstractValidator method = new TextValidator("wrong");
        method.setExpressionToValidate(result);
        method.execute(context);
    }

    /**
     * Verifies {@link TextValidator} throws an {@link AssertionError} when the result list is null
     *
     * @throws Exception
     */
    @Test(expected = AssertionError.class)
    public void testTextValidatorNull() throws Exception
    {
        final AbstractValidator method = new TextValidator("test");
        method.setExpressionToValidate(null);
        method.execute(context);
    }

    /**
     * Verifies {@link TextValidator} throws an {@link AssertionError} when the result list is empty
     *
     * @throws Exception
     */
    @Test(expected = AssertionError.class)
    public void testTextValidatorEmptyList() throws Exception
    {
        final AbstractValidator method = new TextValidator("test");
        method.setExpressionToValidate(new ArrayList<String>());
        method.execute(context);
    }

}
