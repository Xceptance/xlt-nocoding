package com.xceptance.xlt.nocoding.scriptItem.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.InvalidArgumentException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import com.xceptance.xlt.engine.XltWebClient;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.RecentKeyTreeMap;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;
import com.xceptance.xlt.nocoding.util.variableResolver.VariableResolver;

/**
 * Describes a HTTP Request, that gets transformed to a {@link WebRequest} and then sent via the {@link XltWebClient}.
 * 
 * @author ckeiner
 */
public class Request extends AbstractActionItem
{
    /**
     * The URL as String
     */
    private String url;

    /**
     * The HttpMethod for the WebRequest. Defaults to "GET"
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
     * The list of the parameters
     */
    private List<NameValuePair> parameters;

    /**
     * The list of cookies
     */
    private Map<String, String> cookies;

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
        cookies = new LinkedHashMap<String, String>();
        headers = new RecentKeyTreeMap();
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

    public Map<String, String> getCookies()
    {
        return cookies;
    }

    public void setCookies(final Map<String, String> cookies)
    {
        this.cookies = cookies;
    }

    public Map<String, String> getHeaders()
    {
        return headers;
    }

    /**
     * Create a {@link RecentKeyTreeMap} out of the specified map, so headers are always treated case insensitive.
     * 
     * @param headers
     *            The map with the headers
     */
    public void setHeaders(final Map<String, String> headers)
    {
        if (headers != null && !headers.isEmpty())
        {
            this.headers = new RecentKeyTreeMap();
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
     * Fills in the default values for all unspecified attributes.<br>
     * Note, that default headers and cookies are already set at the WebClient..
     * 
     * @param context
     *            The {@link Context} with the {@link DataStorage}
     */
    void fillDefaultData(final Context<?> context)
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

        if (context.getDefaultParameters() != null && !context.getDefaultParameters().getItems().isEmpty())
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
     *            The {@link Context} with the {@link VariableResolver}
     * @throws InvalidArgumentException
     */
    void resolveValues(final Context<?> context) throws InvalidArgumentException
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
     * Builds the {@link WebRequest} with the given {@link Context} and sends the <code>WebRequest</code>. Finally, it
     * stores the corresponding {@link WebResponse} with {@link Context#setWebResponse(WebResponse)}.
     * 
     * @param context
     *            The {@link Context} with the {@link DataStorage}, {@link VariableResolver} and {@link XltWebClient}
     * @throws IOException
     *             if building of sending the <code>WebRequest</code> fails
     * @throws FailingHttpStatusCodeException
     */
    @Override
    public void execute(final Context<?> context) throws FailingHttpStatusCodeException, IOException
    {
        // Fill in the default data if the attribute is not specified
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
            // Load the webResponse
            context.loadWebResponse(webRequest);
        }
    }

    /**
     * Sets the cookies at the web client
     * 
     * @param context
     *            The current {@link Context}
     * @throws MalformedURLException
     *             If {@link #url} of this request cannot be transformed to a URL
     */
    private void setCookiesAtWebClient(final Context<?> context) throws MalformedURLException
    {
        if (getCookies() != null && !getCookies().isEmpty())
        {
            final URL url = new URL(this.getUrl());
            cookies.forEach((key, value) -> {
                context.getWebClient().addCookie(key + "=" + value, url, this);
            });
        }
    }

    /**
     * Builds the {@link WebRequest} that is specified by this object
     * 
     * @param context
     *            The current {@link Context}
     * @return The <code>WebRequest</code> that is specified by this object
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     */
    WebRequest buildWebRequest(final Context<?> context) throws MalformedURLException, UnsupportedEncodingException
    {
        // Create a URL object
        final URL url = new URL(this.url);
        // Create a WebRequest
        final WebRequest webRequest = new WebRequest(url, HttpMethod.valueOf(getHttpMethod()));
        // Set headers if they aren't null or empty
        if (getHeaders() != null || !getHeaders().isEmpty())
        {
            final RecentKeyTreeMap headerExchange = new RecentKeyTreeMap();
            headerExchange.putAll(webRequest.getAdditionalHeaders());
            headerExchange.putAll(headers);
            webRequest.setAdditionalHeaders(headerExchange);
        }
        // Set Xhr if it is specified and can be converted to a boolean
        if (getXhr() != null && Boolean.valueOf(getXhr()))
        {
            webRequest.setXHR();

            // Set XHR headers
            webRequest.getAdditionalHeaders().put("X-Requested-With", "XMLHttpRequest");
            // If there was a previous WebResponse, set the Url als Referer
            if (context.getWebResponse() != null)
            {
                // Get the url of the old webResponse
                webRequest.getAdditionalHeaders().put("Referer", context.getWebResponse().getWebRequest().getUrl().toString());
            }
        }

        // Set parameters if they aren't null or empty
        if (getParameters() != null && !getParameters().isEmpty())
        {
            // Decode parameters if they are specified and set to "true"
            if (getEncodeParameters() != null && !Boolean.valueOf(getEncodeParameters()))
            {
                decodeParameters();
            }
            handleParameters(webRequest, parameters);
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

        // Sets the cookies at the web client
        setCookiesAtWebClient(context);

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

    /**
     * Provides the url and method used
     */
    public String toSimpleDebugString()
    {
        final String output = "Request-URL: " + url + " with Method." + httpMethod;

        return output;
    }

    private void handleParameters(final WebRequest webRequest, final List<NameValuePair> parameters) throws MalformedURLException
    {
        final HttpMethod method = webRequest.getHttpMethod();
        if (parameters != null && !parameters.isEmpty())
        {
            // POST as well as PUT and PATCH
            if (method.equals(HttpMethod.POST) || method.equals(HttpMethod.PUT) || method.equals(HttpMethod.PATCH))
            {
                webRequest.setRequestParameters(parameters);
            }
            else
            {
                final URL url = buildNewUrl(webRequest.getUrl(), parameters);
                webRequest.setUrl(url);
            }
        }
    }

    private static URL buildNewUrl(final URL url, final List<NameValuePair> parameters) throws MalformedURLException
    {
        URL newUrl = url;
        if (parameters != null && !parameters.isEmpty())
        {
            final String query = url.getQuery();
            String parameterToQuery = "";

            // Add query and "&" to the url
            if (query != null)
            {
                parameterToQuery += query + "&";
            }
            for (final NameValuePair parameter : parameters)
            {
                parameterToQuery += parameter.getName() + "=" + parameter.getValue() + "&";
            }
            parameterToQuery = parameterToQuery.substring(0, parameterToQuery.lastIndexOf("&"));
            newUrl = UrlUtils.getUrlWithNewQuery(url, parameterToQuery);
        }
        return newUrl;
    }

}
