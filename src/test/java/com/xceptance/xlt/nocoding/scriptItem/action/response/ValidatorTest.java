package com.xceptance.xlt.nocoding.scriptItem.action.response;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.XltMockWebConnection;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.AbstractValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.CookieValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.HeaderValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.RegExpValidator;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.Context;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;

public class ValidatorTest
{
    private Context context;

    private XltMockWebConnection webConnection;

    public AbstractValidator validator;

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
        this.context = new Context(XltProperties.getInstance(), new DataStorage());
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

    @Test
    public void HeaderValidatorTest() throws Exception
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
        validator = new HeaderValidator("HeaderValidation Standard", Constants.EXISTS, header);
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
        // Build the validator, that validates the content of the header equals text
        validator = new HeaderValidator("HeaderValidation Text", Constants.TEXT, header, text, null);
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
        // Build the validator that validates the number of occurences of the header equals count
        validator = new HeaderValidator("HeaderValidation Count", Constants.COUNT, header, null, count);
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
    }

    @Test
    public void CookieValidatorTest() throws Exception
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
        validator = new CookieValidator("CookieValidation Standard", Constants.EXISTS, cookie);
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
        // Build another validator that searches for the cookie and verifies the content equals text
        validator = new CookieValidator("CookieValidation Text", cookie, text);
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
    }

    @Test
    public void RegExpValidatorTest() throws Exception
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
        validator = new RegExpValidator("RegExpValidation Standard", Constants.EXISTS, pattern);
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
        // Build a validator, that searches for the pattern and verifies text is the first match
        validator = new RegExpValidator("RegExpValidation Text", pattern, text);
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
        ;
        // Build a validator, that searches for the pattern and verifies text is the match in the matching group of group
        validator = new RegExpValidator("RegExpValidation Text+Group", pattern, text, group);
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
    }

    @Test
    public void HeaderValidatorWithVariables() throws Exception
    {
        // Store the header name
        context.getDataStorage().storeVariable("header_1", "Cache-Control");
        // Store some of the content
        context.getDataStorage().storeVariable("content_1", "no-cache, no-store, max-");
        // Store the expected count of headers
        context.getDataStorage().storeVariable("count_1", "1");
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
        // Build the validator that searches for the header
        validator = new HeaderValidator("HeaderValidationVariable Standard", Constants.EXISTS, header);
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
        // Build the validator, that validates the content of the header equals text
        validator = new HeaderValidator("HeaderValidationVariable Text", Constants.TEXT, header, text, null);
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
        // Build the validator that validates the number of occurences of the header equals count
        validator = new HeaderValidator("HeaderValidationVariable Count", Constants.COUNT, header, null, count);
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
    }

    @Test
    public void CookieValidatorWithVariables() throws Exception
    {
        // Store the cookie name
        context.getDataStorage().storeVariable("cookie", "NINJA_FLASH");
        // Store the first part of the cookie content
        context.getDataStorage().storeVariable("cookie_content_name", "success");
        // Store the second part of the cookie content
        context.getDataStorage().storeVariable("cookie_content_value", "Login+successful.+Have+fun+in+our+shop%21");
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
        // Build the validator that searches only for the cookie
        validator = new CookieValidator("CookieValidationVariable Standard", Constants.EXISTS, cookie);
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
        // Build another validator that searches for the cookie and verifies the content equals text
        validator = new CookieValidator("CookieValidationVariable Text", cookie, text);
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
    }

    @Test
    public void RegExpValidatorWithVariables() throws Exception
    {
        // Store a pattern
        context.getDataStorage().storeVariable("pattern", "class.*>");
        // Store the expected match
        context.getDataStorage().storeVariable("text", "class=\"userMenuHeader\">Welcome: Guest</li>");
        // Store the group the match is in
        context.getDataStorage().storeVariable("group", "1");
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
        validator = new RegExpValidator("RegExpValidationVariable Standard", Constants.EXISTS, pattern);
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
        // Build a validator, that searches for the pattern and verifies text is the first match
        validator = new RegExpValidator("RegExpValidationVariable Text", pattern, text);
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
        // Build a validator, that searches for the pattern and verifies text is the match in the matching group of group
        validator = new RegExpValidator("RegExpValidationVariable Text+Group", pattern, text, group);
        context.setWebResponse(webResponse);
        // Execute
        validator.execute(context);
    }

}
