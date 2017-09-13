package com.xceptance.xlt.nocoding.util.action.execution;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.util.MockObjects;
import com.xceptance.xlt.nocoding.util.NoCodingPropAdmin;

public class LightWeightPageActionFactoryTest
{
    private NoCodingPropAdmin propAdmin;

    private MockObjects mockObjects;

    private WebRequest request;

    @Before
    public void setup()
    {
        propAdmin = new NoCodingPropAdmin(XltProperties.getInstance());
        mockObjects = new MockObjects();
        mockObjects.setUrlString(mockObjects.urlStringDemoHtml);
        mockObjects.initURL();
        mockObjects.initWebRequest();
        request = mockObjects.getRequest();
    }

    @Test
    public void testConstructor()
    {
        final LightWeightPageActionFactory factory = new LightWeightPageActionFactory(propAdmin);
        Assert.assertEquals(propAdmin, factory.getPropAdmin());
    }

    @Test
    public void testCreatePageAction()
    {
        final LightWeightPageActionFactory factory = new LightWeightPageActionFactory(propAdmin);
        final URLActionDataExecutionable executionable = factory.createPageAction("Action", request);
        executionable.executeItem();
        Assert.assertNotNull(executionable.getResult());
    }

    @Test
    public void testCreateXhrPageAction()
    {
        final LightWeightPageActionFactory factory = new LightWeightPageActionFactory(propAdmin);
        URLActionDataExecutionable executionable = factory.createPageAction("Action", request);
        executionable.executeItem();
        executionable = factory.createXhrPageAction("Xhr", request);
        executionable.executeItem();
        Assert.assertNotNull(executionable.getResult());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateXhrPageActionAsFirstAction()
    {
        final LightWeightPageActionFactory factory = new LightWeightPageActionFactory(propAdmin);
        @SuppressWarnings("unused")
        final URLActionDataExecutionable executionable = factory.createXhrPageAction("Xhr", request);
    }
}
