package com.xceptance.xlt.nocoding.util.action.data;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.data.GeneralDataProvider;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.util.bsh.ParameterInterpreter;

public class URLActionDataBuilderTest
{
    private static ParameterInterpreter interpreter;

    private static XltProperties properties;

    private static GeneralDataProvider dataProvider;

    private static String name;

    private static String type;

    private static String url;

    private static String method;

    private static String encodeParameters;

    private static String encodeBody;

    private static String httpResponceCode;

    private static String body;

    private static List<URLActionDataValidation> validations = new ArrayList<>();

    private static URLActionDataValidation validation;

    private static List<URLActionDataStore> store = Collections.emptyList();

    private static URLActionDataStore storeItem;

    private static List<NameValuePair> parameters = Collections.emptyList();

    private static List<NameValuePair> cookies = Collections.emptyList();

    private static List<NameValuePair> headers = Collections.emptyList();

    private static String d_name;

    private static String d_type;

    private static String d_url;

    private static String d_method;

    private static String d_encode_parameters;

    private static String d_encode_body;

    private static String d_httpResponceCode;

    private static String d_body;

    private static List<URLActionDataValidation> d_validations = Collections.emptyList();

    private static List<URLActionDataStore> d_store = Collections.emptyList();

    private static List<NameValuePair> d_parameters = Collections.emptyList();

    private static List<NameValuePair> d_cookies = Collections.emptyList();

    private static List<NameValuePair> d_headers = Collections.emptyList();

    @BeforeClass
    public static void setup()
    {
        properties = XltProperties.getInstance();
        dataProvider = GeneralDataProvider.getInstance();
        interpreter = new ParameterInterpreter(properties, dataProvider);

        validation = new URLActionDataValidation("validation", URLActionDataValidation.XPATH, "xpath", URLActionDataValidation.MATCHES,
                                                 "matcher", interpreter);

        storeItem = new URLActionDataStore("store", URLActionDataStore.XPATH, "some xpath", interpreter);

        name = "name";

        type = URLActionData.TYPE_ACTION;

        url = "http://www.xceptance.com";

        method = URLActionData.METHOD_GET;

        encodeParameters = "true";

        encodeBody = "true";

        httpResponceCode = "400";

        body = "body";

        validations = new ArrayList<>();
        validations.add(validation);

        store = new ArrayList<>();
        store.add(storeItem);

        parameters = new ArrayList<>();
        parameters.add(new NameValuePair("parameter_1", "parameter_value_1"));

        cookies = new ArrayList<>();
        cookies.add(new NameValuePair("cookie_1", "cookie_value_1"));

        headers = new ArrayList<>();
        headers.add(new NameValuePair("header_1", "header_value_1"));

        d_name = "d_name";

        d_type = URLActionData.TYPE_STATIC;

        d_url = "http://www.blog.xceptance.com";

        d_method = URLActionData.METHOD_POST;

        d_encode_parameters = "false";

        d_encode_body = "false";

        d_httpResponceCode = "500";

        d_body = "d_body";

        d_validations = new ArrayList<>();

        d_store = new ArrayList<>();

        d_parameters = new ArrayList<>();

        d_cookies = new ArrayList<>();

        d_headers = new ArrayList<>();
    }

