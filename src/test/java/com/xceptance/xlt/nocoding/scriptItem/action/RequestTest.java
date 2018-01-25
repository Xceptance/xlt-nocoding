package com.xceptance.xlt.nocoding.scriptItem.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.InvalidArgumentException;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultHeader;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultParameter;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.RecentKeyTreeMap;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.context.LightWeightContext;

/**
 * Tests {@link Request}
 */
public class RequestTest
{
    public Request request;

    public WebRequest webRequest;

    public Context<?> context;

    private final String url = "https://localhost:8443/posters/";

    /**
     * Creates a new {@link Context}
     */
    @Before
    public void init()
    {
        context = new LightWeightContext(XltProperties.getInstance());
    }

    /**
     * Verifies {@link Request} builds the {@link WebRequest} correctly
     * 
     * @throws InvalidArgumentException
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     */
    @Test
    public void buildWebRequest() throws InvalidArgumentException, MalformedURLException, UnsupportedEncodingException
    {
        final HttpMethod method = HttpMethod.GET;
        final List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        final String body = null;
        final Boolean encodeBody = false;
        final Boolean encodeParameters = false;
        final Map<String, String> headers = new HashMap<String, String>();
        final Boolean xhr = false;

        request = new Request(url);
        request.setHttpMethod(method.toString());
        request.setParameters(parameters);
        request.setBody(body);
        request.setEncodeBody(encodeBody.toString());
        request.setEncodeParameters(encodeParameters.toString());
        request.setHeaders(headers);
        request.setXhr(xhr.toString());
        webRequest = request.buildWebRequest(context);
        // URL, Method
        final WebRequest expected = new WebRequest(new URL(url), method);
        // Parameters
        expected.setRequestParameters(parameters);
        // Headers
        expected.getAdditionalHeaders().putAll(context.getDefaultHeaders().getItems());
        expected.getAdditionalHeaders().putAll(headers);
        if (xhr)
        {
            expected.setXHR();
        }
        if (expected.getHttpMethod() == HttpMethod.POST || expected.getHttpMethod() == HttpMethod.PUT
            || expected.getHttpMethod() == HttpMethod.PATCH)
        {
            expected.setRequestBody(body);
        }

        Assert.assertEquals(expected.getRequestBody(), webRequest.getRequestBody());
        Assert.assertEquals(expected.getRequestParameters(), webRequest.getRequestParameters());
        Assert.assertEquals(expected.getAdditionalHeaders(), webRequest.getAdditionalHeaders());
        Assert.assertEquals(expected.getHttpMethod(), webRequest.getHttpMethod());
        Assert.assertEquals(expected.getUrl(), webRequest.getUrl());
        Assert.assertEquals(expected.isXHR(), webRequest.isXHR());
        Assert.assertEquals(expected.toString(), webRequest.toString());

    }

