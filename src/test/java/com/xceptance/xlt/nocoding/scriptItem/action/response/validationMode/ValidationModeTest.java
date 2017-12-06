package com.xceptance.xlt.nocoding.scriptItem.action.response.validationMode;

import org.junit.Before;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.util.Context;

public abstract class ValidationModeTest
{
    protected Context context;

    @Before
    public void init()
    {
        context = new Context(XltProperties.getInstance());
    }

}
