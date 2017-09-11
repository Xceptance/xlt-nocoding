package com.xceptance.xlt.nocoding.parser;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.xceptance.xlt.nocoding.scriptItem.NoCodingScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.action.NoCodingLightWeigthAction;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;
import com.xceptance.xlt.nocoding.scriptItem.action.response.AbstractValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.RegExpValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;

public class MockParser implements Parser
{

    @Override
    public List<NoCodingScriptItem> parse()
    {
        // TODO Auto-generated method stub
        return this.parseEasy();
    }

    public List<NoCodingScriptItem> parseEasy()
    {
        final List<NoCodingScriptItem> itemList = new ArrayList<NoCodingScriptItem>();
        final Request request = new Request("https://localhost:8443/posters/");
        request.setMethod(HttpMethod.GET);
        request.setParameters(null);
        request.setBody("");
        request.setEncodeBody(false);
        request.setEncodeParameters(false);
        request.setHeaders(null);
        request.setXhr(false);

        // private final int httpcode;
        // private final Store storage;

        final List<AbstractValidator> validation = new ArrayList<AbstractValidator>();
        validation.add(new RegExpValidator("<title>Posters\\s-\\sThe\\sUltimate\\sOnline\\sShop</title>"));

        final Response response = new Response(200, null, validation);
        // final Subrequest subrequest = new Subrequest();
        final NoCodingScriptItem actionItem = new NoCodingLightWeigthAction(null, "Login", request, response, null);
        itemList.add(actionItem);
        return itemList;
    }

}
