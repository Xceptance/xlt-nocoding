package com.xceptance.xlt.nocoding.scriptItem.action;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.StaticSubrequest;
import com.xceptance.xlt.nocoding.scriptItem.storeDefault.StoreDefaultStatic;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.context.LightWeightContext;

/**
 * Tests {@link ActionImpl}
 * 
 * @author ckeiner
 */
public class LightWeightActionTest
{
    Context<?> context;

    /**
     * Creates a new {@link Context}
     */
    @Before
    public void init()
    {
        context = new LightWeightContext(XltProperties.getInstance());
    }

    /**
     * Verifies {@link ActionImpl} adds a default {@link Request} if no {@link Request} is specified
     */
    @Test
    public void testDefaultRequest()
    {
        // Set url
        final String url = "https://www.xceptance.com";
        context.getDefaultItems().store(Constants.URL, url);
        final String configName = Constants.NAME;
        final String value = "TestName";
        context.getDefaultItems().store(configName, value);

        final ActionImpl impl = new ActionImpl();
        impl.fillDefaultData(context);
        Assert.assertNotNull(impl.getActionItems());
        Assert.assertFalse(impl.getActionItems().isEmpty());
        Assert.assertTrue(impl.getActionItems().get(0) instanceof Request);
        final Request request = (Request) impl.getActionItems().get(0);
        request.fillDefaultData(context);
        Assert.assertEquals(url, request.getUrl());
    }

    /**
     * Verifies {@link ActionImpl} adds default {@link StaticSubrequest}
     * 
     * @throws Throwable
     */
    @Test
    public void testDefaultStatic() throws Throwable
    {
        final String url = "http://www.xceptance.net";
        final List<ScriptItem> store = new ArrayList<ScriptItem>();
        store.add(new StoreDefaultStatic(url));
        store.add(new StoreDefaultStatic(url));
        store.add(new StoreDefaultStatic(url));
        store.add(new StoreDefaultStatic(url));
        store.add(new StoreDefaultStatic(url));
        Assert.assertTrue(context.getDefaultStatics().getItems().isEmpty());
        for (final ScriptItem scriptItem : store)
        {
            scriptItem.execute(context);
            Assert.assertTrue(context.getDefaultStatics().getItems().contains(url));
        }
        // Set url
        context.getDefaultItems().store(Constants.URL, "https://www.xceptance.com");
        final String configName = Constants.NAME;
        final String value = "TestName";
        context.getDefaultItems().store(configName, value);
        final ActionImpl impl = new ActionImpl();
        impl.fillDefaultData(context);
        Assert.assertEquals(value, impl.getName());

        Assert.assertEquals(3, impl.getActionItems().size());
        Assert.assertTrue(impl.getActionItems().get(0) instanceof Request);
        Assert.assertTrue(impl.getActionItems().get(1) instanceof Response);
        Assert.assertTrue(impl.getActionItems().get(2) instanceof StaticSubrequest);
        final StaticSubrequest subrequest = (StaticSubrequest) impl.getActionItems().get(2);
        Assert.assertEquals(5, subrequest.getUrls().size());
        for (final String subrequestUrl : subrequest.getUrls())
        {
            Assert.assertTrue(subrequestUrl.equals(url));
        }
    }

    /**
     * Verifies {@link ActionImpl} adds a default {@link Response} if no {@link Response} is specified
     */
    @Test
    public void testDefaultResponse()
    {
        // Set url
        context.getDefaultItems().store(Constants.URL, "https://www.xceptance.com");
        final String configName = Constants.NAME;
        final String value = "TestName";
        context.getDefaultItems().store(configName, value);
        final ActionImpl impl = new ActionImpl();
        impl.fillDefaultData(context);

        Assert.assertEquals(2, impl.getActionItems().size());
        Assert.assertTrue(impl.getActionItems().get(0) instanceof Request);
        Assert.assertTrue(impl.getActionItems().get(1) instanceof Response);
    }

    /**
     * Verifies {@link ActionImpl} adds a default {@link ActionImpl#getName()} if no name is specified
     */
    @Test
    public void testDefaultName()
    {
        // Set url
        context.getDefaultItems().store(Constants.URL, "https://www.xceptance.com");
        final String configName = Constants.NAME;
        final String value = "TestName";
        context.getDefaultItems().store(configName, value);
        final ActionImpl impl = new ActionImpl();
        impl.fillDefaultData(context);
        Assert.assertEquals(value, impl.getName());
    }

    /**
     * Verifies {@link ActionImpl} does not resolve {@link ActionImpl#getName()}
     * 
     * @throws Throwable
     */
    @Test
    public void testResolveName() throws Throwable
    {
        // Set url
        context.getDefaultItems().store(Constants.URL, "https://www.xceptance.com");
        final String configName = Constants.NAME;
        final String value = "${name}";
        context.getDefaultItems().store(configName, value);

        context.getVariables().store("name", "TestName");

        final ActionImpl impl = new ActionImpl();
        impl.execute(context);
        Assert.assertNotEquals("TestName", impl.getName());
    }

}
