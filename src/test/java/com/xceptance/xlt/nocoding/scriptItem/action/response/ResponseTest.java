package com.xceptance.xlt.nocoding.scriptItem.action.response;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.api.validators.HttpResponseCodeValidator;
import com.xceptance.xlt.nocoding.util.MockObjects;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Tests {@link Response}
 * 
 * @author ckeiner
 */
public class ResponseTest
{
    Context context;

    MockObjects mockObjects;

    /**
     * Sets {@link WebResponse} via {@link MockObjects#loadResponse()} in {@link Context}
     */
    @Before
    public void init()
    {
        context = new Context(XltProperties.getInstance());
        mockObjects = new MockObjects();
        mockObjects.loadResponse();
        context.setWebResponse(mockObjects.getResponse());
    }

    /**
     * Verifies a {@link HttpcodeValidator} is added when {@link Response#Response()} is used
     * 
     * @throws Throwable
     */
    @Test
    public void testFillDefaultNoResponseItems() throws Throwable
    {
        final Response response = new Response();
        response.execute(context);
        Assert.assertNotNull(response.getResponseItems());
        Assert.assertEquals(1, response.getResponseItems().size());
        Assert.assertTrue(response.getResponseItems().get(0) instanceof HttpcodeValidator);
    }

    /**
     * Verifies a {@link HttpcodeValidator} is added when {@link Response#Response(List)} is used and the specified
     * {@link List} does not contain a {@link HttpResponseCodeValidator}.
     * 
     * @throws Throwable
     */
    @Test
    public void testFillDefaultWithResponseItems() throws Throwable
    {
        final Response response = new Response(new ArrayList<AbstractResponseItem>());
        response.execute(context);
        Assert.assertNotNull(response.getResponseItems());
        Assert.assertEquals(1, response.getResponseItems().size());
        Assert.assertTrue(response.getResponseItems().get(0) instanceof HttpcodeValidator);
    }

    /**
     * Verifies no {@link HttpcodeValidator} is added when already specified
     * 
     * @throws Throwable
     */
    @Test
    public void testDefaultResponseItemsSpecified() throws Throwable
    {
        final String httpcode = "303";
        final List<AbstractResponseItem> responseItems = new ArrayList<AbstractResponseItem>();
        responseItems.add(new HttpcodeValidator(httpcode));
        final Response response = new Response(responseItems);
        response.fillDefaultData(context);
        Assert.assertNotNull(response.getResponseItems());
        Assert.assertEquals(1, response.getResponseItems().size());
        Assert.assertTrue(response.getResponseItems().get(0) instanceof HttpcodeValidator);
        Assert.assertEquals(httpcode, ((HttpcodeValidator) response.getResponseItems().get(0)).getHttpcode());
    }

}
