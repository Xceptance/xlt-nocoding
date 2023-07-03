/*
 * Copyright (c) 2013-2023 Xceptance Software Technologies GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xceptance.xlt.nocoding.parser.yaml;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.yaml.snakeyaml.parser.ParserException;

import com.xceptance.xlt.nocoding.command.Command;
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

}
