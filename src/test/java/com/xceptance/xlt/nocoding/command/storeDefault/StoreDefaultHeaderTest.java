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
import org.junit.Ignore;
import org.junit.Test;

import com.xceptance.xlt.nocoding.command.Command;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Tests {@link StoreDefaultHeader}
 *
 * @author ckeiner
 */
public class StoreDefaultHeaderTest extends AbstractStoreDefaultTest
{
    public StoreDefaultHeaderTest(final Context<?> context)
    {
        super(context);
    }

    /**
     * Verifies {@link StoreDefaultHeader} can store one default header
     *
     * @throws Throwable
     */
    @Test
    public void singleStore() throws Throwable
    {
        final Command store = new StoreDefaultHeader("header_1", "value");
        Assert.assertFalse(context.getDefaultHeaders().getItems().contains("header_1"));
        store.execute(context);
        Assert.assertTrue(context.getDefaultHeaders().getItems().contains("header_1"));
    }

    /**
     * Verifies {@link StoreDefaultHeader} can delete a specified header
     *
     * @throws Throwable
     */
    @Test
    public void deleteStore() throws Throwable
    {
        Command store = new StoreDefaultHeader("header_1", "value");
        Assert.assertFalse(context.getDefaultHeaders().getItems().contains("header_1"));
        store.execute(context);
        Assert.assertTrue(context.getDefaultHeaders().getItems().contains("header_1"));
        store = new StoreDefaultHeader("header_1", Constants.DELETE);
        store.execute(context);
        Assert.assertFalse(context.getDefaultHeaders().getItems().contains("header_1"));
    }

    /**
     * Verifies {@link StoreDefaultHeader} can delete all headers
     *
     * @throws Throwable
     */
    @Test
    public void deleteAllDefaultHeaders() throws Throwable
    {
        final List<Command> store = new ArrayList<>();
        store.add(new StoreDefaultHeader("header_1", "value"));
        store.add(new StoreDefaultHeader("header_2", "value"));
        store.add(new StoreDefaultHeader("header_3", "value"));
        store.add(new StoreDefaultHeader("header_4", "value"));
        store.add(new StoreDefaultHeader("header_5", "value"));
        Assert.assertFalse(context.getDefaultHeaders().getItems().contains("header_1"));
        int i = 1;
        for (final Command scriptItem : store)
        {
            scriptItem.execute(context);
            Assert.assertTrue(context.getDefaultHeaders().getItems().contains("header_" + i));
            i++;
        }
        final Command deleteIt = new StoreDefaultHeader(Constants.HEADERS, Constants.DELETE);
        deleteIt.execute(context);
        Assert.assertFalse(context.getDefaultHeaders().getItems().contains("header_1"));
        Assert.assertTrue(context.getDefaultHeaders().getItems().isEmpty());
    }

    /**
     * Verifies headers stored via {@link StoreDefaultHeader} are stored case insensitive
     *
     * @throws Throwable
     */
    @Test
    // TODO ignored test
    @Ignore
    public void storeCaseInsensitiveHeaders() throws Throwable
    {
        final AbstractStoreDefaultItem item1 = new StoreDefaultHeader("heAder_1", "heAder_1");
        final AbstractStoreDefaultItem item2 = new StoreDefaultHeader("header_1", "header_1");
        Assert.assertFalse(context.getDefaultHeaders().getItems().contains("header_1"));
        item1.execute(context);
        Assert.assertTrue(context.getDefaultHeaders().getItems().contains("header_1"));
        item2.execute(context);
        Assert.assertTrue(context.getDefaultHeaders().getItems().contains("header_1"));
        Assert.assertFalse(context.getDefaultHeaders().getItems().contains("heAder_1"));
    }

}
