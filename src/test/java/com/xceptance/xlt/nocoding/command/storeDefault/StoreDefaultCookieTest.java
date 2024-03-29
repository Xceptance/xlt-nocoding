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

import org.htmlunit.util.Cookie;
import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.nocoding.command.Command;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Tests {@link StoreDefaultCookie}
 *
 * @author ckeiner
 */
public class StoreDefaultCookieTest extends AbstractStoreDefaultTest
{

    public StoreDefaultCookieTest(final Context<?> context)
    {
        super(context);
    }

    final String prefixDomain = "domain=";

    final String prefixPath = "path=";

    final String prefixMaxAge = "max-age=";

    final String prefixSecure = "secure=";

    /**
     * Verifies {@link StoreDefaultCookie} can store a minimal default cookie. A minimal default cookie consists of a
     * name, a value and a domain.
     *
     * @throws Throwable
     */
    @Test
    public void singleStore() throws Throwable
    {
        final String name = "name";
        final String value = "value";
        final String domain = ".xceptance.com";

        final String cookieValue = value + ";" + prefixDomain + domain + ";";
        final Command store = new StoreDefaultCookie(name, cookieValue);
        Assert.assertFalse(context.getDefaultCookies().getItems().contains(name));
        Assert.assertNull(context.getWebClient().getCookieManager().getCookie(name));
        store.execute(context);
        Assert.assertTrue(context.getDefaultCookies().getItems().contains(name));
        Assert.assertNotNull(context.getWebClient().getCookieManager().getCookie(name));
        final Cookie cookie = context.getWebClient().getCookieManager().getCookie(name);
        Assert.assertEquals(value, cookie.getValue());
        Assert.assertEquals(domain, cookie.getDomain());

    }

    /**
     * Verifies {@link StoreDefaultCookie} doesn't save the cookie when no value is set
     *
     * @throws Throwable
     */
    @Test
    public void shouldNotStoreWhenNoValueIsSet() throws Throwable
    {
        final String name = "name";
        final String value = ";";
        final Command store = new StoreDefaultCookie(name, value);
        Assert.assertFalse(context.getDefaultCookies().getItems().contains(name));
        Assert.assertNull(context.getWebClient().getCookieManager().getCookie(name));
        store.execute(context);
        Assert.assertFalse(context.getDefaultCookies().getItems().contains(name));
        Assert.assertNull(context.getWebClient().getCookieManager().getCookie(name));
    }

    /**
     * Verifies {@link StoreDefaultCookie} doesn't save the cookie when no value is set
     *
     * @throws Throwable
     */
    @Test
    public void shouldNotStoreWhenNoDomainSet() throws Throwable
    {
        final String name = "name";
        final String value = "value";
        final Command store = new StoreDefaultCookie(name, value);
        Assert.assertFalse(context.getDefaultCookies().getItems().contains(name));
        Assert.assertNull(context.getWebClient().getCookieManager().getCookie(name));
        store.execute(context);
        Assert.assertFalse(context.getDefaultCookies().getItems().contains(name));
        Assert.assertNull(context.getWebClient().getCookieManager().getCookie(name));
    }

    /**
     * Verifies {@link StoreDefaultCookie} can delete a specified cookie
     *
     * @throws Throwable
     */
    @Test
    public void deleteStore() throws Throwable
    {
        final String name = "name";
        final String value = "value";
        final String domain = ".xceptance.com";
        final String cookieValue = value + ";" + prefixDomain + domain + ";";

        Command store = new StoreDefaultCookie(name, cookieValue);
        Assert.assertFalse(context.getDefaultCookies().getItems().contains(name));
        Assert.assertNull(context.getWebClient().getCookieManager().getCookie(name));
        store.execute(context);
        Assert.assertTrue(context.getDefaultCookies().getItems().contains(name));
        Assert.assertNotNull(context.getWebClient().getCookieManager().getCookie(name));
        final Cookie cookie = context.getWebClient().getCookieManager().getCookie(name);
        Assert.assertEquals(value, cookie.getValue());
        Assert.assertEquals(domain, cookie.getDomain());

        store = new StoreDefaultCookie(name, Constants.DELETE);
        store.execute(context);
        Assert.assertFalse(context.getDefaultCookies().getItems().contains(name));
        Assert.assertNull(context.getWebClient().getCookieManager().getCookie(name));
    }

    /**
     * Verifies {@link StoreDefaultCookie} can delete all cookies
     *
     * @throws Throwable
     */
    @Test
    public void deleteAllDefaultCookies() throws Throwable
    {
        final String name = "name_";
        final String value = "value";
        final String domain = ".xceptance.com";
        final String cookieValue = value + ";" + prefixDomain + domain + ";";

        final List<Command> store = new ArrayList<>();
        store.add(new StoreDefaultCookie(name + "1", cookieValue));
        store.add(new StoreDefaultCookie(name + "2", cookieValue));
        store.add(new StoreDefaultCookie(name + "3", cookieValue));
        store.add(new StoreDefaultCookie(name + "4", cookieValue));
        store.add(new StoreDefaultCookie(name + "5", cookieValue));
        Assert.assertTrue(context.getDefaultCookies().getItems().isEmpty());
        int i = 1;
        for (final Command scriptItem : store)
        {
            scriptItem.execute(context);
            Assert.assertTrue(context.getDefaultCookies().getItems().contains("name_" + i));
            i++;
        }
        final Command deleteIt = new StoreDefaultCookie(Constants.COOKIES, Constants.DELETE);
        deleteIt.execute(context);
        Assert.assertTrue(context.getDefaultCookies().getItems().isEmpty());
    }

}
