package com.xceptance.xlt.nocoding.rebuild.scriptItem.action.request;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.InvalidArgumentException;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.Context;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;

/**
 * Tests the functionality of buildWebRequest() of the Request class
 */
public class RequestWebRequestBuilder
{
    public Request request;

    public WebRequest webRequest;

    public Context context;

    @Before
    public void init()
    {
        context = new Context(XltProperties.getInstance(), new DataStorage());
    }

    @Test
    public void buildWebRequest()
    {
        final String url = "https://localhost:8443/posters/";
        final HttpMethod method = HttpMethod.GET;
        final List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        final String body = null;
        final Boolean encodeBody = false;
        final Boolean encodeParameters = false;
        final Map<String, String> headers = new HashMap<String, String>();
        final Boolean xhr = false;

        request = new Request(url, "Visit Homepage");
        request.setMethod(method.toString());
        request.setParameters(parameters);
        request.setBody(body);
        request.setEncodeBody(encodeBody.toString());
        request.setEncodeParameters(encodeParameters.toString());
        request.setHeaders(headers);
        request.setXhr(xhr.toString());

        try
        {
            webRequest = request.buildWebRequest(context);
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
        catch (InvalidArgumentException | MalformedURLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Test
    public void buildWebRequestWithVariables()
    {
        String key, value;
        final String url = "https://localhost:8443/posters/";
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
        // TODO was wenn das null ist?
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
        // TODO was wenn das null ist?
        value = "";
        headers.put(key, value);
        context.getDataStorage().storeVariable("header3_key", key);
        context.getDataStorage().storeVariable("header3_value", value);
        headers_var.put("${header3_key}", "${header3_value}");

        request = new Request(url_var, "Visit Homepage");
        request.setMethod(method_var);
        request.setParameters(parameters_var);
        request.setBody(body_var);
        request.setEncodeBody(encodeBody_var);
        request.setEncodeParameters(encodeParameters_var);
        request.setHeaders(headers_var);
        request.setXhr(xhr_var);

        try
        {
            webRequest = request.buildWebRequest(context);
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
        catch (InvalidArgumentException | MalformedURLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
