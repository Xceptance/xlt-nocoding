package com.xceptance.xlt.nocoding.scriptItem.action;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.PropertyManager;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;

public class Request
{

    private String name;

    private String urlAsString;

    private HttpMethod httpmethod;

    private Boolean isXhr;

    private Boolean encodeParameters;

    private List<NameValuePair> parameters;

    private Map<String, String> headers;

    private String body;

    private Boolean encodeBody;

    public Map<String, String> variables;

    public Request(final Map<String, String> variables)
    {
        this.variables = variables;
    }

    public Request(final String url, final String name)
    {
        this.urlAsString = url;
        this.name = name;
        this.variables = new HashMap<String, String>();
    }

    public HttpMethod getHttpmethod()
    {
        return httpmethod;
    }

    public void setHttpmethod(final HttpMethod httpmethod)
    {
        this.httpmethod = httpmethod;
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

    public Map<String, String> getVariables()
    {
        return variables;
    }

    public void setVariables(final Map<String, String> variables)
    {
        this.variables = variables;
    }

    /**
     * Fills in the default values for each attribute
     */
    private void fillDefaultData(final PropertyManager propertyManager)
    {
        final DataStorage globalStorage = propertyManager.getDataStorage();
        if (this.getHttpmethod() == null)
        {
            // final String method = globalStorage.getConfigItemByKey(Constants.METHOD);
            variables.put(Constants.METHOD, globalStorage.getConfigItemByKey(Constants.METHOD));
            // this.httpmethod = HttpMethod.valueOf(method);

        }
        if (this.isXhr == null)
        {
            variables.put(Constants.XHR, globalStorage.getConfigItemByKey(Constants.XHR));
            // final String isXhr = globalStorage.getConfigItemByKey(Constants.XHR);
            // this.isXhr = Boolean.valueOf(isXhr);
        }
        if (this.encodeParameters == null)
        {
            variables.put(Constants.ENCODEPARAMETERS, globalStorage.getConfigItemByKey(Constants.ENCODEPARAMETERS));
            // final String encodeParameters = globalStorage.getConfigItemByKey(Constants.ENCODEPARAMETERS);
            // this.encodeParameters = Boolean.valueOf(encodeParameters);
        }
        if (this.encodeBody == null)
        {
            variables.put(Constants.ENCODEBODY, globalStorage.getConfigItemByKey(Constants.ENCODEBODY));
            // final String encodeBody = globalStorage.getConfigItemByKey(Constants.ENCODEBODY);
            // this.encodeBody = Boolean.valueOf(encodeBody);
        }
    }

    private void resolveValues(final PropertyManager propertyManager)
    {
        // TODO Test these very throughly
        String variable = propertyManager.resolveString(variables.get(Constants.NAME));
        name = variable;
        variable = propertyManager.resolveString(variables.get(Constants.URL));
        urlAsString = variable;
        variable = propertyManager.resolveString(variables.get(Constants.METHOD));
        httpmethod = HttpMethod.valueOf(variable);
        variable = propertyManager.resolveString(variables.get(Constants.XHR));
        isXhr = Boolean.valueOf(variable);
        variable = propertyManager.resolveString(variables.get(Constants.ENCODEPARAMETERS));
        encodeParameters = Boolean.valueOf(variable);

        variable = propertyManager.resolveString(variables.get(Constants.PARAMETERS));
        final List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        while (variable != null && !variable.isEmpty())
        {
            final String name = StringUtils.substringBefore(variable, Constants.PARAMETER_NAME_VALUE_SEPARATOR);
            StringUtils.remove(variable, name + Constants.PARAMETER_NAME_VALUE_SEPARATOR);
            final String value = StringUtils.substringBefore(variable, Constants.PARAMETER_PAIR_SEPARATOR);
            StringUtils.remove(variable, value + Constants.PARAMETER_PAIR_SEPARATOR);
            parameters.add(new NameValuePair(name, value));
        }
        this.parameters = parameters;

        variable = propertyManager.resolveString(variables.get(Constants.HEADERS));
        final Map<String, String> headers = new HashMap<String, String>();
        while (variable != null && !variable.isEmpty())
        {
            final String name = StringUtils.substringBefore(variable, Constants.HEADER_NAME_VALUE_SEPARATOR);
            StringUtils.remove(variable, name + Constants.HEADER_NAME_VALUE_SEPARATOR);
            final String value = StringUtils.substringBefore(variable, Constants.HEADER_PAIR_SEPARATOR);
            StringUtils.remove(variable, value + Constants.HEADER_PAIR_SEPARATOR);
            headers.put(name, value);
        }
        this.headers = headers;

        // TODO Body und encode body
        variable = propertyManager.resolveString(variables.get(Constants.ENCODEBODY));
        encodeBody = Boolean.valueOf(variable);
        variable = propertyManager.resolveString(variables.get(Constants.BODY));
        body = variable;
    }

    public WebRequest buildWebRequest(final PropertyManager propertyManager) throws MalformedURLException
    {
        // fill everything with data
        fillDefaultData(propertyManager);
        // then resolve variables
        resolveValues(propertyManager);
        // build the webRequest

        if (isEncodeBody() != null && isEncodeBody())
        {
            encodeBody();
        }

        if (isEncodeParameters() != null && isEncodeParameters())
        {
            encodeParameters();
        }

        final URL url = new URL(this.urlAsString);
        final WebRequest webRequest = new WebRequest(url, getHttpmethod());

        if (isXhr() != null && isXhr())
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
        // if (getHttpmethod() == HttpMethod.POST || getHttpmethod() == HttpMethod.PUT || getHttpmethod() == HttpMethod.PATCH)
        // {
        // if (body != null)
        // {
        // webRequest.setRequestBody(body);
        // }
        // }
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
