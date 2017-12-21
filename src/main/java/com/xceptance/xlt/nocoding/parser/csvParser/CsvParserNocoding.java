package com.xceptance.xlt.nocoding.parser.csvParser;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.csvParser.scriptItems.ActionItemParser;
import com.xceptance.xlt.nocoding.parser.csvParser.scriptItems.StaticItemParser;
import com.xceptance.xlt.nocoding.parser.csvParser.scriptItems.XhrItemParser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.action.Action;
import com.xceptance.xlt.nocoding.util.ParserUtils;

/**
 * Reads a Csv file, provided per constructor, and generates an {@link List} filled with {@link ScriptItem}s out of the
 * Csv file.
 * 
 * @author ckeiner
 */
public class CsvParserNocoding extends Parser
{

    /**
     * Creates a new instance of {@link CsvParserNocoding}
     * 
     * @param pathToFile
     *            The path to the file
     */
    public CsvParserNocoding(final String pathToFile)
    {
        super(pathToFile);
    }

    /**
     * Creates a {@link JsonNode} out of the Csv file with the first (not commented) line as fieldNames. Then uses these
     * {@link JsonNode}s to parse the data to a {@link List}<{@link ScriptItem}>. Keep in mind, that each second
     * {@link JsonNode} is an empty node
     */
    @Override
    public List<ScriptItem> parse() throws Exception
    {
        final List<ScriptItem> scriptItems = new ArrayList<ScriptItem>();

        final CsvMapper mapper = new CsvMapper();
        // Configure mapper:

        // Needed so we read everything with readTree
        mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
        // Needed so the comments are skipped
        mapper.configure(Feature.ALLOW_YAML_COMMENTS, true);
        mapper.configure(CsvParser.Feature.SKIP_EMPTY_LINES, true);

        // Create a schema that uses the first line as header
        final CsvSchema schema = mapper.schemaWithHeader();

        // Create a reader for a JsonNode, that uses the provided schema and builds a Tree out of the input file
        final JsonNode root = mapper.readerFor(JsonNode.class).with(schema).readTree(new FileInputStream(getFile()));

        // Get an iterator over all elements
        final Iterator<JsonNode> elements = root.elements();
        try
        {
            // Save the last action, so we can still manipulate it
            Action lastAction = null;
            while (elements.hasNext())
            {
                // Get the next node
                final JsonNode element = elements.next();

                // Set type to default
                String type = CsvConstants.TYPE_DEFAULT;

                // If type is defined, use the defined type
                if (element.has(CsvConstants.TYPE))
                {
                    final String value = ParserUtils.readSingleValue(element.get(CsvConstants.TYPE));
                    if (value != null && !value.isEmpty())
                    {
                        // Remove whitespaces in the beginning and end
                        type = value.trim();
                    }
                }

                // Differentiate between the different types
                switch (type)
                {
                    case CsvConstants.TYPE_ACTION:
                        // Create a new action with the ActionItemParser
                        final Action currentAction = new ActionItemParser().parse(element);
                        // Verify, that lastAction was not an empty line, by asserting that: the action has action items, has a name, and
                        // that name is not null
                        if (!currentAction.getActionItems().isEmpty() && currentAction.getName() != null
                            && !currentAction.getName().isEmpty())
                        {
                            // Add the action to the scriptitems
                            lastAction = currentAction;
                            scriptItems.add(lastAction);
                        }
                        break;
                    case CsvConstants.TYPE_STATIC:
                        // Add a static subrequest to the last Action
                        lastAction.getActionItems().add(new StaticItemParser().parse(element));
                        break;
                    case CsvConstants.TYPE_XHR_ACTION:
                        // Add an XhrSubrequest to the last Action
                        lastAction.getActionItems().add(new XhrItemParser().parse(element));
                        break;
                    default:
                        // Everything else is unknown
                        throw new IllegalArgumentException("Unknown Type: " + type);
                }

            }
        }
        catch (final Exception e)
        {
            // TODO find better error
            throw new IllegalStateException("Unknown error: " + e.getMessage(), e);
        }

        // Return all scriptItems
        return scriptItems;
    }

}