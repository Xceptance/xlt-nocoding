package com.xceptance.xlt.nocoding.parser;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.action.AbstractActionItem;
import com.xceptance.xlt.nocoding.scriptItem.action.LightWeigthAction;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;
import com.xceptance.xlt.nocoding.scriptItem.action.response.AbstractResponseItem;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.response.stores.HeaderStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.stores.RegExpStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.RegExpValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.AbstractSubrequest;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.StaticSubrequest;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.XhrSubrequest;

public class MockParser implements Parser
{

    @Override
    public List<ScriptItem> parse()
    {
        // TODO Auto-generated method stub
        return this.parseMediumLogin();
    }

    /**
     * Parses all fields that can be specified
     * 
     * @return List of NoCodingScriptItems
     */
    private List<ScriptItem> parseEasy()
    {
        final List<AbstractActionItem> actionItems = new ArrayList<AbstractActionItem>();

        /**
         * Request
         */
        final Request request = new Request("https://localhost:8443/posters/");
        request.setMethod(HttpMethod.GET.toString());
        request.setParameters(null);
        request.setBody(null);
        request.setEncodeBody("false");
        request.setEncodeParameters("false");
        request.setHeaders(null);
        request.setXhr("false");

        /**
         * Response
         */

        // Validator
        final List<AbstractResponseItem> responseItem = new ArrayList<AbstractResponseItem>();
        responseItem.add(new RegExpValidator("Title", "<title>Posters\\s-\\sThe\\sUltimate\\sOnline\\sShop</title>"));
        // Store
        responseItem.add(new RegExpStore("Blub", "<title>(.*?)</title>"));
        // Response
        final Response response = new Response("200", responseItem);

        /**
         * Subrequest
         */
        final List<AbstractSubrequest> subrequests = new ArrayList<AbstractSubrequest>();
        // final Request requestOfSubrequest = new Request("https://localhost:8443/posters/topCategory/Dining",
        // "Navigate final to second product page.");
        // final List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        // parameters.add(new NameValuePair("categoryID", "2"));
        // parameters.add(new NameValuePair("page", "2"));
        // requestOfSubrequest.setParameters(parameters);
        //
        // final List<AbstractValidator> validationForResponseOfSubrequest = new ArrayList<AbstractValidator>();
        // validationForResponseOfSubrequest.add(new RegExpValidator("<h4*> Peperoni Sandwich</h4>"));
        // final Response responseOfSubrequest = new Response(200, null, validationForResponseOfSubrequest);
        // final XHRSubrequest subrequest = new XHRSubrequest("Navigate to second product page.", requestOfSubrequest,
        // responseOfSubrequest);
        // subrequests.add(subrequest);

        /**
         * ONE Action
         */
        final List<ScriptItem> itemList = new ArrayList<ScriptItem>();
        actionItems.add(request);
        actionItems.add(response);
        for (final AbstractSubrequest subrequest : subrequests)
        {
            actionItems.add(subrequest);
        }
        final ScriptItem scriptItem = new LightWeigthAction("Visit Homepage", actionItems);
        itemList.add(scriptItem);
        return itemList;
    }

    /**
     * Parses only the mandatory information, meaning that default values have to be read
     * 
     * @return List of NoCodingScriptItems
     */
    private List<ScriptItem> parseMedium()
    {
        final List<AbstractActionItem> actionItems = new ArrayList<AbstractActionItem>();

        final List<ScriptItem> itemList = new ArrayList<ScriptItem>();
        final Request request = new Request("https://localhost:8443/posters/");

        actionItems.add(request);
        final ScriptItem scriptItem = new LightWeigthAction("Visit Homepage", actionItems);
        itemList.add(scriptItem);
        return itemList;
    }

    /**
     * this time we have to parse some parameters, namely ${host} and such
     * 
     * @return List of NoCodingScriptItems
     */
    private List<ScriptItem> parseHard()
    {
        final List<AbstractActionItem> actionItems = new ArrayList<AbstractActionItem>();

        final List<ScriptItem> itemList = new ArrayList<ScriptItem>();
        final Request request = new Request("${host}/posters/");

        // final Subrequest subrequest = new Subrequest();
        actionItems.add(request);
        final ScriptItem scriptItem = new LightWeigthAction("Visit Homepage", actionItems);
        itemList.add(scriptItem);
        return itemList;

    }

    /**
     * Parses all fields that can be specified
     * 
     * @return List of NoCodingScriptItems
     */
    private List<ScriptItem> parseMediumLogin()
    {
        List<AbstractActionItem> actionItems = new ArrayList<AbstractActionItem>();
        /**
         * Request
         */

        final Request request1 = new Request("https://localhost:8443/posters/");
        final Request request2 = new Request("https://localhost:8443/posters/login");
        final Request request3 = new Request("https://localhost:8443/posters/login");
        request3.setMethod(HttpMethod.POST.toString());
        final List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new NameValuePair("email", "john@doe.com"));
        parameters.add(new NameValuePair("password", "topsecret"));
        parameters.add(new NameValuePair("btnSignIn", ""));
        request3.setParameters(parameters);
        final Request request4 = new Request("https://localhost:8443/posters/");

