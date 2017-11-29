package com.xceptance.xlt.nocoding.scriptItem.action.subrequest;

import java.util.List;

import com.xceptance.xlt.nocoding.util.Context;

/**
 * Creates a static subrequest, that downloads all specified URLs. The amount of parallel download is specified in the
 * properties with the Key "com.xceptance.xlt.staticContent.downloadThreads".
 * 
 * @author ckeiner
 */
public class StaticSubrequest extends AbstractSubrequest
{
    /**
     * The list of all URLs that are to be downloaded
     */
    private final List<String> urls;

    /**
     * Creates an instance of {@link StaticSubrequest} that sets {@link #urls}.
     * 
     * @param urls
     *            The list of all URLs that are to be downloaded
     */
    public StaticSubrequest(final List<String> urls)
    {
        this.urls = urls;
    }

    /**
     * Executes the static subrequest by creating a {@link Downloader} with the values in the properties. Then the
     * {@link #urls} are added to the {@link Downloader} via {@link Downloader#addRequest(String)} and then loaded.
     */
    @Override
    public void execute(final Context context) throws Exception
    {
        // Get the number of threads in the properties
        final String numberThreads = context.getPropertyByKey("com.xceptance.xlt.staticContent.downloadThreads");
        // Get the UID in the properties
        final String userAgentUID = context.getPropertyByKey("userAgent.UID");
        // Create a Downloader
        final Downloader downloader = new Downloader(context.getWebClient(), Integer.valueOf(numberThreads), Boolean.valueOf(userAgentUID));
        // Add all URLs to the Downloader
        for (final String url : urls)
        {
            downloader.addRequest(url);
        }
        // Load all requests/URLs
        downloader.loadRequests();
    }

    public List<String> getUrls()
    {
        return urls;
    }

}
