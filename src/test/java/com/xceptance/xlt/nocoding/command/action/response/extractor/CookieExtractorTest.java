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
 * Tests for {@link CookieExtractor}
 *
 * @author ckeiner
 */
public class CookieExtractorTest extends ExtractorTest
{

    public CookieExtractorTest(final Context<?> context)
    {
        super(context);
    }

    /**
     * Verifies the {@link CookieExtractor} extracts the cookie from {@link MockObjects}
     *
     * @throws Throwable
     */
    @Test
    public void testCookieExtractor() throws Throwable
    {
        final AbstractExtractor extractor = new CookieExtractor(mockObjects.cookieName1);
        extractor.execute(context);
        final List<String> result = extractor.getResult();
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(mockObjects.cookieValue1, result.get(0));
    }

    /**
     * Verifies the {@link CookieExtractor} can extract a cookie when its a variable
     *
     * @throws Throwable
     */
    @Test
    public void testCookieExtractorWithVariables() throws Throwable
    {
        final String variableName = "variable_1";
        final String storeValue = mockObjects.cookieName1;
        context.getVariables().store(variableName, storeValue);
        final AbstractExtractor extractor = new CookieExtractor("${" + variableName + "}");
        extractor.execute(context);
        final List<String> result = extractor.getResult();
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(mockObjects.cookieValue1, result.get(0));
    }

    /**
     * Verifies the {@link CookieExtractor} finds nothing if the name of the cookie is not found
     *
     * @throws Throwable
     */
    @Test
    public void testCookieExtractorNoResult() throws Throwable
    {
        final AbstractExtractor extractor = new CookieExtractor(mockObjects.nonExistentCookie);
        extractor.execute(context);
        final List<String> result = extractor.getResult();
        Assert.assertEquals(0, result.size());
    }

    /**
     * Verifies the {@link CookieExtractor} can extract a part of a cookie value using an additional regex and matching
     * group.
     *
     * @throws Throwable
     */
    @Test
    public void testCookieExtractorWithValueRegexAndGroup() throws Throwable
    {
        final AbstractExtractor extractor = new CookieExtractor(mockObjects.cookieName1, "\\d(\\d+)", "1");
        extractor.execute(context);
        final List<String> result = extractor.getResult();
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(mockObjects.cookieValue1.substring(1), result.get(0));
    }

    /**
     * Verifies the {@link CookieExtractor} can extract a part of a cookie value when regex and matching group are
     * variables.
     *
     * @throws Throwable
     */
    @Test
    public void testCookieExtractorWithVariablesForRegexAndGroup() throws Throwable
    {
        final String regexVariableName = "variable_1";
        final String groupVariableName = "variable_2";
        context.getVariables().store(regexVariableName, "\\d(\\d+)");
        context.getVariables().store(groupVariableName, "1");
        final AbstractExtractor extractor = new CookieExtractor(mockObjects.cookieName1, "${" + regexVariableName + "}",
                                                                "${" + groupVariableName + "}");
        extractor.execute(context);
        final List<String> result = extractor.getResult();
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(mockObjects.cookieValue1.substring(1), result.get(0));
    }
}
