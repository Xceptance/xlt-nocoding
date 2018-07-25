package com.xceptance.xlt.nocoding.util.storage.unit.unique;

import com.xceptance.xlt.nocoding.util.RecentKeySet;

public class RecentKeyUniqueSingleStorage extends UniqueSingleStorage
{
    public RecentKeyUniqueSingleStorage()
    {
        super(new RecentKeySet());
    }

}
