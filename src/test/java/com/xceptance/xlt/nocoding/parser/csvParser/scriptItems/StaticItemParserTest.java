package com.xceptance.xlt.nocoding.parser.csvParser.scriptItems;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xceptance.xlt.nocoding.parser.csvParser.CsvConstants;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.StaticSubrequest;

public class StaticItemParserTest extends AbstractScriptItemParserTest
{

    /**
     * Tests parsing with only url specified
     */
    @Test
    public void testParse()
    {
        final String type = CsvConstants.TYPE_STATIC;
        final String url = "url";
        final JsonNodeFactory nodeFactory = new JsonNodeFactory(false);
        final ObjectNode actionNodeContent = nodeFactory.objectNode();
        actionNodeContent.put(CsvConstants.TYPE, type);
        actionNodeContent.put(CsvConstants.URL, url);

        final StaticItemParser parser = new StaticItemParser();
        final StaticSubrequest subrequest = parser.parse(actionNodeContent);
        Assert.assertEquals(1, subrequest.getUrls().size());
        Assert.assertEquals(url, subrequest.getUrls().get(0));
    }

}
