package com.xceptance.xlt.nocoding.scriptItem.action.response.validator;

import org.junit.Before;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validator.AbstractValidator;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.context.LightWeightContext;

/**
 * Prepares {@link AbstractValidator} tests by creating a new context
 * 
 * @author ckeiner
 */
public abstract class ValidationMethodTest
{
    protected Context<?> context;

    /**
     * Creates a new {@link Context}
     */
    @Before
    public void init()
    {
        context = new LightWeightContext(XltProperties.getInstance());
    }

}