    @Test
    public void testCorrectActionBuild() throws MalformedURLException
    {
        final URLActionDataBuilder builder = new URLActionDataBuilder();

        builder.setBody(body);
        builder.setCookies(cookies);
        builder.setEncodeParameters(encodeParameters);
        builder.setEncodeBody(encodeBody);
        builder.setHeaders(headers);
        builder.setHttpResponceCode(httpResponceCode);
        builder.setValidations(validations);
        builder.setUrl(url);
        builder.setType(type);
        builder.setStore(store);
        builder.setParameters(parameters);
        builder.setName(name);
        builder.setMethod(method);
        builder.setInterpreter(interpreter);

        builder.setDefaultBody(d_body);
        builder.setDefaultCookies(d_cookies);
        builder.setDefaultEncodeParameters(d_encode_parameters);
        builder.setDefaultEncodeBody(d_encode_body);
        builder.setDefaultHeaders(d_headers);
        builder.setDefaultHttpResponceCode(d_httpResponceCode);
        builder.setDefaultValidations(d_validations);
        builder.setDefaultUrl(d_url);
        builder.setDefaultType(d_type);
        builder.setDefaultStore(d_store);
        builder.setDefaultParameters(d_parameters);
        builder.setDefaultName(d_name);
        builder.setDefaultMethod(d_method);
        builder.setInterpreter(interpreter);

        URLActionData action = builder.build();

        Assert.assertEquals(action.getName(), name);
        Assert.assertEquals(action.getBody(), body);
        Assert.assertEquals(action.getType(), type);
        Assert.assertEquals(action.getMethod().toString(), method);
        Assert.assertEquals(action.getUrlString(), url);
        Assert.assertFalse(action.getValidations().isEmpty());
        Assert.assertFalse(action.getStore().isEmpty());
        Assert.assertFalse(action.getHeaders().isEmpty());
        Assert.assertFalse(action.getParameters().isEmpty());
        Assert.assertFalse(action.getCookies().isEmpty());
        Assert.assertEquals(action.encodeBody().toString(), encodeBody);
        Assert.assertEquals(action.encodeParameters().toString(), encodeParameters);

        builder.setBody(null);
        builder.setCookies(Collections.<NameValuePair> emptyList());
        builder.setEncodeParameters(null);
        builder.setEncodeBody(null);
        builder.setHeaders(Collections.<NameValuePair> emptyList());
        builder.setHttpResponceCode(null);
        builder.setValidations(Collections.<URLActionDataValidation> emptyList());
        builder.setUrl(null);
        builder.setType(null);
        builder.setStore(Collections.<URLActionDataStore> emptyList());
        builder.setParameters(Collections.<NameValuePair> emptyList());
        builder.setName(null);
        builder.setMethod(null);
        builder.setInterpreter(interpreter);

        action = builder.build();

        Assert.assertEquals(action.getName(), d_name);
        Assert.assertEquals(action.getBody(), d_body);
        Assert.assertEquals(action.getType(), d_type);
        Assert.assertEquals(action.getMethod().toString(), d_method);
        Assert.assertEquals(action.getUrlString(), d_url);
        Assert.assertTrue(action.getValidations().isEmpty());
        Assert.assertTrue(action.getStore().isEmpty());
        Assert.assertTrue(action.getHeaders().isEmpty());
        Assert.assertTrue(action.getParameters().isEmpty());
        Assert.assertTrue(action.getCookies().isEmpty());
        Assert.assertEquals(action.encodeBody().toString(), d_encode_body);
        Assert.assertEquals(action.encodeParameters().toString(), d_encode_parameters);

    }

    @Test
    public void constructActionFromDefaults()
    {
        final URLActionDataBuilder builder = new URLActionDataBuilder();
        builder.setDefaultBody(body);
        builder.setDefaultCookies(cookies);
        builder.setDefaultEncodeParameters(encodeParameters);
        builder.setDefaultHeaders(headers);
        builder.setDefaultHttpResponceCode(httpResponceCode);
        builder.setDefaultValidations(validations);
        builder.setDefaultUrl(url);
        builder.setDefaultType(type);
        builder.setDefaultStore(store);
        builder.setDefaultParameters(parameters);
        builder.setDefaultName(name);
        builder.setDefaultMethod(method);
        builder.setInterpreter(interpreter);

        final URLActionData action = builder.build();

        Assert.assertEquals(action.getName(), name);
        Assert.assertEquals(action.getBody(), body);
        Assert.assertEquals(action.getType(), type);
        Assert.assertEquals(action.getMethod().toString(), method);
        Assert.assertEquals(action.getUrlString(), url);
        Assert.assertEquals(action.getValidations(), validations);
        Assert.assertEquals(action.getStore(), store);
        Assert.assertEquals(action.getHeaders(), headers);
        Assert.assertEquals(action.getParameters(), parameters);
        Assert.assertEquals(action.getCookies(), cookies);
        Assert.assertEquals(action.encodeBody().toString(), encodeBody);
        Assert.assertEquals(action.encodeParameters().toString(), encodeParameters);
    }

    @Test(expected = IllegalArgumentException.class)
    public void missingName()
    {
        final URLActionDataBuilder builder = new URLActionDataBuilder();
        builder.setUrl(url);
        builder.setInterpreter(interpreter);
        @SuppressWarnings("unused")
        final URLActionData action = builder.build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void missingUrl()
    {
        final URLActionDataBuilder builder = new URLActionDataBuilder();
        builder.setName(name);
        builder.setInterpreter(interpreter);
        @SuppressWarnings("unused")
        final URLActionData action = builder.build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void missingInterpreter()
    {
        final URLActionDataBuilder builder = new URLActionDataBuilder();
        builder.setUrl(url);
        builder.setName(name);
        @SuppressWarnings("unused")
        final URLActionData action = builder.build();
    }

}
