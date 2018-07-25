package com.xceptance.xlt.nocoding.parser.csv;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.AbstractParserTest;
import com.xceptance.xlt.nocoding.parser.csv.CsvParser;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.action.Action;
import com.xceptance.xlt.nocoding.scriptItem.action.request.Request;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.StaticSubrequest;

public class CsvParserTest extends AbstractParserTest
{
    protected final String path = super.path + "csv/";

    protected final String fileSimpleFile = path + "simple.csv";

    protected final String fileStaticFile = path + "static.csv";

    protected final String fileRegexpStoreWithLettersFile = path + "regexpStoreHeaderWithLetters.csv";

    protected final String fileRegexpStoreFile = path + "regexpStoreHeader.csv";

    protected final String fileXpathStoreWithLettersFile = path + "xpathStoreHeaderWithLetters.csv";

    protected final String fileXpathStoreFile = path + "xpathStoreHeader.csv";

    /**
     * Verifies a simple csv file can be parsed
     * 
     * @throws IOException
     */
    @Test
    public void testSimpleFileParsing() throws IOException
    {
        final String url = "http://hostname/";
        final Parser parser = new CsvParser();
        final List<ScriptItem> scriptItems = parser.parse(fileSimpleFile);

        Assert.assertEquals(1, scriptItems.size());
        Assert.assertTrue(scriptItems.get(0) instanceof Action);
        final Action action = (Action) scriptItems.get(0);
        Assert.assertEquals(2, action.getActionItems().size());
        Assert.assertTrue(action.getActionItems().get(0) instanceof Request);
        Assert.assertEquals(url, ((Request) action.getActionItems().get(0)).getUrl());
    }

    /**
     * Verifies static requests are parsed correctly
     * 
     * @throws IOException
     */
    @Test
    public void testStaticFileParsing() throws IOException
    {
        final String url = "http://www.xceptance.net";
        final Parser parser = new CsvParser();
        final List<ScriptItem> scriptItems = parser.parse(fileStaticFile);

        Assert.assertEquals(1, scriptItems.size());
        Assert.assertTrue(scriptItems.get(0) instanceof Action);
        final Action action = (Action) scriptItems.get(0);
        Assert.assertEquals(3, action.getActionItems().size());
        // Request
        Assert.assertTrue(action.getActionItems().get(0) instanceof Request);
        Assert.assertEquals(url, ((Request) action.getActionItems().get(0)).getUrl());

        // Response
        Assert.assertTrue(action.getActionItems().get(1) instanceof Response);

        // Subrequest
        Assert.assertTrue(action.getActionItems().get(2) instanceof StaticSubrequest);
        final List<String> urls = ((StaticSubrequest) action.getActionItems().get(2)).getUrls();
        Assert.assertEquals(4, urls.size());
        for (final String urlOfList : urls)
        {
            Assert.assertEquals(url, urlOfList);
        }
    }

    /**
     * Verifies correctly formed Regexp Store header can be parsed
     * 
     * @throws IOException
     */
    @Test
    public void testRegexpStoreParsing() throws IOException
    {
        final Parser parser = new CsvParser();
        parser.parse(fileRegexpStoreFile);
    }

    /**
     * Verifies correctly formed Xpath Store header can be parsed
     * 
     * @throws IOException
     */
    @Test
    public void testXpathStoreParsing() throws IOException
    {
        final Parser parser = new CsvParser();
        parser.parse(fileXpathStoreFile);
    }

    /**
     * Verifies a Regexp Store header with letters can be parsed
     * 
     * @throws IOException
     */
    @Test
    public void testRegexpStoreWithLettersParsing() throws IOException
    {
        final Parser parser = new CsvParser();
        parser.parse(fileRegexpStoreWithLettersFile);
    }

    /**
     * Verifies a Xpath Store header with letters can be parsed
     */
    @Test
    public void testXpathStoreWithLettersParsing() throws IOException
    {
        final Parser parser = new CsvParser();
        parser.parse(fileXpathStoreWithLettersFile);
    }

}
