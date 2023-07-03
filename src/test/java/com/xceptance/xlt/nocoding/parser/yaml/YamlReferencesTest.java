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

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.yaml.snakeyaml.parser.ParserException;

import com.xceptance.xlt.nocoding.command.Command;
import com.xceptance.xlt.nocoding.command.action.Action;
import com.xceptance.xlt.nocoding.parser.AbstractParserTest;
import com.xceptance.xlt.nocoding.parser.Parser;

public class YamlReferencesTest extends AbstractParserTest
{

    protected final String path = super.path + "references/";

    protected final String fileReferenceParsing = path + "referenceParsing.yml";

    protected final String fileReferenceParsingComplexError = path + "referenceParsingComplexError.yml";

    protected final String fileReferenceParsingMappingError = path + "referenceParsingMappingError.yml";

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
     * Verifies a misplaced anchor throws an error.<br>
     * It is <b>not</b> checked for line numbers. See issue #5 for more information.
     * 
     * @throws IOException
     */
    @Test(expected = ParserException.class)
    public void complexReferenceParsing() throws IOException
    {
        final Parser parser = new YamlParser();
        parser.parse(fileReferenceParsingComplexError);
    }

    /**
     * Verifies a more complex play of anchors throws an error.<br>
     * It is <b>not</b> checked for line numbers. See issue #5 for more information.
     * 
     * @throws IOException
     */
    @Test(expected = ParserException.class)
    public void mappingReferenceParsing() throws IOException
    {
        final Parser parser = new YamlParser();
        parser.parse(fileReferenceParsingMappingError);
    }
}
