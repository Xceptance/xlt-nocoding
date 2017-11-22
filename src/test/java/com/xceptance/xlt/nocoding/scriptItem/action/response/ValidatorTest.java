package com.xceptance.xlt.nocoding.scriptItem.action.response;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.scriptItem.action.response.selector.AbstractSelector;
import com.xceptance.xlt.nocoding.scriptItem.action.response.selector.CookieSelector;
import com.xceptance.xlt.nocoding.scriptItem.action.response.selector.HeaderSelector;
import com.xceptance.xlt.nocoding.scriptItem.action.response.selector.RegexpSelector;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMode.AbstractValidationMode;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMode.CountValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMode.ExistsValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMode.MatchesValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMode.TextValidator;
import com.xceptance.xlt.nocoding.util.Context;
import com.xceptance.xlt.nocoding.util.MockObjects;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;

public class ValidatorTest
{
    public Context context;

    public MockObjects mockObjects;

    @Before
    public void init()
    {
        context = new Context(XltProperties.getInstance(), new DataStorage());
        mockObjects = new MockObjects();
        mockObjects.loadResponse();
        context.setWebResponse(mockObjects.getResponse());
    }

    @Test
    public void testValidationWithCookie() throws Throwable
    {
        // Build Validator with ExistsModule
        final String validationName = "Cookie Validation";
        AbstractSelector selector = new CookieSelector(mockObjects.cookieName1);
        AbstractValidationMode mode = new ExistsValidator();
        Validator validator = new Validator(validationName, selector, mode);

        executeRequest(validator);

        // Build Validator with TextModule
        selector = new CookieSelector(mockObjects.cookieName1);
        mode = new TextValidator(mockObjects.cookieValue1);
        validator = new Validator(validationName, selector, mode);
        executeRequest(validator);

        // Build Validator with MatchesModule
        selector = new CookieSelector(mockObjects.cookieName1);
        mode = new MatchesValidator(mockObjects.cookieValue1);
        validator = new Validator(validationName, selector, mode);
        executeRequest(validator);

        // Build Validator with MatchesModule
        selector = new CookieSelector(mockObjects.cookieName1);
        mode = new CountValidator("1");
        validator = new Validator(validationName, selector, mode);
        executeRequest(validator);

    }

    @Test
    public void testValidationWithHeader() throws Throwable
    {
        // Build Validator with ExistsModule
        final String validationName = "Header Validation";
        AbstractSelector selector = new HeaderSelector("Set-Cookie");
        AbstractValidationMode mode = new ExistsValidator();
        Validator validator = new Validator(validationName, selector, mode);

        executeRequest(validator);

        // Build Validator with TextModule
        selector = new HeaderSelector("Set-Cookie");
        mode = new TextValidator(mockObjects.cookieName1 + "=" + mockObjects.cookieValue1);
        validator = new Validator(validationName, selector, mode);
        executeRequest(validator);

        // Build Validator with MatchesModule
        selector = new HeaderSelector("Set-Cookie");
        mode = new MatchesValidator(mockObjects.cookieName1 + "=" + mockObjects.cookieValue1);
        validator = new Validator(validationName, selector, mode);
        executeRequest(validator);

        // Build Validator with MatchesModule
        selector = new HeaderSelector("Set-Cookie");
        mode = new CountValidator("3");
        validator = new Validator(validationName, selector, mode);
        executeRequest(validator);

    }

    @Test
    public void testValidationWithRegexp() throws Throwable
    {
        // Build Validator with ExistsModule
        final String validationName = "Regexp Validation";
        AbstractSelector selector = new RegexpSelector(mockObjects.regexString);
        AbstractValidationMode mode = new ExistsValidator();
        Validator validator = new Validator(validationName, selector, mode);

        executeRequest(validator);

        // Build Validator with TextModule
        selector = new RegexpSelector(mockObjects.regexString);
        mode = new TextValidator(mockObjects.regexStringExpected);
        validator = new Validator(validationName, selector, mode);
        executeRequest(validator);

        // Build Validator with MatchesModule
        selector = new RegexpSelector(mockObjects.regexString);
        mode = new MatchesValidator(mockObjects.regexStringExpected);
        validator = new Validator(validationName, selector, mode);
        executeRequest(validator);

        // Build Validator with MatchesModule
        selector = new RegexpSelector(mockObjects.regexString);
        mode = new CountValidator("1");
        validator = new Validator(validationName, selector, mode);
        executeRequest(validator);
    }

    @Test
    public void testNoResolvingOfValidationName() throws Throwable
    {
        final String variableName = "validationName";
        final String validationName = "Regexp Validation";
        context.storeVariable(variableName, validationName);
        // Build Validator with ExistsModule
        final AbstractSelector selector = new RegexpSelector(mockObjects.regexString);
        final AbstractValidationMode mode = new ExistsValidator();
        final Validator validator = new Validator("${" + variableName + "}", selector, mode);
        executeRequest(validator);
        Assert.assertNotEquals(validationName, validator.getValidationName());
    }

    @Test
    public void testGroupRegexpValidation() throws Throwable
    {
        final String validationName = "Regexp Validation";
        // Build Validator with ExistsModule
        final AbstractSelector selector = new RegexpSelector(mockObjects.regexString);
        final AbstractValidationMode mode = new ExistsValidator();
        final Validator validator = new Validator(validationName, selector, mode, "0");
        executeRequest(validator);
        Assert.assertTrue(selector instanceof RegexpSelector);
        Assert.assertEquals("0", ((RegexpSelector) selector).getGroup());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGroupValidation() throws Throwable
    {
        final String validationName = "Regexp Validation";
        // Build Validator with ExistsModule
        final AbstractSelector selector = new HeaderSelector("Set-Cookie");
        final AbstractValidationMode mode = new ExistsValidator();
        final Validator validator = new Validator(validationName, selector, mode, "0");
        executeRequest(validator);
    }

    /*
     * Helper Methods
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
