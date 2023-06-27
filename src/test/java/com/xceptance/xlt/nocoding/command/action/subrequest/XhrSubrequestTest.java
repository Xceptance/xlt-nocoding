package com.xceptance.xlt.nocoding.command.action.subrequest;

import java.util.ArrayList;
import java.util.List;

import org.htmlunit.WebResponse;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.nocoding.command.AbstractContextTest;
import com.xceptance.xlt.nocoding.command.action.AbstractActionSubItem;
import com.xceptance.xlt.nocoding.command.action.request.Request;
import com.xceptance.xlt.nocoding.command.action.response.AbstractResponseSubItem;
import com.xceptance.xlt.nocoding.command.action.response.HttpCodeValidator;
import com.xceptance.xlt.nocoding.command.action.response.Response;
import com.xceptance.xlt.nocoding.command.action.response.Validator;
import com.xceptance.xlt.nocoding.command.action.response.extractor.RegexpExtractor;
import com.xceptance.xlt.nocoding.util.MockObjects;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Tests {@link XhrSubrequest}
 *
 * @author ckeiner
 */
public class XhrSubrequestTest extends AbstractContextTest
{
    private MockObjects mockObjects;

    public XhrSubrequestTest(final Context<?> context)
    {
        super(context);
        init();
    }

    /**
     * Sets {@link WebResponse} via {@link MockObjects#loadResponse()} in {@link Context}
     */
    @Before
    public void init()
    {
        mockObjects = new MockObjects();
        context.getWebClient().setTimerName("Xhr-TimerName");
        mockObjects.loadResponse();
        context.setWebResponse(mockObjects.getResponse());
    }

    /**
     * Creates an {@link XhrSubrequest} with request, response and executes the subrequest
     *
     * @throws Throwable
     */
    @Test
    public void testExecute() throws Throwable
    {
        final List<AbstractActionSubItem> actionItems = new ArrayList<>();
        AbstractActionSubItem actionItem = new Request(mockObjects.urlStringDemoHtml);
        actionItems.add(actionItem);

        final List<AbstractResponseSubItem> responseItems = new ArrayList<>();
        responseItems.add(new HttpCodeValidator("200"));
        final AbstractResponseSubItem responseItem = new Validator("Validate Title", new RegexpExtractor(mockObjects.regexStringExpected),
                                                                   null);
        responseItems.add(responseItem);
        actionItem = new Response(responseItems);
        actionItems.add(actionItem);
        final AbstractSubrequest xhrSubrequest = new XhrSubrequest("XhrSubrequest", actionItems);

        xhrSubrequest.execute(context);
    }

}
