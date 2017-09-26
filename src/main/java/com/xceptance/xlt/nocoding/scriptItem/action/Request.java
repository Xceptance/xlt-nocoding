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
import com.xceptance.xlt.nocoding.util.PropertyManager;
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
     * The HttpMethod for the webrequest
     */
    private String method;

    /**
     * Sets the WebRequest as Xhr request
     */
    private String Xhr;

    private String encodeParameters;

    private List<NameValuePair> parameters;

    private Map<String, String> headers;

    private String body;

    private String encodeBody;

    /**
     * Simple constructor so we do not have to build a variable map when we want to fire basic requests
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
     * @param propertyManager
     *            The propertyManager with the DataStorage
     */
    private void fillDefaultData(final PropertyManager propertyManager)
    {
        final DataStorage globalStorage = propertyManager.getDataStorage();
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
        if (this.getEncodeBody() == null)
        {
            setEncodeBody(globalStorage.getConfigItemByKey(Constants.ENCODEBODY));
            // this.encodeBody = Boolean.valueOf(encodeBody);
        }
    }

    /**
     * Tries to resolve all variables of non-null attributes. Variables are specified with by "${variable}".
     * 
     * @param propertyManager
     *            The propertyManager with the DataStorage to use
     * @throws InvalidArgumentException
     */
    private void resolveValues(final PropertyManager propertyManager) throws InvalidArgumentException
    {
        // Resolve name
        String resolvedValue = propertyManager.resolveString(getName());
        setName(resolvedValue);

        // Resolve Url
        resolvedValue = propertyManager.resolveString(getUrlAsString());
        setUrlAsString(resolvedValue);

        // Resolve Httpmethod
        resolvedValue = propertyManager.resolveString(getMethod());

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
            resolvedValue = propertyManager.resolveString(getXhr());
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
            resolvedValue = propertyManager.resolveString(getEncodeParameters());
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
                resolvedParameterName = propertyManager.resolveString(parameter.getName());
                // Resolve the value of the parameter
                resolvedParameterValue = propertyManager.resolveString(parameter.getValue());
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
                resolvedHeaders.put(propertyManager.resolveString(name), propertyManager.resolveString(key));
            });
            // Reassign the header to its resolved values
            setHeaders(resolvedHeaders);
        }

        // Resolve (is)EncodeBody
        if (getEncodeBody() != null)
        {
            resolvedValue = propertyManager.resolveString(getEncodeBody());
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
            resolvedValue = propertyManager.resolveString(getBody());
            setBody(resolvedValue);
        }
    }

    /**
     * Builds the web request that is specified by this instance
     * 
     * @param propertyManager
     *            The property manager that has the data storage
     * @return WebRequest - The WebRequest that is generated based on the instance
     * @throws MalformedURLException
     */
    public WebRequest buildWebRequest(final PropertyManager propertyManager) throws MalformedURLException, InvalidArgumentException
    {
        // fill in the default data if the attribute is not specified
        fillDefaultData(propertyManager);
        // Then resolve all variables
        resolveValues(propertyManager);

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

    // private void handleParameters()
    // {
    // String parametersAsString = "";
    // final Iterator<NameValuePair> iterator = parameters.iterator();
    // NameValuePair next;
    // while (iterator.hasNext())
    // {
    // // TODO nach helpern dafür suchen, die das lösen
    // next = iterator.next();
    // if (iterator.hasNext())
    // {
    // parametersAsString += next.getName() + "=" + next.getValue() + "&";
    // }
    // else
    // {
    // parametersAsString += next.getName() + "=" + next.getValue();
    // }
    // }
    // if (this.method == HttpMethod.POST)
    // {
    // body = parametersAsString;
    // }
    // else
    // {
    // this.urlAsString += "?" + parametersAsString;
    // }
    // }

    private void encodeParameters()
    {
        // TODO encode Parameters
        // String urlString = url.toString();
        // urlString = StringUtils.replace(urlString, "&amp;", "&");
        // URL newUrl = new URL(urlString);
    }

}
