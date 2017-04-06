package com.xceptance.xlt.nocoding.util.action.data;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.util.bsh.ParameterInterpreter;

/**
 * Helper class to build an {@link URLActionData}.
 * <ul>
 * <li>Construct a {@link URLActionData} object step by step. For this fill the URLActionBuilder with values via setters
 * until you want to create a URLActionData. For this call {@link #build()}
 * <li>Offers a way to default common used values. If default values are set, the URLActionDataBuilder refers to them if
 * no individual data for the construction of a URLActionData object is available. <br>
 * Default attributes are prefixed with "d_".
 * <ul>
 *
 * @author matthias mitterreiter
 */
public class URLActionDataBuilder
{

    private String name;

    private String type;

    private String url;

    private String method;

    private String encodeParameters;

    private String encodeBody;

    private String httpResponceCode;

    private String body;

    private List<URLActionDataValidation> validations = Collections.emptyList();

    private List<URLActionDataStore> store = Collections.emptyList();

    private List<NameValuePair> parameters = Collections.emptyList();

    private List<NameValuePair> cookies = Collections.emptyList();

    private List<NameValuePair> headers = Collections.emptyList();

    private String d_name;

    private String d_type;

    private String d_url;

    private String d_method;

    private String d_encodeParameters;

    private String d_encodeBody;

    private String d_httpResponceCode;

    private String d_body;

    private List<URLActionDataValidation> d_validations = Collections.emptyList();

    private List<URLActionDataStore> d_store = Collections.emptyList();

    private List<NameValuePair> d_parameters = Collections.emptyList();

    private List<NameValuePair> d_cookies = Collections.emptyList();

    private List<NameValuePair> d_headers = Collections.emptyList();

    private ParameterInterpreter interpreter;

    /**
     * For debugging purpose. <br>
     * 'err-streams' the attributes of the object. <br>
     */
    public void outline()
    {
        System.err.println("UrlActionBuilder");
        if (name != null)
        {
            System.err.println("Name : " + name);
        }
        if (d_name != null)
        {
            System.err.println("D_Name : " + d_name);
        }
        if (type != null)
        {
            System.err.println("Type : " + type);
        }
        if (d_type != null)
        {
            System.err.println("D_Type : " + d_type);
        }
        if (url != null)
        {
            System.err.println("Url : " + url);
        }
        if (d_url != null)
        {
            System.err.println("D_Url : " + d_url);
        }
        if (method != null)
        {
            System.err.println("Method : " + method);
        }
        if (d_method != null)
        {
            System.err.println("D_Method : " + d_method);
        }
        if (httpResponceCode != null)
        {
            System.err.println("HttpCode : " + httpResponceCode);
        }
        if (d_httpResponceCode != null)
        {
            System.err.println("D_HttpCode : " + d_httpResponceCode);
        }
        if (body != null)
        {
            System.err.println("Body : " + body);
        }
        if (d_body != null)
        {
            System.err.println("D_Body : " + d_body);
        }
        if (!parameters.isEmpty())
        {
            System.err.println("Parameters:");
            for (final NameValuePair nvp : parameters)
            {

                System.err.println("\t" + nvp.getName() + " : " + nvp.getValue());
            }
        }
        if (!d_parameters.isEmpty())
        {
            System.err.println("D_Parameters:");
            for (final NameValuePair nvp : d_parameters)
            {

                System.err.println("\t" + nvp.getName() + " : " + nvp.getValue());
            }
        }
        if (!cookies.isEmpty())
        {
            System.err.println("Cookies:");
            for (final NameValuePair nvp : cookies)
            {

                System.err.println("\t" + nvp.getName() + " : " + nvp.getValue());
            }
        }
        if (!d_cookies.isEmpty())
        {
            System.err.println("D_Cookies:");
            for (final NameValuePair nvp : d_cookies)
            {

                System.err.println("\t" + nvp.getName() + " : " + nvp.getValue());
            }
        }
        if (!headers.isEmpty())
        {
            System.err.println("Headers:");
            for (final NameValuePair nvp : headers)
            {

                System.err.println("\t" + nvp.getName() + " : " + nvp.getValue());
            }
        }
        if (!d_headers.isEmpty())
        {
            System.err.println("D_Headers:");
            for (final NameValuePair nvp : d_headers)
            {

                System.err.println("\t" + nvp.getName() + " : " + nvp.getValue());
            }
        }
        if (!store.isEmpty())
        {
            System.err.println("Store:");
            for (final URLActionDataStore nvp : store)
            {

                nvp.outline();
            }
        }
        if (!d_store.isEmpty())
        {
            System.err.println("D_Store:");
            for (final URLActionDataStore nvp : d_store)
            {

                nvp.outline();
            }
        }
        if (!validations.isEmpty())
        {
            System.err.println("validations:");
            for (final URLActionDataValidation nvp : validations)
            {

                nvp.outline();
            }
        }
        if (!d_validations.isEmpty())
        {
            System.err.println("D_validations:");
            for (final URLActionDataValidation nvp : d_validations)
            {

                nvp.outline();
            }
        }
    }

