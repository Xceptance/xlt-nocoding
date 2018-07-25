package com.xceptance.xlt.nocoding.command.action.response;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.XltMockWebConnection;
import com.xceptance.xlt.nocoding.command.action.response.Validator;
import com.xceptance.xlt.nocoding.command.action.response.extractor.AbstractExtractor;
import com.xceptance.xlt.nocoding.command.action.response.extractor.CookieExtractor;
import com.xceptance.xlt.nocoding.command.action.response.extractor.HeaderExtractor;
import com.xceptance.xlt.nocoding.command.action.response.extractor.RegexpExtractor;
import com.xceptance.xlt.nocoding.command.action.response.validator.AbstractValidator;
import com.xceptance.xlt.nocoding.command.action.response.validator.CountValidator;
import com.xceptance.xlt.nocoding.command.action.response.validator.ExistsValidator;
import com.xceptance.xlt.nocoding.command.action.response.validator.MatchesValidator;
import com.xceptance.xlt.nocoding.command.action.response.validator.TextValidator;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.context.LightWeightContext;

/**
 * Test {@link Validator} with all combinations of {@link AbstractExtractor} and {@link AbstractValidator}
 * 
 * @author ckeiner
 */
public class ExtendedValidatorTest
{
    private Context<?> context;

    private XltMockWebConnection webConnection;

    public Validator validator;

    /**
     * Instantiate the fields and set answers to localhost/posters/ and localhost/posters/POST
     * 
     * @throws MalformedURLException
     */
    @Before
    public void init() throws MalformedURLException
    {
        URL url;
        String content;
        int statusCode;
        String statusMessage;
        String contentType;
        List<NameValuePair> headers;
        String name;
        String value;
        this.context = new LightWeightContext(XltProperties.getInstance());
        webConnection = new XltMockWebConnection(context.getWebClient());

        // Set answer to localhost/posters/
        url = new URL("https://localhost:8443/posters/");
        // content = "blub";
        content = "<li class=\"userMenuHeader\">Welcome: Guest</li>\n" + "<li class=\"userMenuContent\"><a href=\"/posters/registration\"\n"
                  + " class=\"goToRegistration\" title=\"Create new account\"> <span\n"
                  + " class=\"glyphicon glyphicon-user\"></span> <span class=\"margin_left5\"></span><span\n"
                  + " class=\"text-primary\">Create new account</span>\n" + "</a></li>\n" + "<li class=\"userMenuContent\"><a\n"
                  + " href=\"/posters/login\" class=\"goToLogin\"\n" + " title=\"Sign In\"><span\n"
                  + " class=\"glyphicon glyphicon-log-in\"></span> <span class=\"margin_left5\"></span><span\n"
                  + " class=\"text-primary\">Sign In</span>\n" + "</a></li>";
        statusCode = 200;
        statusMessage = "A-OK";
        contentType = "text/html";
        headers = new ArrayList<NameValuePair>();

        name = "Cache-Control";
        value = "no-cache, no-store, max-age=0, must-revalidate";
        headers.add(new NameValuePair(name, value));
        name = "Set-Cookie";
        value = "NINJA_SESSION=13df523184375d64180d6adf4194f5a250229afe-___TS=1506611416747&cart=5163fb7b-08bf-4dbd-bf98-4145039eddb2;Path=/;Expires=Thu, 28-Sep-2017 16:10:16 GMT;Max-Age=3600;HttpOnly";
        headers.add(new NameValuePair(name, value));

        webConnection.setResponse(url, content, statusCode, statusMessage, contentType, headers);

        // Set answer to localhost/posters/POST
        url = new URL("https://localhost:8443/posters/login/POST");
        content = "blub";
        statusCode = 200;
        statusMessage = "A-OK";
        contentType = "text/html";
        headers = new ArrayList<NameValuePair>();
        name = "Set-Cookie";
        value = "NINJA_FLASH=success=Login+successful.+Have+fun+in+our+shop%21;Path=/";
        headers.add(new NameValuePair(name, value));
        webConnection.setResponse(url, content, statusCode, statusMessage, contentType, headers);
    }

