package com.xceptance.xlt.nocoding.util.context;

import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.util.XltProperties;

public class LightWeightContextTest
{

    public Context context;

    @Before
    public void init()
    {
        context = new LightWeightContext(XltProperties.getInstance());
    }

    @Test
    public void testGetLightWeightPage()
    {
        context.getLightWeightPage();
    }

    @Test
    public void testSetLightWeightPage()
    {
        context.setLightWeightPage(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSgmlPage()
    {
        context.getSgmlPage();
    }

    @Test(expected = IllegalStateException.class)
    public void testSetSgmlPage()
    {
        context.setSgmlPage(null);
    }

}
