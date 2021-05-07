package com.xceptance.xlt.nocoding.command.action.response;

import java.util.ArrayList;
import java.util.List;

import com.xceptance.xlt.nocoding.util.NoCodingPropertyAdmin;
import org.junit.Assert;
import org.junit.Before;
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

    @Before
    public void beforeEach() {
        // reset context to default: not set
        context.getPropertyAdmin().getProperties().removeProperty(NoCodingPropertyAdmin.DEFAULT_HTTP_CODE_VALIDATION);
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

    /**
     * Verifies no {@link HttpCodeValidator} is added when {@link Response#execute(Context)} is called and
     * <code>com.xceptance.xlt.nocoding.defaultHttpCodeValidation</code> is set to false
     *
     * @throws Throwable
     */
    @Test
    public void testFillDefaultWhenPropertyFalse() throws Throwable
    {
        final List<AbstractResponseSubItem> responseItems = new ArrayList<>();
        final Response response = new Response(responseItems);
        Assert.assertNull(context.getPropertyByKey(NoCodingPropertyAdmin.DEFAULT_HTTP_CODE_VALIDATION));
        context.getPropertyAdmin().getProperties().setProperty(NoCodingPropertyAdmin.DEFAULT_HTTP_CODE_VALIDATION, "false");
        response.execute(context);
        Assert.assertNotNull(response.getResponseItems());
        Assert.assertEquals(0, response.getResponseItems().size());
    }

    /**
     * Verifies {@link HttpCodeValidator} is present when {@link Response#execute(Context)} is called and
     * <code>com.xceptance.xlt.nocoding.defaultHttpCodeValidation</code> is not set
     *
     * @throws Throwable
     */
    @Test
    public void testFillDefaultWhenPropertyMissing() throws Throwable
    {
        final List<AbstractResponseSubItem> responseItems = new ArrayList<>();
        final Response response = new Response(responseItems);
        Assert.assertNull(context.getPropertyByKey(NoCodingPropertyAdmin.DEFAULT_HTTP_CODE_VALIDATION));
        response.execute(context);
        Assert.assertNotNull(response.getResponseItems());
        Assert.assertEquals(1, response.getResponseItems().size());
        Assert.assertTrue(response.getResponseItems().get(0) instanceof HttpCodeValidator);
    }

    /**
     * Verifies {@link HttpCodeValidator} is present when {@link Response#execute(Context)} is called and
     * <code>com.xceptance.xlt.nocoding.defaultHttpCodeValidation</code> is set to true
     *
     * @throws Throwable
     */
    @Test
    public void testFillDefaultWhenPropertyTrue() throws Throwable
    {
        final List<AbstractResponseSubItem> responseItems = new ArrayList<>();
        final Response response = new Response(responseItems);
        Assert.assertNull(context.getPropertyByKey(NoCodingPropertyAdmin.DEFAULT_HTTP_CODE_VALIDATION));
        context.getPropertyAdmin().getProperties().setProperty(NoCodingPropertyAdmin.DEFAULT_HTTP_CODE_VALIDATION, "true");
        response.execute(context);
        Assert.assertNotNull(response.getResponseItems());
        Assert.assertEquals(1, response.getResponseItems().size());
        Assert.assertTrue(response.getResponseItems().get(0) instanceof HttpCodeValidator);
    }
}
