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
package com.xceptance.xlt.nocoding.command.action.response.store;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.htmlunit.HttpMethod;
import org.htmlunit.WebRequest;
import org.htmlunit.WebResponse;
import org.htmlunit.util.NameValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.nocoding.XltMockWebConnection;
import com.xceptance.xlt.nocoding.command.AbstractContextTest;
import com.xceptance.xlt.nocoding.command.action.response.extractor.CookieExtractor;
import com.xceptance.xlt.nocoding.command.action.response.extractor.HeaderExtractor;
import com.xceptance.xlt.nocoding.command.action.response.extractor.RegexpExtractor;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Tests {@link AbstractResponseStore}
 *
 * @author ckeiner
 */
public class StoreTest extends AbstractContextTest
{
    public StoreTest(final Context<?> context)
    {
        super(context);
    }

    private XltMockWebConnection webConnection;

    public AbstractResponseStore store;

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
        headers = new ArrayList<>();

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
        headers = new ArrayList<>();
        name = "Set-Cookie";
        value = "NINJA_FLASH=success=Login+successful.+Have+fun+in+our+shop%21;Path=/";
        headers.add(new NameValuePair(name, value));
        webConnection.setResponse(url, content, statusCode, statusMessage, contentType, headers);
    }

    /**
     * Verifies a header can be stored
     *
     * @throws Exception
     */
    @Test
    public void HeaderStoreTest() throws Exception
    {
        // Build WebRequest with localhost/posters as url
        final URL url = new URL("https://localhost:8443/posters/");
        final WebRequest settings = new WebRequest(url);
        // Get response from the XltMockWebConnection
        final WebResponse webResponse = webConnection.getResponse(settings);
        // Define the header you want to check for
        final String header = "Cache-Control";
        // Build the store unit that searches for the header
        store = new ResponseStore("header", new HeaderExtractor(header));

        context.setWebResponse(webResponse);
        // Execute
        store.execute(context);

        final String actual = context.resolveString("${header}");
        Assert.assertEquals("no-cache, no-store, max-age=0, must-revalidate", actual);

    }

    /**
     * Verifies a cookie can be stored
     *
     * @throws Exception
     */
    @Test
    public void CookieStoreTest() throws Exception
    {
        // Build WebRequest with localhost/posters/login/POST as url
        final URL url = new URL("https://localhost:8443/posters/login/POST");
        final WebRequest settings = new WebRequest(url, HttpMethod.POST);
        // Add the login parameters
        final List<NameValuePair> parameters = new ArrayList<>();
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
        // Build the store unit that searches only for the cookie
        store = new ResponseStore("cookie", new CookieExtractor(cookie));
        context.setWebResponse(webResponse);
        // Execute
        store.execute(context);

        final String text = "success=Login+successful.+Have+fun+in+our+shop%21";
        final String actual = context.resolveString("${cookie}");
        Assert.assertEquals(text, actual);
    }

    /**
     * Verifies a result found via regular expression can be stored
     *
     * @throws Exception
     */
    @Test
    public void RegExpStoreTest() throws Exception
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

        // Build a validator, that searches for the pattern and verifies text is the first match
        store = new ResponseStore("firstClass", new RegexpExtractor(pattern));
        context.setWebResponse(webResponse);
        // Execute
        store.execute(context);

        String actual = context.resolveString("${firstClass}");
        Assert.assertEquals(text, actual);

        // Define the group the text should be in
        final String group = "1";
        // Build a validator, that searches for the pattern and verifies text is the match in the matching group of
        // group
        store = new ResponseStore("firstClassWithGroup", new RegexpExtractor(pattern, group));
        context.setWebResponse(webResponse);
        // Execute
        store.execute(context);

        actual = context.resolveString("${firstClassWithGroup}");
        Assert.assertEquals(text, actual);
    }
}
