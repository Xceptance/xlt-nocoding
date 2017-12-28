package com.xceptance.xlt.nocoding.parser.csvParser;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

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

}
