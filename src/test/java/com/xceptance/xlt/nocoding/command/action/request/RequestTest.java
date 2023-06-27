package com.xceptance.xlt.nocoding.command.action.request;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.htmlunit.HttpMethod;
import org.htmlunit.WebRequest;
import org.htmlunit.util.Cookie;
import org.htmlunit.util.NameValuePair;
import org.junit.After;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.InvalidArgumentException;

import com.xceptance.xlt.engine.XltWebClient;
import com.xceptance.xlt.nocoding.command.AbstractContextTest;
import com.xceptance.xlt.nocoding.command.Command;
import com.xceptance.xlt.nocoding.command.storeDefault.StoreDefaultHeader;
import com.xceptance.xlt.nocoding.command.storeDefault.StoreDefaultParameter;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.RecentKeyTreeMap;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.storage.DataStorage;

/**
 * Tests {@link Request}
 */
public class RequestTest extends AbstractContextTest
{
    public Request request;

    public WebRequest webRequest;

    private final String url = "https://localhost:8443/posters/";

    public RequestTest(final Context<?> context)
    {
        super(context);
    }

    @After
    public void clearStorage()
    {
        final DataStorage storage = context.getDataStorage();
        storage.getDefaultCookies().clear();
        storage.getDefaultHeaders().clear();
        storage.getDefaultItems().clear();
        storage.getDefaultParameters().clear();
        storage.getDefaultStatics().clear();
        context.getWebClient().getCookieManager().clearCookies();
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
        final List<NameValuePair> parameters = new ArrayList<>();
        final String body = null;
        final Boolean encodeBody = false;
        final Boolean encodeParameters = false;
        final Map<String, String> headers = new HashMap<>();
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
        final List<NameValuePair> parameters = new ArrayList<>();
        final String body = null;
        final Boolean encodeBody = false;
        final Boolean encodeParameters = false;
        final Map<String, String> headers = new HashMap<>();
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
        final List<NameValuePair> parameters_var = new ArrayList<>();
        final String body_var = "${" + Constants.BODY + "}";
        final String encodeBody_var = "${" + Constants.ENCODEBODY + "}";
        final String encodeParameters_var = "${" + Constants.ENCODEPARAMETERS + "}";
        final Map<String, String> headers_var = new HashMap<>();
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
        else
        {
            String paramQuery = "";
            for (final NameValuePair parameter : expected.getRequestParameters())
            {
                paramQuery += parameter.getName() + "=" + parameter.getValue() + "&";
            }
            paramQuery = paramQuery.substring(0, paramQuery.lastIndexOf("&"));
            Assert.assertEquals(paramQuery, webRequest.getUrl().getQuery());
        }

        Assert.assertEquals(expected.getRequestBody(), webRequest.getRequestBody());
        Assert.assertEquals(expected.getAdditionalHeaders(), webRequest.getAdditionalHeaders());
        Assert.assertEquals(expected.getHttpMethod(), webRequest.getHttpMethod());
        final String url = webRequest.getUrl().toString().replaceFirst("\\?.*$", "");
        Assert.assertEquals(expected.getUrl().toString(), url);
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
    // TODO ignored test
    @Ignore
    public void testCaseInsensitiveHeaders() throws Throwable
    {
        final Map<String, String> defaultValues = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        defaultValues.put("Accept", "text/html");
        defaultValues.put("Cookie", "cookie1=value1");
        final List<Command> scriptItems = new ArrayList<>();
        defaultValues.forEach((key, value) -> {
            scriptItems.add(new StoreDefaultHeader(key, value));
        });

        for (final Command scriptItem : scriptItems)
        {
            scriptItem.execute(context);
        }

        request = new Request(url);
        final Map<String, String> newHeader = new RecentKeyTreeMap();
        newHeader.put("AccEpt", "application/xhtml+xml");
        newHeader.put("COOkIE", "cookieName=cookieValue");
        request.setHeaders(newHeader);
        request.fillDefaultData(context);
        webRequest = request.buildWebRequest(context);

        final Map<String, String> actualHeader = webRequest.getAdditionalHeaders();
        // Check new Header keys are in actual header
        newHeader.forEach((key, value) -> {
            if (!(actualHeader.containsKey(key)))
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
    // TODO ignored test
    @Ignore
    public void testDefaultHeaders() throws Throwable
    {
        // Simply use default headers
        final Map<String, String> defaultValues = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        defaultValues.put("Accept", "text/html");
        defaultValues.put("Accept-Encoding", "gzip");
        defaultValues.put("Cookie", "cookie1=value1");
        defaultValues.put("Referer", "https://www.xceptance.com");
        final List<Command> scriptItems = new ArrayList<>();
        defaultValues.forEach((key, value) -> {
            scriptItems.add(new StoreDefaultHeader(key, value));
        });

        for (final Command scriptItem : scriptItems)
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
    // TODO ignored test
    @Ignore
    public void testOverwriteDefaultHeaders() throws Throwable
    {
        final Map<String, String> defaultValues = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        defaultValues.put("Accept", "text/html");
        defaultValues.put("Accept-Encoding", "gzip");
        defaultValues.put("Cookie", "cookie1=value1");
        defaultValues.put("Referer", "https://www.xceptance.com");
        final List<Command> scriptItems = new ArrayList<>();
        defaultValues.forEach((key, value) -> {
            scriptItems.add(new StoreDefaultHeader(key, value));
        });

        for (final Command scriptItem : scriptItems)
        {
            scriptItem.execute(context);
        }

        request = new Request(url);
        final Map<String, String> newHeader = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
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
        final Map<String, String> defaultValues = new HashMap<>();
        defaultValues.put("param_1", "value_1");
        defaultValues.put("param_2", "value_2");
        defaultValues.put("param_3", "value_3");
        defaultValues.put("param_4", "value_4");
        final List<Command> scriptItems = new ArrayList<>();
        defaultValues.forEach((key, value) -> {
            scriptItems.add(new StoreDefaultParameter(key, value));
        });

        for (final Command scriptItem : scriptItems)
        {
            scriptItem.execute(context);
        }

        for (final Command scriptItem : scriptItems)
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
        final Map<String, String> defaultValues = new HashMap<>();
        defaultValues.put("param_1", "value_1");
        defaultValues.put("param_2", "value_2");
        defaultValues.put("param_3", "value_3");
        defaultValues.put("param_4", "value_4");
        final List<Command> scriptItems = new ArrayList<>();
        defaultValues.forEach((key, value) -> {
            scriptItems.add(new StoreDefaultParameter(key, value));
        });

        // Execute all store default parameters
        for (final Command scriptItem : scriptItems)
        {
            scriptItem.execute(context);
        }
        // Execute all store default parameters again
        for (final Command scriptItem : scriptItems)
        {
            scriptItem.execute(context);
        }
        request = new Request(url);
        final List<NameValuePair> parameters = new ArrayList<>();
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
        headers = new HashMap<>();
        headers.put("Accept", "text/html");
        request.setHeaders(headers);
        Assert.assertTrue(request.getHeaders() instanceof RecentKeyTreeMap);
        Assert.assertFalse(request.getHeaders().isEmpty());
    }

    /**
     * Verifies cookies are set in the {@link XltWebClient}.<br>
     * Given two cookies are set in the {@link Request},<br>
     * when the {@link WebRequest} is built,<br>
     * Then the WebClient should have both cookies.
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
        Map<String, String> cookies = null;
        request.setCookies(cookies);
        WebRequest webRequest = request.buildWebRequest(context);
        final Map<String, String> headers = webRequest.getAdditionalHeaders();
        Assert.assertFalse(headers.containsKey(Constants.COOKIE));

        cookies = new LinkedHashMap<>();
        cookies.put("headerCookie1", "cookieValue1");
        cookies.put("headerCookie2", "cookieValue2");
        request.setCookies(cookies);
        webRequest = request.buildWebRequest(context);
        final Set<Cookie> webRequestCookies = context.getWebClient().getCookies(new URL(url));
        Assert.assertEquals(2, webRequestCookies.size());
        int i = 1;
        for (final Cookie cookie : webRequestCookies)
        {
            Assert.assertEquals("headerCookie" + i, cookie.getName());
            Assert.assertEquals("cookieValue" + i, cookie.getValue());
            i++;
        }
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
        final Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put(Constants.COOKIE, "headerCookie=headerValue");
        request.setHeaders(requestHeaders);
        final WebRequest webRequest = request.buildWebRequest(context);
        final Map<String, String> headers = webRequest.getAdditionalHeaders();

        Assert.assertTrue(headers.containsKey(Constants.COOKIE));
        final String cookieString = headers.get(Constants.COOKIE);
        Assert.assertEquals("headerCookie=headerValue", cookieString);
    }

    /**
     * Verifies post-a-like requests without parameters are built correctly.
     *
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     */
    @Test
    public void postalikeParameterRequestWithoutAny() throws MalformedURLException, UnsupportedEncodingException
    {
        final List<String> httpMethods = new ArrayList<>();
        httpMethods.add("PATCH");
        httpMethods.add("POST");
        httpMethods.add("PUT");
        final String urlString = "https://www.xceptance.net/en/";
        final URL normalUrl = new URL(urlString);

        for (final String httpMethod : httpMethods)
        {
            // Build the request
            final Request normalRequest = new Request(urlString);
            normalRequest.setHttpMethod(httpMethod);
            // Build the WebRequest
            final WebRequest normalWebRequest = normalRequest.buildWebRequest(context);
            // Verify that the url is the same as the normal one
            Assert.assertTrue(normalWebRequest.getUrl().sameFile(normalUrl));
            // Verify that there is no query
            Assert.assertNull(normalWebRequest.getUrl().getQuery());
        }
    }

    /**
     * Verifies get-a-like requests without parameters are built correctly.
     *
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     */
    @Test
    public void getalikeParameterRequestWithoutAny() throws MalformedURLException, UnsupportedEncodingException
    {
        final List<String> httpMethods = new ArrayList<>();
        httpMethods.add("DELETE");
        httpMethods.add("GET");
        httpMethods.add("HEAD");
        httpMethods.add("OPTIONS");
        httpMethods.add("TRACE");
        final String urlString = "https://www.xceptance.net/en/";
        final URL normalUrl = new URL(urlString);

        for (final String httpMethod : httpMethods)
        {
            // Request without Query and Param
            // Build request
            final Request normalRequest = new Request(urlString);
            normalRequest.setHttpMethod(httpMethod);
            // Build WebRequest
            final WebRequest normalWebRequest = normalRequest.buildWebRequest(context);
            // Assert that the Url stays the same
            Assert.assertTrue(normalWebRequest.getUrl().sameFile(normalUrl));
            // Assert that there is no query
            Assert.assertNull(normalWebRequest.getUrl().getQuery());
        }
    }

    /**
     * Verifies post-a-like requests with specified parameters attribute are built correctly.
     *
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     */
    @Test
    public void postalikeParameterRequestWithParameter() throws MalformedURLException, UnsupportedEncodingException
    {
        final List<String> httpMethods = new ArrayList<>();
        httpMethods.add("PATCH");
        httpMethods.add("POST");
        httpMethods.add("PUT");
        final String urlString = "https://www.xceptance.net/en/";
        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new NameValuePair("parameterParam_1", "parameterValue_1"));
        parameters.add(new NameValuePair("parameterParam_2", "parameterValue_2"));

        for (final String httpMethod : httpMethods)
        {
            // Request with Param and without Query
            // Build the request with parameters
            final Request paramRequest = new Request(urlString);
            paramRequest.setHttpMethod(httpMethod);
            paramRequest.setParameters(parameters);
            // Build the WebRequest
            final WebRequest paramWebRequest = paramRequest.buildWebRequest(context);
            // Verify there is no query
            Assert.assertNull(paramWebRequest.getUrl().getQuery());
            // Verify the parameters are the parameters of the WebRequest
            Assert.assertEquals(parameters, paramWebRequest.getRequestParameters());
        }
    }

    /**
     * Verifies get-a-like requests with specified parameters attribute are built correctly.
     *
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     */
    @Test
    public void getalikeParameterRequestWithParameter() throws MalformedURLException, UnsupportedEncodingException
    {
        final List<String> httpMethods = new ArrayList<>();
        httpMethods.add("DELETE");
        httpMethods.add("GET");
        httpMethods.add("HEAD");
        httpMethods.add("OPTIONS");
        httpMethods.add("TRACE");
        final String urlString = "https://www.xceptance.net/en/";
        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new NameValuePair("parameterParam_1", "parameterValue_1"));
        parameters.add(new NameValuePair("parameterParam_2", "parameterValue_2"));
        final String parameterQuery = "parameterParam_1=parameterValue_1&parameterParam_2=parameterValue_2";

        for (final String httpMethod : httpMethods)
        {
            // Request with Param and without Query
            // Build request with parameters
            final Request paramRequest = new Request(urlString);
            paramRequest.setHttpMethod(httpMethod);
            paramRequest.setParameters(parameters);
            // Build WebRequest
            final WebRequest paramWebRequest = paramRequest.buildWebRequest(context);
            // Verify that the Url has a query
            Assert.assertNotNull(paramWebRequest.getUrl().getQuery());
            // Verify that the parameter are the query
            Assert.assertEquals(parameterQuery, paramWebRequest.getUrl().getQuery());
            // Verify the url is still correct
            Assert.assertEquals(urlString + "?" + parameterQuery, paramWebRequest.getUrl().toString());
        }
    }

    /**
     * Verifies post-a-like requests with a query in their URL are built correctly.
     *
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     */
    @Test
    public void postalikeParameterRequestWithQueryUrl() throws MalformedURLException, UnsupportedEncodingException
    {
        final List<String> httpMethods = new ArrayList<>();
        httpMethods.add("PATCH");
        httpMethods.add("POST");
        httpMethods.add("PUT");

        final String urlString = "https://www.xceptance.net/en/";
        final String urlQuery = "urlParam=urlValue";
        final String urlQueryString = urlString + "?" + urlQuery;
        final URL queryUrl = new URL(urlQueryString);

        // Iterate over all post-a-like methods
        for (final String httpMethod : httpMethods)
        {
            // Request with Query and without Param
            // Build the request with the query
            final Request queryRequest = new Request(urlQueryString);
            queryRequest.setHttpMethod(httpMethod);
            // Build the WebRequest
            final WebRequest queryWebRequest = queryRequest.buildWebRequest(context);
            // Verify the url is the same as the query url
            Assert.assertTrue(queryWebRequest.getUrl().sameFile(queryUrl));
            // Verify that there is a query
            Assert.assertNotNull(queryWebRequest.getUrl().getQuery());
            // Verify the query is the correct one
            Assert.assertEquals(urlQuery, queryWebRequest.getUrl().getQuery());
            // Verify the url is correct
            Assert.assertEquals(urlQueryString, queryWebRequest.getUrl().toString());
        }
    }

    /**
     * Verifies get-a-like requests with a query in their URL are built correctly.
     *
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     */
    @Test
    public void getalikeParameterRequestWithQueryUrl() throws MalformedURLException, UnsupportedEncodingException
    {
        final List<String> httpMethods = new ArrayList<>();
        httpMethods.add("DELETE");
        httpMethods.add("GET");
        httpMethods.add("HEAD");
        httpMethods.add("OPTIONS");
        httpMethods.add("TRACE");

        final String urlString = "https://www.xceptance.net/en/";
        final String urlQuery = "urlParam=urlValue";
        final String urlQueryString = urlString + "?" + urlQuery;
        final URL queryUrl = new URL(urlQueryString);

        // Iterate over all get-a-like methods
        for (final String httpMethod : httpMethods)
        {
            // Request with Query and without Param
            // Build Request with Query
            final Request queryRequest = new Request(urlQueryString);
            queryRequest.setHttpMethod(httpMethod);
            // Build WebRequest
            final WebRequest queryWebRequest = queryRequest.buildWebRequest(context);
            // Verify the url is the same as the query url
            Assert.assertTrue(queryWebRequest.getUrl().sameFile(queryUrl));
            // Verify there is a query
            Assert.assertNotNull(queryWebRequest.getUrl().getQuery());
            // Verify that the query is the same as the url query
            Assert.assertEquals(urlQuery, queryWebRequest.getUrl().getQuery());
            // Verify that the url is the same as the url query string
            Assert.assertEquals(urlQueryString, queryWebRequest.getUrl().toString());
        }
    }

    /**
     * Verifies parameters are handled correctly for Post and Post-a-like Requests.
     *
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     */
    @Test
    public void postalikeParameterRequestWithQueryUrlAndParameter() throws MalformedURLException, UnsupportedEncodingException
    {
        final List<String> httpMethods = new ArrayList<>();
        httpMethods.add("PATCH");
        httpMethods.add("POST");
        httpMethods.add("PUT");

        final String urlString = "https://www.xceptance.net/en/";
        final String urlQuery = "urlParam=urlValue";
        final String urlQueryString = urlString + "?" + urlQuery;
        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new NameValuePair("parameterParam_1", "parameterValue_1"));
        parameters.add(new NameValuePair("parameterParam_2", "parameterValue_2"));
        final URL queryUrl = new URL(urlQueryString);

        // Iterate over all post-a-like methods
        for (final String httpMethod : httpMethods)
        {
            // Request with Query and without Param
            // Build the request with the query
            final Request queryRequest = new Request(urlQueryString);
            queryRequest.setHttpMethod(httpMethod);
            // Build the WebRequest
            final WebRequest queryWebRequest = queryRequest.buildWebRequest(context);
            // Verify the url is the same as the query url
            Assert.assertTrue(queryWebRequest.getUrl().sameFile(queryUrl));
            // Verify that there is a query
            Assert.assertNotNull(queryWebRequest.getUrl().getQuery());
            // Verify the query is the correct one
            Assert.assertEquals(urlQuery, queryWebRequest.getUrl().getQuery());
            // Verify the url is correct
            Assert.assertEquals(urlQueryString, queryWebRequest.getUrl().toString());

            // Request with Query and Param
            // Build the request with query and parameters
            final Request paramQueryRequest = new Request(urlQueryString);
            paramQueryRequest.setHttpMethod(httpMethod);
            paramQueryRequest.setParameters(parameters);
            // Build the WebRequest
            final WebRequest paramQueryWebRequest = paramQueryRequest.buildWebRequest(context);
            // Verify there is a query
            Assert.assertNotNull(paramQueryWebRequest.getUrl().getQuery());
            // Verify the query is correct
            Assert.assertEquals(urlQuery, paramQueryWebRequest.getUrl().getQuery());
            // Verify the url is correct
            Assert.assertEquals(urlQueryString, paramQueryWebRequest.getUrl().toString());
            // Verify the parameters are the parameters of the WebRequest
            Assert.assertEquals(parameters, paramQueryWebRequest.getRequestParameters());
        }
    }

    /**
     * Verifies parameters are handled correctly for Get and Get-a-like Requests.
     *
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     */
    @Test
    public void getalikeParameterRequestWithQueryUrlAndParameter() throws MalformedURLException, UnsupportedEncodingException
    {
        final List<String> httpMethods = new ArrayList<>();
        httpMethods.add("DELETE");
        httpMethods.add("GET");
        httpMethods.add("HEAD");
        httpMethods.add("OPTIONS");
        httpMethods.add("TRACE");

        final String urlString = "https://www.xceptance.net/en/";
        final String urlQuery = "urlParam=urlValue";
        final String urlQueryString = urlString + "?" + urlQuery;
        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new NameValuePair("parameterParam_1", "parameterValue_1"));
        parameters.add(new NameValuePair("parameterParam_2", "parameterValue_2"));
        final String parameterQuery = "parameterParam_1=parameterValue_1&parameterParam_2=parameterValue_2";
        final String fullParamQueryString = urlQuery + "&" + parameterQuery;

        // Iterate over all get-a-like methods
        for (final String httpMethod : httpMethods)
        {
            // Request with Query and Param
            // Build the request with query and parameters
            final Request paramQueryRequest = new Request(urlQueryString);
            paramQueryRequest.setHttpMethod(httpMethod);
            paramQueryRequest.setParameters(parameters);
            // Build the WebRequest
            final WebRequest paramQueryWebRequest = paramQueryRequest.buildWebRequest(context);
            // Verify a query exists
            Assert.assertNotNull(paramQueryWebRequest.getUrl().getQuery());
            // Verify that the query is the same as the query from the url and the parameters
            Assert.assertEquals(fullParamQueryString, paramQueryWebRequest.getUrl().getQuery());
            // Verify the url is correct
            Assert.assertEquals(urlQueryString + "&" + parameterQuery, paramQueryWebRequest.getUrl().toString());
        }
    }

}
