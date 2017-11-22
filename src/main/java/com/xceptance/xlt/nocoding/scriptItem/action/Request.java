package com.xceptance.xlt.nocoding.scriptItem.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.openqa.selenium.InvalidArgumentException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.engine.XltWebClient;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.Context;

/**
 * Describes the request defined in the file and transposes it to a {@link WebRequest} that is also sent via the
 * {@link XltWebClient}
 * 
 * @author ckeiner
 */
public class Request extends AbstractActionItem
{
    /**
     * The URL of the website as String
     */
    private String url;

    /**
     * The HttpMethod for the webrequest. Defaults to "GET"
     */
    private String httpMethod;

    /**
     * Sets the WebRequest as Xhr request. Defaults to "false"
     */
    private String xhr;

    /**
     * Defines if the parameters are encoded. Defaults to "false"
     */
    private String encodeParameters;

    /**
     * The list of all parameters
     */
    private List<NameValuePair> parameters;

    /**
     * A map that define the headers
     */
    private Map<String, String> headers;

    /**
     * The body of the WebRequest
     */
    private String body;

    /**
     * Defines if the body is encoded. Defaults to "false"
     */
    private String encodeBody;

    /**
     * The webRequest as defined with this request object
     */
    @JsonIgnore
    private WebRequest webRequest;

