package com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.xpathExtractor;

import org.junit.Before;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.util.MockObjects;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.context.DomContext;

public abstract class AbstractXpathExtractorExecutorTest
{
    protected Context<?> context;

    protected MockObjects mockObjects;

    /**
     * Prepares the test run by storing the {@link WebResponse} from {@link MockObjects#loadResponse()} in {@link Context}.
     */
    @Before
    public void init()
    {
        context = new DomContext(XltProperties.getInstance());
        mockObjects = new MockObjects();
        mockObjects.loadResponse();
        context.setWebResponse(mockObjects.getResponse());
    }

}
