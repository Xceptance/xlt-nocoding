package com.xceptance.xlt.nocoding.util.action.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TagsLists
{

    protected HashMap<String, List<String>> mappy = new HashMap<>();

    private final List<String> root;

    private final List<String> requestTags;

    private final List<String> responseTags;

    private final List<String> subRequestTags;

    private final List<String> actionTags;

    public TagsLists()
    {

        root = new ArrayList<>();
        root.add("Action");
        root.add("Store");
        root.add("Name");
        root.add("Httpcode");
        root.add("Url");
        root.add("Method");
        root.add("Xhr");
        root.add("Encode-Parameters");
        root.add("Encode-Body");
        root.add("Headers");
        root.add("Parameters");
        root.add("Cookies");
        root.add("Variables");
        root.add("Header");
        root.add("Encoded");

        actionTags = new ArrayList<>();
        actionTags.add("Name");
        actionTags.add("Request");
        actionTags.add("Response");
        actionTags.add("Subrequests");

        requestTags = new ArrayList<>();
        requestTags.add("Url");
        requestTags.add("Method");
        requestTags.add("Xhr");
        requestTags.add("Encode-Parameters");
        requestTags.add("Parameters");
        requestTags.add("Headers");
        requestTags.add("Body");
        requestTags.add("Encode-Body");

        responseTags = new ArrayList<>();
        responseTags.add("Httpcode");
        responseTags.add("Validate");
        responseTags.add("Store");

        subRequestTags = new ArrayList<>();
        subRequestTags.add("Xhr");
        subRequestTags.add("Static");

        mappy.put("root", root);
        mappy.put("actionTags", actionTags);
        mappy.put("requestTags", requestTags);
        mappy.put("responseTags", responseTags);
        mappy.put("subRequestTags", subRequestTags);
    }

}
