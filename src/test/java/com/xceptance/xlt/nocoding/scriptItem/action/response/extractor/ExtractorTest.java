package com.xceptance.xlt.nocoding.scriptItem.action.response.extractor;

import org.junit.Before;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.util.Context;
import com.xceptance.xlt.nocoding.util.MockObjects;

/**
 * Abstract class for extractor tests. Stores the {@link WebResponse} from {@link MockObjects#loadResponse()} in
 * {@link Context}.
 * 
 * @author ckeiner
 */
public abstract class ExtractorTest
{
    protected Context context;

    protected MockObjects mockObjects;

    /**
     * Prepares the test run by storing the {@link WebResponse} from {@link MockObjects#loadResponse()} in {@link Context}.
     */
    @Before
    public void init()
    {
        context = new Context(XltProperties.getInstance());
        mockObjects = new MockObjects();
        mockObjects.loadResponse();
        context.setWebResponse(mockObjects.getResponse());
    }

}
