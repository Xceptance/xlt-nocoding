package com.xceptance.xlt.nocoding.parser;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.action.LightWeigthAction;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.response.stores.AbstractResponseStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.stores.RegExpStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.AbstractValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.RegExpValidator;

public class MockParser implements Parser
{

    @Override
    public List<ScriptItem> parse()
    {
        // TODO Auto-generated method stub
        return this.parseEasy();
    }

    /**
     * Parses all fields that can be specified
     * 
     * @return List of NoCodingScriptItems
     */
    private List<ScriptItem> parseEasy()
    {
        final List<ScriptItem> itemList = new ArrayList<ScriptItem>();
        final Request request = new Request("https://localhost:8443/posters/", "Visit Homepage");
        request.setMethod(HttpMethod.GET);
        request.setParameters(null);
        request.setBody("");
        request.setEncodeBody(false);
        request.setEncodeParameters(false);
        request.setHeaders(null);
        request.setXhr(false);

        final List<AbstractValidator> validation = new ArrayList<AbstractValidator>();
        validation.add(new RegExpValidator("<title>Posters\\s-\\sThe\\sUltimate\\sOnline\\sShop</title>"));
        final List<AbstractResponseStore> responseStore = new ArrayList<AbstractResponseStore>();
        responseStore.add(new RegExpStore("Blub", "<title>(.*?)</title>"));

        final Response response = new Response(200, responseStore, validation);
        // final Subrequest subrequest = new Subrequest();
        final ScriptItem actionItem = new LightWeigthAction(request, response, null);
        itemList.add(actionItem);
        return itemList;
    }

    /**
     * Parses only the mandatory information, meaning that default values have to be read
     * 
     * @return List of NoCodingScriptItems
     */
    private List<ScriptItem> parseMedium()
    {
        final List<ScriptItem> itemList = new ArrayList<ScriptItem>();
        final Request request = new Request("https://localhost:8443/posters/", "Visit Homepage");

        // final Subrequest subrequest = new Subrequest();
        final ScriptItem actionItem = new LightWeigthAction(request, null, null);
        itemList.add(actionItem);
        return itemList;
    }

    /**
     * this time we have to parse some weird parameters, namely ${host} and such
     * 
     * @return List of NoCodingScriptItems
     */
    private List<ScriptItem> parseHard()
    {
        final List<ScriptItem> itemList = new ArrayList<ScriptItem>();
        final Request request = new Request("${host}/posters/", "Visit Homepage");

        // final Subrequest subrequest = new Subrequest();
        final ScriptItem actionItem = new LightWeigthAction(request, null, null);
        itemList.add(actionItem);
        return itemList;

    }

}
