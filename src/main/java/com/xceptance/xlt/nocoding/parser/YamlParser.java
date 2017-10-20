package com.xceptance.xlt.nocoding.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.StoreItem;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.scriptItem.action.LightWeigthAction;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.response.stores.AbstractResponseStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.stores.CookieStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.stores.HeaderStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.stores.RegExpStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.stores.XpathStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.AbstractValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.CookieValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.HeaderValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.RegExpValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.AbstractSubrequest;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.StaticSubrequest;
import com.xceptance.xlt.nocoding.util.Constants;

public class YamlParser implements Parser
{

    final String pathToFile;

    public YamlParser(final String pathToFile)
    {
        this.pathToFile = pathToFile;
    }

    @Override
    public List<ScriptItem> parse()
    {
        // TODO Auto-generated method stub
        try
        {
            return parseThis();
        }
        catch (final JsonParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (final IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public List<ScriptItem> parseThis() throws JsonParseException, IOException
    {
        final List<ScriptItem> scriptItems = new ArrayList<ScriptItem>();
        final File file = new File(pathToFile);
        final YAMLFactory factory = new YAMLFactory();
        final JsonParser parser = factory.createParser(file);

        int numberObject = 0;

        while (parser.nextToken() != null)
        {
            if (Constants.isPermittedListItem(parser.getText()))
            {
                numberObject++;
                XltLogger.runTimeLogger.info(numberObject + ".th ScriptItem: " + parser.getText());

                if (parser.getText().equals(Constants.STORE))
                {
                    scriptItems.addAll(handleStore(parser));
                }
                else if (parser.getText().equals(Constants.ACTION))
                {
                    scriptItems.addAll(handleAction(parser));
                }
                else
                {
                    final ObjectMapper mapper = new ObjectMapper();
                    final Map<String, Object> map = mapper.readValue(parser, new TypeReference<Map<String, Object>>()
                    {
                    });
                    System.out.println("Other element found: " + map.keySet());
                }
            }

        }

        return scriptItems;
    }

    private List<ScriptItem> handleStore(final JsonParser parser) throws IOException
    {
        // transform current parser to the content of the current node
        final List<ScriptItem> scriptItems = new ArrayList<ScriptItem>();
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode objectNode = mapper.readTree(parser);
        final JsonNode jsonNode = objectNode.get(Constants.STORE);
        final Iterator<JsonNode> iterator = jsonNode.elements();

        while (iterator.hasNext())
        {
            final JsonNode current = iterator.next();
            final Iterator<String> fieldName = current.fieldNames();

            while (fieldName.hasNext())
            {
                final String field = fieldName.next();
                final String textValue = current.get(field).textValue();
                scriptItems.add(new StoreItem(field, textValue));
                XltLogger.runTimeLogger.debug("Added " + field + "=" + textValue + " to parameters");
            }
        }
        return scriptItems;

    }

    private List<ScriptItem> handleAction(final JsonParser parser) throws IOException
    {
        // Initialize variables
        final List<ScriptItem> scriptItems = new ArrayList<ScriptItem>();
        String name = null;
        final List<AbstractActionItem> actionItems = new ArrayList<AbstractActionItem>();

        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode objectNode = mapper.readTree(parser);
        final Iterator<JsonNode> iterator = objectNode.elements();

        while (iterator.hasNext())
        {
            final JsonNode node = iterator.next();
            final Iterator<String> fieldNames = node.fieldNames();

            while (fieldNames.hasNext())
            {
                final String fieldName = fieldNames.next();

                switch (fieldName)
                {
                    case Constants.NAME:
                        name = node.get(fieldName).textValue();
                        System.out.println("Actionname: " + name);
                        break;

                    case Constants.REQUEST:
                        System.out.println("Request: " + node.get(fieldName).toString());
                        actionItems.add(handleRequest(node.get(fieldName)));
                        break;

                    case Constants.RESPONSE:
                        System.out.println("Response: " + node.get(fieldName).toString());
                        actionItems.add(handleResponse(node.get(fieldName)));
                        break;

                    case Constants.SUBREQUESTS:
                        System.out.println("Subrequests: " + node.get(fieldName).toString());
                        actionItems.addAll(handleSubrequest(node.get(fieldName)));
                        break;

                    default:
                        break;
                }
            }
        }

        final ScriptItem scriptItem = new LightWeigthAction(name, actionItems);
        scriptItems.add(scriptItem);
        return scriptItems;

    }

    private Request handleRequest(final JsonNode node) throws IOException
    {

        String url = "";
        String method = null;
        String xhr = null;
        String encodeParameters = null;
        final List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        final Map<String, String> headers = new HashMap<String, String>();
        String body = null;
        String encodeBody = null;

        final Iterator<String> fieldNames = node.fieldNames();

        while (fieldNames.hasNext())
        {
            final String fieldName = fieldNames.next();

            switch (fieldName)
            {
                case Constants.URL:
                    url = node.get(fieldName).textValue();
                    // XltLogger.runTimeLogger.debug("URL: " + url);
                    break;

                case Constants.METHOD:
                    method = node.get(fieldName).textValue();
                    // XltLogger.runTimeLogger.debug("Method: " + method);
                    break;

                case Constants.XHR:
                    xhr = node.get(fieldName).textValue();
                    // XltLogger.runTimeLogger.debug("Xhr: " + xhr);
                    break;

                case Constants.ENCODEPARAMETERS:
                    encodeParameters = node.get(fieldName).textValue();
                    // XltLogger.runTimeLogger.debug("EncodeParameters: " + encodeParameters);
                    break;

                case Constants.PARAMETERS:
                    // Parameter Magic
                    parameters.addAll(handleParameters(node.get(fieldName)));
                    break;

                case Constants.HEADERS:
                    headers.putAll(handleHeaders(node.get(fieldName)));
                    break;

                case Constants.BODY:
                    body = node.get(fieldName).textValue();
                    // XltLogger.runTimeLogger.debug("Body: " + body);
                    break;

                case Constants.ENCODEBODY:
                    encodeBody = node.get(fieldName).textValue();
                    // XltLogger.runTimeLogger.debug("EncodeBody: " + encodeBody);
                    break;

                default:
                    break;
            }
        }

        final Request request = new Request(url);
        request.setMethod(method);
        request.setXhr(xhr);
        request.setEncodeParameters(encodeParameters);
        request.setParameters(parameters);
        request.setHeaders(headers);
        request.setBody(body);
        request.setEncodeBody(encodeBody);

        XltLogger.runTimeLogger.info(request.toSimpleDebugString());

        return request;
    }

    private Map<String, String> handleHeaders(final JsonNode node) throws IOException
    {
        // headers are transformed to a JSONArray

        final Map<String, String> headers = new HashMap<String, String>();

        final Iterator<JsonNode> iterator = node.elements();

        while (iterator.hasNext())
        {
            final JsonNode current = iterator.next();
            final Iterator<String> fieldName = current.fieldNames();

            while (fieldName.hasNext())
            {
                final String field = fieldName.next();
                final String textValue = current.get(field).textValue();
                headers.put(field, textValue);
                XltLogger.runTimeLogger.debug("Added " + field + "=" + headers.get(field) + " to parameters");
            }
        }

        return headers;
    }

    private List<NameValuePair> handleParameters(final JsonNode node) throws IOException
    {
        // parameters are transformed to a JSONArray, thus we cannot directly use the fieldname iterator

        final List<NameValuePair> parameters = new ArrayList<NameValuePair>();

        final Iterator<JsonNode> iterator = node.elements();

        while (iterator.hasNext())
        {
            final JsonNode current = iterator.next();
            final Iterator<String> fieldName = current.fieldNames();

            while (fieldName.hasNext())
            {
                final String field = fieldName.next();
                String textValue = current.get(field).textValue();
                // TODO talk about this
                // Since parameters might be empty and the parser writes "null" we check for it and then remove it
                if (textValue == null || textValue.equals("null"))
                {
                    // numbers wont get read with "textValue" so we have to first figure out if it is a number
                    textValue = current.get(field).toString();
                    // if we still have null, the field really was null
                    if (textValue == null || textValue.equals("null"))
                    {
                        textValue = "";
                    }
                }
                parameters.add(new NameValuePair(field, textValue));
                XltLogger.runTimeLogger.debug("Added " + field + "=" + textValue + " to parameters");
            }
        }

        return parameters;
    }

    private Response handleResponse(final JsonNode node) throws IOException
    {

        String httpcode = null;
        final List<AbstractValidator> validators = new ArrayList<AbstractValidator>();
        final List<AbstractResponseStore> responseStore = new ArrayList<AbstractResponseStore>();

        final Iterator<String> fieldNames = node.fieldNames();

        while (fieldNames.hasNext())
        {
            final String fieldName = fieldNames.next();
            switch (fieldName)
            {
                case Constants.HTTPCODE:
                    // we have to use toString here
                    httpcode = node.get(fieldName).toString();
                    XltLogger.runTimeLogger.debug("Added Httpcode " + httpcode);
                    break;

                case Constants.VALIDATION:
                    validators.addAll(handleValidation(node.get(fieldName)));
                    XltLogger.runTimeLogger.debug("Added Validation");
                    break;

                case Constants.STORE:
                    responseStore.addAll(handleResponseStore(node.get(fieldName)));
                    XltLogger.runTimeLogger.debug("Added Validation");
                    break;

                default:
                    break;
            }
        }

        final Response response = new Response(httpcode, responseStore, validators);
        return response;
    }

    private List<AbstractResponseStore> handleResponseStore(final JsonNode node) throws IOException
    {

        String variableName = null;
        final List<AbstractResponseStore> responseStore = new ArrayList<AbstractResponseStore>();
        // Go through every element
        final Iterator<JsonNode> iterator = node.elements();

        while (iterator.hasNext())
        {
            final JsonNode current = iterator.next();
            final Iterator<String> fieldName = current.fieldNames();

            while (fieldName.hasNext())
            {
                // Name of the variable
                variableName = fieldName.next();
                // The substructure
                final JsonNode storeContent = current.get(variableName);
                final Iterator<String> name = storeContent.fieldNames();
                // Iterate over the content
                while (name.hasNext())
                {
                    final String leftHandExpression = name.next();
                    switch (leftHandExpression)
                    {
                        case Constants.XPATH:
                            // Xpath Magic
                            responseStore.add(new XpathStore(variableName, storeContent.get(leftHandExpression).textValue()));
                            break;

                        case Constants.REGEXP:
                            final String pattern = storeContent.get(leftHandExpression).textValue();
                            String group = null;
                            // if we have another fieldName, this means the optional group is specified
                            if (name.hasNext())
                            {
                                group = storeContent.get(name.next()).textValue();
                            }
                            responseStore.add(new RegExpStore(variableName, pattern, group));
                            break;

                        case Constants.HEADER:
                            responseStore.add(new HeaderStore(variableName, storeContent.get(leftHandExpression).textValue()));
                            break;

                        case Constants.COOKIE:
                            responseStore.add(new CookieStore(variableName, storeContent.get(leftHandExpression).textValue()));
                            break;

                        default:
                            break;
                    }
                }

                XltLogger.runTimeLogger.debug("Added " + variableName + " to ResponseStore");
            }
        }

        return responseStore;
    }

    private List<AbstractValidator> handleValidation(final JsonNode node) throws IOException
    {

        final List<AbstractValidator> validator = new ArrayList<AbstractValidator>();
        String validationName = null;

        final Iterator<JsonNode> iterator = node.elements();

        while (iterator.hasNext())
        {
            final JsonNode current = iterator.next();
            final Iterator<String> fieldName = current.fieldNames();

            while (fieldName.hasNext())
            {
                validationName = fieldName.next();
                // The substructure
                final JsonNode storeContent = current.get(validationName);
                final Iterator<String> name = storeContent.fieldNames();
                // Iterate over the content
                while (name.hasNext())
                {
                    final String leftHandExpression = name.next();
                    switch (leftHandExpression)
                    {
                        case Constants.XPATH:
                            // Xpath Magic

                            break;

                        case Constants.REGEXP:
                            final String pattern = storeContent.get(leftHandExpression).textValue();
                            String group = null;
                            String text = null;
                            // if we have another name, this means the optional text is specified
                            if (name.hasNext())
                            {
                                text = storeContent.get(name.next()).textValue();
                                // if we have yet another name, this is the optional group
                                if (name.hasNext())
                                {
                                    group = storeContent.get(name.next()).textValue();
                                }
                            }

                            validator.add(new RegExpValidator(validationName, pattern, text, group));
                            break;

                        case Constants.HEADER:
                            final String header = storeContent.get(leftHandExpression).textValue();
                            String textOrCountDecider = null;
                            String textOrCount = null;
                            if (name.hasNext())
                            {
                                textOrCountDecider = name.next();
                                textOrCount = storeContent.get(textOrCountDecider).textValue();
                            }
                            validator.add(new HeaderValidator(validationName, header, textOrCountDecider, textOrCount));
                            break;

                        case Constants.COOKIE:
                            final String cookieName = storeContent.get(leftHandExpression).textValue();
                            String cookieContent = null;

                            // If we have another name, it is the optional "matches" field
                            if (name.hasNext())
                            {
                                cookieContent = storeContent.get(name.next()).textValue();
                            }

                            validator.add(new CookieValidator(validationName, cookieName, cookieContent));
                            break;

                        default:
                            break;
                    }
                }

                XltLogger.runTimeLogger.debug("Added " + validationName + " to Validations");
            }
        }
        // return new ArrayList<AbstractValidator>();
        return validator;
    }

    private List<AbstractSubrequest> handleSubrequest(final JsonNode node)
    {
        // TODO Auto-generated method stub
        final List<AbstractSubrequest> subrequest = new ArrayList<AbstractSubrequest>();

        final Iterator<JsonNode> iterator = node.elements();

        while (iterator.hasNext())
        {
            final JsonNode current = iterator.next();
            final Iterator<String> fieldName = current.fieldNames();

            // the type of subrequest
            while (fieldName.hasNext())
            {
                final String name = fieldName.next();
                switch (name)
                {
                    case Constants.XHR:
                        // Do normal action stuff that isn't action stuff
                        break;

                    case Constants.STATIC:
                        // System.out.println(current.get(name));
                        final List<String> urls = new ArrayList<String>();
                        final JsonNode staticUrls = current.get(name);

                        final Iterator<JsonNode> staticUrlsIterator = staticUrls.elements();
                        while (staticUrlsIterator.hasNext())
                        {
                            final String url = staticUrlsIterator.next().textValue();
                            urls.add(url);
                        }

                        subrequest.add(new StaticSubrequest(urls));
                        break;

                    default:
                        break;
                }

            }

        }

        return subrequest;
    }

    private List<ScriptItem> handleDefault(final JsonParser parser)
    {
        final List<ScriptItem> scriptItems = new ArrayList<ScriptItem>();
        return scriptItems;
        // TODO Auto-generated method stub

    }

}
