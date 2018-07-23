package com.xceptance.xlt.nocoding.util.dataStorage.storageUnits.uniqueStorage;

import com.xceptance.xlt.nocoding.util.RecentKeyTreeMap;

public class RecentKeyUniqueStorage extends UniqueStorage
{

    public RecentKeyUniqueStorage()
    {
        super(new RecentKeyTreeMap());
    }

}
