package com.xceptance.xlt.nocoding.scriptItem.storeDefault;

import org.junit.Before;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.util.Context;

/**
 * Abstract class for all store default tests. Creates a new {@link Context}.
 * 
 * @author ckeiner
 */
public abstract class StoreDefaultTest
{

    public Context context;

    /**
     * Creates a new {@link Context}
     */
    @Before
    public void init()
    {
        context = new Context(XltProperties.getInstance());
    }

}
