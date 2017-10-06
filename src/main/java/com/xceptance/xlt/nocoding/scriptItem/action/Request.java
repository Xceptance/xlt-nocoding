package com.xceptance.xlt.nocoding.scriptItem.action;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.InvalidArgumentException;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.Context;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;

public class Request
{
    /**
     * The name of the WebAction
     */
    private String name;

    /**
     * The URL of the website as String
     */
    private String urlAsString;

    /**
     * The HttpMethod for the webrequest. Defaults to "GET"
     */
    private String method;

    /**
     * Sets the WebRequest as Xhr request. Defaults to "false"
     */
    private String Xhr;

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
     * Creates a class with the minimum of information, that is the URL and the name of the Action
     * 
     * @param url
     *            The URL of the website
     * @param name
     *            The name of the action
     */
    public Request(final String url, final String name)
    {
        this.name = name;
        this.urlAsString = url;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getUrlAsString()
    {
        return urlAsString;
    }

    public void setUrlAsString(final String urlAsString)
    {
        this.urlAsString = urlAsString;
    }

    public String getMethod()
    {
        return method;
    }

    public void setMethod(final String method)
    {
        this.method = method;
    }

    public String getXhr()
    {
        return Xhr;
    }

    public void setXhr(final String xhr)
    {
        Xhr = xhr;
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

    /**
     * Fills in the default values for certain attributes
     * 
     * @param context
     *            The propertyManager with the DataStorage
     */
    private void fillDefaultData(final Context context)
    {
        final DataStorage globalStorage = context.getDataStorage();
        // if the name is null, check if it has a default value
        if (this.getMethod() == null)
        {
            setMethod(globalStorage.getConfigItemByKey(Constants.METHOD));
            // this.httpmethod = HttpMethod.valueOf(method);
        }
        if (this.getXhr() == null)
        {
            setXhr(globalStorage.getConfigItemByKey(Constants.XHR));
            // this.isXhr = Boolean.valueOf(isXhr);
        }
        if (this.getEncodeParameters() == null)
        {
            setEncodeParameters(globalStorage.getConfigItemByKey(Constants.ENCODEPARAMETERS));
            // this.encodeParameters = Boolean.valueOf(encodeParameters);
        }
        // TODO Meeting
        if (this.getParameters() == null || (this.getParameters() != null && this.getParameters().isEmpty()))
        {
            // We need to find all default parameters that are for a request
            // Therefore, we have to specify a format in which parameters are saved
            // The count we are currently at
            Integer counter = 0;
            // The configKey we need to use for the name
            final String parameterName = "parameter_name_";
            // The configKey we need to use for the value
            final String parameterValueName = "parameter_value_";
            String currentName = globalStorage.getConfigItemByKey(parameterName + counter.toString());
            String currentValue = globalStorage.getConfigItemByKey(parameterValueName + counter.toString());
            while (currentName != null && currentValue != null)
            {
                // Add the currentName and currentValue to the parameters
                getParameters().add(new NameValuePair(currentName, currentValue));
                // Increment our counter
                counter++;
                // Set the new currentName
                currentName = globalStorage.getConfigItemByKey(parameterName + counter.toString());
                // Set the new currentValue
                currentValue = globalStorage.getConfigItemByKey(parameterValueName + counter.toString());
            }
        }

        if (this.getHeaders() == null || (this.getHeaders() != null && this.getHeaders().isEmpty()))
        {
            // Do something with the headers
        }
        if (this.getBody() == null)
        {
            setBody(globalStorage.getConfigItemByKey(Constants.BODY));
        }
        if (this.getEncodeBody() == null)
        {
            setEncodeBody(globalStorage.getConfigItemByKey(Constants.ENCODEBODY));
            // this.encodeBody = Boolean.valueOf(encodeBody);
        }
    }

    /**
     * Tries to resolve all variables of non-null attributes. Variables are specified by "${variable}".
     * 
     * @param context
     *            The propertyManager with the DataStorage to use
     * @throws InvalidArgumentException
     */
    private void resolveValues(final Context context) throws InvalidArgumentException
    {
        // Resolve name
        String resolvedValue = context.resolveString(getName());
        setName(resolvedValue);

        // Resolve Url
        resolvedValue = context.resolveString(getUrlAsString());
        setUrlAsString(resolvedValue);

        // Resolve Httpmethod
        resolvedValue = context.resolveString(getMethod());

        // TODO [MEETING] In alter Suite wird darauf nicht geachtet, siehe auch weiter unten
        if (HttpMethod.valueOf(resolvedValue) != null)
        {
            setMethod(resolvedValue);
        }
        else
        {
            throw new InvalidArgumentException("Httpmethod unknown");
        }

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
     * Builds the web request that is specified by this instance
     * 
     * @param context
     *            The property manager that has the data storage
     * @return WebRequest - The WebRequest that is generated based on the instance
     * @throws MalformedURLException
     */
    public WebRequest buildWebRequest(final Context context) throws MalformedURLException, InvalidArgumentException
    {
        // fill in the default data if the attribute is not specified
        fillDefaultData(context);
        // Then resolve all variables
        resolveValues(context);

        // Finally build the webRequest
        if (getEncodeBody() != null && Boolean.valueOf(getEncodeBody()))
        {
            encodeBody();
        }

        if (getEncodeParameters() != null && Boolean.valueOf(getEncodeParameters()))
        {
            encodeParameters();
        }

        final URL url = new URL(this.urlAsString);
        final WebRequest webRequest = new WebRequest(url, HttpMethod.valueOf(getMethod()));

        if (getXhr() != null && Boolean.valueOf(getXhr()))
        {
            webRequest.setXHR();
        }

        if (getHeaders() != null)
        {
            webRequest.setAdditionalHeaders(headers);
        }

        if (getParameters() != null)
        {
            webRequest.setRequestParameters(parameters);
        }
        return webRequest;
    }

    private void encodeBody()
    {
        // TODO encode body

    }

    private void encodeParameters()
    {
        // TODO encode Parameters
        // String urlString = url.toString();
        // urlString = StringUtils.replace(urlString, "&amp;", "&");
        // URL newUrl = new URL(urlString);
    }

}
