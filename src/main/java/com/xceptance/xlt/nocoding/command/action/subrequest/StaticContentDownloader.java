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
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.htmlunit.WebClient;
import org.htmlunit.WebRequest;

import com.xceptance.common.util.SynchronizingCounter;
import com.xceptance.common.util.concurrent.DaemonThreadFactory;
import com.xceptance.xlt.api.engine.Session;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.engine.XltWebClient;

/**
 * The {@link StaticContentDownloader} aids in managing the parallel download of resources. The goal is to better
 * simulate the download behavior of real browsers. The number of threads working in parallel is controlled by
 * configuration. Note that there is no differentiation between requests to different domains.
 */
public class StaticContentDownloader implements Serializable
{
    /**
     * The maximum time to wait for {@link #ongoingRequestsCount} to become 0.
     */
    private static final int WAIT_TIMEOUT = 5 * 60 * 1000;

    /**
     * The executor service running RequestHandler instances. The service's thread pool is unbounded, but shrinks
     * automatically if possible.
     */
    private final ExecutorService executorService;

    /**
     * Maintains the number of requests which have been added, but have not been loaded completely yet.
     */
    private final SynchronizingCounter ongoingRequestsCount;

    /**
     * Indicates whether or not parallel downloads of URLs are enabled.
     */
    private boolean parallelModeEnabled;

    /**
     * The number of threads which load URLs asynchronously.
     */
    private final int threadCount;

    /**
     * The web client to use when loading URLs.
     */
    private final XltWebClient webClient;

    /**
     * Use dynamic UID in the user agent
     */
    private final boolean userAgentUID;

    /**
     * Creates a new RequestQueue object and initializes it with the given web client and the number of threads which
     * process the requests.
     *
     * @param webClient
     *            the web client to use
     * @param threadCount
     *            the number of threads
     * @param userAgentUID
     *            do we need dynamic user agent identifiers
     */
    public StaticContentDownloader(final XltWebClient webClient, final int threadCount, final boolean userAgentUID)
    {
        this.webClient = webClient;
        this.threadCount = threadCount;
        parallelModeEnabled = true;
        this.userAgentUID = userAgentUID;

        final ThreadFactory threadFactory = new DaemonThreadFactory(i -> Session.getCurrent().getUserID() + "-pool-" + i);

        executorService = Executors.newFixedThreadPool(threadCount, threadFactory);
        ongoingRequestsCount = new SynchronizingCounter(0);
    }

    /**
     * Adds the given URL to the list of URLs to be loaded. Whether the URL is loaded by the calling or a separate
     * thread is controlled by the parallelModeEnabled flag. If the flag is false, the method does not return until the
     * URL is loaded. If the flag is true, as many URLs as threadCount can be added without blocking.
     *
     * @param url
     *            the URL to load
     */
    public void addRequest(final URL url)
    {
        ongoingRequestsCount.increment();

        RequestHandler requestHandler = null;

        if (isParallelModeEnabled())
        {
            // handle the request asynchronously
            requestHandler = new RequestHandler(url);
            executorService.execute(requestHandler);

            // debug
            // ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)
            // executorService;
            // System.err.printf("active = %d / size = %d\n",
            // threadPoolExecutor.getActiveCount(),
            // threadPoolExecutor.getPoolSize());
        }
        else
        {
            // handle the request synchronously
            requestHandler = new RequestHandler(url);
            requestHandler.run();
        }
    }

    /**
     * Indicates whether or not the parallel execution of requests is enabled.
     *
     * @return the current state
     */
    public boolean isParallelModeEnabled()
    {
        return parallelModeEnabled && threadCount > 1;
    }

    /**
     * Enables or disables the parallel execution of requests.
     *
     * @param value
     *            the new state
     */
    public void setParallelModeEnabled(final boolean value)
    {
        parallelModeEnabled = value;
    }

    /**
     * Shuts this request queue down. Any background thread is terminated.
     */
    public void shutdown()
    {
        executorService.shutdownNow();
    }

    /**
     * Waits until all previously added requests have been executed.
     */
    public void waitForCompletion()
    {
        try
        {
            ongoingRequestsCount.awaitZero(WAIT_TIMEOUT);

            // check whether we really have reached 0
            final int count = ongoingRequestsCount.get();
            if (count != 0)
            {
                // no, log an event
                Session.getCurrent().getDataManager().logEvent("Timed out when loading static content", "Outstanding requests: " + count);
            }
        }
        catch (final InterruptedException ex)
        {
            // ignore
        }
    }

    /**
     * A {@link Runnable} that loads exactly one resource.
     */
    private class RequestHandler implements Runnable
    {
        /**
         * The URL to load.
         */
        private final URL url;

        /**
         * Creates a new RequestHandler object.
         *
         * @param url
         *            the URL to load
         * @param referrerUrl
         *            the referrer URL
         * @param requestStack
         *            the request stack to use
         */
        public RequestHandler(final URL url)
        {
            this.url = url;
        }

        /**
         * Executes the download.
         */
        @Override
        public void run()
        {
            try
            {
                setUserAgentUID(webClient, userAgentUID);
                webClient.loadWebResponse(new WebRequest(url));
            }
            catch (final Exception e)
            {
                XltLogger.runTimeLogger.error("Failed to load static content from: " + url, e);
            }
            finally
            {
                // request is done
                ongoingRequestsCount.decrement();
            }
        }

        /**
         * Constant to identify the UID in the user agent string
         */
        private static final String MARKER = " UID/";

        /**
         * Adds a random UUID to the user agent. Replaces an existing one if already set.
         *
         * @param webClient
         *            the client to manipulate
         * @param active
         *            true when parameter should be inserted
         */
        public void setUserAgentUID(final WebClient webClient, final boolean active)
        {
            if (active)
            {
                final String userAgent = webClient.getBrowserVersion().getUserAgent();
                webClient.addRequestHeader("User-Agent", userAgent.concat(MARKER).concat(UUID.randomUUID().toString()));
            }
        }
    }
}
