package com.xceptance.xlt.nocoding.scriptItem.action;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.util.DefaultValue;
import com.xceptance.xlt.nocoding.util.PropertyManager;

public class Request
{
    private final String name;

    private final String urlAsString;

    private HttpMethod method;

    private Boolean isXhr;

    private Boolean encodeParameters;

    private List<NameValuePair> parameters;

    private Map<String, String> headers;

    private String body;

    private Boolean encodeBody;

    public Request(final String url, final String name)
    {
        this.urlAsString = url;
        this.name = name;
    }

    public HttpMethod getMethod()
    {
        return method;
    }

    public void setMethod(final HttpMethod method)
    {
        this.method = method;
    }

    public Boolean isXhr()
    {
        return isXhr;
    }

    public void setXhr(final boolean isXhr)
    {
        this.isXhr = isXhr;
    }

    public Boolean isEncodeParameters()
    {
        return encodeParameters;
    }

    public void setEncodeParameters(final boolean encodeParameters)
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

    public Boolean isEncodeBody()
    {
        return encodeBody;
    }

    public void setEncodeBody(final boolean encodeBody)
    {
        this.encodeBody = encodeBody;
    }

    public String getUrl()
    {
        return urlAsString;
    }

    public String getName()
    {
        return name;
    }

    /**
     * Fills in the default values for each attribute
     */
    public void fillData(final PropertyManager propertyManager)
    {
        final DefaultValue defaultValues = propertyManager.getDefaultValues();
        if (this.getMethod() == null)
        {
            this.method = defaultValues.METHOD;
        }
        if (this.isXhr == null)
        {
            this.isXhr = defaultValues.IS_XHR;
        }
        if (this.encodeParameters == null)
        {
            this.encodeParameters = defaultValues.ENCODE_PARAMETERS;
        }
        if (this.encodeBody == null)
        {
            this.encodeBody = defaultValues.ENCODE_BODY;
        }
    }

    public WebRequest buildWebRequest() throws MalformedURLException
    {
        if (isEncodeBody() != null && isEncodeBody())
        {
            encodeBody();
        }

        if (isEncodeParameters() != null && isEncodeParameters())
        {
            encodeParameters();
        }

        final URL url = new URL(this.urlAsString);
        final WebRequest webRequest = new WebRequest(url, this.method);

        if (isXhr() != null && isXhr())
        {
            webRequest.setXHR();
        }

        if (getHeaders() != null)
        {
            webRequest.setAdditionalHeaders(headers);
        }

        if (getMethod() == HttpMethod.POST || getMethod() == HttpMethod.PUT || getMethod() == HttpMethod.PATCH)
        {
            webRequest.setRequestBody(body);
        }
        else
        {
            // TODO hier vielleicht lieber weglassen und im parser dafür ne leere liste erzeugen
            if (parameters != null)
            {
                webRequest.setRequestParameters(parameters);
            }
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
