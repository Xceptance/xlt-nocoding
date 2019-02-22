package com.xceptance.xlt.nocoding.command.storeDefault;

import com.xceptance.xlt.nocoding.command.AbstractContextTest;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Abstract class for all store default tests. Creates a new {@link Context}.
 *
 * @author ckeiner
 */
public abstract class AbstractStoreDefaultTest extends AbstractContextTest
{
    public AbstractStoreDefaultTest(final Context<?> context)
    {
        super(context);
    }

}
