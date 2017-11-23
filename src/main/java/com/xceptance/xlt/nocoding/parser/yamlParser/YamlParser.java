package com.xceptance.xlt.nocoding.parser.yamlParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.AbstractScriptItemParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.ActionItemParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.DefaultItemParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.StoreItemParser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * Reads a yaml file, provided per constructor, and generates an {@link ArrayList} filled with {@link ScriptItem}s out
 * of the yaml file.
 * 
 * @author ckeiner
 */
public class YamlParser extends Parser
{

    /**
     * Creates a {@link File} with the provided path to the file.
     * 
     * @param pathToFile
     *            The path to the file.
     */
    public YamlParser(final String pathToFile)
    {
        super(pathToFile);
    }

    /**
     * Parses the file and returns a list of ScriptItem
     */
    @Override
    public List<ScriptItem> parse() throws Exception
    {
        final List<ScriptItem> scriptItems = new ArrayList<ScriptItem>();
        // Build the parser
        final YAMLFactory factory = new YAMLFactory();
        final JsonParser parser = factory.createParser(getFile());
        // Allow comments in the parser, so we have the correct line numbers
        parser.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_COMMENTS, true);

        // Map the parsed content to JsonNodes
        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode root = mapper.readTree(parser);

        // If we have a root, extract its elements
        if (root != null)
        {
            final Iterator<JsonNode> nodes = root.elements();

            int numberObject = 0;

            // Iterate over all tokens
            while (nodes.hasNext())
            {
                final JsonNode node = nodes.next();
                // final String currentName;
                final String currentName = node.fieldNames().next();

                // Check if we have a permitted list item
                if (Constants.isPermittedListItem(currentName))
                {
                    numberObject++;
                    XltLogger.runTimeLogger.info(numberObject + ".th ScriptItem: " + currentName);

                    try
                    {
                        // Differentiate between Store, Action and default definitions
                        AbstractScriptItemParser scriptItemParser = null;
                        if (currentName.equals(Constants.STORE))
                        {
                            // Set parser to StoreItemParser
                            scriptItemParser = new StoreItemParser();
                        }
                        else if (currentName.equals(Constants.ACTION))
                        {
                            // Set parser to ActionItemParser
                            scriptItemParser = new ActionItemParser();
                        }
                        else
                        {
                            // Set parser to DefaultItemParser
                            scriptItemParser = new DefaultItemParser();
                        }

                        scriptItems.addAll(scriptItemParser.parse(node));

                    }
                    // Catch any exception while parsing, so we can print the current line/column number with the error
                    catch (final Exception e)
                    {
                        throw new JsonParseException(parser, e.getMessage(), e);
                    }
                }
                // If we don't have a list item, check if it is really a field name. If it is, throw an error
                else // if (parser.currentToken() != null && parser.getCurrentToken().equals(JsonToken.FIELD_NAME))
                {

                    throw new JsonParseException(parser, "No permitted list item: " + parser.getText());
                }
                // If we don't have a list item and it's not a field name, we have found null or an Array Entry/Exit or an Object
                // Entry/Exit.

            }
        }

        // Return all scriptItems
        return scriptItems;
    }

}