    /**
     * Verifies {@link Request} with variables builds the {@link WebRequest} correctly
     * 
     * @throws InvalidArgumentException
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     */
    @Test
    public void buildWebRequestWithVariables()
        throws InvalidArgumentException, MalformedURLException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
        UnsupportedEncodingException
    {
        String key, value;
        final HttpMethod method = HttpMethod.GET;
        final List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        final String body = null;
        final Boolean encodeBody = false;
        final Boolean encodeParameters = false;
        final Map<String, String> headers = new HashMap<String, String>();
        final Boolean xhr = false;

        context.getVariables().store(Constants.URL, url);
        context.getVariables().store(Constants.METHOD, method.toString());
        // We need to store individual parameters
        context.getVariables().store(Constants.BODY, body);
        context.getVariables().store(Constants.ENCODEBODY, encodeBody.toString());
        context.getVariables().store(Constants.ENCODEPARAMETERS, encodeParameters.toString());
        // Store single headers
        context.getVariables().store(Constants.XHR, xhr.toString());
        final String url_var = "${" + Constants.URL + "}";
        final String method_var = "${" + Constants.METHOD + "}";
        final List<NameValuePair> parameters_var = new ArrayList<NameValuePair>();
        final String body_var = "${" + Constants.BODY + "}";
        final String encodeBody_var = "${" + Constants.ENCODEBODY + "}";
        final String encodeParameters_var = "${" + Constants.ENCODEPARAMETERS + "}";
        final Map<String, String> headers_var = new HashMap<String, String>();
        final String xhr_var = "${" + Constants.XHR + "}";

        // Parameters
        key = "login";
        value = "john@doe.com";
        parameters.add(new NameValuePair(key, value));
        context.getVariables().store("param1_key", key);
        context.getVariables().store("param1_value", value);
        parameters_var.add(new NameValuePair("${param1_key}", "${param1_value}"));
        key = "password";
        value = "topsecret";
        parameters.add(new NameValuePair(key, value));
        context.getVariables().store("param2_key", key);
        context.getVariables().store("param2_value", value);
        parameters_var.add(new NameValuePair("${param2_key}", "${param2_value}"));
        key = "btnSignIn";
        value = "";
        parameters.add(new NameValuePair(key, value));
        context.getVariables().store("param3_key", key);
        context.getVariables().store("param3_value", value);
        parameters_var.add(new NameValuePair("${param3_key}", "${param3_value}"));

        // Headers
        key = "login";
        value = "john@doe.com";
        headers.put(key, value);
        context.getVariables().store("header1_key", key);
        context.getVariables().store("header1_value", value);
        headers_var.put("${header1_key}", "${header1_value}");
        key = "password";
        value = "topsecret";
        headers.put(key, value);
        context.getVariables().store("header2_key", key);
        context.getVariables().store("header2_value", value);
        headers_var.put("${header2_key}", "${header2_value}");
        key = "btnSignIn";
        value = "";
        headers.put(key, value);
        context.getVariables().store("header3_key", key);
        context.getVariables().store("header3_value", value);
        headers_var.put("${header3_key}", "${header3_value}");

        request = new Request(url_var);
        request.setHttpMethod(method_var);
        request.setParameters(parameters_var);
        request.setBody(body_var);
        request.setEncodeBody(encodeBody_var);
        request.setEncodeParameters(encodeParameters_var);
        request.setHeaders(headers_var);
        request.setXhr(xhr_var);

        request.resolveValues(context);

        webRequest = request.buildWebRequest(context);
        // URL, Method
        final WebRequest expected = new WebRequest(new URL(url), method);
        // Parameters
        expected.setRequestParameters(parameters);
        // Headers

        expected.getAdditionalHeaders().putAll(context.getDefaultHeaders().getItems());
        expected.getAdditionalHeaders().putAll(headers);
        if (xhr)
        {
            expected.setXHR();
        }
        if (expected.getHttpMethod() == HttpMethod.POST || expected.getHttpMethod() == HttpMethod.PUT
            || expected.getHttpMethod() == HttpMethod.PATCH)
        {
            expected.setRequestBody(body);
        }

        Assert.assertEquals(expected.getRequestBody(), webRequest.getRequestBody());
        Assert.assertEquals(expected.getRequestParameters(), webRequest.getRequestParameters());
        Assert.assertEquals(expected.getAdditionalHeaders(), webRequest.getAdditionalHeaders());
        Assert.assertEquals(expected.getHttpMethod(), webRequest.getHttpMethod());
        Assert.assertEquals(expected.getUrl(), webRequest.getUrl());
        Assert.assertEquals(expected.isXHR(), webRequest.isXHR());

    }

