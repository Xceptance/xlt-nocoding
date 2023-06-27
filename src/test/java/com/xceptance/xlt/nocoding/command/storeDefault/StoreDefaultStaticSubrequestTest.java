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
package com.xceptance.xlt.nocoding.command.storeDefault;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.command.Command;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Tests {@link StoreDefaultStaticSubrequest}
 *
 * @author ckeiner
 */
public class StoreDefaultStaticSubrequestTest extends AbstractStoreDefaultTest
{

    public StoreDefaultStaticSubrequestTest(final Context<?> context)
    {
        super(context);
    }

    /**
     * Verifies {@link StoreDefaultStaticSubrequest} can store one default static url
     *
     * @throws Throwable
     */
    @Test
    public void singleStore() throws Throwable
    {
        final String url = "http://www.xceptance.net";
        final Command store = new StoreDefaultStaticSubrequest(url);
        Assert.assertTrue(context.getDefaultStatics().getItems().isEmpty());
        store.execute(context);
        Assert.assertTrue(context.getDefaultStatics().getItems().contains(url));
    }

    /**
     * Verifies {@link StoreDefaultStaticSubrequest} can delete one default static url
     *
     * @throws Throwable
     */
    @Test
    public void deleteStore() throws Throwable
    {
        final String url = "http://www.xceptance.net";
        Command store = new StoreDefaultStaticSubrequest(url);
        Assert.assertTrue(context.getDefaultStatics().getItems().isEmpty());
        store.execute(context);
        Assert.assertTrue(context.getDefaultStatics().getItems().contains(url));

        store = new StoreDefaultStaticSubrequest(Constants.DELETE);
        store.execute(context);
        Assert.assertTrue(context.getDefaultStatics().getItems().isEmpty());
    }

    /**
     * Verifies {@link StoreDefaultStaticSubrequest} can delete all default static url
     *
     * @throws Throwable
     */
    @Test
    public void deleteAllDefaultHeaders() throws Throwable
    {
        final String url = "http://www.xceptance.net";
        final List<Command> store = new ArrayList<>();
        store.add(new StoreDefaultStaticSubrequest(url));
        store.add(new StoreDefaultStaticSubrequest(url));
        store.add(new StoreDefaultStaticSubrequest(url));
        store.add(new StoreDefaultStaticSubrequest(url));
        store.add(new StoreDefaultStaticSubrequest(url));
        Assert.assertTrue(context.getDefaultStatics().getItems().isEmpty());
        for (final Command scriptItem : store)
        {
            scriptItem.execute(context);
            Assert.assertTrue(context.getDefaultStatics().getItems().contains(url));
        }
        Assert.assertEquals(context.getDefaultStatics().getItems().size(), 5);
        final Command deleteIt = new StoreDefaultStaticSubrequest(Constants.DELETE);
        deleteIt.execute(context);
        Assert.assertTrue(context.getDefaultStatics().getItems().isEmpty());
    }

}
