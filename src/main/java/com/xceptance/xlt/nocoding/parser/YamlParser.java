package com.xceptance.xlt.nocoding.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
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
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.AbstractSubrequest;
import com.xceptance.xlt.nocoding.util.Constants;

public class YamlParser implements Parser
{

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
        final File file = new File("./config/data/default/TLLogin.yml");
        final YAMLFactory factory = new YAMLFactory();
        final JsonParser parser = factory.createParser(file);
        int i = 0;
        String wholeContent = "";

        while (parser.nextToken() != null)
        {
            i++;
            final String currentName = parser.getText();
            if (currentName != null)
            {
                if (Constants.isPermittedListItem(currentName))
                {
                    if (currentName.equals(Constants.STORE))
                    {
                        scriptItems.addAll(handleStore(parser));
                    }
                    else if (currentName.equals(Constants.ACTION))
                    {
                        // final JsonToken token = parser.currentToken();
                        scriptItems.addAll(handleAction(parser));
                    }
                    else
                    {
                        // final JsonToken token = parser.currentToken();
                        scriptItems.addAll(handleDefault(parser));
                    }
                }
                else
                {
                    // TODO uncomment when done
                    // XltLogger.runTimeLogger.error("Unknown List Item");
                    // throw new IllegalArgumentException();
                }
            }
            wholeContent += currentName;
            if (i != 1 && i % 2 == 1)
            {
                XltLogger.runTimeLogger.debug(i / 2 + ".th Item: " + currentName);
            }
        }
        XltLogger.runTimeLogger.debug(wholeContent);
        return scriptItems;
    }

    private List<ScriptItem> handleStore(final JsonParser parser) throws IOException
    {
        final List<ScriptItem> scriptItems = new ArrayList<ScriptItem>();
        // Increments with { and decrements with } thus when reaching 0 we are done
        // And since an item starts with a {, we initialize with 1
        int state = 1;
        // Iterate over the next tokens
        while (parser.nextToken() != null)
        {
            final String currentName = parser.getText();
            if (currentName != null)
            {
                // If we find {, increment state
                if (currentName.equals("{"))
                {
                    state++;
                }
                // If we find }, decrement state
                else if (currentName.equals("}"))
                {
                    state--;
                    if (state == 0)
                    {
                        break;
                    }
                }

                /*
                 * A parsed store has multiple '{', '[', '}' in it while variables don't have any of these thus, if we reach a
                 * character, we know it must be a variable and directly after it stands the value
                 */
                else if (!(currentName.equals("{") || currentName.equals("}") || currentName.equals("[") || currentName.equals("]")))
                {
                    final StoreItem storeItem = new StoreItem(currentName, parser.nextTextValue());
                    scriptItems.add(storeItem);
                }
            }
        }
        return scriptItems;

    }

    private List<ScriptItem> handleAction(final JsonParser parser) throws IOException
    {
        // Increments with { and decrements with } thus when reaching 0 we are done
        // And since an item starts with a {, we initialize with 1
        int state = 1;

        // Initialize variables
        final List<ScriptItem> scriptItems = new ArrayList<ScriptItem>();
        String name = "";
        final List<AbstractActionItem> actionItems = new ArrayList<AbstractActionItem>();

        // Go through every element
        while (parser.nextToken() != null)
        {
            final String currentName = parser.getText();
            if (currentName != null)
            {
                // If we find {, increment state
                if (currentName.equals("{"))
                {
                    state++;
                }
                // If we find }, decrement state
                else if (currentName.equals("}"))
                {
                    state--;
                    if (state == 0)
                    {
                        break;
                    }
                }
                // if it is an action item
                else if (Constants.isPermittedActionItem(currentName))
                {
                    if (currentName.equals(Constants.NAME))
                    {
                        name = parser.nextTextValue();
                    }
                    else if (currentName.equals(Constants.REQUEST))
                    {
                        actionItems.add(handleRequest(parser));
                    }
                    else if (currentName.equals(Constants.RESPONSE))
                    {
                        actionItems.add(handleResponse(parser));
                    }
                    else if (currentName.equals(Constants.SUBREQUESTS))
                    {
                        actionItems.add(handleSubrequest(parser));
                    }
                }
                // if we find a list item, we are at the end
                else if (Constants.isPermittedListItem(currentName))
                {
                    break;
                }
            }
        }
        final ScriptItem scriptItem = new LightWeigthAction(name, actionItems);
        scriptItems.add(scriptItem);
        return scriptItems;

    }

    private Request handleRequest(final JsonParser parser) throws IOException
    {
        // parser.nextToken();
        // parser.nextToken();
        // final ObjectMapper mapper = new ObjectMapper();
        // final Request request = mapper.readValue(parser, Request.class);

        // Increments with { and decrements with } thus when reaching 0 we are done
        // And since an item starts with a {, we initialize with 1
        int state = 1;

        String url = "";
        String method = null;
        String xhr = null;
        String encodeParameters = null;
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        Map<String, String> headers = new HashMap<String, String>();
        String body = null;
        String encodeBody = null;
        // Go through every element
        while (parser.nextToken() != null)
        {
            final String currentName = parser.getText();
            if (currentName != null)
            {
                // If we find {, increment state
                if (currentName.equals("{"))
                {
                    state++;
                }
                // If we find }, decrement state
                else if (currentName.equals("}"))
                {
                    state--;
                    if (state == 0)
                    {
                        break;
                    }
                }
                // if it is an action item
                if (currentName.equals(Constants.URL))
                {
                    url = parser.nextTextValue();
                }
                else if (currentName.equals(Constants.METHOD))
                {
                    method = parser.nextTextValue();
                }
                else if (currentName.equals(Constants.XHR))
                {
                    xhr = parser.nextTextValue();
                }
                else if (currentName.equals(Constants.ENCODEPARAMETERS))
                {
                    encodeParameters = parser.nextTextValue();
                }
                else if (currentName.equals(Constants.PARAMETERS))
                {
                    parameters = handleParameters(parser);
                }
                else if (currentName.equals(Constants.HEADERS))
                {
                    headers = handleHeaders(parser);
                }
                else if (currentName.equals(Constants.BODY))
                {
                    body = parser.nextTextValue();
                }
                else if (currentName.equals(Constants.ENCODEBODY))
                {
                    encodeBody = parser.nextTextValue();
                }
                // if we find anything else than an action item,
                else if (!(currentName.equals("{") || currentName.equals("}") || currentName.equals("[")))
                {
                    if (!Constants.isPermittedActionItem(currentName))
                        break;
                }
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

        return request;
    }

    private Map<String, String> handleHeaders(final JsonParser parser)
    {
        // TODO Auto-generated method stub
        return null;
    }

    private List<NameValuePair> handleParameters(final JsonParser parser) throws IOException
    {
        // Increments with { and decrements with } thus when reaching 0 we are done
        // And since an item starts with a {, we initialize with 1
        int state = 1;

        final List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        // Go through every element
        while (parser.nextToken() != null)
        {
            final String currentName = parser.getText();
            if (currentName != null)
            {
                // If we find {, increment state
                if (currentName.equals("{"))
                {
                    state++;
                }
                // If we find }, decrement state
                else if (currentName.equals("}"))
                {
                    state--;
                    if (state == 0)
                    {
                        break;
                    }
                }
                // It's a string
                else if (!(currentName.equals("{") || currentName.equals("}") || currentName.equals("[") || currentName.equals("]")))
                {
                    parameters.add(new NameValuePair(parser.getText(), parser.getValueAsString()));
                }
            }
        }
        return parameters;
    }

    private Response handleResponse(final JsonParser parser) throws IOException
    {
        // Increments with { and decrements with } thus when reaching 0 we are done
        // And since an item starts with a {, we initialize with 1
        int state = 1;

        String httpcode = null;
        final List<AbstractValidator> validators = new ArrayList<AbstractValidator>();
        final List<AbstractResponseStore> responseStore = new ArrayList<AbstractResponseStore>();
        // Go through every element
        while (parser.nextToken() != null)
        {
            final String currentName = parser.getText();
            if (currentName != null)
            {
                // If we find {, increment state
                if (currentName.equals("{"))
                {
                    state++;
                }
                // If we find }, decrement state
                else if (currentName.equals("}"))
                {
                    state--;
                    if (state == 0)
                    {
                        break;
                    }
                }
                else if (currentName.equals(Constants.HTTPCODE))
                {
                    httpcode = parser.getValueAsString();
                }
                else if (currentName.equals(Constants.VALIDATION))
                {
                    validators.add(handleValidation(parser));
                }
                else if (currentName.equals(Constants.STORE))
                {
                    responseStore.add(handleResponseStore(parser));
                }
            }
        }

        final Response response = new Response(httpcode, responseStore, validators);
        return response;
    }

    private AbstractResponseStore handleResponseStore(final JsonParser parser) throws IOException
    {
        // Increments with { and decrements with } thus when reaching 0 we are done
        // And since an item starts with a {, we initialize with 1
        int state = 1;

        String variableName = null;
        AbstractResponseStore responseStore = null;
        // Go through every element
        while (parser.nextToken() != null)
        {
            final String currentName = parser.getText();
            if (currentName != null)
            {
                // If we find {, increment state
                if (currentName.equals("{"))
                {
                    state++;
                }
                // If we find }, decrement state
                else if (currentName.equals("}"))
                {
                    state--;
                    if (state == 0)
                    {
                        break;
                    }
                }
                // It's a string
                else if (!(currentName.equals("{") || currentName.equals("}") || currentName.equals("[") || currentName.equals("]")))
                {
                    variableName = parser.getText();
                    while (parser.nextToken() != null)
                    {
                        final String curName = parser.getText();
                        if (curName.equals(Constants.REGEXP))
                        {
                            // TODO optional group
                            responseStore = new RegExpStore(variableName, parser.getValueAsString());
                            break;
                        }
                        else if (curName.equals(Constants.XPATH))
                        {
                            responseStore = new XpathStore(variableName, parser.getValueAsString());
                            break;
                        }
                        else if (curName.equals(Constants.HEADER))
                        {
                            responseStore = new HeaderStore(variableName, parser.getValueAsString());
                            break;
                        }
                        else if (curName.equals(Constants.COOKIE))
                        {
                            responseStore = new CookieStore(variableName, parser.getValueAsString());
                            break;
                        }
                    }
                }
            }
        }
        return responseStore;
    }

    private AbstractValidator handleValidation(final JsonParser parser)
    {
        // TODO Auto-generated method stub
        return null;
    }

    private AbstractSubrequest handleSubrequest(final JsonParser parser)
    {
        // TODO Auto-generated method stub
        final AbstractSubrequest subrequest = null;
        return subrequest;
    }

    private List<ScriptItem> handleDefault(final JsonParser parser)
    {
        final List<ScriptItem> scriptItems = new ArrayList<ScriptItem>();
        return scriptItems;
        // TODO Auto-generated method stub

    }

}
