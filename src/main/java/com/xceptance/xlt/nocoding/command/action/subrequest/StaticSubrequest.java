/*
 * Copyright (c) 2013-2023 Xceptance Software Technologies GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xceptance.xlt.nocoding.command.action.subrequest;

import java.util.ArrayList;
import java.util.List;

import com.xceptance.xlt.nocoding.util.NoCodingPropertyAdmin;
import com.xceptance.xlt.nocoding.util.context.Context;

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
    private List<String> urls;

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
    public void execute(final Context<?> context) throws Exception
    {
        // Resolve urls
        resolveValues(context);
        // Get the number of threads in the properties
        final String numberThreads = context.getPropertyByKey(NoCodingPropertyAdmin.DOWNLOADTHREADS);
        // Get the UID in the properties
        final String userAgentUID = context.getPropertyByKey(NoCodingPropertyAdmin.USERAGENTUID);
        // Create a Downloader
        Downloader downloader = null;
        if (numberThreads != null && userAgentUID != null)
        {
            downloader = new Downloader(context.getWebClient(), Integer.valueOf(numberThreads), Boolean.valueOf(userAgentUID));
        }
        else
        {
            downloader = new Downloader(context.getWebClient());
        }
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

    @Override
    public void fillDefaultData(final Context<?> context)
    {
        // No need to do anything since there are default static subrequests but no default static urls

    }

    /**
     * Resolves each static url
     *
     * @param context
     */
    public void resolveValues(final Context<?> context)
    {
        final List<String> newUrls = new ArrayList<>();
        for (final String url : urls)
        {
            newUrls.add(context.resolveString(url));
        }
        urls = newUrls;
    }

}
