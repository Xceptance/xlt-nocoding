package com.xceptance.xlt.nocoding.command.action.response;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.api.validators.HttpResponseCodeValidator;
import com.xceptance.xlt.nocoding.command.AbstractContextTest;
import com.xceptance.xlt.nocoding.util.MockObjects;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Tests {@link Response}
 *
 * @author ckeiner
 */
public class ResponseTest extends AbstractContextTest
{
    MockObjects mockObjects;

    public ResponseTest(final Context<?> context)
    {
        super(context);
        mockObjects = new MockObjects();
        mockObjects.loadResponse();
        context.setWebResponse(mockObjects.getResponse());
    }

    /**
     * Verifies a {@link HttpCodeValidator} is added when {@link Response#Response()} is used
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
        Assert.assertTrue(response.getResponseItems().get(0) instanceof HttpCodeValidator);
    }

    /**
     * Verifies a {@link HttpCodeValidator} is added when {@link Response#Response(List)} is used and the specified
     * {@link List} does not contain a {@link HttpResponseCodeValidator}.
     *
     * @throws Throwable
     */
    @Test
    public void testFillDefaultWithResponseItems() throws Throwable
    {
        final Response response = new Response(new ArrayList<AbstractResponseSubItem>());
        response.execute(context);
        Assert.assertNotNull(response.getResponseItems());
        Assert.assertEquals(1, response.getResponseItems().size());
        Assert.assertTrue(response.getResponseItems().get(0) instanceof HttpCodeValidator);
    }

    /**
     * Verifies no {@link HttpCodeValidator} is added when already specified
     *
     * @throws Throwable
     */
    @Test
    public void testDefaultResponseItemsSpecified() throws Throwable
    {
        final String httpcode = "303";
        final List<AbstractResponseSubItem> responseItems = new ArrayList<>();
        responseItems.add(new HttpCodeValidator(httpcode));
        final Response response = new Response(responseItems);
        response.fillDefaultData(context);
        Assert.assertNotNull(response.getResponseItems());
        Assert.assertEquals(1, response.getResponseItems().size());
        Assert.assertTrue(response.getResponseItems().get(0) instanceof HttpCodeValidator);
        Assert.assertEquals(httpcode, ((HttpCodeValidator) response.getResponseItems().get(0)).getHttpcode());
    }

}
