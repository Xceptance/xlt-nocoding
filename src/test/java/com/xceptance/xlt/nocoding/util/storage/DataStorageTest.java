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
package com.xceptance.xlt.nocoding.util.storage;

import org.htmlunit.HttpMethod;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.nocoding.util.Constants;
import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.context.LightWeightContext;

/**
 * Tests {@link DataStorage}
 *
 * @author ckeiner
 */
public class DataStorageTest
{
    private Context<?> context;

    /**
     * Creates a new {@link Context}
     */
    @Before
    public void init()
    {
        context = new LightWeightContext(XltProperties.getInstance());
    }

    @Test
    public void oneKey()
    {
        final String key = "test";
        final String value = "t";
        context.getVariables().store(key, value);
        context.getDefaultItems().store(key, value);

        Assert.assertEquals("t", context.getVariables().get(key));
        Assert.assertEquals("t", context.getDefaultItems().get(key));
    }

    @Test
    public void sameKey()
    {
        String key = "test";
        String value = "t";
        context.getVariables().store(key, value);
        context.getDefaultItems().store(key, value);

        Assert.assertEquals("t", context.getVariables().get(key));
        Assert.assertEquals("t", context.getDefaultItems().get(key));

        key = "test";
        value = "e";
        context.getVariables().store(key, value);
        context.getDefaultItems().store(key, value);

        Assert.assertEquals("e", context.getVariables().get(key));
        Assert.assertEquals("e", context.getDefaultItems().get(key));
        Assert.assertNotEquals("t", context.getVariables().get(key));
        Assert.assertNotEquals("t", context.getDefaultItems().get(key));

        key = "test";
        value = "s";
        context.getVariables().store(key, value);
        context.getDefaultItems().store(key, value);

        Assert.assertEquals("s", context.getVariables().get(key));
        Assert.assertEquals("s", context.getDefaultItems().get(key));
        Assert.assertNotEquals("t", context.getVariables().get(key));
        Assert.assertNotEquals("t", context.getDefaultItems().get(key));
        Assert.assertNotEquals("e", context.getVariables().get(key));
        Assert.assertNotEquals("e", context.getDefaultItems().get(key));
    }

    @Test
    public void noResult()
    {
        final String key = "test";

        Assert.assertNull(context.getVariables().get(key));
        Assert.assertNull(context.getDefaultItems().get(key));

    }

    @Test
    public void deleteConfigItem()
    {
        final String key = "test";
        final String value = "t";

        context.getDefaultItems().store(key, value);
        Assert.assertEquals(value, context.getDefaultItems().get(key));
        context.getDefaultItems().remove(key);
        Assert.assertNotEquals(value, context.getDefaultItems().get(key));
        Assert.assertEquals(null, context.getDefaultItems().get(key));

        final String fallback = "est";
        context.getDefaultItems().store(key, value, fallback);
        Assert.assertEquals(value, context.getDefaultItems().get(key));
        context.getDefaultItems().remove(key);
        Assert.assertEquals(fallback, context.getDefaultItems().get(key));

    }

    /**
     * Verifies that default values are set
     */
    @Test
    public void loadDefaultValues()
    {
        Assert.assertEquals(DefaultValues.METHOD, context.getDefaultItems().get(Constants.METHOD));
        Assert.assertEquals(DefaultValues.HTTPCODE, context.getDefaultItems().get(Constants.HTTPCODE));
        Assert.assertEquals(DefaultValues.ENCODEBODY, context.getDefaultItems().get(Constants.ENCODEBODY));
        Assert.assertEquals(DefaultValues.ENCODEPARAMETERS, context.getDefaultItems().get(Constants.ENCODEPARAMETERS));
        Assert.assertEquals(DefaultValues.XHR, context.getDefaultItems().get(Constants.XHR));

    }

    /**
     * Verifies default values can be overwritten. Furthermore verifies, when the values are deleted, these default
     * values are used again
     */
    @Test
    public void overwriteDefaultValues()
    {
        final String method = HttpMethod.POST.toString();
        final String httpcode = "303";
        final String encodebody = "true";
        final String encodeparameters = "true";
        final String xhr = "true";

        Assert.assertEquals(DefaultValues.METHOD, context.getDefaultItems().get(Constants.METHOD));
        Assert.assertEquals(DefaultValues.HTTPCODE, context.getDefaultItems().get(Constants.HTTPCODE));
        Assert.assertEquals(DefaultValues.ENCODEBODY, context.getDefaultItems().get(Constants.ENCODEBODY));
        Assert.assertEquals(DefaultValues.ENCODEPARAMETERS, context.getDefaultItems().get(Constants.ENCODEPARAMETERS));
        Assert.assertEquals(DefaultValues.XHR, context.getDefaultItems().get(Constants.XHR));

        context.getDefaultItems().store(Constants.METHOD, method);
        context.getDefaultItems().store(Constants.HTTPCODE, httpcode);
        context.getDefaultItems().store(Constants.ENCODEBODY, encodebody);
        context.getDefaultItems().store(Constants.ENCODEPARAMETERS, encodeparameters);
        context.getDefaultItems().store(Constants.XHR, xhr);

        Assert.assertEquals(method, context.getDefaultItems().get(Constants.METHOD));
        Assert.assertEquals(httpcode, context.getDefaultItems().get(Constants.HTTPCODE));
        Assert.assertEquals(encodebody, context.getDefaultItems().get(Constants.ENCODEBODY));
        Assert.assertEquals(encodeparameters, context.getDefaultItems().get(Constants.ENCODEPARAMETERS));
        Assert.assertEquals(xhr, context.getDefaultItems().get(Constants.XHR));

        context.getDefaultItems().remove(Constants.METHOD);
        context.getDefaultItems().remove(Constants.HTTPCODE);
        context.getDefaultItems().remove(Constants.ENCODEBODY);
        context.getDefaultItems().remove(Constants.ENCODEPARAMETERS);
        context.getDefaultItems().remove(Constants.XHR);

        Assert.assertEquals(DefaultValues.METHOD, context.getDefaultItems().get(Constants.METHOD));
        Assert.assertEquals(DefaultValues.HTTPCODE, context.getDefaultItems().get(Constants.HTTPCODE));
        Assert.assertEquals(DefaultValues.ENCODEBODY, context.getDefaultItems().get(Constants.ENCODEBODY));
        Assert.assertEquals(DefaultValues.ENCODEPARAMETERS, context.getDefaultItems().get(Constants.ENCODEPARAMETERS));
        Assert.assertEquals(DefaultValues.XHR, context.getDefaultItems().get(Constants.XHR));

    }

}
