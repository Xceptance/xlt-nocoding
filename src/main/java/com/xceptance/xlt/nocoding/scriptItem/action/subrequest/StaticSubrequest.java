package com.xceptance.xlt.nocoding.scriptItem.action.subrequest;

import java.util.List;

import com.xceptance.xlt.nocoding.util.Context;

public class StaticSubrequest extends AbstractSubrequest
{
    private final List<String> urls;

    public StaticSubrequest(final List<String> urls)
    {
        this.urls = urls;
    }

    @Override
    public void execute(final Context context) throws Exception
    {
        // TODO parallel downloads? how specified
        final Downloader downloader = new Downloader(context.getWebClient());
        for (final String url : urls)
        {
            downloader.addRequest(url);
        }
        downloader.loadRequests();
    }
}
