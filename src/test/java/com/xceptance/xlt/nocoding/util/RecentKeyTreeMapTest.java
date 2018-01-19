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
        this.treeMap = new RecentKeyTreeMap();
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
