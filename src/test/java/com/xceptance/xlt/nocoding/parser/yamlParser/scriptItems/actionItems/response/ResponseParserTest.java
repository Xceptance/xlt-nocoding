package com.xceptance.xlt.nocoding.parser.yamlParser.scriptItems.actionItems.response;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xceptance.xlt.nocoding.parser.Parser;
import com.xceptance.xlt.nocoding.parser.ParserTest;
import com.xceptance.xlt.nocoding.parser.yamlParser.YamlParser;
import com.xceptance.xlt.nocoding.scriptItem.action.response.AbstractResponseItem;
import com.xceptance.xlt.nocoding.scriptItem.action.response.HttpcodeValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Validator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.CookieExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.RegexpExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.store.ResponseStore;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * Tests for parsing the Response Tag
 * 
 * @author ckeiner
 */
public class ResponseParserTest extends ParserTest
{
    protected final String path = super.path + "actionItems/response/";

    protected final String fileSyntaxErrorResponse = path + "syntaxErrorResponse.yml";

    protected final String fileSyntaxErrorResponseArrayNotObject = path + "syntaxErrorResponseArrayNotObject.yml";

    protected final String fileSyntaxErrorResponseStoreItemArrayNotObject = path + "syntaxErrorResponseStoreItemArrayNotObject.yml";

    protected final String fileSyntaxErrorResponseStoreObjectNotArray = path + "syntaxErrorResponseStoreObjectNotArray.yml";

    protected final String fileSyntaxErrorResponseValidationItemArrayNotObject = path
                                                                                 + "syntaxErrorResponseValidationItemArrayNotObject.yml";

    protected final String fileSyntaxErrorResponseValidationObjectNotArray = path + "syntaxErrorResponseValidationObjectNotArray.yml";

    /**
     * Verifies an error happens when "Response" has an invalid tag
     * 
     * @throws Exception
     */
    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorResponseParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorResponse);
        parser.parse();
    }

    /**
     * Verifies an error happens when "Response" has an array beneath it and not objects
     * 
     * @throws Exception
     */
    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorResponseArrayNotObjectParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorResponseArrayNotObject);
        parser.parse();
    }

    /**
     * Verifies an error happens when the variable beneath "Store" beneath "Response" has an array beneath it and not
     * objects
     * 
     * @throws Exception
     */
    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorResponseStoreItemArrayNotObjectParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorResponseStoreItemArrayNotObject);
        parser.parse();
    }

    /**
     * Verifies an error happens when "Store" beneath "Response" has an object beneath it and not an array
     * 
     * @throws Exception
     */
    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorResponseStoreObjectNotArrayParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorResponseStoreObjectNotArray);
        parser.parse();
    }

    /**
     * Verifies an error happens when the validation beneath "Validate" beneath "Response" has an array beneath it and not
     * objects
     * 
     * @throws Exception
     */
    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorResponseValidationItemArrayNotObjectParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorResponseValidationItemArrayNotObject);
        parser.parse();
    }

    /**
     * Verifies an error happens when "Validate" beneath "Response" has an object beneath it and not an array
     * 
     * @throws Exception
     */
    @Test(expected = JsonParseException.class)
    public void testSyntaxErrorResponseValidationObjectNotArrayParsing() throws Exception
    {
        final Parser parser = new YamlParser(fileSyntaxErrorResponseValidationObjectNotArray);
        parser.parse();
    }

    /**
     * Verifies Response is parsed correctly
     * 
     * @throws Exception
     */
    @Test
    public void testResponseParsing() throws Exception
    {
        final String httpcode = "200";
        final String validation_extractionExpression = ".*";
        final String validation_name = "validationName";
        final String store_extractionExpression = "cookieName";
        final String store_name = "variableName";
        final JsonNodeFactory jf = new JsonNodeFactory(false);

        // Lowest level validation content -> Extractor, ValidationMethod, Group
        final ObjectNode singleValidationContent = jf.objectNode();
        singleValidationContent.put(Constants.REGEXP, validation_extractionExpression);
        // Name of the validation
        final ObjectNode validationName = jf.objectNode();
        validationName.set(validation_name, singleValidationContent);
        // Save all validation in the ArrayNode
        final ArrayNode validationContent = jf.arrayNode();
        validationContent.add(validationName);

        // Lowest level store content -> Extractor, Group
        final ObjectNode singleStoreContent = jf.objectNode();
        singleStoreContent.put(Constants.COOKIE, store_extractionExpression);
        // Name of the variable
        final ObjectNode storeName = jf.objectNode();
        storeName.set(store_name, singleStoreContent);
        // Save all stores in the ArrayNode
        final ArrayNode storeContent = jf.arrayNode();
        storeContent.add(storeName);

        // Build the response node
        final ObjectNode responseNode = jf.objectNode();
        responseNode.put(Constants.HTTPCODE, httpcode);
        responseNode.set(Constants.VALIDATION, validationContent);
        responseNode.set(Constants.STORE, storeContent);

        // Parse response
        final Response response = (Response) new ResponseParser().parse(responseNode).get(0);

        // Verify response is correct
        final List<AbstractResponseItem> responseItems = response.getResponseItems();
        Assert.assertTrue(responseItems.get(0) instanceof HttpcodeValidator);
        Assert.assertEquals(httpcode, ((HttpcodeValidator) responseItems.get(0)).getHttpcode());
        Assert.assertTrue(responseItems.get(1) instanceof Validator);
        final Validator validator = (Validator) responseItems.get(1);
        Assert.assertEquals(validation_name, validator.getValidationName());
        Assert.assertTrue(validator.getExtractor() instanceof RegexpExtractor);
        Assert.assertEquals(validation_extractionExpression, validator.getExtractor().getExtractionExpression());
        Assert.assertNull(validator.getMethod());

        Assert.assertTrue(responseItems.get(2) instanceof ResponseStore);
        final ResponseStore store = (ResponseStore) responseItems.get(2);
        Assert.assertEquals(store_name, store.getVariableName());
        Assert.assertTrue(store.getExtractor() instanceof CookieExtractor);
        Assert.assertEquals(store_extractionExpression, store.getExtractor().getExtractionExpression());
    }

}