    /**
     * Validates {@link HeaderExtractor} with multiple {@link AbstractValidator}.
     * 
     * @throws Exception
     */
    @Test
    public void HeaderValidationTest() throws Exception
    {
        // Build WebRequest with localhost/posters as url
        final URL url = new URL("https://localhost:8443/posters/");
        final WebRequest settings = new WebRequest(url);
        // Get response from the XltMockWebConnection
        final WebResponse webResponse = webConnection.getResponse(settings);
        // Define the header you want to check for
        final String header = "Cache-Control";
        // Define the text you want to see in the header
        final String text = "no-cache, no-store, max-age=0, must-revalidate";
        // Define the count of occurences of the header
        final String count = "1";
        // Build the validator that searches for the header

        validator = new Validator("HeaderValidation Standard", new HeaderExtractor(header), null);
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
        // Assert that we validated with an exists validator
        Assert.assertTrue(validator.getMethod() instanceof ExistsValidator);
        // Build the validator, that validates the content of the header equals text
        validator = new Validator("HeaderValidation Text", new HeaderExtractor(header), new TextValidator(text));
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
        // Build the validator that validates the number of occurences of the header equals count
        validator = new Validator("HeaderValidation Count", new HeaderExtractor(header), new CountValidator(count));
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
    }

    /**
     * Validates {@link CookieExtractor} with multiple {@link AbstractValidator}.
     * 
     * @throws Exception
     */
    @Test
    public void CookieValidationTest() throws Exception
    {
        // Build WebRequest with localhost/posters/login/POST as url
        final URL url = new URL("https://localhost:8443/posters/login/POST");
        final WebRequest settings = new WebRequest(url, HttpMethod.POST);
        // Add the login parameters
        final List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new NameValuePair("email", "john@doe.com"));
        parameters.add(new NameValuePair("password", "topsecret"));
        parameters.add(new NameValuePair("btnSignIn", ""));
        // Set the login parameters as request parameters for the WebRequest - this needn't be done
        settings.setRequestParameters(parameters);
        // Get response from the XltMockWebConnection
        final WebResponse webResponse = webConnection.getResponse(settings);
        // Define the cookie you want to search for
        final String cookie = "NINJA_FLASH";
        // Define the content of the cookie
        final String text = "success=Login+successful.+Have+fun+in+our+shop%21";
        // Build the validator that searches only for the cookie
        validator = new Validator("CookieValidation Standard", new CookieExtractor(cookie), null);
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
        // Assert that we validated with an exists validator
        Assert.assertTrue(validator.getMethod() instanceof ExistsValidator);
        // Build another validator that searches for the cookie and verifies the content equals text
        validator = new Validator("CookieValidation Text", new CookieExtractor(cookie), new TextValidator(text));
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
        // Build another validator that searches for the cookie and verifies the content matches the text
        final String matches = "success=Login\\+successful.\\+Have\\+fun\\+in\\+our\\+shop%21";
        validator = new Validator("CookieValidation Matches", new CookieExtractor(cookie), new MatchesValidator(matches));
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
    }

    /**
     * Validates {@link RegexpExtractor} with multiple {@link AbstractValidator}.
     * 
     * @throws Exception
     */
    @Test
    public void RegexpValidationTest() throws Exception
    {
        // Build WebRequest with localhost/posters as url
        final URL url = new URL("https://localhost:8443/posters/");
        final WebRequest settings = new WebRequest(url);
        // Get response from the XltMockWebConnection
        final WebResponse webResponse = webConnection.getResponse(settings);

        // Define the pattern you want to search for
        final String pattern = "(class.*>)";
        // Define the expected text of the match
        final String text = "class=\"userMenuHeader\">Welcome: Guest</li>";
        // Define the group the text should be in
        final String group = "1";

        // Build a validator, that searches for the pattern
        validator = new Validator("RegExpValidation Standard", new RegexpExtractor(pattern), null);
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
        // Assert that we validated with an exists validator
        Assert.assertTrue(validator.getMethod() instanceof ExistsValidator);
        // Build a validator, that searches for the pattern and verifies text is the first match
        validator = new Validator("RegExpValidation Text", new RegexpExtractor(pattern), new TextValidator(text));
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
        ;
        // Build a validator, that searches for the pattern and verifies text is the match in the matching group of group
        validator = new Validator("RegExpValidation Text+Group", new RegexpExtractor(pattern, group), new TextValidator(text));
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
    }

    /**
     * Validates {@link HeaderExtractor} with multiple {@link AbstractValidator}. Both have variables.
     * 
     * @throws Exception
     */
    @Test
    public void HeaderValidationWithVariables() throws Exception
    {
        // Store the header name
        context.getDataStorage().getVariables().store("header_1", "Cache-Control");
        // Store some of the content
        context.getDataStorage().getVariables().store("content_1", "no-cache, no-store, max-");
        // Store the expected count of headers
        context.getDataStorage().getVariables().store("count_1", "1");
        // Build WebRequest with localhost/posters as url
        final URL url = new URL("https://localhost:8443/posters/");
        final WebRequest settings = new WebRequest(url);
        // Get response from the XltMockWebConnection
        final WebResponse webResponse = webConnection.getResponse(settings);
        // Define the header you want to check for
        final String header = "${header_1}";
        // Define the text you want to see in the header
        final String text = "${content_1}age=0, must-revalidate";
        // Define the count of occurences of the header
        final String count = "${count_1}";

        validator = new Validator("HeaderValidation Standard", new HeaderExtractor(header), null);
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
        // Assert that we validated with an exists validator
        Assert.assertTrue(validator.getMethod() instanceof ExistsValidator);
        // Build the validator, that validates the content of the header equals text
        validator = new Validator("HeaderValidation Text", new HeaderExtractor(header), new TextValidator(text));
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
        // Build the validator that validates the number of occurences of the header equals count
        validator = new Validator("HeaderValidation Count", new HeaderExtractor(header), new CountValidator(count));
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
    }

    /**
     * Validates {@link CookieExtractor} with multiple {@link AbstractValidator}. Both have variables.
     * 
     * @throws Exception
     */
    @Test
    public void CookieValidationWithVariables() throws Exception
    {
        // Store the cookie name
        context.getDataStorage().getVariables().store("cookie", "NINJA_FLASH");
        // Store the first part of the cookie content
        context.getDataStorage().getVariables().store("cookie_content_name", "success");
        // Store the second part of the cookie content
        context.getDataStorage().getVariables().store("cookie_content_value", "Login+successful.+Have+fun+in+our+shop%21");
        // Build WebRequest with localhost/posters/login/POST as url
        final URL url = new URL("https://localhost:8443/posters/login/POST");
        final WebRequest settings = new WebRequest(url, HttpMethod.POST);
        // Add the login parameters
        final List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new NameValuePair("email", "john@doe.com"));
        parameters.add(new NameValuePair("password", "topsecret"));
        parameters.add(new NameValuePair("btnSignIn", ""));
        // Set the login parameters as request parameters for the WebRequest - this needn't be done
        settings.setRequestParameters(parameters);
        // Get response from the XltMockWebConnection
        final WebResponse webResponse = webConnection.getResponse(settings);
        // Define the cookie you want to search for
        final String cookie = "${cookie}";
        // Define the content of the cookie
        final String text = "${cookie_content_name}=${cookie_content_value}";

        validator = new Validator("CookieValidation Standard", new CookieExtractor(cookie), null);
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
        // Assert that we validated with an exists validator
        Assert.assertTrue(validator.getMethod() instanceof ExistsValidator);
        // Build another validator that searches for the cookie and verifies the content equals text
        validator = new Validator("CookieValidation Text", new CookieExtractor(cookie), new TextValidator(text));
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
        // Build another validator that searches for the cookie and verifies the content matches the text
        final String matches = "success=Login\\+successful.\\+Have\\+fun\\+in\\+our\\+shop%21";
        validator = new Validator("CookieValidation Matches", new CookieExtractor(cookie), new MatchesValidator(matches));
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
    }

    /**
     * Validates {@link RegexpExtractor} with multiple {@link AbstractValidator}. Both have variables.
     * 
     * @throws Exception
     */
    @Test
    public void RegexpValidationWithVariables() throws Exception
    {
        // Store a pattern
        context.getDataStorage().getVariables().store("pattern", "class.*>");
        // Store the expected match
        context.getDataStorage().getVariables().store("text", "class=\"userMenuHeader\">Welcome: Guest</li>");
        // Store the group the match is in
        context.getDataStorage().getVariables().store("group", "1");
        // Build WebRequest with localhost/posters as url
        final URL url = new URL("https://localhost:8443/posters/");
        final WebRequest settings = new WebRequest(url);
        // Get response from the XltMockWebConnection
        final WebResponse webResponse = webConnection.getResponse(settings);

        // Define the pattern you want to search for
        final String pattern = "(${pattern})";
        // Define the expected text of the match
        final String text = "${text}";
        // Define the group the text should be in
        final String group = "${group}";

        // Build a validator, that searches for the pattern
        validator = new Validator("RegExpValidation Standard", new RegexpExtractor(pattern), null);
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
        // Assert that we validated with an exists validator
        Assert.assertTrue(validator.getMethod() instanceof ExistsValidator);
        // Build a validator, that searches for the pattern and verifies text is the first match
        validator = new Validator("RegExpValidation Text", new RegexpExtractor(pattern), new TextValidator(text));
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
        ;
        // Build a validator, that searches for the pattern and verifies text is the match in the matching group of group
        validator = new Validator("RegExpValidation Text+Group", new RegexpExtractor(pattern, group), new TextValidator(text));
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
    }

}
