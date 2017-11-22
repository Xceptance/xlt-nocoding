package com.xceptance.xlt.nocoding.scriptItem.action.response;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.util.Context;
import com.xceptance.xlt.nocoding.util.MockObjects;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;

public class ResponseTest
{
    Context context;

    MockObjects mockObjects;

    @Before
    public void init()
    {
        context = new Context(XltProperties.getInstance(), new DataStorage());
        mockObjects = new MockObjects();
        mockObjects.loadResponse();
        context.setWebResponse(mockObjects.getResponse());
    }

    @Test
    public void testFillDefaultNoResponseItems() throws Throwable
    {
        final Response response = new Response();
        response.execute(context);
        Assert.assertNotNull(response.getResponseItems());
        Assert.assertEquals(1, response.getResponseItems().size());
        Assert.assertTrue(response.getResponseItems().get(0) instanceof HttpcodeValidator);
    }

    @Test
    public void testFillDefaultWithResponseItems() throws Throwable
    {
        final Response response = new Response(new ArrayList<AbstractResponseItem>());
        response.execute(context);
        Assert.assertNotNull(response.getResponseItems());
        Assert.assertEquals(1, response.getResponseItems().size());
        Assert.assertTrue(response.getResponseItems().get(0) instanceof HttpcodeValidator);
    }

}