    /**
     * Verifies a {@link Request} with no default url throws an {@link InvalidArgumentException}
     * 
     * @throws Exception
     */
    @Test(expected = InvalidArgumentException.class)
    public void testEmptyRequest() throws Exception
    {
        request = new Request(null);
        try
        {
            request.execute(context);
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Verifies the headers in {@link Request} are case insensitive
     * 
     * @throws Throwable
     */
    @Test
    public void testCaseInsensitiveHeaders() throws Throwable
    {
        final Map<String, String> defaultValues = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
        defaultValues.put("Accept", "text/html");
        defaultValues.put("Cookie", "cookie1=value1");
        final List<ScriptItem> scriptItems = new ArrayList<ScriptItem>();
        defaultValues.forEach((key, value) -> {
            scriptItems.add(new StoreDefaultHeader(key, value));
        });

        for (final ScriptItem scriptItem : scriptItems)
        {
            scriptItem.execute(context);
        }

        request = new Request(url);
        final Map<String, String> newHeader = new RecentKeyTreeMap();
        newHeader.put("Accept-Encoding", "gzip, deflate");
        newHeader.put("AccEpt", "application/xhtml+xml");
        newHeader.put("COOkIE", "cookieName=cookieValue");
        request.setHeaders(newHeader);
        request.fillDefaultData(context);
        webRequest = request.buildWebRequest(context);

        final Map<String, String> actualHeader = webRequest.getAdditionalHeaders();

        actualHeader.forEach((key, value) -> {
            if (!(newHeader.containsKey(key)))
            {
                throw new AssertionError();
            }
            else
            {
                System.out.println("Key is: " + key);
                Assert.assertEquals(newHeader.get(key), value);
            }
        });
    }

    /**
     * Verifies all default data is added, this means: Url, HttpMethod, Xhr, EncodeParameters, EncodeBody
     * 
     * @throws InvalidArgumentException
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testDefaultData() throws InvalidArgumentException, MalformedURLException, UnsupportedEncodingException
    {
        context.getDefaultItems().store(Constants.URL, url);
        request = new Request();
        request.fillDefaultData(context);
        webRequest = request.buildWebRequest(context);

        // Normally, Method, Xhr, Encode-Parameters, and Encode-Body are set
        Assert.assertEquals(HttpMethod.GET, webRequest.getHttpMethod());
        Assert.assertFalse(webRequest.isXHR());
        Assert.assertFalse(Boolean.getBoolean(request.getEncodeParameters()));
        Assert.assertFalse(Boolean.getBoolean(request.getEncodeBody()));
    }

    /**
     * Verifies defaulöt headers are added correctly
     * 
     * @throws Throwable
     */
    @Test
    public void testDefaultHeaders() throws Throwable
    {
        // Simply use default headers
        final Map<String, String> defaultValues = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
        defaultValues.put("Accept", "text/html");
        defaultValues.put("Accept-Encoding", "gzip");
        defaultValues.put("Cookie", "cookie1=value1");
        defaultValues.put("Referer", "https://www.xceptance.com");
        final List<ScriptItem> scriptItems = new ArrayList<ScriptItem>();
        defaultValues.forEach((key, value) -> {
            scriptItems.add(new StoreDefaultHeader(key, value));
        });

        for (final ScriptItem scriptItem : scriptItems)
        {
            scriptItem.execute(context);
        }
        request = new Request(url);
        request.fillDefaultData(context);
        webRequest = request.buildWebRequest(context);

        final Map<String, String> actualHeader = webRequest.getAdditionalHeaders();

        actualHeader.forEach((key, value) -> {
            Assert.assertTrue(defaultValues.containsKey(key));
            Assert.assertEquals(defaultValues.get(key), value);
        });
    }

    /**
     * Verifies default headers are overwritten when specified in {@link Request#getHeaders()}
     * 
     * @throws Throwable
     */
    @Test
    public void testOverwriteDefaultHeaders() throws Throwable
    {
        final Map<String, String> defaultValues = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
        defaultValues.put("Accept", "text/html");
        defaultValues.put("Accept-Encoding", "gzip");
        defaultValues.put("Cookie", "cookie1=value1");
        defaultValues.put("Referer", "https://www.xceptance.com");
        final List<ScriptItem> scriptItems = new ArrayList<ScriptItem>();
        defaultValues.forEach((key, value) -> {
            scriptItems.add(new StoreDefaultHeader(key, value));
        });

        for (final ScriptItem scriptItem : scriptItems)
        {
            scriptItem.execute(context);
        }

        request = new Request(url);
        final Map<String, String> newHeader = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
        newHeader.put("Accept", "application/xhtml+xml");
        newHeader.put("Cookie", "cookieName=cookieValue");
        request.setHeaders(newHeader);
        request.fillDefaultData(context);
        webRequest = request.buildWebRequest(context);

        final Map<String, String> actualHeader = webRequest.getAdditionalHeaders();

        actualHeader.forEach((key, value) -> {
            if (!(newHeader.containsKey(key)))
            {
                Assert.assertTrue(defaultValues.containsKey(key));
                Assert.assertEquals(defaultValues.get(key), value);
            }
            else
            {
                Assert.assertEquals(newHeader.get(key), value);
            }
        });
    }

    /**
     * Verifies default parameters are added
     * 
     * @throws Throwable
     */
    @Test
    public void testDefaultParameters() throws Throwable
    {
        final Map<String, String> defaultValues = new HashMap<String, String>();
        defaultValues.put("param_1", "value_1");
        defaultValues.put("param_2", "value_2");
        defaultValues.put("param_3", "value_3");
        defaultValues.put("param_4", "value_4");
        final List<ScriptItem> scriptItems = new ArrayList<ScriptItem>();
        defaultValues.forEach((key, value) -> {
            scriptItems.add(new StoreDefaultParameter(key, value));
        });

        for (final ScriptItem scriptItem : scriptItems)
        {
            scriptItem.execute(context);
        }

        for (final ScriptItem scriptItem : scriptItems)
        {
            scriptItem.execute(context);
        }
        request = new Request(url);
        request.fillDefaultData(context);
        webRequest = request.buildWebRequest(context);

        final List<NameValuePair> actualParameters = webRequest.getRequestParameters();

        for (final NameValuePair parameter : actualParameters)
        {
            Assert.assertTrue(defaultValues.containsKey(parameter.getName()));
            Assert.assertEquals(defaultValues.get(parameter.getName()), parameter.getValue());
        }
    }

    /**
     * Verifies default parameters are added and not overwritten
     * 
     * @throws Throwable
     */
    @Test
    public void testOverwriteDefaultParameters() throws Throwable
    {
        final Map<String, String> defaultValues = new HashMap<String, String>();
        defaultValues.put("param_1", "value_1");
        defaultValues.put("param_2", "value_2");
        defaultValues.put("param_3", "value_3");
        defaultValues.put("param_4", "value_4");
        final List<ScriptItem> scriptItems = new ArrayList<ScriptItem>();
        defaultValues.forEach((key, value) -> {
            scriptItems.add(new StoreDefaultParameter(key, value));
        });

        // Execute all store default parameters
        for (final ScriptItem scriptItem : scriptItems)
        {
            scriptItem.execute(context);
        }
        // Execute all store default parameters again
        for (final ScriptItem scriptItem : scriptItems)
        {
            scriptItem.execute(context);
        }
        request = new Request(url);
        final List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new NameValuePair("param_1", "aDifferentValue"));
        parameters.add(new NameValuePair("param_5", "anotherDifferentValue"));
        request.fillDefaultData(context);
        webRequest = request.buildWebRequest(context);

        final List<NameValuePair> actualParameters = webRequest.getRequestParameters();

        for (final NameValuePair parameter : actualParameters)
        {
            if (!parameters.contains(parameter))
            {
                Assert.assertTrue(defaultValues.containsKey(parameter.getName()));
                Assert.assertEquals(defaultValues.get(parameter.getName()), parameter.getValue());
            }
        }
    }

    /**
     * Verifies {@link Request#setHeaders(Map)} does not change the type of {@link Request#getHeaders()}, so it stay
     * {@link RecentKeyTreeMap}
     */
    @Test
    public void testSetHeaders()
    {
        final Request request = new Request(null);
        Map<String, String> headers = null;
        request.setHeaders(headers);
        Assert.assertTrue(request.getHeaders() instanceof RecentKeyTreeMap);
        Assert.assertTrue(request.getHeaders().isEmpty());
        headers = new HashMap<String, String>();
        headers.put("Accept", "text/html");
        request.setHeaders(headers);
        Assert.assertTrue(request.getHeaders() instanceof RecentKeyTreeMap);
        Assert.assertFalse(request.getHeaders().isEmpty());
    }

    /**
     * Verifies cookies can be set
     * 
     * @throws InvalidArgumentException
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testSimpleCookie() throws InvalidArgumentException, MalformedURLException, UnsupportedEncodingException
    {
        final Request request = new Request(url);
        request.fillDefaultData(context);
        List<NameValuePair> cookies = null;
        request.setCookies(cookies);
        WebRequest webRequest = request.buildWebRequest(context);
        Map<String, String> headers = webRequest.getAdditionalHeaders();
        Assert.assertFalse(headers.containsKey(Constants.COOKIE));

        cookies = new ArrayList<NameValuePair>();
        cookies.add(new NameValuePair("headerCookie", "cookieValue"));
        request.setCookies(cookies);
        webRequest = request.buildWebRequest(context);
        headers = webRequest.getAdditionalHeaders();
        Assert.assertTrue(headers.containsKey(Constants.COOKIE));
        final String cookieString = headers.get(Constants.COOKIE);
        Assert.assertEquals("headerCookie=cookieValue;", cookieString);
    }

    /**
     * Cookies can be set with {@link Request#setHeaders(Map)} and via {@link Request#setCookies(List)}. This test case
     * verifies, that ,regardless of how the cookies are set, all cookies of both methods are in the {@link WebRequest}.
     * 
     * @throws InvalidArgumentException
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testSetHeaderCookieAndSetCookie() throws InvalidArgumentException, MalformedURLException, UnsupportedEncodingException
    {
        final Request request = new Request(url);
        request.fillDefaultData(context);
        final List<NameValuePair> cookies = new ArrayList<NameValuePair>();
        cookies.add(new NameValuePair("cookieName", "cookieValue"));
        request.setCookies(cookies);
        final Map<String, String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put(Constants.COOKIE, "headerCookie=headerValue");
        request.setHeaders(requestHeaders);
        final WebRequest webRequest = request.buildWebRequest(context);
        final Map<String, String> headers = webRequest.getAdditionalHeaders();

        Assert.assertTrue(headers.containsKey(Constants.COOKIE));
        final String cookieString = headers.get(Constants.COOKIE);
        Assert.assertEquals("headerCookie=headerValue;cookieName=cookieValue;", cookieString);
    }

    /**
     * Verifies that cookies set via {@link Request#setHeaders(Map)} are in {@link WebRequest}.
     * 
     * @throws InvalidArgumentException
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testSetHeaderCookie() throws InvalidArgumentException, MalformedURLException, UnsupportedEncodingException
    {
        final Request request = new Request(url);
        request.fillDefaultData(context);
        final Map<String, String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put(Constants.COOKIE, "headerCookie=headerValue");
        request.setHeaders(requestHeaders);
        final WebRequest webRequest = request.buildWebRequest(context);
        final Map<String, String> headers = webRequest.getAdditionalHeaders();

        Assert.assertTrue(headers.containsKey(Constants.COOKIE));
        final String cookieString = headers.get(Constants.COOKIE);
        Assert.assertEquals("headerCookie=headerValue", cookieString);
    }

}
