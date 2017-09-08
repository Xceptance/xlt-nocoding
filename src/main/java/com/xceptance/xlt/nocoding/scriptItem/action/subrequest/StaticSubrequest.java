package com.xceptance.xlt.nocoding.scriptItem.action.subrequest;

import java.util.List;

public class StaticSubrequest extends AbstractSubrequest
{
    private final List<String> urls;

    public StaticSubrequest(final List<String> urls)
    {
        this.urls = urls;
    }
}
