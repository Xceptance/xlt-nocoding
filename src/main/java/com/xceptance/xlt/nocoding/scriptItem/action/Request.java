package com.xceptance.xlt.nocoding.scriptItem.action;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

public class Request
{
    private String urlAsString;

    private HttpMethod method;

    private boolean isXhr;

    private boolean encodeParameters;

    private List<NameValuePair> parameters;

    private Map<String, String> headers;

    private String body;

    private boolean encodeBody;

    public Request(final String url)
    {
        this.urlAsString = url;
    }

    public HttpMethod getMethod()
    {
        return method;
    }

    public void setMethod(final HttpMethod method)
    {
        this.method = method;
    }

    public boolean isXhr()
    {
        return isXhr;
    }

    public void setXhr(final boolean isXhr)
    {
        this.isXhr = isXhr;
    }

    public boolean isEncodeParameters()
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

    public boolean isEncodeBody()
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

    public WebRequest buildWebRequest() throws MalformedURLException
    {
        if (getParameters() != null)
        {
            if (isEncodeParameters())
            {
                encodeParameters();
            }
            handleParameters();
        }
        if (isEncodeBody())
        {
            encodeBody();
        }
        // TODO Encode URL?

        final URL url = new URL(this.urlAsString);
        final WebRequest webRequest = new WebRequest(url, this.method);

        if (isXhr())
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
        return webRequest;
    }

    private void encodeBody()
    {
        // TODO encode body

    }

    private void handleParameters()
    {
        String parametersAsString = "";
        final Iterator<NameValuePair> iterator = parameters.iterator();
        NameValuePair next;
        while (iterator.hasNext())
        {
            next = iterator.next();
            if (iterator.hasNext())
            {
                parametersAsString += next.getName() + "=" + next.getValue() + "&";
            }
            else
            {
                parametersAsString += next.getName() + "=" + next.getValue();
            }
        }
        if (this.method == HttpMethod.POST)
        {
            body = body + parametersAsString;
        }
        else
        {
            this.urlAsString += "?" + parametersAsString;
        }
    }

    private void encodeParameters()
    {
        // TODO encode Parameters
        // String urlString = url.toString();
        // urlString = StringUtils.replace(urlString, "&amp;", "&");
        // URL newUrl = new URL(urlString);
    }

}
