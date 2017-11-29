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

import org.openqa.selenium.InvalidArgumentException;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.engine.XltWebClient;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.Context;
import com.xceptance.xlt.nocoding.util.RecentKeyTreeMap;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;
import com.xceptance.xlt.nocoding.util.variableResolver.VariableResolver;

/**
 * Describes a HTTP Request, that gets transformed to a {@link WebRequest} and then sent with the {@link XltWebClient}.
 * 
 * @author ckeiner
 */
public class Request extends AbstractActionItem
{
    /**
     * The URL as {@link String}
     */
    private String url;

    /**
     * The HttpMethod for the {@link WebRequest}. Defaults to "GET"
     */
    private String httpMethod;

    /**
     * Sets the {@link WebRequest} as Xhr request. Defaults to "false"
     */
    private String xhr;

    /**
     * Defines if the parameters are encoded. Defaults to "false"
     */
    private String encodeParameters;

    /**
     * The list of the parameters
     */
    private List<NameValuePair> parameters;

    /**
     * A map that defines the headers
     */
    private Map<String, String> headers;

    /**
     * The body of the {@link WebRequest}
     */
    private String body;

    /**
     * Defines if the body is encoded. Defaults to "false"
     */
    private String encodeBody;

    /**
     * The {@link WebRequest} defined by the instance
     */
    private WebRequest webRequest;

    /**
     * Creates an instance of {@link Request}, that sets {@link #url}.
     * 
     * @param url
     *            The URL as {@link String}.
     */
    public Request(final String url)
    {
        this.url = url;
        parameters = new ArrayList<NameValuePair>();
        headers = new RecentKeyTreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
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

    /**
     * Create a {@link RecentKeyTreeMap} out of the specified map, so headers are treated case insensitive.
     * 
     * @param headers
     *            The map with the headers
     */
    public void setHeaders(final Map<String, String> headers)
    {
        if (headers != null && !headers.isEmpty())
        {
            this.headers = new RecentKeyTreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
            this.headers.putAll(headers);
        }
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
     * Fills in the default values for all attributes if the attribute isn't specified
     * 
     * @param context
     *            The {@link Context} with the {@link DataStorage}
     */
    void fillDefaultData(final Context context)
    {
        // Set default URL if it isn't specified
        if (getUrl() == null || getUrl().isEmpty())
        {
            setUrl(context.getConfigItemByKey(Constants.URL));
        }

        // Set default HttpMethod if it isn't specified
        if (this.getHttpMethod() == null)
        {
            setHttpMethod(context.getConfigItemByKey(Constants.METHOD));
        }

        // Set default Xhr if it isn't specified
        if (this.getXhr() == null)
        {
            setXhr(context.getConfigItemByKey(Constants.XHR));
        }

        // Set default encodeParameters if it isn't specified
        if (this.getEncodeParameters() == null)
        {
            setEncodeParameters(context.getConfigItemByKey(Constants.ENCODEPARAMETERS));
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
            // Add all parameters from the map to the list newParameters
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
            final RecentKeyTreeMap<String, String> defaultHeaders = new RecentKeyTreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
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

        // Set default body if it isn't specified
        if (this.getBody() == null)
        {
            setBody(context.getConfigItemByKey(Constants.BODY));
        }

        // Set default encodeBody if it isn't specified
        if (this.getEncodeBody() == null)
        {
            setEncodeBody(context.getConfigItemByKey(Constants.ENCODEBODY));
        }

    }

    /**
     * Tries to resolve all variables of non-null attributes
     * 
     * @param context
     *            The {@link Context} with the {@link DataStorage}
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
     * Builds the {@link WebRequest} with the given {@link Context} and sends the {@link WebRequest} with
     * {@link Context#getWebClient()}. Finally, it stores the {@link WebResponse} with
     * {@link Context#setWebResponse(WebResponse)}.
     * 
     * @param context
     *            The {@link Context} with the {@link DataStorage}, {@link VariableResolver} and {@link XltWebClient}
     * @throws IOException
     * @throws InvalidArgumentException
     */
    @Override
    public void execute(final Context context) throws InvalidArgumentException, IOException
    {
        // fill in the default data if the attribute is not specified
        fillDefaultData(context);
        // Then resolve all variables
        resolveValues(context);

        // Throw an error if the url is null or empty
        if (this.url == null || this.url.isEmpty())
        {
            throw new InvalidArgumentException("Url is empty. Please set a default url or specify a url.");
        }

        // Build the WebRequest and set it
        setWebRequest(buildWebRequest());
        // Check that we have built the webRequest
        if (getWebRequest() != null)
        {
            // load the response
            final WebResponse webResponse = context.getWebClient().loadWebResponse(getWebRequest());
            // And set it in the context
            context.setWebResponse(webResponse);
        }
    }

    /**
     * Builds the web request that is specified by this object
     * 
     * @return The {@link WebRequest} that is specified by this object
     * @throws MalformedURLException
     * @throws InvalidArgumentException
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
