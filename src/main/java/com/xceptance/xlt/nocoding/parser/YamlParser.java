package com.xceptance.xlt.nocoding.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.StoreItem;

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
                if (currentName.equals("Store"))
                {
                    // final JsonToken token = parser.currentToken();
                    // parser.clearCurrentToken();
                    // XltLogger.runTimeLogger.debug("GetCurrentName: " + parser.getCurrentName());
                    // XltLogger.runTimeLogger.debug("GetText: " + parser.getText());
                    // XltLogger.runTimeLogger.debug("Value as String: " + parser.getValueAsString());
                    scriptItems.addAll(handleStore(parser));
                }
                else if (currentName.equals("Action"))
                {
                    // final JsonToken token = parser.currentToken();
                    handleAction(parser);
                }
                else
                {
                    // final JsonToken token = parser.currentToken();
                    handleDefault(parser);
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
        while (parser.nextToken() != null)
        {
            final String currentName = parser.getText();
            if (currentName != null)
            {
                // if we reach the end of the store module
                if (currentName.equals("]"))
                {
                    break;
                }
                if (!(currentName.equals("{") || currentName.equals("}") || currentName.equals("[")))
                {
                    parser.nextToken();
                    final StoreItem storeItem = new StoreItem(currentName, parser.getText());
                    scriptItems.add(storeItem);
                }
            }
        }
        return scriptItems;

    }

    private List<ScriptItem> handleAction(final JsonParser parser) throws IOException
    {
        final List<ScriptItem> scriptItems = new ArrayList<ScriptItem>();
        while (parser.nextToken() != null)
        {
            final String currentName = parser.getText();
            if (currentName != null)
            {
                // if we reach the end of the store module
                if (currentName.equals("Name"))
                {
                    break;
                }
                else if (currentName.equals("Request"))
                {

                }
                else if (currentName.equals("Response"))
                {

                }
                else if (currentName.equals("Subrequests"))
                {

                }
            }
        }
        return scriptItems;

    }

    private List<ScriptItem> handleDefault(final JsonParser parser)
    {
        return null;
        // TODO Auto-generated method stub

    }

}
