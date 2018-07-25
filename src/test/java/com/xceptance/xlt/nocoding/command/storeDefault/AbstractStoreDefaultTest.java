package com.xceptance.xlt.nocoding.command.storeDefault;

import org.junit.Before;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.context.LightWeightContext;

/**
 * Abstract class for all store default tests. Creates a new {@link Context}.
 *
 * @author ckeiner
 */
public abstract class AbstractStoreDefaultTest
{

    public Context<?> context;

    /**
     * Creates a new {@link Context}
     */
    @Before
    public void init()
    {
        context = new LightWeightContext(XltProperties.getInstance());
    }

}
