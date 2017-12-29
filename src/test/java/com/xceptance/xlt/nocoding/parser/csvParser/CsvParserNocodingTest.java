package com.xceptance.xlt.nocoding.parser.csvParser;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.ParserTest;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.action.Action;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.StaticSubrequest;

public class CsvParserNocodingTest extends ParserTest
{
    protected final String path = super.path + "csv/";

    protected final String fileSimpleFile = path + "simple.csv";

    protected final String fileStaticFile = path + "static.csv";

    /**
     * Verifies a simple csv file can be parsed
     * 
     * @throws Exception
     */
    @Test
    public void testSimpleFileParsing() throws Exception
    {
        final String url = "http://hostname/";
        final Parser parser = new CsvParserNocoding(fileSimpleFile);
        final List<ScriptItem> scriptItems = parser.parse();

        Assert.assertEquals(1, scriptItems.size());
        Assert.assertTrue(scriptItems.get(0) instanceof Action);
        final Action action = (Action) scriptItems.get(0);
        Assert.assertEquals(1, action.getActionItems().size());
        Assert.assertTrue(action.getActionItems().get(0) instanceof Request);
        Assert.assertEquals(url, ((Request) action.getActionItems().get(0)).getUrl());
    }

    /**
     * Verifies static requests are parsed correctly
     * 
     * @throws Exception
     */
    @Test
    public void testStaticFileParsing() throws Exception
    {
        final String url = "http://www.xceptance.net";
        final Parser parser = new CsvParserNocoding(fileStaticFile);
        final List<ScriptItem> scriptItems = parser.parse();

        Assert.assertEquals(1, scriptItems.size());
        Assert.assertTrue(scriptItems.get(0) instanceof Action);
        final Action action = (Action) scriptItems.get(0);
        Assert.assertEquals(2, action.getActionItems().size());
        // Request
        Assert.assertTrue(action.getActionItems().get(0) instanceof Request);
        Assert.assertEquals(url, ((Request) action.getActionItems().get(0)).getUrl());

        // Subrequest
        Assert.assertTrue(action.getActionItems().get(1) instanceof StaticSubrequest);
        final List<String> urls = ((StaticSubrequest) action.getActionItems().get(1)).getUrls();
        Assert.assertEquals(4, urls.size());
        for (final String urlOfList : urls)
        {
            Assert.assertEquals(url, urlOfList);
        }
    }

    /**
     * Verifies CsvFeature.SKIP_EMTPY_LINES skips empty lines<br>
     * Fails until https://github.com/FasterXML/jackson-dataformats-text/issues/15 is closed
     * 
     * @throws IOException
     */
    @Test
    public void testSkipEmtpyLinesWithNewLine() throws IOException
    {
        final String csv = "Name, URL, RegExp\n" + "#Some comment\n" + "\n" + "ActionName, ActionUrl, ActionRegExp";
        final CsvMapper mapper = new CsvMapper();

        // Configure mapper:
        // Needed so we read everything with readTree
        // Needed so the comments are skipped
        mapper.enable(Feature.ALLOW_YAML_COMMENTS);
        // mapper.enable(CsvParser.Feature.SKIP_EMPTY_LINES);
        mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);

        // Create a schema that uses the first line as header
        final CsvSchema schema = mapper.schemaWithHeader();
        // mapper.setPropertyNamingStrategy(s);
        final MappingIterator<JsonNode> iterator = mapper.readerFor(JsonNode.class)
                                                         .with(CsvParser.Feature.SKIP_EMPTY_LINES)
                                                         .with(schema)
                                                         .readValues(csv);

        while (iterator.hasNext())
        {
            final JsonNode node = iterator.next();
            final Iterator<String> fieldNames = node.fieldNames();
            int nodeCount = 0;
            while (fieldNames.hasNext())
            {
                final String fieldName = fieldNames.next();
                if (nodeCount == 0)
                {
                    Assert.assertFalse("Empty name node found", node.get(fieldName).asText().isEmpty());
                }
            }
            nodeCount++;
        }
    }

    /**
     * Verifies CsvFeature.SKIP_EMTPY_LINES doesn't skip fully defined empty lines<br>
     * Fails until https://github.com/FasterXML/jackson-dataformats-text/issues/15 is closed
     * 
     * @throws IOException
     */
    @Test
    public void testSkipEmtpyLinesWithEmptyData() throws IOException
    {
        final String csv = "Name, URL, RegExp\n" + "#Some comment\n" + ",,,\n" + "ActionName, ActionUrl, ActionRegExp";
        final CsvMapper mapper = new CsvMapper();

        // Configure mapper:
        // Needed so we read everything with readTree
        // Needed so the comments are skipped
        mapper.enable(Feature.ALLOW_YAML_COMMENTS);
        mapper.enable(CsvParser.Feature.SKIP_EMPTY_LINES);
        mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);

        // Create a schema that uses the first line as header
        final CsvSchema schema = mapper.schemaWithHeader();
        // mapper.setPropertyNamingStrategy(s);
        final MappingIterator<JsonNode> iterator = mapper.readerFor(JsonNode.class).with(schema).readValues(csv);

        int nodeCount = 0;
        while (iterator.hasNext())
        {
            final JsonNode node = iterator.next();
            final Iterator<String> fieldNames = node.fieldNames();
            while (fieldNames.hasNext())
            {
                final String fieldName = fieldNames.next();
                if (nodeCount != 0)
                {
                    Assert.assertFalse("Empty name node found", node.get(fieldName).asText().isEmpty());
                }
            }
            nodeCount++;
        }
    }

}
