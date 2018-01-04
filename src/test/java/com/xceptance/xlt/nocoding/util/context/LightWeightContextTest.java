package com.xceptance.xlt.nocoding.util.context;

import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.util.XltProperties;

public class LightWeightContextTest
{

    public Context<?> context;

    @Before
    public void init()
    {
        context = new LightWeightContext(XltProperties.getInstance());
    }

    @Test
    public void testGetLightWeightPage()
    {
        context.getPage();
    }

    @Test
    public void testSetLightWeightPage()
    {
        context.setPage(null);
    }

}