    /**
     * <p>
     * Builds an {@link URLActionData} object from the values of the local attributes, set via setters(). <br>
     * If an attribute is not set, it checks the local default attributes. <br>
     * If neither an attribute nor a default attribute is given, <br>
     * it tries to construct the URLActionData object without this attribute. If this attribute is important but not
     * here (e.g url), it throws.
     * </p>
     * attributes are reset to 'null' after execution, default attributes not.
     *
     * @return {@link URLActionData}
     * @throws IllegalArgumentException
     */
    public URLActionData build()
    {
        URLActionData resultAction = null;

        // for debugging, use if u want to print the stored information
        // of URLActionDataBuilder
        // this.outline();

        try
        {
            resultAction = new URLActionData(getName(), getUrl(), getInterpreter());
            resultAction.setType(getType());
            resultAction.setMethod(getMethod());
            resultAction.setEncodeParameters(encodeParameters());
            resultAction.setEncodeBody(encodeBody());
            resultAction.setHttpResponceCode(getHttpResponceCode());
            resultAction.setBody(getBody());
            resultAction.setCookies(getCookies());
            resultAction.setHeaders(getHeaders());
            resultAction.setParameters(getParameters());
            resultAction.setValidations(getValidations());
            resultAction.setStore(getStore());
        }
        catch (final IllegalArgumentException e)
        {
            throw new IllegalArgumentException("Failed to create URLAction for action \"" + name + "\". " + e.getMessage(), e);
        }

        reset();

        return resultAction;
    }

    /**
     * Resets all the NON default attributes.
     */
    public void reset()
    {

        interpreter = null;
        name = null;
        type = null;
        url = null;
        method = null;
        encodeParameters = null;
        encodeBody = null;
        httpResponceCode = null;
        body = null;
        validations = Collections.emptyList();
        store = Collections.emptyList();
        parameters = Collections.emptyList();
        cookies = Collections.emptyList();
        headers = Collections.emptyList();

        XltLogger.runTimeLogger.debug("Resetting stored values!");
    }

    public String getName()
    {
        String result = null;
        if (name != null)
        {
            result = name;
        }
        else if (d_name != null)
        {
            result = d_name;
        }
        return result;
    }

    public String getType()
    {
        String result = null;
        if (type != null)
        {
            result = type;
        }
        else if (d_type != null)
        {
            result = d_type;
        }
        return result;
    }

    public String getUrl()
    {
        String result = null;
        if (url != null)
        {
            result = url;
        }
        else if (d_url != null)
        {
            result = d_url;
        }
        return result;
    }

    public String getMethod()
    {
        String result = null;
        if (method != null)
        {
            result = method;
        }
        else if (d_method != null)
        {
            result = d_method;
        }
        return result;
    }

    public String encodeParameters()
    {
        String result = null;
        if (encodeParameters != null)
        {
            result = encodeParameters;
        }
        else if (d_encodeParameters != null)
        {
            result = d_encodeParameters;
        }
        return result;
    }

    public String encodeBody()
    {
        String result = null;
        if (encodeBody != null)
        {
            result = encodeBody;
        }
        else if (d_encodeBody != null)
        {
            result = d_encodeBody;
        }
        return result;
    }

    public String getHttpResponceCode()
    {
        String result = null;
        if (httpResponceCode != null)
        {
            result = httpResponceCode;
        }
        else if (d_httpResponceCode != null)
        {
            result = d_httpResponceCode;
        }
        return result;
    }

