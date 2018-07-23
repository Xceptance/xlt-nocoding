package com.xceptance.xlt.nocoding.util.context;

import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.util.XltProperties;

public class DomContextTest
{

    public Context<?> context;

    @Before
    public void init()
    {
        context = new DomContext(XltProperties.getInstance());
    }

    @Test
    public void testGetSgmlPage()
    {
        context.getPage();
    }

    @Test
    public void testSetSgmlPage()
    {
        context.setPage(null);
    }

}
