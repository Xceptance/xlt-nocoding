package com.xceptance.xlt.nocoding.util;

import java.util.TreeSet;

public class RecentKeySet extends TreeSet<String>
{
    public RecentKeySet()
    {
        super(String.CASE_INSENSITIVE_ORDER);
    }

    @Override
    public boolean add(final String item)
    {
        if (contains(item))
        {
            super.remove(item);
        }
        super.add(item);
        return false;
    }

}
