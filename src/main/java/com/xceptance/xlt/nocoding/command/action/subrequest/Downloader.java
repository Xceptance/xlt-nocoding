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

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.engine.XltWebClient;

/**
 * Loads all the static content stuff with a passed {@link XltWebClient}, distributed on some threads. For this the
 * {@link StaticContentDownloader} is used.
 */
public class Downloader implements Serializable
{
    private final List<String> urls = new ArrayList<>();

    private final XltWebClient webClient;

    private final int threadCount;

    private final boolean userAgentUID;

    /**
     * @param webClient
     *            : the {@link XltWebClient}, that fires the requests.
     */
    public Downloader(final XltWebClient webClient)
    {
        this(webClient, 1, false);
    }

    /**
     * @param webClient
     *            : the {@link XltWebClient}, that fires the requests.
     * @param threadCount
     *            : amount of threads for parallel loading
     * @param userAgentUID
     */
    public Downloader(final XltWebClient webClient, final int threadCount, final boolean userAgentUID)
    {
        // ParameterUtils.isNotNull(webClient, "XltWebClient");

        this.userAgentUID = userAgentUID;
        this.threadCount = threadCount < 0 ? 1 : threadCount;
        this.webClient = webClient;
    }

    /**
     * Adds a request.
     *
     * @param url
     *            : request url
     */
    public void addRequest(final String url)
    {
        // ParameterUtils.isNotNull(url, "URL");
        urls.add(url);
        XltLogger.runTimeLogger.debug("Adding Static Request: " + url);
    }

    /**
     * loads all the requests that were previously added via {@link #addRequest(String)}
     *
     * @throws Exception
     */
    public void loadRequests() throws Exception
    {
        if (!urls.isEmpty())
        {
            // build a static content downloader only when needed
            final StaticContentDownloader downloader = new StaticContentDownloader(webClient, getThreadCount(), isUserAgentUID());
            try
            {
                // load the additional URLs
                for (final String url : urls)
                {
                    downloader.addRequest(new URL(url));
                }
            }
            finally
            {
                // make sure we wait for all resources and clean up too
                if (downloader != null)
                {
                    downloader.waitForCompletion();
                    downloader.shutdown();
                }
            }
        }
    }

    public int getThreadCount()
    {
        return threadCount;
    }

    public boolean isUserAgentUID()
    {
        return userAgentUID;
    }
}
