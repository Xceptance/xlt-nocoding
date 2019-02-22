package com.xceptance.xlt.nocoding.command.action.response.extractor.xpath;

import org.junit.Before;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.nocoding.command.AbstractContextTest;
import com.xceptance.xlt.nocoding.util.MockObjects;
import com.xceptance.xlt.nocoding.util.context.Context;

public abstract class AbstractXpathExtractorExecutorTest extends AbstractContextTest
{

    public AbstractXpathExtractorExecutorTest(final Context<?> context)
    {
        super(context);
    }

    protected MockObjects mockObjects;

    /**
     * Prepares the test run by storing the {@link WebResponse} from {@link MockObjects#loadResponse()} in
     * {@link Context}.
     */
    @Before
    public void init()
    {
        mockObjects = new MockObjects();
        mockObjects.loadResponse();
        context.setWebResponse(mockObjects.getResponse());
    }

}
