package com.xceptance.xlt.nocoding.rebuild.util;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.xceptance.xlt.nocoding.util.ParserUtils;

public class ParserUtilsTest
{

    protected String booleanString = "booleanString : true";

    protected String singleDecimalString = "singleDecimal : 1";

    protected String multipleDecimalString = "multipleDecimal : 123";

    protected String emtpyString = "empty : ";

    protected String arrayString = "array : \n" + "    - element_1 : value_1\n" + "    - element_2 : value_2";

    protected String simpleString = "simple : string";

    @Test
    public void testGetNodeAt() throws IOException
    {
        final YAMLFactory yaml = new YAMLFactory();
        final JsonParser parser = yaml.createParser(emtpyString);
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode nodes = mapper.readTree(parser);
        final ObjectNode objectNode = ParserUtils.getNodeAt(parser);
        final Iterator<JsonNode> iterator = objectNode.elements();
    }

}
