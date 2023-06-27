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

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.xceptance.xlt.engine.XltWebClient;

/**
 * Tests {@link Downloader}
 *
 * @author ckeiner
 */
public class DownloaderTest
{
    private static XltWebClient client;

    private static boolean userAgentUID;

    private static int threadCount;

    private static final List<String> urls = new ArrayList<>();

    @BeforeClass
    public static void setup()
    {
        userAgentUID = false;
        threadCount = 2;
        client = new XltWebClient();

        client.setTimerName("Test"); // NECESSARY OR CRASH

        urls.add("https://www.xceptance.com/css/font-awesome.min.css?1432903128");
        urls.add("https://www.xceptance.com/images/xceptance-logo-transparent-202px.png");
        urls.add("https://www.xceptance.com/js/jquery-1.11.1.min.js");
    }

    /**
     * Tests the constructor of {@link Downloader}
     */
    @Test
    public void testConstructors()
    {
        Downloader downloader;
        downloader = new Downloader(client);
        Assert.assertEquals(1, downloader.getThreadCount());
        Assert.assertEquals(false, downloader.isUserAgentUID());

        downloader = new Downloader(client, threadCount, userAgentUID);
        Assert.assertEquals(threadCount, downloader.getThreadCount());
        Assert.assertEquals(userAgentUID, downloader.isUserAgentUID());
    }

    /**
     * Uses {@link Downloader} with an empty url list
     */
    @Test
    public void testEmptyUrls()
    {
        Downloader downloader;
        downloader = new Downloader(client);
        try
        {
            downloader.loadRequests();
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * {@link Downloader} loads content linear
     *
     * @throws Exception
     */
    @Test
    public void testLinearDownload() throws Exception
    {
        client.setTimerName("Test");
        // client.getPage("https://www.xceptance.com/en/");

        final Downloader downloader = new Downloader(client);
        for (final String url : urls)
        {
            downloader.addRequest(url);
        }
        downloader.loadRequests();
    }

    /**
     * {@link Downloader} loads content parallel
     *
     * @throws Exception
     */
    @Test
    public void testParallelDownload() throws Exception
    {
        client.setTimerName("Test");
        // client.getPage("https://www.xceptance.com/en/");

        final Downloader downloader = new Downloader(client, urls.size(), false);
        for (final String url : urls)
        {
            downloader.addRequest(url);
        }
        downloader.loadRequests();
    }

    /**
     * Uses {@link Downloader} with {@link Downloader#Downloader(XltWebClient, int, boolean)} whereas the last argument
     * (boolean userAgentUID) is true
     *
     * @throws Exception
     */
    @Test
    public void testWithUAUID() throws Exception
    {
        client.setTimerName("Test");
        // client.getPage("https://www.xceptance.com/en/");

        final Downloader downloader = new Downloader(client, urls.size(), true);
        for (final String url : urls)
        {
            downloader.addRequest(url);
        }
        downloader.loadRequests();
    }
}