    /**
     * Creates a class with the minimum of information, that is the URL and the name of the Action
     * 
     * @param url
     *            The URL of the website
     * @param name
     *            The name of the action
     */
    public Request(final String url)
    {
        this.url = url;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(final String url)
    {
        this.url = url;
    }

    public String getHttpMethod()
    {
        return httpMethod;
    }

    public void setHttpMethod(final String httpMethod)
    {
        this.httpMethod = httpMethod;
    }

    public String getXhr()
    {
        return xhr;
    }

    public void setXhr(final String xhr)
    {
        this.xhr = xhr;
    }

    public String getEncodeParameters()
    {
        return encodeParameters;
    }

    public void setEncodeParameters(final String encodeParameters)
    {
        this.encodeParameters = encodeParameters;
    }

    public List<NameValuePair> getParameters()
    {
        return parameters;
    }

    public void setParameters(final List<NameValuePair> parameters)
    {
        this.parameters = parameters;
    }

    public Map<String, String> getHeaders()
    {
        return headers;
    }

    public void setHeaders(final Map<String, String> headers)
    {
        this.headers = headers;
    }

    public String getBody()
    {
        return body;
    }

    public void setBody(final String body)
    {
        this.body = body;
    }

    public String getEncodeBody()
    {
        return encodeBody;
    }

    public void setEncodeBody(final String encodeBody)
    {
        this.encodeBody = encodeBody;
    }

    public WebRequest getWebRequest()
    {
        return webRequest;
    }

    public void setWebRequest(final WebRequest webRequest)
    {
        this.webRequest = webRequest;
    }

    /**
     * Fills in the default values for certain attributes
     * 
     * @param context
     *            The propertyManager with the DataStorage
     */
    void fillDefaultData(final Context context)
    {
        if (getUrl() == null || getUrl().isEmpty())
        {
            setUrl(context.getConfigItemByKey(Constants.URL));
        }

        // if the name is null, check if it has a default value
        if (this.getHttpMethod() == null)
        {
            setHttpMethod(context.getConfigItemByKey(Constants.METHOD));
            // this.httpmethod = HttpMethod.valueOf(method);
        }
        if (this.getXhr() == null)
        {
            setXhr(context.getConfigItemByKey(Constants.XHR));
            // this.isXhr = Boolean.valueOf(isXhr);
        }
        if (this.getEncodeParameters() == null)
        {
            setEncodeParameters(context.getConfigItemByKey(Constants.ENCODEPARAMETERS));
            // this.encodeParameters = Boolean.valueOf(encodeParameters);
        }

        /*
         * Set default parameters
         */

        if (context.getDefaultParameters() != null)
        {
            // Get default parameters
            final Map<String, String> defaultParameters = context.getDefaultParameters();
            // Overwrite the default values with the current ones or add the current ones
            if (getParameters() != null)
            {
                for (final NameValuePair parameters : getParameters())
                {
                    defaultParameters.put(parameters.getName(), parameters.getValue());
                }
            }
            // Create new list in which we store the all parameters
            final List<NameValuePair> newParameters = new ArrayList<NameValuePair>(defaultParameters.size());
            defaultParameters.forEach((key, value) -> {
                newParameters.add(new NameValuePair(key, value));
            });
            // Assign newParameters to the parameters for this request
            setParameters(newParameters);
        }

        /*
         * Set default headers
         */

        if (context.getDefaultHeaders() != null)
        {
            // Create a tree map that is case insensitive (since headers are case insensitive
            final TreeMap<String, String> defaultHeaders = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
            // Get the default headers
            defaultHeaders.putAll(context.getDefaultHeaders());
            // Overwrite the default values with the current ones and/or add the current ones
            if (getHeaders() != null)
            {
                defaultHeaders.putAll(getHeaders());
            }
            // Assign default headers as headers for this request
            setHeaders(defaultHeaders);
        }

        if (this.getBody() == null)
        {
            setBody(context.getConfigItemByKey(Constants.BODY));
        }
        if (this.getEncodeBody() == null)
        {
            setEncodeBody(context.getConfigItemByKey(Constants.ENCODEBODY));
        }

    }

    /**
     * Tries to resolve all variables of non-null attributes. Variables are specified by "${variable}".
     * 
     * @param context
     *            The propertyManager with the DataStorage to use
     * @throws InvalidArgumentException
     */
    void resolveValues(final Context context) throws InvalidArgumentException
    {
        String resolvedValue;

        // Resolve Url
        resolvedValue = context.resolveString(getUrl());
        setUrl(resolvedValue);

        // Resolve Httpmethod
        resolvedValue = context.resolveString(getHttpMethod());

        setHttpMethod(resolvedValue);

        // Resolve (is)Xhr if it exists
        if (getXhr() != null)
        {
            resolvedValue = context.resolveString(getXhr());
            // confirm that the value is either false or true
            if (resolvedValue.equalsIgnoreCase("false") || resolvedValue.equalsIgnoreCase("true"))
            {
                setXhr(resolvedValue);
            }
            // if the value is neither false nor true, throw an exception
            else
            {
                throw new InvalidArgumentException("Xhr is neither true nor false");
            }
        }

        // Resolve (is)EncodeParameters if it exists
        if (getEncodeParameters() != null)
        {
            resolvedValue = context.resolveString(getEncodeParameters());
            // confirm that the value is either false or true
            if (resolvedValue.equalsIgnoreCase("false") || resolvedValue.equalsIgnoreCase("true"))
            {
                setEncodeParameters(resolvedValue);
            }
            // if the value is neither false nor true, throw an exception
            else
            {
                throw new InvalidArgumentException("Xhr is neither true nor false");
            }
        }

        // Resolve each parameter in parameters if they exist
        if (getParameters() != null && !getParameters().isEmpty())
        {
            final List<NameValuePair> resolvedParameters = new ArrayList<NameValuePair>();
            String resolvedParameterName, resolvedParameterValue;
            // Iterrate over the list
            for (final NameValuePair parameter : parameters)
            {
                // Resolve the name of the parameter
                resolvedParameterName = context.resolveString(parameter.getName());
                // Resolve the value of the parameter
                resolvedParameterValue = context.resolveString(parameter.getValue());
                // Add the resolved name and resolved value to the new parameter list
                resolvedParameters.add(new NameValuePair(resolvedParameterName, resolvedParameterValue));
            }
            // Reassign the parameter list to the resolved parameter list
            setParameters(resolvedParameters);
        }

        // Resolve each header in headers if they exists
        if (getHeaders() != null && !getHeaders().isEmpty())
        {
            // Make a new map
            final Map<String, String> resolvedHeaders = new HashMap<String, String>();
            // And insert the resolved name and key of the old map into the new one
            getHeaders().forEach((final String name, final String key) -> {
                resolvedHeaders.put(context.resolveString(name), context.resolveString(key));
            });
            // Reassign the header to its resolved values
            setHeaders(resolvedHeaders);
        }

        // Resolve (is)EncodeBody
        if (getEncodeBody() != null)
        {
            resolvedValue = context.resolveString(getEncodeBody());
            // confirm that the value is either false or true
            if (resolvedValue.equalsIgnoreCase("false") || resolvedValue.equalsIgnoreCase("true"))
            {
                setEncodeBody(resolvedValue);
            }
            // if the value is neither false nor true, throw an exception
            else
            {
                throw new InvalidArgumentException("Xhr is neither true nor false");
            }
        }

        // Resolve Body
        if (getBody() != null)
        {
            resolvedValue = context.resolveString(getBody());
            setBody(resolvedValue);
        }
    }

    /**
     * Builds the webRequest with the given context and stores it locally. Access it with getWebRequest()
     * 
     * @throws {@link
     *             IOException}
     * @throws {@link
     *             InvalidArgumentException}
     */
    @Override
    public void execute(final Context context) throws InvalidArgumentException, IOException
    {
        // fill in the default data if the attribute is not specified
        fillDefaultData(context);
        // Then resolve all variables
        resolveValues(context);

        if (this.url == null || this.url.isEmpty())
        {
            throw new InvalidArgumentException("Url is empty. Please set a default url or specify a url.");
        }

        setWebRequest(buildWebRequest());
        // Check that we have built the webRequest
        if (getWebRequest() != null)
        {
            // load the response
            final WebResponse webResponse = context.getWebClient().loadWebResponse(getWebRequest());
            context.setWebResponse(webResponse);
        }
    }

    /**
     * Builds the web request that is specified by this instance
     * 
     * @param context
     *            The property manager that has the data storage
     * @return WebRequest - The WebRequest that is generated based on the instance
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     */
    public WebRequest buildWebRequest() throws MalformedURLException, InvalidArgumentException, UnsupportedEncodingException
    {
        final URL url = new URL(this.url);
        final WebRequest webRequest = new WebRequest(url, HttpMethod.valueOf(getHttpMethod()));

        if (getXhr() != null && Boolean.valueOf(getXhr()))
        {
            webRequest.setXHR();
        }

        if (getHeaders() != null)
        {
            webRequest.setAdditionalHeaders(headers);
        }

        if (getParameters() != null && !getParameters().isEmpty())
        {
            if (getEncodeParameters() != null && !Boolean.valueOf(getEncodeParameters()))
            {
                decodeParameters();
            }
            webRequest.setRequestParameters(parameters);
        }

        // Set Body if specified and no parameters are set
        if (getBody() != null && !getBody().isEmpty() && (getParameters() == null || getParameters().isEmpty()))
        {
            if (getEncodeBody() != null && !Boolean.valueOf(getEncodeBody()))
            {
                decodeBody();
            }
            webRequest.setRequestBody(body);
        }
        return webRequest;
    }

    private void decodeBody() throws UnsupportedEncodingException
    {
        setBody(URLDecoder.decode(getBody(), "UTF-8"));

    }

    private void decodeParameters() throws UnsupportedEncodingException
    {
        final List<NameValuePair> decodedParameters = new ArrayList<>();
        for (final NameValuePair parameter : getParameters())
        {
            final String decodedName = parameter.getName() != null ? URLDecoder.decode(parameter.getName(), "UTF-8") : null;
            final String decodedValue = parameter.getValue() != null ? URLDecoder.decode(parameter.getValue(), "UTF-8") : null;

            final NameValuePair decodedParameter = new NameValuePair(decodedName, decodedValue);

            decodedParameters.add(decodedParameter);

        }
        setParameters(decodedParameters);
    }

    /*
     * Provides the url and method used
     */
    public String toSimpleDebugString()
    {
        final String output = "Request-URL: " + url + " with Method." + httpMethod;

        return output;
    }

}
