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
import com.xceptance.xlt.nocoding.util.Context;
import com.xceptance.xlt.nocoding.util.RecentKeyTreeMap;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;

/**
 * Tests the functionality of buildWebRequest() of the Request class
 */
public class RequestTest
{
    public Request request;

    public WebRequest webRequest;

    public Context context;

    private final String url = "https://localhost:8443/posters/";

    @Before
    public void init()
    {
        context = new Context(XltProperties.getInstance(), new DataStorage());
    }

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
        webRequest = request.buildWebRequest();
        // URL, Method
        final WebRequest expected = new WebRequest(new URL(url), method);
        // Parameters
        expected.setRequestParameters(parameters);
        // Headers
        expected.setAdditionalHeaders(headers);
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

        context.getDataStorage().storeVariable(Constants.URL, url);
        context.getDataStorage().storeVariable(Constants.METHOD, method.toString());
        // We need to store individual parameters
        // propertyManager.getDataStorage().storeVariable(Constants.PARAMETERS, parameters);
        context.getDataStorage().storeVariable(Constants.BODY, body);
        context.getDataStorage().storeVariable(Constants.ENCODEBODY, encodeBody.toString());
        context.getDataStorage().storeVariable(Constants.ENCODEPARAMETERS, encodeParameters.toString());
        // Store single headers
        // propertyManager.getDataStorage().storeVariable(Constants.ENCODEPARAMETERS, encodeParameters.toString());
        context.getDataStorage().storeVariable(Constants.XHR, xhr.toString());
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
        context.getDataStorage().storeVariable("param1_key", key);
        context.getDataStorage().storeVariable("param1_value", value);
        parameters_var.add(new NameValuePair("${param1_key}", "${param1_value}"));
        key = "password";
        value = "topsecret";
        parameters.add(new NameValuePair(key, value));
        context.getDataStorage().storeVariable("param2_key", key);
        context.getDataStorage().storeVariable("param2_value", value);
        parameters_var.add(new NameValuePair("${param2_key}", "${param2_value}"));
        key = "btnSignIn";
        value = "";
        parameters.add(new NameValuePair(key, value));
        context.getDataStorage().storeVariable("param3_key", key);
        context.getDataStorage().storeVariable("param3_value", value);
        parameters_var.add(new NameValuePair("${param3_key}", "${param3_value}"));

        // Headers
        key = "login";
        value = "john@doe.com";
        headers.put(key, value);
        context.getDataStorage().storeVariable("header1_key", key);
        context.getDataStorage().storeVariable("header1_value", value);
        headers_var.put("${header1_key}", "${header1_value}");
        key = "password";
        value = "topsecret";
        headers.put(key, value);
        context.getDataStorage().storeVariable("header2_key", key);
        context.getDataStorage().storeVariable("header2_value", value);
        headers_var.put("${header2_key}", "${header2_value}");
        key = "btnSignIn";
        value = "";
        headers.put(key, value);
        context.getDataStorage().storeVariable("header3_key", key);
        context.getDataStorage().storeVariable("header3_value", value);
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

        webRequest = request.buildWebRequest();
        // URL, Method
        final WebRequest expected = new WebRequest(new URL(url), method);
        // Parameters
        expected.setRequestParameters(parameters);
        // Headers
        expected.setAdditionalHeaders(headers);
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

    @Test(expected = InvalidArgumentException.class)
    public void testEmptyRequest()
        throws InvalidArgumentException, MalformedURLException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
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
        final Map<String, String> newHeader = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
        newHeader.put("AccEpt", "application/xhtml+xml");
        newHeader.put("COOkIE", "cookieName=cookieValue");
        request.setHeaders(newHeader);
        request.fillDefaultData(context);
        webRequest = request.buildWebRequest();

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

    @Test
    public void testDefaultData() throws InvalidArgumentException, MalformedURLException, UnsupportedEncodingException
    {
        request = new Request(url);
        request.fillDefaultData(context);
        webRequest = request.buildWebRequest();

        // Normally, Method, Xhr, Encode-Parameters, and Encode-Body are set
        Assert.assertEquals(HttpMethod.GET, webRequest.getHttpMethod());
        Assert.assertFalse(webRequest.isXHR());
        Assert.assertFalse(Boolean.getBoolean(request.getEncodeParameters()));
        Assert.assertFalse(Boolean.getBoolean(request.getEncodeParameters()));
    }

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
        webRequest = request.buildWebRequest();

        final Map<String, String> actualHeader = webRequest.getAdditionalHeaders();

        actualHeader.forEach((key, value) -> {
            Assert.assertTrue(defaultValues.containsKey(key));
            Assert.assertEquals(defaultValues.get(key), value);
        });
    }

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
        webRequest = request.buildWebRequest();

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
        webRequest = request.buildWebRequest();

        final List<NameValuePair> actualParameters = webRequest.getRequestParameters();

        for (final NameValuePair parameter : actualParameters)
        {
            Assert.assertTrue(defaultValues.containsKey(parameter.getName()));
            Assert.assertEquals(defaultValues.get(parameter.getName()), parameter.getValue());
        }
    }

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

        for (final ScriptItem scriptItem : scriptItems)
        {
            scriptItem.execute(context);
        }

        for (final ScriptItem scriptItem : scriptItems)
        {
            scriptItem.execute(context);
        }
        request = new Request(url);
        final List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new NameValuePair("param_1", "aDifferentValue"));
        parameters.add(new NameValuePair("param_5", "anotherDifferentValue"));
        request.fillDefaultData(context);
        webRequest = request.buildWebRequest();

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

}
