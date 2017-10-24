package com.xceptance.xlt.nocoding.parser.yamlParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.StoreItem;
import com.xceptance.xlt.nocoding.util.Constants;

public class StoreItemParser extends AbstractScriptItemParser
{

    public List<ScriptItem> parse(final JsonParser parser) throws IOException
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

}
