package com.xceptance.xlt.nocoding.myUtilTests.action.response;

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
import com.xceptance.xlt.nocoding.util.PropertyManager;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;

public class ValidatorTest
{
    private PropertyManager propertyManager;

    private XltMockWebConnection webConnection;

    public AbstractValidator validator;

    @Before
    public void init()
    {
        URL url;
        String content;
        int statusCode;
        String statusMessage;
        String contentType;
        List<NameValuePair> headers;
        String name;
        String value;
        this.propertyManager = new PropertyManager(XltProperties.getInstance(), new DataStorage());
        webConnection = new XltMockWebConnection(propertyManager.getWebClient());
        try
        {
            url = new URL("https://localhost:8443/posters/");
            content = "<li class=\"userMenuHeader\">Welcome: Guest</li>\n"
                      + "<li class=\"userMenuContent\"><a href=\"/posters/registration\"\n"
                      + "    class=\"goToRegistration\" title=\"Create new account\"> <span\n"
                      + "        class=\"glyphicon glyphicon-user\"></span> <span class=\"margin_left5\"></span><span\n"
                      + "        class=\"text-primary\">Create new account</span>\n" + "</a></li>\n" + "<li class=\"userMenuContent\"><a\n"
                      + "    href=\"/posters/login\" class=\"goToLogin\"\n" + "    title=\"Sign In\"><span\n"
                      + "        class=\"glyphicon glyphicon-log-in\"></span> <span class=\"margin_left5\"></span><span\n"
                      + "        class=\"text-primary\">Sign In</span>\n" + "</a></li>";
            statusCode = 200;
            statusMessage = "A-OK";
            contentType = "text/html";
            headers = new ArrayList<NameValuePair>();

            name = "Content-Encoding";
            value = "gzip";
            headers.add(new NameValuePair(name, value));
            name = "Cache-Control";
            value = "no-cache, no-store, max-age=0, must-revalidate";
            headers.add(new NameValuePair(name, value));
            name = "Set-Cookie";
            value = "NINJA_SESSION=13df523184375d64180d6adf4194f5a250229afe-___TS=1506611416747&cart=5163fb7b-08bf-4dbd-bf98-4145039eddb2;Path=/;Expires=Thu, 28-Sep-2017 16:10:16 GMT;Max-Age=3600;HttpOnly";
            headers.add(new NameValuePair(name, value));

            webConnection.setResponse(url, content, statusCode, statusMessage, contentType, headers);

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
        catch (final MalformedURLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void HeaderValidatorTest() throws Exception
    {
        final URL url = new URL("https://localhost:8443/posters/");
        final WebRequest settings = new WebRequest(url);
        final WebResponse webResponse = webConnection.getResponse(settings);
        final String header = "Content-Encoding";
        validator = new HeaderValidator("Standard", header);
        validator.validate(propertyManager, webResponse);
        validator = new HeaderValidator("Text", header, Constants.TEXT, "gzip");
        validator.validate(propertyManager, webResponse);
        validator = new HeaderValidator("Count", header, Constants.COUNT, "1");
        validator.validate(propertyManager, webResponse);
    }

    @Test
    public void CookieValidatorTest() throws Exception
    {
        final URL url = new URL("https://localhost:8443/posters/login/POST");
        final WebRequest settings = new WebRequest(url, HttpMethod.POST);
        final List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new NameValuePair("email", "john@doe.com"));
        parameters.add(new NameValuePair("password", "topsecret"));
        parameters.add(new NameValuePair("btnSignIn", ""));
        settings.setRequestParameters(parameters);
        final WebResponse webResponse = webConnection.getResponse(settings);
        final String cookie = "NINJA_FLASH";
        final String text = "success=Login+successful.+Have+fun+in+our+shop%21";
        validator = new CookieValidator("CookieValidationTest", cookie);
        validator.validate(propertyManager, webResponse);
        validator = new CookieValidator("CookieValidationTest", cookie, text);
    }

    @Test
    public void RegExpValidatorTest() throws Exception
    {
        final URL url = new URL("https://localhost:8443/posters/");
        final WebRequest settings = new WebRequest(url);
        final List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new NameValuePair("email", "john@doe.com"));
        parameters.add(new NameValuePair("password", "topsecret"));
        parameters.add(new NameValuePair("btnSignIn", ""));
        settings.setRequestParameters(parameters);
        final WebResponse webResponse = webConnection.getResponse(settings);

        final String pattern = "class.*>";
        final String text = "class=\"userMenuHeader\">Welcome: Guest</li>";
        final String group = "1";

        validator = new RegExpValidator("CookieValidationTest", pattern, text, group);
        validator.validate(propertyManager, webResponse);
        validator = new RegExpValidator("CookieValidationTest", pattern, text);
        validator.validate(propertyManager, webResponse);
    }

}
