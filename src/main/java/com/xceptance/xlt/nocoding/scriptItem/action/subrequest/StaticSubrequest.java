package com.xceptance.xlt.nocoding.scriptItem.action.subrequest;

import java.util.List;

public class StaticSubrequest extends AbstractSubrequest
{
    private final List<String> urls;

    public StaticSubrequest(final List<String> urls)
    {
        this.urls = urls;
    }

    @Override
    public void execute() throws Exception
    {
        // TODO Auto-generated method stub
        final Downloader downloader = new Downloader(this.propertyManager.getWebClient());
        for (final String url : urls)
        {
            downloader.addRequest(url);
        }
        downloader.loadRequests();
    }
}
