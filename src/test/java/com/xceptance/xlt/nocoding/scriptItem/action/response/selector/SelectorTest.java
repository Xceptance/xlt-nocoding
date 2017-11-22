package com.xceptance.xlt.nocoding.scriptItem.action.response.selector;

import org.junit.Before;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.util.Context;
import com.xceptance.xlt.nocoding.util.MockObjects;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;

public abstract class SelectorTest
{
    public Context context;

    public MockObjects mockObjects;

    @Before
    public void init()
    {
        context = new Context(XltProperties.getInstance(), new DataStorage());
        mockObjects = new MockObjects();
        mockObjects.loadResponse();
        context.setWebResponse(mockObjects.getResponse());
    }

}
