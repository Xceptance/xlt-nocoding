package com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.uniqueStorage;

import com.xceptance.xlt.nocoding.util.RecentKeySet;

public class RecentKeyUniqueSingleStorage extends UniqueSingleStorage
{
    public RecentKeyUniqueSingleStorage()
    {
        super(new RecentKeySet());
    }

}