    public String getBody()
    {
        String result = null;
        if (body != null)
        {
            result = body;
        }
        else if (d_body != null)
        {
            result = d_body;
        }
        return result;
    }

    public List<URLActionDataValidation> getValidations()
    {
        List<URLActionDataValidation> result = Collections.emptyList();
        if (!validations.isEmpty())
        {
            result = validations;
        }
        else if (!d_validations.isEmpty())
        {
            result = d_validations;
        }
        return result;
    }

    public List<NameValuePair> getParameters()
    {
        List<NameValuePair> result = Collections.emptyList();
        if (!parameters.isEmpty())
        {
            result = parameters;
        }
        else if (!d_parameters.isEmpty())
        {
            result = d_parameters;
        }
        return result;
    }

    public List<NameValuePair> getHeaders()
    {
        List<NameValuePair> result = Collections.emptyList();
        if (!headers.isEmpty())
        {
            result = headers;
        }
        else if (!d_headers.isEmpty())
        {
            result = d_headers;
        }
        return result;
    }

    public List<NameValuePair> getCookies()
    {
        List<NameValuePair> result = Collections.emptyList();
        if (!cookies.isEmpty())
        {
            result = cookies;
        }
        else if (!d_cookies.isEmpty())
        {
            result = d_cookies;
        }
        return result;
    }

    public List<URLActionDataStore> getStore()
    {
        List<URLActionDataStore> result = Collections.emptyList();
        if (!store.isEmpty())
        {
            result = store;
        }
        else if (!d_store.isEmpty())
        {
            result = d_store;
        }
        return result;
    }

    public ParameterInterpreter getInterpreter()
    {
        return interpreter;
    }

    public void setName(final String name)
    {
        this.name = name;
        XltLogger.runTimeLogger.debug(infoSetTagToValue("name", name));
    }

    public void setType(final String type)
    {
        this.type = type;
        XltLogger.runTimeLogger.debug(infoSetTagToValue("type", type));
    }

    public void setUrl(final String url)
    {
        this.url = url;
        XltLogger.runTimeLogger.debug(infoSetTagToValue("url", url));
    }

    public void setMethod(final String method)
    {
        this.method = method;
        XltLogger.runTimeLogger.debug(infoSetTagToValue("method", method));
    }

    public void setEncodeParameters(final String encoded)
    {
        encodeParameters = encoded;
        XltLogger.runTimeLogger.debug(infoSetTagToValue("encodeParameters", encoded));
    }

    public void setEncodeBody(final String encoded)
    {
        encodeBody = encoded;
        XltLogger.runTimeLogger.debug(infoSetTagToValue("encodeBody", encoded));
    }

    public void setHttpResponceCode(final String httpResponceCode)
    {
        this.httpResponceCode = httpResponceCode;
        XltLogger.runTimeLogger.debug(infoSetTagToValue("httpResponseCode", httpResponceCode));
    }

    public void setBody(final String body)
    {
        this.body = body;
        XltLogger.runTimeLogger.debug(infoSetTagToValue("body", body));
    }

    public void setValidations(final List<URLActionDataValidation> validations)
    {
        this.validations = validations;
        XltLogger.runTimeLogger.debug(infoSetTag("validations"));
    }

    public void setStore(final List<URLActionDataStore> store)
    {
        this.store = store;
        XltLogger.runTimeLogger.debug(infoSetTag("store"));
    }

    public void setParameters(final List<NameValuePair> parameters)
    {
        this.parameters = parameters;
        XltLogger.runTimeLogger.debug(infoSetTag("parameters"));
    }

    public void setCookies(final List<NameValuePair> cookies)
    {
        this.cookies = cookies;
        XltLogger.runTimeLogger.debug(infoSetTag("cookies"));
    }

    public void setHeaders(final List<NameValuePair> headers)
    {
        this.headers = headers;
        XltLogger.runTimeLogger.debug(infoSetTag("headers"));
    }

    public void setInterpreter(final ParameterInterpreter interpreter)
    {
        this.interpreter = interpreter;
        XltLogger.runTimeLogger.debug(infoSetTag("interpreter"));
    }

    public void setDefaultName(final String d_name)
    {
        this.d_name = d_name;
        XltLogger.runTimeLogger.debug(infoSetTagToValue("d_name", d_name));
    }

    public void setDefaultType(final String d_type)
    {
        this.d_type = d_type;
        XltLogger.runTimeLogger.debug(infoSetTagToValue("d_type", d_type));
    }

