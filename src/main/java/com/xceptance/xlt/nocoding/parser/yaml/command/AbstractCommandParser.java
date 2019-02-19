package com.xceptance.xlt.nocoding.parser.yaml.command;

import java.util.List;

import org.yaml.snakeyaml.nodes.Node;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.command.Command;

/**
 * The abstract class for parsing commands.
 *
 * @author ckeiner
 */
public abstract class AbstractCommandParser
{

    /**
     * Parses the command from the given {@link JsonNode}.
     *
     * @param node
     *            The <code>Node</code> the command starts at
     * @return A list of all {@link Command}s defined by the specified <code>JsonNode</code>
     */
    public abstract List<Command> parse(Node scriptItemNode);

}
