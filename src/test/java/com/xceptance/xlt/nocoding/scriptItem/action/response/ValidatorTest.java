package com.xceptance.xlt.nocoding.scriptItem.action.response;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.scriptItem.StoreItem;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.AbstractExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.CookieExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.HeaderExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.RegexpExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validator.AbstractValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validator.CountValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validator.ExistsValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validator.MatchesValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validator.TextValidator;
import com.xceptance.xlt.nocoding.util.MockObjects;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.context.LightWeightContext;

/**
 * Tests {@link Validator}
 * 
 * @author ckeiner
 */
public class ValidatorTest
{
    public Context<?> context;

    public MockObjects mockObjects;

    /**
     * Sets {@link WebResponse} via {@link MockObjects#loadResponse()} in {@link Context}
     */
    @Before
    public void init()
    {
        context = new LightWeightContext(XltProperties.getInstance());
        mockObjects = new MockObjects();
        mockObjects.loadResponse();
        context.setWebResponse(mockObjects.getResponse());
    }

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
        final StoreItem store = new StoreItem("${" + variableName + "}", validationName);
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
        final List<AbstractResponseItem> responseItems = new ArrayList<AbstractResponseItem>();
        responseItems.add(validator);
        final Response response = new Response(responseItems);
        // Execute Response
        response.execute(context);
    }

}
