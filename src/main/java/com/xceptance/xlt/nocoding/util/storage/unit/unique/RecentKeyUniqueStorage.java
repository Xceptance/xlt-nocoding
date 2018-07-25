package com.xceptance.xlt.nocoding.util.storage.unit.unique;

import com.xceptance.xlt.nocoding.util.RecentKeyTreeMap;

public class RecentKeyUniqueStorage extends UniqueStorage
{

    public RecentKeyUniqueStorage()
    {
        super(new RecentKeyTreeMap());
    }

}
