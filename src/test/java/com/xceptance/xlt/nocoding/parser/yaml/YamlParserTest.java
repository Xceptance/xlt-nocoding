package com.xceptance.xlt.nocoding.parser.yaml;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.yaml.snakeyaml.parser.ParserException;

import com.xceptance.xlt.nocoding.command.Command;
import com.xceptance.xlt.nocoding.command.action.Action;
import com.xceptance.xlt.nocoding.parser.AbstractParserTest;
import com.xceptance.xlt.nocoding.parser.Parser;

/**
 * Tests for parsing multiple tags, i.e. "Store" and "Action"
 *
 * @author ckeiner
 */
public class YamlParserTest extends AbstractParserTest
{
    protected final String fileEmptyFile = path + "emptyFile.yml";

    protected final String fileReferenceParsing = path + "referenceParsing.yml";

    protected final String fileReferenceParsingComplexError = path + "referenceParsingComplexError.yml";

    protected final String fileNotExistingFile = path + "notExistingFile.yml";

    protected final String fileSyntaxErrorRoot = path + "syntaxErrorRoot.yml";

    protected final String fileSyntaxErrorRootObjectNotArray = path + "syntaxErrorRootObjectNotArray.yml";

    /**
     * Verifies an empty file can be parsed
     *
     * @throws Exception
     */
    @Test
    public void testEmptyFileParsing() throws Exception
    {
        final Parser parser = new YamlParser();
        final List<Command> scriptItems = parser.parse(fileEmptyFile);
        Assert.assertTrue(scriptItems.isEmpty());
    }

    /**
     * Verifies Yaml references can be parsed
     *
     * @throws Exception
     */
    @Test
    public void testReferenceFileParsing() throws Exception
    {
        final Parser parser = new YamlParser();
        final List<Command> scriptItems = parser.parse(fileReferenceParsing);
        Assert.assertEquals(2, scriptItems.size());
        final Command referencedCommand = scriptItems.get(0);
        final Command referencingCommand = scriptItems.get(1);
        Assert.assertTrue(referencedCommand instanceof Action);
        Assert.assertTrue(referencedCommand.getClass().isInstance(referencingCommand));

    }

    /**
     * Verifies a misplaced anchor throws an error.
     * 
     * @throws IOException
     */
    @Test
    public void complexReferenceParsing() throws IOException
    {
        final Parser parser = new YamlParser();
        try
        {
            parser.parse(fileReferenceParsingComplexError);
            Assert.assertFalse(true);
        }
        catch (final ParserException parserException)
        {
            Assert.assertEquals(22, parserException.getContextMark().getLine());
            Assert.assertEquals(14, parserException.getProblemMark().getLine());
        }
    }

    /**
     * Verifies an error is thrown if the file is not found
     *
     * @throws Exception
     */
    @Test(expected = FileNotFoundException.class)
    public void testNotExistingFileParsing() throws Exception
    {
        final Parser parser = new YamlParser();
        final List<Command> scriptItems = parser.parse(fileNotExistingFile);
        Assert.assertFalse(scriptItems.isEmpty());
        Assert.assertEquals(2, scriptItems.size());
    }

    /**
     * Verifies an error is thrown when an invalid list item is found
     *
     * @throws Exception
     */
    @Test(expected = ParserException.class)
    public void testSyntaxErrorRootParsing() throws Exception
    {
        final Parser parser = new YamlParser();
        parser.parse(fileSyntaxErrorRoot);
    }

    /**
     * Verifies an error is thrown when the list items are objects and not in an array
     *
     * @throws Exception
     */
    @Test(expected = ParserException.class)
    public void testSyntaxErrorRootObjectNotArrayParsing() throws Exception
    {
        final Parser parser = new YamlParser();
        parser.parse(fileSyntaxErrorRootObjectNotArray);
    }

    @Ignore
    @Test
    public void shoudlShowCorrectLineForReferences()
    {
        final String yamlSpec = "- Action :\n" + //
                                "   Name : Reference Action\n" + //
                                "   Request :\n" + //
                                "       Url : &id2\n" + //
                                "           Request\n" + //
                                "       Method : GET\n" + //
                                "   Response :\n" + //
                                "       Httpcode : 200\n" + //
                                "\n" + //
                                "- Action :\n" + //
                                "   Name : Reference Action\n" + //
                                "   Request : \n" + //
                                "       Url : &id1\n" + //
                                "           Request\n" + //
                                "       Method : GET\n" + //
                                "   Response :\n" + //
                                "       Httpcode : 200\n" + //
                                "\n" + //
                                "- Action :\n" + //
                                "\n" + //
                                "    *id2 : *id1"; //
        YamlParserTestHelper.parseToNode(yamlSpec);

    }

}
