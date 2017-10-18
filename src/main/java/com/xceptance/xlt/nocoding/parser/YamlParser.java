package com.xceptance.xlt.nocoding.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.StoreItem;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.scriptItem.action.LightWeigthAction;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
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
            XltLogger.runTimeLogger.debug(i + ".th Line: " + currentName);
        }
        XltLogger.runTimeLogger.debug(wholeContent);
        return scriptItems;
    }

    private List<ScriptItem> handleStore(final JsonParser parser) throws IOException
    {
        final List<ScriptItem> scriptItems = new ArrayList<ScriptItem>();
        // Iterate over the next tokens
        while (parser.nextToken() != null)
        {
            final String currentName = parser.getText();
            if (currentName != null)
            {
                if (currentName.equals("]"))
                {
                    break;
                }
                // // If we reach a permitted list item, we are done with the store command
                // if (Constants.isPermittedListItem(currentName))
                // {
                // break;
                // }
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
                // if it is an action item
                if (Constants.isPermittedActionItem(currentName))
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
        parser.nextToken();
        parser.nextToken();
        final ObjectMapper mapper = new ObjectMapper();
        final Request request = mapper.readValue(parser, Request.class);

        // String url = "";
        // String method = null;
        // String xhr = null;
        // String encodeParameters = null;
        // final List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        // final Map<String, String> headers = new HashMap<String, String>();
        // String body = null;
        // String encodeBody = null;
        // // Go through every element
        // while (parser.nextToken() != null)
        // {
        // final String currentName = parser.getText();
        // if (currentName != null)
        // {
        // // if it is an action item
        // if (currentName.equals(Constants.URL))
        // {
        // url = parser.nextTextValue();
        // }
        // else if (currentName.equals(Constants.METHOD))
        // {
        // method = parser.nextTextValue();
        // }
        // else if (currentName.equals(Constants.XHR))
        // {
        // xhr = parser.nextTextValue();
        // }
        // else if (currentName.equals(Constants.ENCODEPARAMETERS))
        // {
        // encodeParameters = parser.nextTextValue();
        // }
        // else if (currentName.equals(Constants.PARAMETERS))
        // {
        // // TODO Handle Parameters
        // }
        // else if (currentName.equals(Constants.HEADERS))
        // {
        // // TODO Handle Headers
        // }
        // else if (currentName.equals(Constants.BODY))
        // {
        // body = parser.nextTextValue();
        // }
        // else if (currentName.equals(Constants.ENCODEBODY))
        // {
        // encodeBody = parser.nextTextValue();
        // }
        // // if we find anything else than an action item,
        // else if (!(currentName.equals("{") || currentName.equals("}") || currentName.equals("[")))
        // {
        // if (!Constants.isPermittedActionItem(currentName))
        // break;
        // }
        // }
        // }
        // final Request request = new Request(url);
        // request.setMethod(method);
        // request.setXhr(xhr);
        // request.setEncodeParameters(encodeParameters);
        // request.setParameters(parameters);
        // request.setHeaders(headers);
        // request.setBody(body);
        // request.setEncodeBody(encodeBody);

        return request;
    }

    private Response handleResponse(final JsonParser parser)
    {
        // TODO Auto-generated method stub
        final Response response = new Response();
        return response;
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
