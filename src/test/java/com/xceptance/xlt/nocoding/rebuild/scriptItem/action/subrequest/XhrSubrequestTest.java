package com.xceptance.xlt.nocoding.rebuild.scriptItem.action.subrequest;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;
import com.xceptance.xlt.nocoding.scriptItem.action.response.AbstractResponseItem;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.RegExpValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.AbstractSubrequest;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.XhrSubrequest;
import com.xceptance.xlt.nocoding.util.Context;
import com.xceptance.xlt.nocoding.util.MockObjects;
import com.xceptance.xlt.nocoding.util.dataStorage.DataStorage;

public class XhrSubrequestTest
{
    private MockObjects mockObject;

    private Context context;

    @Before
    public void init()
    {
        mockObject = new MockObjects();
        context = new Context(XltProperties.getInstance(), new DataStorage());
        context.getWebClient().setTimerName("Xhr-TimerName");
    }

    @Test
    public void textExecute() throws Throwable
    {
        final List<AbstractActionItem> actionItems = new ArrayList<AbstractActionItem>();
        AbstractActionItem actionItem = new Request(mockObject.urlStringDemoHtml);
        actionItems.add(actionItem);

        final List<AbstractResponseItem> responseItems = new ArrayList<AbstractResponseItem>();
        final AbstractResponseItem responseItem = new RegExpValidator("Validate Title", mockObject.regexStringExpected);
        responseItems.add(responseItem);
        actionItem = new Response("200", responseItems);
        actionItems.add(actionItem);
        final AbstractSubrequest xhrSubrequest = new XhrSubrequest("XhrSubrequest", actionItems);

        xhrSubrequest.execute(context);
    }

}
