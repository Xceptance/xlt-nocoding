package com.xceptance.xlt.nocoding.parser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;

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
        final File file = new File("./config/data/default/TLLogin.yml");
        final YAMLFactory factory = new YAMLFactory();
        final JsonParser parser = factory.createParser(file);
        while (parser.nextToken() != null)
        {
            if (parser.getCurrentName() != null)
            {
                if (parser.getCurrentName() == "Store")
                {
                    final JsonToken token = parser.currentToken();
                    // handleStore();
                }
                else if (parser.getCurrentName() == "Action")
                {
                    // handleAction();
                }
                else
                {
                    // handleDefault();
                }
            }

            System.out.println(parser.getCurrentName());
        }
        return null;
    }

}
