package com.xceptance.xlt.nocoding.scriptItem.action.subrequest;

import java.util.List;

import com.xceptance.xlt.engine.XltWebClient;

public class StaticSubrequest extends AbstractSubrequest
{
    private final List<String> urls;

    public StaticSubrequest(final List<String> urls)
    {
        this.urls = urls;
    }

    @Override
    public void execute(final XltWebClient webClient) throws Exception
    {
        // TODO parallel downloads? how specified
        final Downloader downloader = new Downloader(webClient);
        for (final String url : urls)
        {
            downloader.addRequest(url);
        }
        downloader.loadRequests();
    }
}
