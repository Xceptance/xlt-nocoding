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
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod.AbstractValidationMethod;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod.CountValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod.ExistsValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod.MatchesValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod.TextValidator;
import com.xceptance.xlt.nocoding.util.Context;
import com.xceptance.xlt.nocoding.util.MockObjects;

/**
 * Tests {@link Validator}
 * 
 * @author ckeiner
 */
public class ValidatorTest
{
    public Context context;

    public MockObjects mockObjects;

    /**
     * Sets {@link WebResponse} via {@link MockObjects#loadResponse()} in {@link Context}
     */
    @Before
    public void init()
    {
        context = new Context(XltProperties.getInstance());
        mockObjects = new MockObjects();
        mockObjects.loadResponse();
        context.setWebResponse(mockObjects.getResponse());
    }

    /**
     * Validates {@link CookieExtractor} with multiple {@link AbstractValidationMethod}.
     * 
     * @throws Exception
     */
    @Test
    public void testValidationWithCookie() throws Throwable
    {
        // Build Validator with ExistsModule
        final String validationName = "Cookie Validation";
        AbstractExtractor extractor = new CookieExtractor(mockObjects.cookieName1);
        AbstractValidationMethod method = new ExistsValidator();
        Validator validator = new Validator(validationName, extractor, method);

        executeRequest(validator);

        // Build Validator with TextModule
        extractor = new CookieExtractor(mockObjects.cookieName1);
        method = new TextValidator(mockObjects.cookieValue1);
        validator = new Validator(validationName, extractor, method);
        executeRequest(validator);

        // Build Validator with MatchesModule
        extractor = new CookieExtractor(mockObjects.cookieName1);
        method = new MatchesValidator(mockObjects.cookieValue1);
        validator = new Validator(validationName, extractor, method);
        executeRequest(validator);

        // Build Validator with MatchesModule
        extractor = new CookieExtractor(mockObjects.cookieName1);
        method = new CountValidator("1");
        validator = new Validator(validationName, extractor, method);
        executeRequest(validator);

    }

    /**
     * Validates {@link HeaderExtractor} with multiple {@link AbstractValidationMethod}.
     * 
     * @throws Exception
     */
    @Test
    public void testValidationWithHeader() throws Throwable
    {
        // Build Validator with ExistsModule
        final String validationName = "Header Validation";
        AbstractExtractor extractor = new HeaderExtractor("Set-Cookie");
        AbstractValidationMethod method = new ExistsValidator();
        Validator validator = new Validator(validationName, extractor, method);

        executeRequest(validator);

        // Build Validator with TextModule
        extractor = new HeaderExtractor("Set-Cookie");
        method = new TextValidator(mockObjects.cookieName1 + "=" + mockObjects.cookieValue1);
        validator = new Validator(validationName, extractor, method);
        executeRequest(validator);

        // Build Validator with MatchesModule
        extractor = new HeaderExtractor("Set-Cookie");
        method = new MatchesValidator(mockObjects.cookieName1 + "=" + mockObjects.cookieValue1);
        validator = new Validator(validationName, extractor, method);
        executeRequest(validator);

        // Build Validator with MatchesModule
        extractor = new HeaderExtractor("Set-Cookie");
        method = new CountValidator("3");
        validator = new Validator(validationName, extractor, method);
        executeRequest(validator);

    }

    /**
     * Validates {@link RegexpExtractor} with multiple {@link AbstractValidationMethod}.
     * 
     * @throws Exception
     */
    @Test
    public void testValidationWithRegexp() throws Throwable
    {
        // Build Validator with ExistsModule
        final String validationName = "Regexp Validation";
        AbstractExtractor extractor = new RegexpExtractor(mockObjects.regexString);
        AbstractValidationMethod method = new ExistsValidator();
        Validator validator = new Validator(validationName, extractor, method);

        executeRequest(validator);

        // Build Validator with TextModule
        extractor = new RegexpExtractor(mockObjects.regexString);
        method = new TextValidator(mockObjects.regexStringExpected);
        validator = new Validator(validationName, extractor, method);
        executeRequest(validator);

        // Build Validator with MatchesModule
        extractor = new RegexpExtractor(mockObjects.regexString);
        method = new MatchesValidator(mockObjects.regexStringExpected);
        validator = new Validator(validationName, extractor, method);
        executeRequest(validator);

        // Build Validator with MatchesModule
        extractor = new RegexpExtractor(mockObjects.regexString);
        method = new CountValidator("1");
        validator = new Validator(validationName, extractor, method);
        executeRequest(validator);
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
        final AbstractValidationMethod method = new ExistsValidator();
        final Validator validator = new Validator("${" + variableName + "}", extractor, method);
        executeRequest(validator);
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
        final AbstractValidationMethod method = new ExistsValidator();
        final Validator validator = new Validator(validationName, extractor, method);
        executeRequest(validator);
        Assert.assertTrue(extractor instanceof RegexpExtractor);
        Assert.assertEquals("0", ((RegexpExtractor) extractor).getGroup());
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
    public void executeRequest(final Validator validator) throws Throwable
    {
        // Build Response
        final List<AbstractResponseItem> responseItems = new ArrayList<AbstractResponseItem>();
        responseItems.add(validator);
        final Response response = new Response(responseItems);
        // Execute Response
        response.execute(context);
    }

}