        /**
         * Response
         */

        // Validator
        List<AbstractResponseItem> responseItem = new ArrayList<AbstractResponseItem>();
        responseItem.add(new RegExpValidator("Title", "<title>Posters\\s-\\sThe\\sUltimate\\sOnline\\sShop</title>"));
        // Response
        final Response response1 = new Response(null, responseItem);

        responseItem = new ArrayList<AbstractResponseItem>();
        responseItem.add(new RegExpValidator("login-form Existance", "<form\\sid=\"formLogin\"[\\s\\S]+?>"));
        // Response
        final Response response2 = new Response(null, responseItem);

        // Store
        responseItem = new ArrayList<AbstractResponseItem>();
        responseItem.add(new HeaderStore("loginRedirectLocation", "Location"));
        // Response
        // final Response response3 = new Response(303, responseStore, null);
        final Response response3 = new Response("303", responseItem);

        responseItem = new ArrayList<AbstractResponseItem>();
        responseItem.add(new RegExpValidator("Login Greeting", "John"));
        // Response
        final Response response4 = new Response("200", responseItem);

        /**
         * Action
         */
        final List<ScriptItem> itemList = new ArrayList<ScriptItem>();
        ScriptItem scriptItem;
        actionItems.add(request1);
        actionItems.add(response1);
        scriptItem = new LightWeigthAction("Open Website", actionItems);
        itemList.add(scriptItem);
        actionItems = new ArrayList<AbstractActionItem>();
        actionItems.add(request2);
        actionItems.add(response2);
        scriptItem = new LightWeigthAction("Go to Login", actionItems);
        itemList.add(scriptItem);
        actionItems = new ArrayList<AbstractActionItem>();
        actionItems.add(request3);
        actionItems.add(response3);
        scriptItem = new LightWeigthAction("login", actionItems);
        itemList.add(scriptItem);
        actionItems = new ArrayList<AbstractActionItem>();
        actionItems.add(request4);
        actionItems.add(response4);
        scriptItem = new LightWeigthAction("Login Redirection", actionItems);
        itemList.add(scriptItem);
        actionItems = new ArrayList<AbstractActionItem>();
        return itemList;
    }

    private List<ScriptItem> parseHardWithSubRequests()
    {
        final List<AbstractActionItem> actionItems = new ArrayList<AbstractActionItem>();

        final List<ScriptItem> itemList = new ArrayList<ScriptItem>();
        final Request request = new Request("${host}/posters/topCategory/Dining?categoryId=2");

        final List<AbstractSubrequest> subrequests = new ArrayList<AbstractSubrequest>();
        // Static Subrequest
        final List<String> urls = new ArrayList<String>();
        urls.add("https://www.xceptance.com/css/font-awesome.min.css?1432903128");
        urls.add("https://www.xceptance.com/images/xceptance-logo-transparent-202px.png");
        urls.add("https://www.xceptance.com/js/jquery-1.11.1.min.js");
        subrequests.add(new StaticSubrequest(urls));

        // TODO Meeting
        // XHRSubrequest
        // Request
        final Request requestOfSubrequest = new Request("${host}/posters/getProductOfTopCategory");
        requestOfSubrequest.setMethod(HttpMethod.POST.toString());
        final List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new NameValuePair("categoryId", "2"));
        parameters.add(new NameValuePair("page", "2"));
        requestOfSubrequest.setParameters(parameters);
        // Response
        final List<AbstractResponseItem> responseItem = new ArrayList<AbstractResponseItem>();
        responseItem.add(new RegExpValidator("blub", "Grilled Salmon with Potato Wedges", "Grilled Salmon with Potato Wedges"));
        final Response responseOfSubrequest = new Response(null, responseItem);
        final List<AbstractActionItem> subrequestItems = new ArrayList<AbstractActionItem>();
        subrequestItems.add(requestOfSubrequest);
        subrequestItems.add(responseOfSubrequest);
        final XhrSubrequest xhrSubrequest = new XhrSubrequest("Navigate to second product page.", subrequestItems);
        subrequests.add(xhrSubrequest);

        actionItems.add(request);
        for (final AbstractSubrequest subrequest : subrequests)
        {
            actionItems.add(subrequest);
        }
        final ScriptItem actionItem = new LightWeigthAction("Visit Category Page", actionItems);
        itemList.add(actionItem);
        return itemList;
    }

}
