package com.xceptance.xlt.nocoding.scriptItem.action.response.extractor;

import org.junit.Before;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.util.Context;
import com.xceptance.xlt.nocoding.util.MockObjects;

public abstract class ExtractorTest
{
    protected Context context;

    protected MockObjects mockObjects;

    @Before
    public void init()
    {
        context = new Context(XltProperties.getInstance());
        mockObjects = new MockObjects();
        mockObjects.loadResponse();
        context.setWebResponse(mockObjects.getResponse());
    }

}
