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
     * The list of cookies
     */
    private List<NameValuePair> cookies;

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
     * Creates an instance of {@link Request}, that sets {@link #url} to null.
     */
    public Request()
    {
        this(null);
    }

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
        cookies = new ArrayList<NameValuePair>();
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

    public List<NameValuePair> getCookies()
    {
        return cookies;
    }

    public void setCookies(final List<NameValuePair> cookies)
    {
        this.cookies = cookies;
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
            setUrl(context.getDefaultItems().get(Constants.URL));
        }

        // Set default HttpMethod if it isn't specified
        if (this.getHttpMethod() == null)
        {
            setHttpMethod(context.getDefaultItems().get(Constants.METHOD));
        }

        // Set default Xhr if it isn't specified
        if (this.getXhr() == null)
        {
            setXhr(context.getDefaultItems().get(Constants.XHR));
        }

        // Set default encodeParameters if it isn't specified
        if (this.getEncodeParameters() == null)
        {
            setEncodeParameters(context.getDefaultItems().get(Constants.ENCODEPARAMETERS));
        }

        /*
         * Set default parameters
         */

        if (context.getDefaultParameters() != null)
        {
            // Get default parameters
            final List<NameValuePair> defaultParameters = context.getDefaultParameters().getItems();
            // Overwrite the default values with the current ones or add the current ones
            if (getParameters() != null)
            {
                for (final NameValuePair defaultParameter : defaultParameters)
                {
                    if (!getParameters().contains(defaultParameter))
                    {
                        getParameters().add(defaultParameter);
                    }
                }
            }
        }

        /*
         * Set default cookies
         */

        if (context.getDefaultParameters() != null)
        {
            // Get default cookies
            final List<NameValuePair> defaultCookies = context.getDefaultCookies().getItems();
            // Overwrite the default values with the current ones or add the current ones
            if (getCookies() != null)
            {
                for (final NameValuePair defaultCookie : defaultCookies)
                {
                    if (!getCookies().contains(defaultCookie))
                    {
                        getCookies().add(defaultCookie);
                    }
                }
            }
        }

        /*
         * Set default headers
         */

        if (context.getDefaultHeaders() != null)
        {
            // Create a tree map that is case insensitive (since headers are case insensitive
            final RecentKeyTreeMap<String, String> defaultHeaders = new RecentKeyTreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
            // Get the default headers
            defaultHeaders.putAll(context.getDefaultHeaders().getItems());
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
            setBody(context.getDefaultItems().get(Constants.BODY));
        }

        // Set default encodeBody if it isn't specified
        if (this.getEncodeBody() == null)
        {
            setEncodeBody(context.getDefaultItems().get(Constants.ENCODEBODY));
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

        // Resolve Xhr if it isn't null
        if (getXhr() != null)
        {
            resolvedValue = context.resolveString(getXhr());
            setXhr(resolvedValue);
        }

        // Resolve encodeParameters if it isn't null
        if (getEncodeParameters() != null)
        {
            resolvedValue = context.resolveString(getEncodeParameters());
            setEncodeParameters(resolvedValue);
        }

        // Resolve each parameter in parameters if is neither null nor empty
        if (getParameters() != null && !getParameters().isEmpty())
        {
            final List<NameValuePair> resolvedParameters = new ArrayList<NameValuePair>();
            String resolvedParameterName, resolvedParameterValue;
            // Iterate over the list
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

        // Resolve each header in headers if is neither null nor empty
        if (getHeaders() != null && !getHeaders().isEmpty())
        {
            // Create a new map
            final Map<String, String> resolvedHeaders = new HashMap<String, String>();
            // And insert the resolved key and value of the old map into the new one
            getHeaders().forEach((final String key, final String value) -> {
                resolvedHeaders.put(context.resolveString(key), context.resolveString(value));
            });
            // Reassign the header to its resolved values
            setHeaders(resolvedHeaders);
        }

        // Resolve encodeBody if it isn't null
        if (getEncodeBody() != null)
        {
            resolvedValue = context.resolveString(getEncodeBody());
            setEncodeBody(resolvedValue);
        }

        // Resolve Body if it isn't null
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

        // Build the WebRequest
        final WebRequest webRequest = buildWebRequest(context);

        // Check that the webRequest isn't null
        if (webRequest != null)
        {
            // Load the response
            final WebResponse webResponse = context.getWebClient().loadWebResponse(webRequest);
            // And set it in the context
            context.setWebResponse(webResponse);
        }
    }

    /**
     * Builds the web request that is specified by this object
     * 
     * @param context
     *            The current {@link Context}
     * @return The {@link WebRequest} that is specified by this object
     * @throws MalformedURLException
     * @throws InvalidArgumentException
     * @throws UnsupportedEncodingException
     */
    WebRequest buildWebRequest(final Context context) throws MalformedURLException, InvalidArgumentException, UnsupportedEncodingException
    {
        // Create a URL object
        final URL url = new URL(this.url);
        // Create a WebRequest
        final WebRequest webRequest = new WebRequest(url, HttpMethod.valueOf(getHttpMethod()));

        // Set Xhr if it is specified and can be converted to a boolean
        if (getXhr() != null && Boolean.valueOf(getXhr()))
        {
            webRequest.setXHR();
        }

        // Set headers if they aren't null or empty
        if (getHeaders() != null || !getHeaders().isEmpty())
        {
            webRequest.setAdditionalHeaders(headers);
        }

        // Set parameters if they aren't null or empty
        if (getParameters() != null && !getParameters().isEmpty())
        {
            // Decode parameters if they are specified and set to "true"
            if (getEncodeParameters() != null && !Boolean.valueOf(getEncodeParameters()))
            {
                decodeParameters();
            }
            webRequest.setRequestParameters(parameters);
        }

        // Set Body if specified and no parameters are set
        if (getBody() != null && !getBody().isEmpty() && (getParameters() == null || getParameters().isEmpty()))
        {
            // Decode Body if they are specified and set to "true"
            if (getEncodeBody() != null && !Boolean.valueOf(getEncodeBody()))
            {
                decodeBody();
            }
            webRequest.setRequestBody(body);
        }

        // Set cookies
        if (getCookies() != null && !getCookies().isEmpty())
        {
            // Get already defined cookies
            String cookieString = webRequest.getAdditionalHeaders().get(Constants.COOKIE);
            // If there are already cookies
            if (cookieString != null)
            {
                // Make sure there is a semicolon at the end
                if (!cookieString.endsWith(";"))
                {
                    cookieString += ";";
                }
            }
            // Else, make the string empty
            else
            {
                cookieString = "";
            }
            // Append all cookies at the end
            for (final NameValuePair cookie : cookies)
            {
                cookieString += cookie.getName() + "=" + cookie.getValue() + ";";
            }
            webRequest.setAdditionalHeader(Constants.COOKIE, cookieString);
        }

        // Return the webRequest
        return webRequest;
    }

    /**
     * Decodes the body.
     * 
     * @throws UnsupportedEncodingException
     */
    private void decodeBody() throws UnsupportedEncodingException
    {
        setBody(URLDecoder.decode(getBody(), "UTF-8"));

    }

    /**
     * Decodes each parameter in {@link #parameters}.
     * 
     * @throws UnsupportedEncodingException
     */
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
