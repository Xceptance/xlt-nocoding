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
package com.xceptance.xlt.nocoding.command.action.response.extractor;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.util.MockObjects;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Tests for {@link RegexpExtractor}
 *
 * @author ckeiner
 */
public class RegexpExtractorTest extends ExtractorTest
{

    public RegexpExtractorTest(final Context<?> context)
    {
        super(context);
    }

    /**
     * Verifies {@link RegexpExtractor} extracts the correct string with the pattern from
     * {@link MockObjects#regexString}
     */
    @Test
    public void testRegExpSelector()
    {
        final AbstractExtractor extractor = new RegexpExtractor(mockObjects.regexString);
        extractor.execute(context);
        final List<String> result = extractor.getResult();
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(mockObjects.regexStringExpected, result.get(0));
    }

    /**
     * Verifies {@link RegexpExtractor} extracts the correct string with the pattern from
     * {@link MockObjects#regexString} if the pattern is a variable
     */
    @Test
    public void testRegExpSelectorWithVariables()
    {
        final String variableName = "variable_1";
        final String storeValue = mockObjects.regexString;
        context.getVariables().store(variableName, storeValue);
        final AbstractExtractor extractor = new RegexpExtractor("${" + variableName + "}");
        extractor.execute(context);
        final List<String> result = extractor.getResult();
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(mockObjects.regexStringExpected, result.get(0));
    }

    /**
     * Verifies {@link RegexpExtractor} finds nothing if the pattern results in nothing
     */
    @Test
    public void testRegExpSelectorNoResult()
    {
        final AbstractExtractor extractor = new RegexpExtractor(mockObjects.regexStringNoResult);
        extractor.execute(context);
        final List<String> result = extractor.getResult();
        Assert.assertEquals(0, result.size());
    }

    /**
     * Verifies {@link RegexpExtractor} with a specified group returns the capturing group
     */
    @Test
    public void testRegExpSelectorOneGroup()
    {
        final AbstractExtractor extractor = new RegexpExtractor("(<div (.*)>)", "1");
        extractor.execute(context);
        final List<String> result = extractor.getResult();
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("<div class=\"contentWithClass>", result.get(0));
    }

}
