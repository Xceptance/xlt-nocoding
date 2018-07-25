package com.xceptance.xlt.nocoding.command.storeDefault;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.util.Cookie;
import com.xceptance.xlt.nocoding.command.Command;
import com.xceptance.xlt.nocoding.command.storeDefault.StoreDefaultCookie;
import com.xceptance.xlt.nocoding.util.Constants;

/**
 * Tests {@link StoreDefaultCookie}
 * 
 * @author ckeiner
 */
public class StoreDefaultCookieTest extends AbstractStoreDefaultTest
{

    final String prefixDomain = "domain=";

    final String prefixPath = "path=";

    final String prefixMaxAge = "max-age=";

    final String prefixSecure = "secure=";

    /**
     * Verifies {@link StoreDefaultCookie} can store a minimal default cookie. A minimal default cookie consists of a name,
     * a value and a domain.
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

        final List<Command> store = new ArrayList<Command>();
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
