package com.xceptance.xlt.nocoding.rebuild.parser;

import org.junit.Test;

import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.YamlParser;

public class YamlParserTest
{
    Parser parser;

    /*
     * Constructor Tests
     */

    // @Test
    // public void correctConstructor() throws Exception
    // {
    // this.parser = new YamlParser("./config/data/TLOrder.yml");
    // parser.parse();
    // }

    @Test(expected = NullPointerException.class)
    public void noFileName() throws Exception
    {
        this.parser = new YamlParser(null);
        parser.parse();
    }

    @Test
    public void invalidFileName() throws Exception
    {
        this.parser = new YamlParser("test");
        parser.parse();
    }

    @Test
    public void noYmlFile() throws Exception
    {
        this.parser = new YamlParser("test.csv");
        parser.parse();
    }

    // /*
    // * Specific Values
    // */
    //
    // @Test
    // public void testDefaultValues()
    // {
    // this.parser = new YamlParser("./config/data/TLOrder.yml");
    // final List<ScriptItem> scriptItems = parser.parse();
    // }
    //
    // @Test
    // public void testActionRequestValues()
    // {
    // this.parser = new YamlParser("./config/data/TLOrder.yml");
    // final List<ScriptItem> scriptItems = parser.parse();
    // }
    //
    // @Test
    // public void testActionResponseValues()
    // {
    // this.parser = new YamlParser("./config/data/TLOrder.yml");
    // final List<ScriptItem> scriptItems = parser.parse();
    // }
    //
    // @Test
    // public void testActionStaticSubrequestValues()
    // {
    // this.parser = new YamlParser("./config/data/TLOrder.yml");
    // final List<ScriptItem> scriptItems = parser.parse();
    // }
    //
    // @Test
    // public void testActionXhrSubrequestValues()
    // {
    // this.parser = new YamlParser("./config/data/TLOrder.yml");
    // final List<ScriptItem> scriptItems = parser.parse();
    // }
    //
    // @Test
    // public void testActionXhrSubrequestWithResponseValues()
    // {
    // this.parser = new YamlParser("./config/data/TLOrder.yml");
    // final List<ScriptItem> scriptItems = parser.parse();
    // }
    //
    // @Test
    // public void testStoreValues()
    // {
    // this.parser = new YamlParser("./config/data/TLOrder.yml");
    // final List<ScriptItem> scriptItems = parser.parse();
    // }
    //
    // @Test
    // public void testDefaultAndDeleteValues()
    // {
    // this.parser = new YamlParser("./config/data/TLOrder.yml");
    // final List<ScriptItem> scriptItems = parser.parse();
    // }

}
