package com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod;

import org.junit.Before;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.util.Context;

/**
 * Prepares {@link AbstractValidationMethod} tests by creating a new context
 * 
 * @author ckeiner
 */
public abstract class ValidationMethodTest
{
    protected Context context;

    /**
     * Creates a new {@link Context}
     */
    @Before
    public void init()
    {
        context = new Context(XltProperties.getInstance());
    }

}
