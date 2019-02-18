package com.xceptance.xlt.nocoding.parser.yaml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.command.Command;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.yaml.command.action.ActionParser;
import com.xceptance.xlt.nocoding.parser.yaml.command.store.StoreParser;
import com.xceptance.xlt.nocoding.parser.yaml.command.storeDefault.StoreDefaultParser;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * Reads a Yaml file and generates a list filled with {@link Command}s out of the Yaml file.
 *
 * @author ckeiner
 */
public class YamlParser implements Parser
{

    /**
     * Parses the content of the file at the <code>pathToFile</code> to a list of {@link Command}s
     *
     * @param pathToFile
     *            The String that describes the path to the file
     * @return A list of {@link Command}s
     * @throws IOException
     *             If a {@link Reader} or {@link JsonParser} cannot be created or the file cannot be mapped to a
     *             {@link JsonNode}.
     */
    @Override
    public List<Command> parse(final String pathToFile) throws IOException
    {
        final List<Command> scriptItems = new ArrayList<>();
        // Resolve Anchors
        final ByteArrayOutputStream anchorlessYaml = resolveAnchors(pathToFile);
        // Place the content inside an ByteArryInputSteam
        final ByteArrayInputStream anchorlessYamlInputSteam = new ByteArrayInputStream(anchorlessYaml.toByteArray());
        // Create the parser
        final JsonParser parser = new YAMLFactory().createParser(anchorlessYamlInputSteam);
        // Allow comments in the parser, so we have the correct line numbers
        parser.enable(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_COMMENTS);
        parser.enable(Feature.ALLOW_YAML_COMMENTS);

        // Create an ObjectMapper
        final ObjectMapper mapperYaml = new ObjectMapper();
        // Map the parsed content to JsonNodes, which are easier to use
        final JsonNode root = mapperYaml.readTree(parser);

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
                        XltLogger.runTimeLogger.info(numberObject + ". ScriptItem: " + currentName);

                        // Try and catch, so we can use a JsonParseException, which prints the line/column number
                        try
                        {
                            // Differentiate between Store, Action and default definitions
                            switch (currentName)
                            {
                                case Constants.STORE:
                                    // Set parser to StoreItemParser
                                    scriptItems.addAll(new StoreParser().parse(node.get(currentName)));
                                    break;
                                case Constants.ACTION:
                                    // Set parser to ActionItemParser
                                    scriptItems.addAll(new ActionParser().parse(node.get(currentName)));
                                    break;

                                default:
                                    // Set parser to DefaultItemParser
                                    scriptItems.addAll(new StoreDefaultParser(currentName).parse(node.get(currentName)));
                                    break;
                            }
                        }
                        // Catch any exception while parsing, so we can print the current line/column number with the
                        // error
                        catch (final Exception e)
                        {
                            throw new JsonParseException(parser, e.getMessage(), e);
                        }
                    }
                    // If the item wasn't a permitted list item, throw an exception
                    else
                    {
                        throw new JsonParseException(parser, "No permitted list item: " + currentName);
                    }
                }
            }
        }
        // Return all scriptItems
        return scriptItems;
    }

    /**
     * Resolves anchors and aliases in the Yaml-file and returns an ByteArrayOutputStream.<br>
     * Since Jackson cannot resolve anchors and aliases, snakeyml is used to parse it to objects, which are then
     * transformed via Jackson's {@link SequenceWriter}.
     * 
     * @param pathToFile
     *            The String that describes the path to the file
     * @return A ByteArrayOutputStream with the anchors resolved as their own object.
     * @throws IOException
     *             If a {@link Reader} or {@link SequenceWriter} cannot be created.
     */
    protected ByteArrayOutputStream resolveAnchors(final String pathToFile) throws IOException
    {
        // Parse the Yaml with snakeyml
        final Yaml yaml = new Yaml();
        final Object loadedYaml = yaml.load(createReader(pathToFile));

        // Place the parsed Yaml with Jacksons SequenceWriter in an ByteArrayOutputSteam
        final ByteArrayOutputStream anchorlessYaml = new ByteArrayOutputStream();
        final SequenceWriter sw = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValues(anchorlessYaml);
        sw.write(loadedYaml);
        // Place the content inside an ByteArryInputSteam
        return anchorlessYaml;
    }

    @Override
    public List<String> getExtensions()
    {
        return Arrays.asList("yml", "yaml");
    }

}
