package com.xceptance.xlt.nocoding.parser.yamlParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.AbstractScriptItemParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.ActionItemParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.StoreDefaultParser;
import com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.StoreItemParser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * Reads a Yaml file, provided per constructor, and generates an List filled with {@link ScriptItem}s out of the Yaml
 * file.
 * 
 * @author ckeiner
 */
public class YamlParser extends Parser
{

    /**
     * Parses the content of the file at the location of the parameter to a list of {@link ScriptItem}s
     * 
     * @param pathToFile
     *            The String that describes the path to the file
     * @return A list of {@link ScriptItem}s.
     * @throws IOException
     *             If a {@link JsonParser} cannot be created or the file cannot be mapped to a {@link JsonNode}.
     */
    @Override
    public List<ScriptItem> parse(final String pathToFile) throws IOException
    {
        final File file = new File(pathToFile);

        final List<ScriptItem> scriptItems = new ArrayList<ScriptItem>();
        // Build the factory
        final YAMLFactory factory = new YAMLFactory();
        // Create the parser
        final JsonParser parser = factory.createParser(file);
        // Allow comments in the parser, so we have the correct line numbers
        parser.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_COMMENTS, true);
        parser.configure(Feature.ALLOW_YAML_COMMENTS, true);

        // Create an ObjectMapper
        final ObjectMapper mapper = new ObjectMapper();
        // Map the parsed content to JsonNodes, which is easier to use
        final JsonNode root = mapper.readTree(parser);

        // Check that the root, which consists of the list items, is an ArrayNode
        if (root != null)
        {
            if (!(root instanceof ArrayNode))
            {
                throw new IllegalArgumentException("Expected list items to be of type ArrayNode but is of type "
                                                   + root.getClass().getSimpleName());
            }
            // If root is an ArrayNode
            else
            {
                // Get an iterator over the elements
                final Iterator<JsonNode> nodes = root.elements();
                // Count the number of list items we have
                int numberObject = 0;

                // Iterate over all tokens
                while (nodes.hasNext())
                {
                    // Get the next element
                    final JsonNode node = nodes.next();
                    // final String currentName;
                    final String currentName = node.fieldNames().next();
                    // Increase the counter
                    numberObject++;

                    // Check if we have a permitted list item
                    if (Constants.isPermittedListItem(currentName))
                    {
                        XltLogger.runTimeLogger.info(numberObject + ".th ScriptItem: " + currentName);

                        // Try and catch, so we can use a JsonParseException, which prints the line/column number
                        try
                        {
                            // Differentiate between Store, Action and default definitions
                            AbstractScriptItemParser scriptItemParser = null;
                            switch (currentName)
                            {
                                case Constants.STORE:
                                    // Set parser to StoreItemParser
                                    scriptItemParser = new StoreItemParser();
                                    break;
                                case Constants.ACTION:
                                    // Set parser to ActionItemParser
                                    scriptItemParser = new ActionItemParser();
                                    break;

                                default:
                                    // Set parser to DefaultItemParser
                                    scriptItemParser = new StoreDefaultParser();
                                    break;
                            }
                            // Parse the scriptItem
                            scriptItems.addAll(scriptItemParser.parse(node));
                        }
                        // Catch any exception while parsing, so we can print the current line/column number with the error
                        catch (final Exception e)
                        {
                            throw new JsonParseException(parser, e.getMessage(), e);
                        }
                    }
                    // If the item wasn't a permitted list item, throw an exception
                    else
                    {
                        throw new JsonParseException(parser, "No permitted list item: " + parser.getText());
                    }
                }
            }
        }
        // Return all scriptItems
        return scriptItems;
    }

    @Override
    public List<String> getExtensions()
    {
        return Arrays.asList("yml", "yaml");
    }

}
