package com.xceptance.xlt.nocoding.parser.yaml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.parser.ParserException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
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
        final List<Command> commands = new ArrayList<Command>();
        // Parse the Yaml with snakeyml
        final Yaml yaml = new Yaml();
        // final Object loadedYaml = yaml.load(createReader(pathToFile));
        final Node root = yaml.compose(createReader(pathToFile));
        if (root instanceof SequenceNode)
        {
            final List<Node> wrapperList = ((SequenceNode) root).getValue();
            for (final Node allCommandWrapper : wrapperList)
            {
                if (allCommandWrapper instanceof MappingNode)
                {
                    final List<NodeTuple> allCommands = ((MappingNode) allCommandWrapper).getValue();
                    allCommands.stream().forEachOrdered(commandWrapper -> {
                        final String commandName = ((ScalarNode) commandWrapper.getKeyNode()).getValue();
                        // Verify the command is legal
                        if (Constants.isPermittedListItem(commandName))
                        {
                            if (commandName.equals("Action"))
                            {
                                commands.addAll(ActionParser.parse(commandWrapper.getValueNode()));
                            }
                            else if (commandName.equals("Store"))
                            {
                                commands.addAll(StoreParser.parse(commandWrapper.getValueNode()));
                            }
                            else
                            {
                                commands.addAll(StoreDefaultParser.parse(commandWrapper));
                            }
                        }
                        else
                        {
                            throw new ParserException("Node at", ((ScalarNode) commandWrapper.getKeyNode()).getStartMark(),
                                                      " is not a permitted list item", null);
                        }
                    });
                }
            }
        }
        else if (root == null)
        {
            // Do nothing
        }
        else
        {
            throw new ParserException("Node at", root.getStartMark(), " is " + root.getNodeId().toString() + " but needs to be an array.",
                                      null);
            // throw new ParserEx
        }

        return commands;
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
    protected File resolveAnchors(final String pathToFile) throws IOException
    {
        // Parse the Yaml with snakeyml
        final Yaml yaml = new Yaml();
        final Object loadedYaml = yaml.load(createReader(pathToFile));

        // Place the parsed Yaml with Jacksons SequenceWriter in an FileOutputSteam
        final File file = File.createTempFile("AnchorlessYaml", ".yml");
        file.deleteOnExit();
        final FileOutputStream outputSteam = new FileOutputStream(file);
        final SequenceWriter swToFile = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValues(outputSteam);
        swToFile.write(loadedYaml);
        return file;

        // // Place the parsed Yaml with Jacksons SequenceWriter in an ByteArrayOutputSteam
        // final ByteArrayOutputStream anchorlessYaml = new ByteArrayOutputStream();
        // final SequenceWriter sw = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValues(anchorlessYaml);
        // sw.write(loadedYaml);
        // return anchorlessYaml;
    }

    @Override
    public List<String> getExtensions()
    {
        return Arrays.asList("yml", "yaml");
    }

}
