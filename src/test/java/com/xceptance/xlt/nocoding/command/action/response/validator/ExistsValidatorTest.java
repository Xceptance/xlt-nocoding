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
 * Tests {@link ExistsValidator}
 *
 * @author ckeiner
 */
public class ExistsValidatorTest extends ValidationMethodTest
{

    public ExistsValidatorTest(final Context<?> context)
    {
        super(context);
    }

    /**
     * Verifies {@link ExistsValidator} validates that the result list has an item in it
     *
     * @throws Exception
     */
    @Test
    public void testExistsValidator() throws Exception
    {
        final List<String> result = new ArrayList<>();
        result.add("test");
        final AbstractValidator method = new ExistsValidator();
        method.setExpressionToValidate(result);
        method.execute(context);
    }

    /**
     * Verifies {@link ExistsValidator} throws an {@link AssertionError} if the result list is null
     *
     * @throws Exception
     */
    @Test(expected = AssertionError.class)
    public void testExistsValidatorNull() throws Exception
    {
        final AbstractValidator method = new ExistsValidator();
        method.setExpressionToValidate(null);
        method.execute(context);
    }

    /**
     * Verifies {@link ExistsValidator} throws an {@link AssertionError} if the result list is empty
     *
     * @throws Exception
     */
    @Test(expected = AssertionError.class)
    public void testExistsValidatorEmptyList() throws Exception
    {
        final AbstractValidator method = new ExistsValidator();
        method.setExpressionToValidate(new ArrayList<String>());
        method.execute(context);
    }

}