    public void setDefaultUrl(final String d_url)
    {
        this.d_url = d_url;
        XltLogger.runTimeLogger.debug(infoSetTagToValue("d_url", d_url));
    }

    public void setDefaultMethod(final String d_method)
    {
        this.d_method = d_method;
        XltLogger.runTimeLogger.debug(infoSetTagToValue("d_method", d_method));
    }

    public void setDefaultEncodeParameters(final String d_encoded)
    {
        d_encodeParameters = d_encoded;
        XltLogger.runTimeLogger.debug(infoSetTagToValue("d_encodeParameters", d_encoded));
    }

    public void setDefaultEncodeBody(final String d_encoded)
    {
        d_encodeBody = d_encoded;
        XltLogger.runTimeLogger.debug(infoSetTagToValue("d_encodeBody", d_encoded));
    }

    public void setDefaultHttpResponceCode(final String d_httpResponceCode)
    {
        this.d_httpResponceCode = d_httpResponceCode;
        XltLogger.runTimeLogger.debug(infoSetTagToValue("d_httpResponceCode", d_httpResponceCode));
    }

    public void setDefaultBody(final String d_body)
    {
        this.d_body = d_body;
        XltLogger.runTimeLogger.debug(infoSetTagToValue("d_body", d_body));

    }

    public void setDefaultValidations(final List<URLActionDataValidation> d_validations)
    {
        this.d_validations = d_validations;
        XltLogger.runTimeLogger.debug(infoSetTag("d_validations"));
    }

    public void setDefaultStore(final List<URLActionDataStore> d_store)
    {
        this.d_store = d_store;
        XltLogger.runTimeLogger.debug(infoSetTag("d_store"));
    }

    public void setDefaultParameters(final List<NameValuePair> d_parameters)
    {
        this.d_parameters = d_parameters;
        XltLogger.runTimeLogger.debug(infoSetTag("d_parameters"));
    }

    public void setDefaultCookies(final List<NameValuePair> d_cookies)
    {
        this.d_cookies = d_cookies;
        XltLogger.runTimeLogger.debug(infoSetTag("d_cookies"));
    }

    public void setDefaultHeaders(final List<NameValuePair> d_headers)
    {
        this.d_headers = d_headers;
        XltLogger.runTimeLogger.debug(infoSetTag("d_headers"));
    }

    public void addCookie(final NameValuePair cookie)
    {
        if (cookies.isEmpty() && cookie != null)
        {
            cookies = new ArrayList<>();
        }
        if (cookies != null)
        {
            cookies.add(cookie);
            XltLogger.runTimeLogger.debug(infoAddedNameValueToTag("Cookies", cookie.getName(), cookie.getValue()));
        }
    }

    public void addStore(final URLActionDataStore storeItem)
    {
        if (store.isEmpty() && storeItem != null)
        {
            store = new ArrayList<>();
        }
        if (storeItem != null)
        {
            store.add(storeItem);
            XltLogger.runTimeLogger.debug("Added URLActionDataStore");
        }
    }

    public void addHeader(final NameValuePair header)
    {
        if (headers.isEmpty() && header != null)
        {
            headers = new ArrayList<>();
        }
        if (header != null)
        {
            headers.add(header);
            XltLogger.runTimeLogger.debug(infoAddedNameValueToTag("Headers", header.getName(), header.getValue()));
        }
    }

    public void addValidation(final URLActionDataValidation validation)
    {
        if (validations.isEmpty() && validation != null)
        {
            validations = new ArrayList<>();
        }
        if (validation != null)
        {
            validations.add(validation);
            XltLogger.runTimeLogger.debug("Added URLActionDataValidation");
        }
    }

    private String infoSetTagToValue(final String tag, final String value)
    {
        final String message = MessageFormat.format("Set tag \"{0}\" = \"{1}\" ", tag, value);
        return message;
    }

    private String infoSetTag(final String tag)
    {
        final String message = MessageFormat.format("Set tag \"{0}\" ", tag);
        return message;
    }

    private String infoAddedNameValueToTag(final String tag, final String name, final String value)
    {
        final String message = MessageFormat.format("Added \"{0}\" = \"{1}\" to tag \"{2}\" ", name, value, tag);
        return message;
    }

}
