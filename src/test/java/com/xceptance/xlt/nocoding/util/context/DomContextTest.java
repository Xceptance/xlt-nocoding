package com.xceptance.xlt.nocoding.util.context;

import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.util.XltProperties;

public class DomContextTest
{

    public Context context;

    @Before
    public void init()
    {
        context = new DomContext(XltProperties.getInstance());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetLightWeightPage()
    {
        context.getLightWeightPage();
    }

    @Test(expected = IllegalStateException.class)
    public void testSetLightWeightPage()
    {
        context.setLightWeightPage(null);
    }

    @Test
    public void testGetSgmlPage()
    {
        context.getSgmlPage();
    }

    @Test
    public void testSetSgmlPage()
    {
        context.setSgmlPage(null);
    }

}
