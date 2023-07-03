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
package com.xceptance.xlt.nocoding.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RecentKeyTreeMapTest
{
    Map<String, String> treeMap;

    @Before
    public void init()
    {
        treeMap = new RecentKeyTreeMap();
    }

    @Test
    public void testPutRecentKeys()
    {
        String value = "value";
        List<String> keys = new ArrayList<>();
        keys.add("test");
        keys.add("testing");
        keys.add("another String");
        for (final String key : keys)
        {
            treeMap.put(key, value);
        }
        System.out.println(treeMap.toString());
        Iterator<String> keySet = treeMap.keySet().iterator();

        while (keySet.hasNext())
        {
            final String nextKey = keySet.next();
            Assert.assertTrue(keys.contains(nextKey));
            Assert.assertEquals(value, treeMap.get(nextKey));
        }

        value = "another value";
        keys = new ArrayList<>();
        keys.add("TeSt");
        keys.add("TesTING");
        keys.add("ANOTHER STRING");

        for (final String key : keys)
        {
            treeMap.put(key, value);
        }
        System.out.println(treeMap.toString());
        keySet = treeMap.keySet().iterator();

        while (keySet.hasNext())
        {
            final String nextKey = keySet.next();
            Assert.assertTrue(keys.contains(nextKey));
            Assert.assertEquals(value, treeMap.get(nextKey));
        }

    }

    @Test
    public void testPutAllRecentKeys()
    {
        String value = "value";
        Map<String, String> anotherMap = new HashMap<>();
        anotherMap.put("test", value);
        anotherMap.put("testing", value);
        anotherMap.put("another String", value);
        treeMap.putAll(anotherMap);
        System.out.println(treeMap.toString());
        Iterator<String> keySet = treeMap.keySet().iterator();

        while (keySet.hasNext())
        {
            final String nextKey = keySet.next();
            Assert.assertTrue(anotherMap.containsKey(nextKey));
            Assert.assertEquals(value, treeMap.get(nextKey));
        }

        value = "another value";
        anotherMap = new HashMap<>();
        anotherMap.put("TeSt", value);
        anotherMap.put("TesTING", value);
        anotherMap.put("ANOTHER STRING", value);
        treeMap.putAll(anotherMap);
        System.out.println(treeMap.toString());
        keySet = treeMap.keySet().iterator();

        while (keySet.hasNext())
        {
            final String nextKey = keySet.next();
            Assert.assertTrue(anotherMap.containsKey(nextKey));
            Assert.assertEquals(value, treeMap.get(nextKey));
        }

    }

}
