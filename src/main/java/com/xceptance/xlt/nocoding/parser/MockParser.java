package com.xceptance.xlt.nocoding.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.nocoding.scriptItem.ScriptItem;
import com.xceptance.xlt.nocoding.scriptItem.action.LightWeigthAction;
import com.xceptance.xlt.nocoding.scriptItem.action.Request;
import com.xceptance.xlt.nocoding.scriptItem.action.response.Response;
import com.xceptance.xlt.nocoding.scriptItem.action.response.stores.AbstractResponseStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.stores.HeaderStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.stores.RegExpStore;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.AbstractValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validators.RegExpValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.subrequest.AbstractSubrequest;
import com.xceptance.xlt.nocoding.util.Constants;

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
        /**
         * Request
         */
        final Map<String, String> variables = new HashMap<String, String>();
        variables.put(Constants.NAME, "Visit Homepage");
        variables.put(Constants.URL, "${host}");
        variables.put(Constants.METHOD, "GET");
        variables.put(Constants.PARAMETERS, null);
        variables.put(Constants.BODY, null);
        variables.put(Constants.ENCODEBODY, "false");
        variables.put(Constants.ENCODEPARAMETERS, "false");
        variables.put(Constants.HEADERS, null);
        variables.put(Constants.XHR, "false");

        final Request request = new Request(variables);
        // final Request request = new Request("https://localhost:8443/posters/", "Visit Homepage");
        // request.setHttpmethod(HttpMethod.GET);
        // request.setParameters(null);
        // request.setBody(null);
        // request.setEncodeBody(false);
        // request.setEncodeParameters(false);
        // request.setHeaders(null);
        // request.setXhr(false);

        /**
         * Response
         */

        // Validator
        final List<AbstractValidator> validation = new ArrayList<AbstractValidator>();
        validation.add(new RegExpValidator("Title", "<title>Posters\\s-\\sThe\\sUltimate\\sOnline\\sShop</title>"));
        // Store
        final List<AbstractResponseStore> responseStore = new ArrayList<AbstractResponseStore>();
        responseStore.add(new RegExpStore("Blub", "<title>(.*?)</title>"));
        // Response
        final Response response = new Response(200, responseStore, validation);

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
        final ScriptItem actionItem = new LightWeigthAction(request, response, subrequests);
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
        final Map<String, String> variables = new HashMap<String, String>();
        variables.put(Constants.NAME, "Visit Homepage");
        variables.put(Constants.URL, "https://localhost:8443/posters/");
        final Request request = new Request(variables);

        // final Subrequest subrequest = new Subrequest();
        final ScriptItem actionItem = new LightWeigthAction(request, null, null);
        itemList.add(actionItem);
        return itemList;
    }

    /**
     * this time we have to parse some parameters, namely ${host} and such
     * 
     * @return List of NoCodingScriptItems
     */
    private List<ScriptItem> parseHard()
    {
        final List<ScriptItem> itemList = new ArrayList<ScriptItem>();
        final Map<String, String> variables = new HashMap<String, String>();
        variables.put(Constants.NAME, "Visit Homepage");
        variables.put(Constants.URL, "${host}/posters/");
        final Request request = new Request(variables);

        // final Subrequest subrequest = new Subrequest();
        final ScriptItem actionItem = new LightWeigthAction(request, null, null);
        itemList.add(actionItem);
        return itemList;

    }

    /**
     * Parses all fields that can be specified
     * 
     * @return List of NoCodingScriptItems
     */
    private List<ScriptItem> parseMediumLogin()
    {
        /**
         * Request
         */

        final Request request1 = new Request("https://localhost:8443/posters/", "Open Website");
        final Request request2 = new Request("https://localhost:8443/posters/login", "Go to Login");
        final Request request3 = new Request("https://localhost:8443/posters/login", "login");
        request3.setHttpmethod(HttpMethod.POST.toString());
        final List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new NameValuePair("email", "john@doe.com"));
        parameters.add(new NameValuePair("password", "topsecret"));
        parameters.add(new NameValuePair("btnSignIn", ""));
        request3.setParameters(parameters);
        final Request request4 = new Request("https://localhost:8443/posters/", "Login Redirection");

        /**
         * Response
         */

        // Validator
        List<AbstractValidator> validation = new ArrayList<AbstractValidator>();
        validation.add(new RegExpValidator("Title", "<title>Posters\\s-\\sThe\\sUltimate\\sOnline\\sShop</title>"));
        // Response
        final Response response1 = new Response(null, validation);

        validation = new ArrayList<AbstractValidator>();
        validation.add(new RegExpValidator("login-form Existance", "<form\\sid=\"formLogin\"[\\s\\S]+?>"));
        // Response
        final Response response2 = new Response(null, validation);

        // Store
        final List<AbstractResponseStore> responseStore = new ArrayList<AbstractResponseStore>();
        responseStore.add(new HeaderStore("loginRedirectLocation", "Location"));
        // Response
        // final Response response3 = new Response(303, responseStore, null);
        final Response response3 = new Response(303, responseStore, null);

        validation = new ArrayList<AbstractValidator>();
        validation.add(new RegExpValidator("Login Greeting", "John"));
        // Response
        final Response response4 = new Response(303, null, validation);

        /**
         * Action
         */
        final List<ScriptItem> itemList = new ArrayList<ScriptItem>();
        ScriptItem actionItem;
        actionItem = new LightWeigthAction(request1, response1, null);
        itemList.add(actionItem);
        actionItem = new LightWeigthAction(request2, response2, null);
        itemList.add(actionItem);
        actionItem = new LightWeigthAction(request3, response3, null);
        itemList.add(actionItem);
        actionItem = new LightWeigthAction(request4, response4, null);
        itemList.add(actionItem);
        return itemList;
    }

}
