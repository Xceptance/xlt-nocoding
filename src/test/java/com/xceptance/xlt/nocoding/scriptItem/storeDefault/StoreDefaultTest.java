package com.xceptance.xlt.nocoding.scriptItem.storeDefault;

import org.junit.Before;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.util.Context;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;

public abstract class StoreDefaultTest
{

    public Context context;

    @Before
    public void init()
    {
        context = new Context(XltProperties.getInstance(), new DataStorage());
    }

}
