package com.xceptance.xlt.nocoding.util.action.data;

import java.net.MalformedURLException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.data.GeneralDataProvider;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.util.bsh.ParameterInterpreter;

public class YAMLBasedURLActionDataListBuilderTest
{
    ParameterInterpreter interpreter;

    private XltProperties properties;

    private GeneralDataProvider dataProvider;

    private final String path = "./config/data/test/";

    private final String fileTmp = path + "tmp.yml";

    private final String fileXhrSubrequests = path + "xhrSubrequests.yml";

    private final String fileTestData = path + "testData.yml";

    private final String fileEmptyFile = path + "emptyFile.yml";

    private final String fileNotExistingFile = path + "notExistingFile.yml";

    private final String fileSingleActionNoDefaultsData = path + "SAND.yml";

    private final String fileStaticSubrequests = path + "staticSubrequests.yml";

    private final String fileComplexTestCase = path + "complexTestCase.yml";

    private final String fileSyntaxErrorRoot = path + "syntaxErrorRoot.yml";

    private final String fileSyntaxErrorAction = path + "syntaxErrorAction.yml";

    private final String fileSyntaxErrorRequest = path + "syntaxErrorRequest.yml";

    private final String fileSyntaxErrorResponse = path + "syntaxErrorResponse.yml";

    private final String fileSyntaxErrorSubrequests = path + "syntaxErrorSubrequests.yml";

    private final String fileSyntaxErrorXhr = path + "syntaxErrorXhr.yml";

    private final String fileSyntaxErrorStatic = path + "syntaxErrorStatic.yml";

    private final String fileSyntaxErrorActionNameNull = path + "syntaxErrorActionNameNull.yml";

    private final String fileSyntaxErrorUrlNull = path + "syntaxErrorUrlNull.yml";

    private final URLActionDataBuilder actionBuilder = new URLActionDataBuilder();

    private final URLActionDataStoreBuilder storeBuilder = new URLActionDataStoreBuilder();

    private final URLActionDataValidationBuilder validationBuilder = new URLActionDataValidationBuilder();

