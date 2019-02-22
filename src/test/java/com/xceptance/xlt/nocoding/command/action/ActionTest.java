package com.xceptance.xlt.nocoding.command.action;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.command.AbstractContextTest;
import com.xceptance.xlt.nocoding.command.Command;
import com.xceptance.xlt.nocoding.command.action.request.Request;
import com.xceptance.xlt.nocoding.command.action.response.Response;
import com.xceptance.xlt.nocoding.command.action.subrequest.StaticSubrequest;
import com.xceptance.xlt.nocoding.command.storeDefault.StoreDefaultStaticSubrequest;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Tests {@link Action}
 *
 * @author ckeiner
 */
public class ActionTest extends AbstractContextTest
{
    public ActionTest(final Context<?> context)
    {
        super(context);
    }

    /**
     * Verifies {@link Action} adds a default {@link Request} if no {@link Request} is specified
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

        final Action action = new Action();
        action.fillDefaultData(context);
        Assert.assertNotNull(action.getActionItems());
        Assert.assertFalse(action.getActionItems().isEmpty());
        Assert.assertTrue(action.getActionItems().get(0) instanceof Request);
        final Request request = (Request) action.getActionItems().get(0);
        request.fillDefaultData(context);
        Assert.assertEquals(url, request.getUrl());
    }

    /**
     * Verifies {@link Action} adds default {@link StaticSubrequest}
     *
     * @throws Throwable
     */
    @Test
    public void testDefaultStatic() throws Throwable
    {
        final String url = "http://www.xceptance.net";
        final List<Command> store = new ArrayList<>();
        store.add(new StoreDefaultStaticSubrequest(url));
        store.add(new StoreDefaultStaticSubrequest(url));
        store.add(new StoreDefaultStaticSubrequest(url));
        store.add(new StoreDefaultStaticSubrequest(url));
        store.add(new StoreDefaultStaticSubrequest(url));
        Assert.assertTrue(context.getDefaultStatics().getItems().isEmpty());
        for (final Command scriptItem : store)
        {
            scriptItem.execute(context);
            Assert.assertTrue(context.getDefaultStatics().getItems().contains(url));
        }
        // Set url
        context.getDefaultItems().store(Constants.URL, "https://www.xceptance.com");
        final String configName = Constants.NAME;
        final String value = "TestName";
        context.getDefaultItems().store(configName, value);
        final Action action = new Action();
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

    /**
     * Verifies {@link Action} adds a default {@link Response} if no {@link Response} is specified
     */
    @Test
    public void testDefaultResponse()
    {
        // Set url
        context.getDefaultItems().store(Constants.URL, "https://www.xceptance.com");
        final String configName = Constants.NAME;
        final String value = "TestName";
        context.getDefaultItems().store(configName, value);
        final Action action = new Action();
        action.fillDefaultData(context);

        Assert.assertEquals(2, action.getActionItems().size());
        Assert.assertTrue(action.getActionItems().get(0) instanceof Request);
        Assert.assertTrue(action.getActionItems().get(1) instanceof Response);
    }

    /**
     * Verifies {@link Action} adds a default {@link Action#getName()} if no name is specified
     */
    @Test
    public void testDefaultName()
    {
        // Set url
        context.getDefaultItems().store(Constants.URL, "https://www.xceptance.com");
        final String configName = Constants.NAME;
        final String value = "TestName";
        context.getDefaultItems().store(configName, value);
        final Action action = new Action();
        action.fillDefaultData(context);
        Assert.assertEquals(value, action.getName());
    }

    /**
     * Verifies {@link Action} does resolve {@link Action#getName()}
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

        final Action action = new Action();
        action.execute(context);
        Assert.assertEquals("TestName", action.getName());
    }

}
