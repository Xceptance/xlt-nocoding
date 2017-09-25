package com.xceptance.xlt.nocoding.scriptItem.action;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
        this.variables = new HashMap<String, String>();
        variables.put(Constants.URL, url);
        variables.put(Constants.NAME, name);
    }

    public HttpMethod getHttpmethod()
    {
        HttpMethod method = null;
        if (httpmethod == null && variables.containsKey(Constants.METHOD))
        {
            method = HttpMethod.valueOf(variables.get(Constants.METHOD));
        }
        else if (httpmethod != null)
        {
            method = httpmethod;
        }
        return method;
    }

    public void setHttpmethod(final String httpmethod)
    {
        variables.put(Constants.METHOD, httpmethod);
    }

    public Boolean isXhr()
    {
        Boolean isXhr = null;
        if (this.isXhr == null && variables.containsKey(Constants.XHR))
        {
            isXhr = Boolean.valueOf(variables.get(Constants.XHR));
        }
        else if (this.isXhr != null)
        {
            isXhr = this.isXhr;
        }
        return isXhr;
    }

    public void setXhr(final String isXhr)
    {
        variables.put(Constants.XHR, isXhr);
    }

    public Boolean isEncodeParameters()
    {
        Boolean encodeParameters = null;
        if (this.encodeParameters == null && variables.containsKey(Constants.ENCODEPARAMETERS))
        {
            encodeParameters = Boolean.valueOf(variables.get(Constants.ENCODEPARAMETERS));
        }
        else if (this.encodeParameters != null)
        {
            encodeParameters = this.encodeParameters;
        }
        return encodeParameters;
    }

    public void setEncodeParameters(final String encodeParameters)
    {
        variables.put(Constants.ENCODEPARAMETERS, encodeParameters);
    }

    public List<NameValuePair> getParameters()
    {
        List<NameValuePair> parameters = null;
        if (this.parameters == null && variables.containsKey(Constants.PARAMETERS))
        {
            parameters = new ArrayList<NameValuePair>();
            String variable = variables.get(Constants.PARAMETERS);
            while (variable != null && !variable.isEmpty())
            {
                final String name = StringUtils.substringBefore(variable, Constants.PARAMETER_NAME_VALUE_SEPARATOR);
                variable = StringUtils.remove(variable, name + Constants.PARAMETER_NAME_VALUE_SEPARATOR);
                final String value = StringUtils.substringBefore(variable, Constants.PARAMETER_PAIR_SEPARATOR);
                variable = StringUtils.remove(variable, value + Constants.PARAMETER_PAIR_SEPARATOR);
                parameters.add(new NameValuePair(name, value));
            }
        }
        else if (this.parameters != null)
        {
            parameters = this.parameters;
        }
        return parameters;
    }

    public void setParameters(final List<NameValuePair> parameters)
    {
        String value = "";
        final Iterator<NameValuePair> iterator = parameters.iterator();
        while (iterator.hasNext())
        {
            final NameValuePair parameter = iterator.next();
            value += parameter.getName() + Constants.PARAMETER_NAME_VALUE_SEPARATOR + parameter.getValue();

            // if this is not the last one, add a pair separator
            if (iterator.hasNext())
            {
                value += Constants.PARAMETER_PAIR_SEPARATOR;
            }
        }
        variables.put(Constants.PARAMETERS, value);
    }

    public Map<String, String> getHeaders()
    {
        Map<String, String> headers = null;
        if (this.headers == null && variables.containsKey(Constants.HEADERS))
        {
            headers = new HashMap<String, String>();
            String variable = variables.get(Constants.HEADERS);
            while (variable != null && !variable.isEmpty())
            {
                final String name = StringUtils.substringBefore(variable, Constants.HEADER_NAME_VALUE_SEPARATOR);
                variable = StringUtils.remove(variable, name + Constants.HEADER_NAME_VALUE_SEPARATOR);
                final String value = StringUtils.substringBefore(variable, Constants.HEADER_PAIR_SEPARATOR);
                variable = StringUtils.remove(variable, value + Constants.HEADER_PAIR_SEPARATOR);
                headers.put(name, value);
            }
        }
        else if (this.headers != null)
        {
            headers = this.headers;
        }
        return headers;
    }

    public void setHeaders(final Map<String, String> headers)
    {
        String value = "";

        for (final Map.Entry<String, String> header : headers.entrySet())
        {
            value += header.getKey() + Constants.HEADER_NAME_VALUE_SEPARATOR + header.getValue() + Constants.HEADER_PAIR_SEPARATOR;
        }
        // remove the last header pair separator
        value = value.substring(0, value.length() - 1);
        variables.put(Constants.HEADERS, value);
    }

    public String getBody()
    {
        String body = null;
        if (this.body == null && variables.containsKey(Constants.BODY))
        {
            body = variables.get(Constants.BODY);
        }
        else if (this.body != null)
        {
            body = this.body;
        }
        return body;
    }

    public void setBody(final String body)
    {
        variables.put(Constants.BODY, body);
    }

    public Boolean isEncodeBody()
    {
        Boolean encodeBody = null;
        if (this.encodeBody == null && variables.containsKey(Constants.ENCODEBODY))
        {
            encodeBody = Boolean.valueOf(variables.get(Constants.ENCODEBODY));
        }
        else if (this.encodeBody != null)
        {
            encodeBody = this.encodeBody;
        }
        return encodeBody;
    }

    public void setEncodeBody(final String encodeBody)
    {
        variables.put(Constants.ENCODEBODY, encodeBody);
    }

    public String getUrl()
    {
        String urlAsString = null;
        if (this.urlAsString == null && variables.containsKey(Constants.URL))
        {
            urlAsString = variables.get(Constants.URL);
        }
        else if (this.urlAsString != null)
        {
            urlAsString = this.urlAsString;
        }
        return urlAsString;
    }

    public String getName()
    {
        String name = null;
        if (this.name == null && variables.containsKey(Constants.NAME))
        {
            name = variables.get(Constants.NAME);
        }
        else if (this.name != null)
        {
            name = this.name;
        }
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
        if (this.isXhr() == null)
        {
            variables.put(Constants.XHR, globalStorage.getConfigItemByKey(Constants.XHR));
            // final String isXhr = globalStorage.getConfigItemByKey(Constants.XHR);
            // this.isXhr = Boolean.valueOf(isXhr);
        }
        if (this.isEncodeParameters() == null)
        {
            variables.put(Constants.ENCODEPARAMETERS, globalStorage.getConfigItemByKey(Constants.ENCODEPARAMETERS));
            // final String encodeParameters = globalStorage.getConfigItemByKey(Constants.ENCODEPARAMETERS);
            // this.encodeParameters = Boolean.valueOf(encodeParameters);
        }
        if (this.isEncodeBody() == null)
        {
            variables.put(Constants.ENCODEBODY, globalStorage.getConfigItemByKey(Constants.ENCODEBODY));
            // final String encodeBody = globalStorage.getConfigItemByKey(Constants.ENCODEBODY);
            // this.encodeBody = Boolean.valueOf(encodeBody);
        }
    }

    private void resolveValues(final PropertyManager propertyManager)
    {
        // TODO Test these very throughly
        // These have to be set!
        String variable = propertyManager.resolveString(variables.get(Constants.NAME));
        name = variable;
        variable = propertyManager.resolveString(variables.get(Constants.URL));
        urlAsString = variable;
        variable = propertyManager.resolveString(variables.get(Constants.METHOD));
        httpmethod = HttpMethod.valueOf(variable);

        if (variables.containsKey(Constants.XHR))
        {
            variable = propertyManager.resolveString(variables.get(Constants.XHR));
            isXhr = Boolean.valueOf(variable);
        }
        if (variables.containsKey(Constants.ENCODEPARAMETERS))
        {
            variable = propertyManager.resolveString(variables.get(Constants.ENCODEPARAMETERS));
            encodeParameters = Boolean.valueOf(variable);
        }

        if (variables.containsKey(Constants.PARAMETERS))
        {
            variable = propertyManager.resolveString(variables.get(Constants.PARAMETERS));
            final List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            while (variable != null && !variable.isEmpty())
            {
                final String name = StringUtils.substringBefore(variable, Constants.PARAMETER_NAME_VALUE_SEPARATOR);
                variable = StringUtils.remove(variable, name + Constants.PARAMETER_NAME_VALUE_SEPARATOR);
                final String value = StringUtils.substringBefore(variable, Constants.PARAMETER_PAIR_SEPARATOR);
                variable = StringUtils.remove(variable, value + Constants.PARAMETER_PAIR_SEPARATOR);
                parameters.add(new NameValuePair(name, value));
            }
            this.parameters = parameters;
        }

        if (variables.containsKey(Constants.HEADERS))
        {
            variable = propertyManager.resolveString(variables.get(Constants.HEADERS));
            final Map<String, String> headers = new HashMap<String, String>();
            while (variable != null && !variable.isEmpty())
            {
                final String name = StringUtils.substringBefore(variable, Constants.HEADER_NAME_VALUE_SEPARATOR);
                variable = StringUtils.remove(variable, name + Constants.HEADER_NAME_VALUE_SEPARATOR);
                final String value = StringUtils.substringBefore(variable, Constants.HEADER_PAIR_SEPARATOR);
                variable = StringUtils.remove(variable, value + Constants.HEADER_PAIR_SEPARATOR);
                headers.put(name, value);
            }
            this.headers = headers;
        }

        if (variables.containsKey(Constants.ENCODEBODY))
        {
            variable = propertyManager.resolveString(variables.get(Constants.ENCODEBODY));
            encodeBody = Boolean.valueOf(variable);
        }
        if (variables.containsKey(Constants.BODY))
        {
            variable = propertyManager.resolveString(variables.get(Constants.BODY));
            body = variable;
        }
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
