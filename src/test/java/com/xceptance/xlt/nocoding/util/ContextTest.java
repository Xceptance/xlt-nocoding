package com.xceptance.xlt.nocoding.util;

import org.junit.Before;

import com.xceptance.xlt.api.util.XltProperties;

public class ContextTest
{
    Context context;

    @Before
    public void init()
    {
        context = new Context(XltProperties.getInstance());
    }

}
