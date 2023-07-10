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
package com.xceptance.xlt.nocoding.command.action.response;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.command.AbstractContextTest;
import com.xceptance.xlt.nocoding.command.action.response.extractor.AbstractExtractor;
import com.xceptance.xlt.nocoding.command.action.response.extractor.CookieExtractor;
import com.xceptance.xlt.nocoding.command.action.response.extractor.HeaderExtractor;
import com.xceptance.xlt.nocoding.command.action.response.extractor.RegexpExtractor;
import com.xceptance.xlt.nocoding.command.action.response.validator.AbstractValidator;
import com.xceptance.xlt.nocoding.command.action.response.validator.CountValidator;
import com.xceptance.xlt.nocoding.command.action.response.validator.ExistsValidator;
import com.xceptance.xlt.nocoding.command.action.response.validator.MatchesValidator;
import com.xceptance.xlt.nocoding.command.action.response.validator.TextValidator;
import com.xceptance.xlt.nocoding.command.store.Store;
import com.xceptance.xlt.nocoding.util.MockObjects;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Tests {@link Validator}
 *
 * @author ckeiner
 */
public class ValidatorTest extends AbstractContextTest
{
    public ValidatorTest(final Context<?> context)
    {
        super(context);
        mockObjects = new MockObjects();
        mockObjects.loadResponse();
        context.setWebResponse(mockObjects.getResponse());
    }

    public MockObjects mockObjects;

    /**
     * Validates {@link CookieExtractor} with multiple {@link AbstractValidator}.
     *
     * @throws Exception
     */
    @Test
    public void testValidationWithCookie() throws Throwable
    {
        // Build Validator with ExistsModule
        final String validationName = "Cookie Validation";
        AbstractExtractor extractor = new CookieExtractor(mockObjects.cookieName1);
        AbstractValidator method = new ExistsValidator();
        Validator validator = new Validator(validationName, extractor, method);

        executeInARequest(validator);

        // Build Validator with TextModule
        extractor = new CookieExtractor(mockObjects.cookieName1);
        method = new TextValidator(mockObjects.cookieValue1);
        validator = new Validator(validationName, extractor, method);
        executeInARequest(validator);

        // Build Validator with MatchesModule
        extractor = new CookieExtractor(mockObjects.cookieName1);
        method = new MatchesValidator(mockObjects.cookieValue1);
        validator = new Validator(validationName, extractor, method);
        executeInARequest(validator);

        // Build Validator with MatchesModule
        extractor = new CookieExtractor(mockObjects.cookieName1);
        method = new CountValidator("1");
        validator = new Validator(validationName, extractor, method);
        executeInARequest(validator);

    }

    /**
     * Validates {@link HeaderExtractor} with multiple {@link AbstractValidator}.
     *
     * @throws Exception
     */
    @Test
    public void testValidationWithHeader() throws Throwable
    {
        // Build Validator with ExistsModule
        final String validationName = "Header Validation";
        AbstractExtractor extractor = new HeaderExtractor("Set-Cookie");
        AbstractValidator method = new ExistsValidator();
        Validator validator = new Validator(validationName, extractor, method);

        executeInARequest(validator);

        // Build Validator with TextModule
        extractor = new HeaderExtractor("Set-Cookie");
        method = new TextValidator(mockObjects.cookieName1 + "=" + mockObjects.cookieValue1);
        validator = new Validator(validationName, extractor, method);
        executeInARequest(validator);

        // Build Validator with MatchesModule
        extractor = new HeaderExtractor("Set-Cookie");
        method = new MatchesValidator(mockObjects.cookieName1 + "=" + mockObjects.cookieValue1);
        validator = new Validator(validationName, extractor, method);
        executeInARequest(validator);

        // Build Validator with MatchesModule
        extractor = new HeaderExtractor("Set-Cookie");
        method = new CountValidator("3");
        validator = new Validator(validationName, extractor, method);
        executeInARequest(validator);

    }

