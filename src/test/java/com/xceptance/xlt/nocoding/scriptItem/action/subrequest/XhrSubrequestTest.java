package com.xceptance.xlt.nocoding.scriptItem.action.subrequest;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;
import com.xceptance.xlt.nocoding.scriptItem.action.response.AbstractResponseItem;
import com.xceptance.xlt.nocoding.scriptItem.action.response.HttpcodeValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Validator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.RegexpExtractor;
import com.xceptance.xlt.nocoding.util.Context;
import com.xceptance.xlt.nocoding.util.MockObjects;

/**
 * Tests {@link XhrSubrequest}
 * 
 * @author ckeiner
 */
public class XhrSubrequestTest
{
    private MockObjects mockObjects;

    private Context context;

    /**
     * Sets {@link WebResponse} via {@link MockObjects#loadResponse()} in {@link Context}
     */
    @Before
    public void init()
    {
        mockObjects = new MockObjects();
        context = new Context(XltProperties.getInstance());
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
        final List<AbstractActionItem> actionItems = new ArrayList<AbstractActionItem>();
        AbstractActionItem actionItem = new Request(mockObjects.urlStringDemoHtml);
        actionItems.add(actionItem);

        final List<AbstractResponseItem> responseItems = new ArrayList<AbstractResponseItem>();
        responseItems.add(new HttpcodeValidator("200"));
        final AbstractResponseItem responseItem = new Validator("Validate Title", new RegexpExtractor(mockObjects.regexStringExpected),
                                                                null);
        responseItems.add(responseItem);
        actionItem = new Response(responseItems);
        actionItems.add(actionItem);
        final AbstractSubrequest xhrSubrequest = new XhrSubrequest("XhrSubrequest", actionItems);

        xhrSubrequest.execute(context);
    }

}