    @Before
    public void setup()
    {
        properties = XltProperties.getInstance();
        dataProvider = GeneralDataProvider.getInstance();
        interpreter = new ParameterInterpreter(properties, dataProvider);
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testCorrectConstructor()
    {
        @SuppressWarnings("unused")
        final YAMLBasedURLActionDataListBuilder listBuilder = new YAMLBasedURLActionDataListBuilder(fileTestData, interpreter,
                                                                                                    actionBuilder, validationBuilder,
                                                                                                    storeBuilder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOutputForUnExistingFile()
    {
        final YAMLBasedURLActionDataListBuilder listBuilder = new YAMLBasedURLActionDataListBuilder(fileNotExistingFile, interpreter,
                                                                                                    actionBuilder, validationBuilder,
                                                                                                    storeBuilder);
        final List<URLActionData> actions = listBuilder.buildURLActionDataList();
        Assert.assertTrue(actions.isEmpty());
    }

    @Test
    public void testOutputForEmptyFile()
    {
        final YAMLBasedURLActionDataListBuilder listBuilder = new YAMLBasedURLActionDataListBuilder(fileEmptyFile, interpreter,
                                                                                                    actionBuilder, validationBuilder,
                                                                                                    storeBuilder);
        final List<URLActionData> actions = listBuilder.buildURLActionDataList();
        Assert.assertTrue(actions.isEmpty());
    }

    @Test
    public void testOutputForDefaultValues()
    {
        final YAMLBasedURLActionDataListBuilder listBuilder = new YAMLBasedURLActionDataListBuilder(fileTestData, interpreter,
                                                                                                    actionBuilder, validationBuilder,
                                                                                                    storeBuilder);
        @SuppressWarnings("unused")
        final List<URLActionData> actions = listBuilder.buildURLActionDataList();
    }

    @Test
    public void testBuildSingleActionWithoutDefaults() throws MalformedURLException
    {
        final YAMLBasedURLActionDataListBuilder listBuilder = new YAMLBasedURLActionDataListBuilder(fileSingleActionNoDefaultsData,
                                                                                                    interpreter, actionBuilder,
                                                                                                    validationBuilder, storeBuilder);
        final List<URLActionData> actions = listBuilder.buildURLActionDataList();

        Assert.assertFalse(actions.isEmpty());
        final URLActionData action = actions.get(0);

        Assert.assertEquals("name", action.getName());
        Assert.assertEquals("http://www.xceptance.com", action.getUrl().toString());
        Assert.assertEquals(URLActionData.TYPE_XHR, action.getType());
        Assert.assertTrue(action.isXHRAction());
        Assert.assertEquals(URLActionData.METHOD_GET, action.getMethod().toString());
        Assert.assertFalse(action.encodeParameters());
        Assert.assertEquals("body", action.getBody());
        Assert.assertEquals(400, action.getResponseCodeValidator().getHttpResponseCode());

        final List<NameValuePair> headers = action.getHeaders();
        Assert.assertFalse(headers.isEmpty());

        for (int i = 0; i < headers.size(); i++)
        {
            final NameValuePair nvp = headers.get(i);
            Assert.assertEquals("header_" + (i + 1), nvp.getName());
            Assert.assertEquals("header_value_" + (i + 1), nvp.getValue());
        }

        final List<NameValuePair> parameters = action.getParameters();
        Assert.assertFalse(parameters.isEmpty());

        for (int i = 0; i < parameters.size(); i++)
        {
            final NameValuePair nvp = parameters.get(i);
            Assert.assertEquals("parameter_" + (i + 1), nvp.getName());
            Assert.assertEquals("parameter_value_" + (i + 1), nvp.getValue());
        }

        final List<URLActionDataStore> store = action.getStore();
        Assert.assertFalse(store.isEmpty());

        final URLActionDataStore store1 = store.get(0);

        Assert.assertEquals("variable_1", store1.getName());
        Assert.assertEquals("XPath", store1.getSelectionMode());
        Assert.assertEquals("xpath_1", store1.getSelectionContent());

        final URLActionDataStore store2 = store.get(1);

        Assert.assertEquals("variable_2", store2.getName());
        Assert.assertEquals("Regex", store2.getSelectionMode());
        Assert.assertEquals("xpath_2", store2.getSelectionContent());

        final List<URLActionDataValidation> validations = action.getValidations();
        Assert.assertFalse(validations.isEmpty());

        final URLActionDataValidation validation1 = validations.get(0);

        Assert.assertEquals("validation_name_1", validation1.getName());
        Assert.assertEquals(URLActionDataValidation.XPATH, validation1.getSelectionMode());
        Assert.assertEquals("xpath_value_1", validation1.getSelectionContent());
        Assert.assertEquals(URLActionDataValidation.EXISTS, validation1.getValidationMode());
        Assert.assertNull(validation1.getValidationContent());

        final URLActionDataValidation validation2 = validations.get(1);

        Assert.assertEquals("validation_name_2", validation2.getName());
        Assert.assertEquals(URLActionDataValidation.REGEXP, validation2.getSelectionMode());
        Assert.assertEquals("regexp_value_2", validation2.getSelectionContent());
        Assert.assertEquals(URLActionDataValidation.MATCHES, validation2.getValidationMode());
        Assert.assertEquals("matches_value_2", validation2.getValidationContent());

    }

    @Test
    public void testStaticSubrequestCreation() throws MalformedURLException
    {
        final YAMLBasedURLActionDataListBuilder listBuilder = new YAMLBasedURLActionDataListBuilder(fileStaticSubrequests, interpreter,
                                                                                                    actionBuilder, validationBuilder,
                                                                                                    storeBuilder);
        final List<URLActionData> actions = listBuilder.buildURLActionDataList();
        Assert.assertFalse(actions.isEmpty());

        final URLActionData static_action_1 = actions.get(1);

        Assert.assertEquals("https://www.xceptance.com/images/xceptance-logo-transparent-202px.png", static_action_1.getUrl().toString());
        Assert.assertEquals(URLActionData.TYPE_STATIC, static_action_1.getType());
        Assert.assertTrue(static_action_1.isStaticContent());
        Assert.assertEquals(URLActionData.METHOD_GET, static_action_1.getMethod().toString());

        final URLActionData static_action_2 = actions.get(2);

        Assert.assertEquals("https://www.xceptance.com/images/xlt-logo-small.png", static_action_2.getUrl().toString());
        Assert.assertEquals(URLActionData.TYPE_STATIC, static_action_2.getType());
        Assert.assertTrue(static_action_2.isStaticContent());
        Assert.assertEquals(URLActionData.METHOD_GET, static_action_2.getMethod().toString());
    }

    @Test
    public void testXhrSubrequestCreation() throws MalformedURLException
    {
        final YAMLBasedURLActionDataListBuilder listBuilder = new YAMLBasedURLActionDataListBuilder(fileXhrSubrequests, interpreter,
                                                                                                    actionBuilder, validationBuilder,
                                                                                                    storeBuilder);
        final List<URLActionData> actions = listBuilder.buildURLActionDataList();
        Assert.assertFalse(actions.isEmpty());

    }

    @Test
    public void testXomplexTestCase() throws MalformedURLException
    {
        final YAMLBasedURLActionDataListBuilder listBuilder = new YAMLBasedURLActionDataListBuilder(fileComplexTestCase, interpreter,
                                                                                                    actionBuilder, validationBuilder,
                                                                                                    storeBuilder);
        final List<URLActionData> actions = listBuilder.buildURLActionDataList();
        Assert.assertFalse(actions.isEmpty());

    }

    @Test
    public void testTmp() throws MalformedURLException
    {
        final YAMLBasedURLActionDataListBuilder listBuilder = new YAMLBasedURLActionDataListBuilder(fileTmp, interpreter, actionBuilder,
                                                                                                    validationBuilder, storeBuilder);
        @SuppressWarnings("unused")
        final List<URLActionData> actions = listBuilder.buildURLActionDataList();
    }

    @Test
    public void testSyntaxErrorRoot() throws MalformedURLException
    {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Invalid list item");

        final YAMLBasedURLActionDataListBuilder listBuilder = new YAMLBasedURLActionDataListBuilder(fileSyntaxErrorRoot, interpreter,
                                                                                                    actionBuilder, validationBuilder,
                                                                                                    storeBuilder);

        @SuppressWarnings("unused")
        final List<URLActionData> actions = listBuilder.buildURLActionDataList();
    }

    @Test
    public void testSyntaxErrorAction() throws MalformedURLException
    {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("is not a valid child of \"Action\"");

        final YAMLBasedURLActionDataListBuilder listBuilder = new YAMLBasedURLActionDataListBuilder(fileSyntaxErrorAction, interpreter,
                                                                                                    actionBuilder, validationBuilder,
                                                                                                    storeBuilder);

        @SuppressWarnings("unused")
        final List<URLActionData> actions = listBuilder.buildURLActionDataList();
    }

    @Test
    public void testSyntaxErrorActionNameNull() throws MalformedURLException
    {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Name of \"Action\" cannot be Null");

        final YAMLBasedURLActionDataListBuilder listBuilder = new YAMLBasedURLActionDataListBuilder(fileSyntaxErrorActionNameNull,
                                                                                                    interpreter, actionBuilder,
                                                                                                    validationBuilder, storeBuilder);

        @SuppressWarnings("unused")
        final List<URLActionData> actions = listBuilder.buildURLActionDataList();
    }

    @Test
    public void testSyntaxErrorRequest() throws MalformedURLException
    {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("is not a valid child of \"Request\"");

        final YAMLBasedURLActionDataListBuilder listBuilder = new YAMLBasedURLActionDataListBuilder(fileSyntaxErrorRequest, interpreter,
                                                                                                    actionBuilder, validationBuilder,
                                                                                                    storeBuilder);

        @SuppressWarnings("unused")
        final List<URLActionData> actions = listBuilder.buildURLActionDataList();
    }

    @Test
    public void testSyntaxErrorUrlNull() throws MalformedURLException
    {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("'Url' cannot be null");

        final YAMLBasedURLActionDataListBuilder listBuilder = new YAMLBasedURLActionDataListBuilder(fileSyntaxErrorUrlNull, interpreter,
                                                                                                    actionBuilder, validationBuilder,
                                                                                                    storeBuilder);

        @SuppressWarnings("unused")
        final List<URLActionData> actions = listBuilder.buildURLActionDataList();
    }

    @Test
    public void testSyntaxErrorResponse() throws MalformedURLException
    {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("is not a valid child of \"Response\"");

        final YAMLBasedURLActionDataListBuilder listBuilder = new YAMLBasedURLActionDataListBuilder(fileSyntaxErrorResponse, interpreter,
                                                                                                    actionBuilder, validationBuilder,
                                                                                                    storeBuilder);

        @SuppressWarnings("unused")
        final List<URLActionData> actions = listBuilder.buildURLActionDataList();
    }

    @Test
    public void testSyntaxErrorSubrequests() throws MalformedURLException
    {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("is not a valid child of \"Subrequests\"");

        final YAMLBasedURLActionDataListBuilder listBuilder = new YAMLBasedURLActionDataListBuilder(fileSyntaxErrorSubrequests, interpreter,
                                                                                                    actionBuilder, validationBuilder,
                                                                                                    storeBuilder);

        @SuppressWarnings("unused")
        final List<URLActionData> actions = listBuilder.buildURLActionDataList();
    }

    @Test
    public void testSyntaxErrorXhr() throws MalformedURLException
    {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("is not a valid child of \"Xhr\"");

        final YAMLBasedURLActionDataListBuilder listBuilder = new YAMLBasedURLActionDataListBuilder(fileSyntaxErrorXhr, interpreter,
                                                                                                    actionBuilder, validationBuilder,
                                                                                                    storeBuilder);

        @SuppressWarnings("unused")
        final List<URLActionData> actions = listBuilder.buildURLActionDataList();
    }

    @Test
    public void testSyntaxErrorStatic() throws MalformedURLException
    {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("its value is not an array list");

        final YAMLBasedURLActionDataListBuilder listBuilder = new YAMLBasedURLActionDataListBuilder(fileSyntaxErrorStatic, interpreter,
                                                                                                    actionBuilder, validationBuilder,
                                                                                                    storeBuilder);

        @SuppressWarnings("unused")
        final List<URLActionData> actions = listBuilder.buildURLActionDataList();
    }

}