    /**
     * Validates {@link RegexpExtractor} with multiple {@link AbstractValidator}.
     *
     * @throws Exception
     */
    @Test
    public void testValidationWithRegexp() throws Throwable
    {
        // Build Validator with ExistsModule
        final String validationName = "Regexp Validation";
        AbstractExtractor extractor = new RegexpExtractor(mockObjects.regexString);
        AbstractValidator method = new ExistsValidator();
        Validator validator = new Validator(validationName, extractor, method);

        executeInARequest(validator);

        // Build Validator with TextModule
        extractor = new RegexpExtractor(mockObjects.regexString);
        method = new TextValidator(mockObjects.regexStringExpected);
        validator = new Validator(validationName, extractor, method);
        executeInARequest(validator);

        // Build Validator with MatchesModule
        extractor = new RegexpExtractor(mockObjects.regexString);
        method = new MatchesValidator(mockObjects.regexStringExpected);
        validator = new Validator(validationName, extractor, method);
        executeInARequest(validator);

        // Build Validator with MatchesModule
        extractor = new RegexpExtractor(mockObjects.regexString);
        method = new CountValidator("1");
        validator = new Validator(validationName, extractor, method);
        executeInARequest(validator);
    }

    /**
     * Verifies the {@link Validator#getValidationName()} does not get resolved if it is a variable definition
     *
     * @throws Throwable
     */
    @Test
    public void testNoResolvingOfValidationName() throws Throwable
    {
        final String variableName = "validationName";
        final String validationName = "Regexp Validation";
        final Store store = new Store("${" + variableName + "}", validationName);
        store.execute(context);
        context.getVariables().store(variableName, validationName);
        // Build Validator with ExistsModule
        final AbstractExtractor extractor = new RegexpExtractor(mockObjects.regexString);
        final AbstractValidator method = new ExistsValidator();
        final Validator validator = new Validator("${" + variableName + "}", extractor, method);
        executeInARequest(validator);
        Assert.assertNotEquals(validationName, validator.getValidationName());
    }

    /**
     * Verifies {@link RegexpExtractor} can extract a group
     *
     * @throws Throwable
     */
    @Test
    public void testGroupRegexpValidation() throws Throwable
    {
        final String validationName = "Regexp Validation";
        // Build Validator with ExistsModule
        final AbstractExtractor extractor = new RegexpExtractor(mockObjects.regexString, "0");
        final AbstractValidator method = new ExistsValidator();
        final Validator validator = new Validator(validationName, extractor, method);
        executeInARequest(validator);
        Assert.assertTrue(extractor instanceof RegexpExtractor);
        Assert.assertEquals("0", ((RegexpExtractor) extractor).getGroup());
    }

    /**
     * Verifies, that when {@link Validator#getValidationName()} is null or empty, the default name is
     * <code>"Validate Action-" +</code> {@link Context#getActionIndex()}
     *
     * @throws Throwable
     */
    @Test
    public void testValidationNameEmptyGetsDefault() throws Throwable
    {
        final String validationName = "Validate Action-0";
        // Build Validator with ExistsModule
        final AbstractExtractor extractor = new RegexpExtractor(mockObjects.regexString, "0");
        final AbstractValidator method = new ExistsValidator();
        final Validator validator = new Validator("", extractor, method);
        executeInARequest(validator);
        Assert.assertEquals(validationName, validator.getValidationName());
    }

    /*
     * Helper Methods
     */

    /**
     * Creates a {@link Response} object out of the {@link Validator}. Then calls {@link Response#execute(Context)}
     *
     * @param validator
     * @throws Throwable
     */
    public void executeInARequest(final Validator validator) throws Throwable
    {
        // Build Response
        final List<AbstractResponseSubItem> responseItems = new ArrayList<>();
        responseItems.add(validator);
        final Response response = new Response(responseItems);
        // Execute Response
        response.execute(context);
    }

}
