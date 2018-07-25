package com.xceptance.xlt.nocoding.parser.yaml.command;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.xceptance.xlt.nocoding.command.Command;

/**
 * The abstract class for parsing script items.
 *
 * @author ckeiner
 */
public abstract class AbstractCommandParser
{

    /**
     * Parses the script item at the specified {@link JsonNode}.
     *
     * @param scriptItemNode
     *            The <code>JsonNode</code> the script item starts at
     * @return A list of all {@link Command}s defined by the specified <code>JsonNode</code>
     */
    public abstract List<Command> parse(JsonNode scriptItemNode);

}
