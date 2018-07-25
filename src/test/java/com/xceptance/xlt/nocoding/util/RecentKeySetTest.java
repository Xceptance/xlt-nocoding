package com.xceptance.xlt.nocoding.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RecentKeySetTest
{
    Set<String> keySet;

    @Before
    public void init()
    {
        keySet = new RecentKeySet();
    }

    /**
     * Verifies the set has only the most recent names when adding one item at a time
     */
    @Test
    public void testAddToSet()
    {
        final String item = "item";
        Assert.assertTrue(keySet.isEmpty());
        keySet.add(item);
        Assert.assertEquals(1, keySet.size());
        Assert.assertTrue(keySet.contains(item));
        final String sameItem = "iTem";
        Assert.assertFalse(keySet.isEmpty());
        keySet.add(sameItem);
        Assert.assertEquals(1, keySet.size());
        Assert.assertTrue(keySet.contains(sameItem));
        Assert.assertTrue(keySet.contains(item));
    }

    /**
     * Verifies the set has only the most recent names when adding a collection to the set
     */
    @Test
    public void testAddAllToSet()
    {
        final List<String> items = new ArrayList<>();
        items.add("item");
        items.add("another item");
        items.add("different");
        Assert.assertTrue(keySet.isEmpty());
        items.forEach((item) -> {
            keySet.add(item);
        });
        Assert.assertEquals(3, keySet.size());
        items.forEach((item) -> {
            Assert.assertTrue(keySet.contains(item));
        });

        final List<String> sameItems = new ArrayList<>();
        sameItems.add("iTEm");
        sameItems.add("anotHer iteM");
        sameItems.add("DIFFEreNt");
        Assert.assertFalse(keySet.isEmpty());
        sameItems.forEach((item) -> {
            keySet.add(item);
        });
        Assert.assertEquals(3, keySet.size());
        sameItems.forEach((item) -> {
            Assert.assertTrue(keySet.contains(item));
        });
        items.forEach((item) -> {
            Assert.assertTrue(keySet.contains(item));
        });
    }
}
