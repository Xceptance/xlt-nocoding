package com.xceptance.xlt.nocoding.scriptItem.action.subrequest;

import java.util.List;

import com.xceptance.xlt.engine.XltWebClient;
import com.xceptance.xlt.nocoding.util.PropertyManager;

public class StaticSubrequest extends AbstractSubrequest
{
    private final List<String> urls;

    public StaticSubrequest(final List<String> urls)
    {
        this.urls = urls;
    }

    @Override
    public void execute(final PropertyManager propertyManager) throws Exception
    {
        // TODO Auto-generated method stub
        final Downloader downloader = new Downloader((XltWebClient) propertyManager.getWebClient());
        for (final String url : urls)
        {
            downloader.addRequest(url);
        }
        downloader.loadRequests();
    }
}
