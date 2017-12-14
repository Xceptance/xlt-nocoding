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
import com.xceptance.xlt.nocoding.parser.csvParser.scriptItems.AbstractScriptItemParser;
import com.xceptance.xlt.nocoding.parser.csvParser.scriptItems.ActionItemParser;
import com.xceptance.xlt.nocoding.parser.csvParser.scriptItems.StaticItemParser;
import com.xceptance.xlt.nocoding.parser.csvParser.scriptItems.XhrItemParser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.util.ParserUtils;

/**
 * Reads a Csv file, provided per constructor, and generates an {@link List} filled with {@link ScriptItem}s out of the
 * Csv file.
 * 
 * @author ckeiner
 */
public class CsvParser_Jackson extends Parser
{

    public CsvParser_Jackson(final String pathToFile)
    {
        super(pathToFile);
    }

    @Override
    public List<ScriptItem> parse() throws Exception
    {
        final List<ScriptItem> scriptItems = new ArrayList<ScriptItem>();
        final CsvMapper mapper = new CsvMapper();

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
            while (elements.hasNext())
            {
                final JsonNode element = elements.next();
                if (element.isContainerNode())
                {
                    // Set type to default
                    String type = CsvConstants.TYPE_DEFAULT;
                    // If type is defined, use the defined type
                    final JsonNode typeNode = element.get(CsvConstants.TYPE);
                    AbstractScriptItemParser itemParser = null;
                    if (typeNode != null)
                    {
                        type = ParserUtils.readSingleValue(typeNode);
                    }
                    switch (type)
                    {
                        case CsvConstants.TYPE_ACTION:
                            itemParser = new ActionItemParser();
                            break;
                        case CsvConstants.TYPE_STATIC:
                            itemParser = new StaticItemParser();
                            break;
                        case CsvConstants.TYPE_XHR_ACTION:
                            itemParser = new XhrItemParser();
                            break;
                        default:
                            break;
                    }
                    scriptItems.addAll(itemParser.parse(element));
                }
                else
                {
                    // We got an empty line
                }

            }
        }
        catch (final Exception e)
        {
            throw new IllegalStateException("Unknown error: " + e.getMessage(), e);
        }
        return scriptItems;
    }

}
