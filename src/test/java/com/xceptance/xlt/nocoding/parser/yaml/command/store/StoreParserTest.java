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
package com.xceptance.xlt.nocoding.parser.yaml.command.store;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.yaml.snakeyaml.parser.ParserException;

import com.xceptance.xlt.nocoding.command.Command;
import com.xceptance.xlt.nocoding.command.store.Store;
import com.xceptance.xlt.nocoding.command.store.StoreClear;
import com.xceptance.xlt.nocoding.parser.AbstractParserTest;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.yaml.YamlParser;

/**
 * Tests for parsing the "Store" tag
 *
 * @author ckeiner
 */
public class StoreParserTest extends AbstractParserTest
{
    protected String path = super.path + "store/";

    protected final String fileStore = path + "store.yml";

    protected final String fileStoreObjectNotArray = path + "syntaxErrorStoreObjectNotArray.yml";

    /**
     * Verifies two store items can be parsed
     *
     * @throws Exception
     */
    @Test
    public void testStoreParsing() throws Exception
    {
        final Parser parser = new YamlParser();
        final List<Command> scriptItems = parser.parse(fileStore);

        Assert.assertEquals(8, scriptItems.size());

        // Assert it's a StoreItem
        Assert.assertTrue(scriptItems.get(0) instanceof Store);
        Store storeItem = (Store) scriptItems.get(0);
        Assert.assertEquals("var_1", storeItem.getVariableName());
        Assert.assertEquals("val_1", storeItem.getValue());

        Assert.assertTrue(scriptItems.get(1) instanceof Store);
        storeItem = (Store) scriptItems.get(1);
        Assert.assertEquals("var_2", storeItem.getVariableName());
        Assert.assertEquals("val_1", storeItem.getValue());

        Assert.assertTrue(scriptItems.get(2) instanceof Store);
        storeItem = (Store) scriptItems.get(2);
        Assert.assertEquals("name_3", storeItem.getVariableName());
        Assert.assertEquals("${var_2}", storeItem.getValue());

        Assert.assertTrue(scriptItems.get(3) instanceof Store);
        storeItem = (Store) scriptItems.get(3);
        Assert.assertEquals("var_4", storeItem.getVariableName());
        Assert.assertEquals("${var_2}", storeItem.getValue());

        Assert.assertTrue(scriptItems.get(4) instanceof Store);
        storeItem = (Store) scriptItems.get(4);
        Assert.assertEquals("var_5", storeItem.getVariableName());
        Assert.assertEquals("${var_4}", storeItem.getValue());

        Assert.assertTrue(scriptItems.get(5) instanceof Store);
        storeItem = (Store) scriptItems.get(5);
        Assert.assertEquals("1234", storeItem.getVariableName());
        Assert.assertEquals("12", storeItem.getValue());

        Assert.assertTrue(scriptItems.get(6) instanceof Store);
        storeItem = (Store) scriptItems.get(6);
        Assert.assertEquals("var_1", storeItem.getVariableName());
        Assert.assertEquals("", storeItem.getValue());

        Assert.assertTrue(scriptItems.get(7) instanceof StoreClear);
    }

    /**
     * Verifies an error happens when "Store" has objects beneath it and not an array
     *
     * @throws Exception
     */
    @Test
    public void testStoreObjectParse() throws Exception
    {
        try
        {
            final Parser parser = new YamlParser();
            parser.parse(fileStoreObjectNotArray);
            Assert.assertFalse(true);
        }
        catch (final ParserException parserException)
        {
            Assert.assertEquals(0, parserException.getContextMark().getLine());
            Assert.assertEquals(1, parserException.getProblemMark().getLine());
        }
    }
}
