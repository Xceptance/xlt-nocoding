package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.response;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Validator;
import com.xceptance.xlt.nocoding.util.Constants;

public class ValidationParserTest
{

    @Test(expected = IllegalArgumentException.class)
    public void testSyntaxErrorResponseValidationTwoValidationModes() throws Exception
    {
        final JsonNodeFactory jf = new JsonNodeFactory(false);
        final ObjectNode content = jf.objectNode();
        content.put(Constants.REGEXP, "pattern");
        content.put(Constants.TEXT, "pattern");
        content.put(Constants.MATCHES, "pattern");
        final ObjectNode name = jf.objectNode();
        name.set("val_Name_1", content);

        final ArrayNode validate = jf.arrayNode();
        validate.add(name);
        new ValidationParser().parse(validate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSyntaxErrorResponseValidationTwoSelections() throws Exception
    {
        final JsonNodeFactory jf = new JsonNodeFactory(false);
        final ObjectNode content = jf.objectNode();
        content.put(Constants.REGEXP, "pattern");
        content.put(Constants.XPATH, "xpath");
        content.put(Constants.MATCHES, "pattern");
        final ObjectNode name = jf.objectNode();
        name.set("val_Name_1", content);

        final ArrayNode validate = jf.arrayNode();
        validate.add(name);
        new ValidationParser().parse(validate);
    }

    @Test
    public void testValidationParsing() throws Exception
    {
        final JsonNodeFactory jf = new JsonNodeFactory(false);
        final ObjectNode content = jf.objectNode();
        content.put(Constants.REGEXP, "pattern");
        content.put(Constants.XPATH, "xpath");
        content.put(Constants.MATCHES, "pattern");
        final ObjectNode name = jf.objectNode();
        name.set("val_Name_1", content);

        final ArrayNode validate = jf.arrayNode();
        validate.add(name);
        final Validator validator = new ValidationParser().parse(validate).get(0);

    }

}
