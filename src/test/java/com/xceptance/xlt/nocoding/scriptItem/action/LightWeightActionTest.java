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
import com.xceptance.xlt.nocoding.util.Context;

public class LightWeightActionTest
{
    Context context;

    @Before
    public void init()
    {
        context = new Context(XltProperties.getInstance());
    }

    @Test
    public void testDefaultRequest()
    {
        // Set url
        final String url = "https://www.xceptance.com";
        context.getDefaultItems().store(Constants.URL, url);
        final String configName = Constants.NAME;
        final String value = "TestName";
        context.getDefaultItems().store(configName, value);

        final LightWeigthAction action = new LightWeigthAction();
        action.fillDefaultData(context);
        Assert.assertNotNull(action.getActionItems());
        Assert.assertFalse(action.getActionItems().isEmpty());
        Assert.assertTrue(action.getActionItems().get(0) instanceof Request);
        final Request request = (Request) action.getActionItems().get(0);
        request.fillDefaultData(context);
        Assert.assertEquals(url, request.getUrl());
    }

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
        int i = 0;
        for (final ScriptItem scriptItem : store)
        {
            scriptItem.execute(context);
            Assert.assertTrue(context.getDefaultStatics().getItems().contains(url));
            i++;
        }
        // Set url
        context.getDefaultItems().store(Constants.URL, "https://www.xceptance.com");
        final String configName = Constants.NAME;
        final String value = "TestName";
        context.getDefaultItems().store(configName, value);
        final LightWeigthAction action = new LightWeigthAction();
        action.fillDefaultData(context);
        Assert.assertEquals(value, action.getName());

        Assert.assertEquals(3, action.getActionItems().size());
        Assert.assertTrue(action.getActionItems().get(0) instanceof Request);
        Assert.assertTrue(action.getActionItems().get(1) instanceof Response);
        Assert.assertTrue(action.getActionItems().get(2) instanceof StaticSubrequest);
        final StaticSubrequest subrequest = (StaticSubrequest) action.getActionItems().get(2);
        Assert.assertEquals(5, subrequest.getUrls().size());
        for (final String subrequestUrl : subrequest.getUrls())
        {
            Assert.assertTrue(subrequestUrl.equals(url));
        }
    }

    @Test
    public void testDefaultResponse()
    {
        // Set url
        context.getDefaultItems().store(Constants.URL, "https://www.xceptance.com");
        final String configName = Constants.NAME;
        final String value = "TestName";
        context.getDefaultItems().store(configName, value);
        final LightWeigthAction action = new LightWeigthAction();
        action.fillDefaultData(context);

        Assert.assertEquals(2, action.getActionItems().size());
        Assert.assertTrue(action.getActionItems().get(0) instanceof Request);
        Assert.assertTrue(action.getActionItems().get(1) instanceof Response);
    }

    @Test
    public void testDefaultName()
    {
        // Set url
        context.getDefaultItems().store(Constants.URL, "https://www.xceptance.com");
        final String configName = Constants.NAME;
        final String value = "TestName";
        context.getDefaultItems().store(configName, value);
        final LightWeigthAction action = new LightWeigthAction();
        action.fillDefaultData(context);
        Assert.assertEquals(value, action.getName());
    }

    @Test
    public void testResolveName()
    {
        // Set url
        context.getDefaultItems().store(Constants.URL, "https://www.xceptance.com");
        final String configName = Constants.NAME;
        final String value = "${name}";
        context.getDefaultItems().store(configName, value);

        context.getVariables().store("name", "TestName");

        final LightWeigthAction action = new LightWeigthAction();
        action.fillDefaultData(context);
        action.resolveName(context);
        Assert.assertEquals("TestName", action.getName());
    }

}
