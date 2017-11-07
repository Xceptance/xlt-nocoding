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
        final String numberThreads = context.getPropertyByKey("com.xceptance.xlt.staticContent.downloadThreads");
        final String userAgentUID = context.getPropertyByKey("userAgent.UID");
        final Downloader downloader = new Downloader(context.getWebClient(), Integer.valueOf(numberThreads), Boolean.valueOf(userAgentUID));
        ;
        for (final String url : urls)
        {
            downloader.addRequest(url);
        }
        downloader.loadRequests();
    }
}
