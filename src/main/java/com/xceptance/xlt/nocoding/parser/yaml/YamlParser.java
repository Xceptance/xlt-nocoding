package com.xceptance.xlt.nocoding.parser.yaml;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.parser.ParserException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
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
     * Parses the content of the Reader <code>reader</code> to a list of {@link Command}s
     *
     * @param reader
     *            The Reader that contains {@link Command}s in a parser dependent format
     * @return A list of {@link Command}s
     * @throws IOException
     *             If a {@link Reader} or {@link JsonParser} cannot be created or the file cannot be mapped to a
     *             {@link JsonNode}.
     */
    @Override
    public List<Command> parse(final Reader reader) throws IOException
    {
        final List<Command> commands = new ArrayList<Command>();
        // Parse the Yaml with snakeyml
        final Yaml yaml = new Yaml();
        // final Object loadedYaml = yaml.load(createReader(pathToFile));
        final Node root = yaml.compose(reader);
        if (root instanceof SequenceNode)
        {
            final List<Node> commandWrappersList = ((SequenceNode) root).getValue();
            for (final Node singleCommandWrapper : commandWrappersList)
            {
                if (singleCommandWrapper instanceof MappingNode)
                {
                    final List<NodeTuple> commandMapping = ((MappingNode) singleCommandWrapper).getValue();
                    commandMapping.stream().forEachOrdered(singleMapping -> {
                        final String commandName = YamlParserUtils.transformScalarNodeToString(singleCommandWrapper.getStartMark(),
                                                                                               singleMapping.getKeyNode());
                        // Verify the command is legal
                        if (Constants.isPermittedListItem(commandName))
                        {
                            if (commandName.equals("Action"))
                            {
                                commands.addAll(ActionParser.parse(singleCommandWrapper.getStartMark(), singleMapping.getValueNode()));
                            }
                            else if (commandName.equals("Store"))
                            {
                                commands.addAll(StoreParser.parse(singleCommandWrapper.getStartMark(), singleMapping.getValueNode()));
                            }
                            else
                            {
                                commands.addAll(StoreDefaultParser.parse(singleCommandWrapper.getStartMark(), singleMapping));
                            }
                        }
                        else
                        {
                            throw new ParserException("Node", root.getStartMark(), " contains a not permitted list item",
                                                      singleCommandWrapper.getStartMark());
                        }
                    });
                }
                else
                {
                    throw new ParserException("Node", root.getStartMark(),
                                              " contains a " + singleCommandWrapper.getNodeId() + " but it must contain mapping",
                                              singleCommandWrapper.getStartMark());

                }
            }
        }
        else if (root == null)
        {
            // Do nothing
        }
        else
        {
            throw new ParserException("Node at", root.getStartMark(), " contains a " + root.getNodeId() + " but it must contain an array.",
                                      null);
        }

        return commands;
    }

    @Override
    public List<String> getExtensions()
    {
        return Arrays.asList("yml", "yaml");
    }

}
