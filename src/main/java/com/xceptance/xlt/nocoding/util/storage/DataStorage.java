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

import com.xceptance.xlt.nocoding.util.storage.unit.DuplicateStorage;
import com.xceptance.xlt.nocoding.util.storage.unit.SingleStorage;
import com.xceptance.xlt.nocoding.util.storage.unit.unique.DefaultKeyValueStorage;
import com.xceptance.xlt.nocoding.util.storage.unit.unique.RecentKeyUniqueSingleStorage;
import com.xceptance.xlt.nocoding.util.storage.unit.unique.UniqueSingleStorage;
import com.xceptance.xlt.nocoding.util.storage.unit.unique.UniqueStorage;

/**
 * Handles all data storage related matters. This means, the class saves variables, default
 * definitions/headers/parameters/static requests.
 *
 * @author ckeiner
 */
public class DataStorage
{
    protected SingleStorage defaultCookies;

    protected DuplicateStorage defaultParameters;

    protected SingleStorage defaultStatics;

    protected UniqueSingleStorage defaultHeaders;

    protected UniqueStorage variables;

    protected DefaultKeyValueStorage defaultItems;

    public DataStorage()
    {
        defaultCookies = new SingleStorage();
        defaultParameters = new DuplicateStorage();
        defaultStatics = new SingleStorage();
        defaultHeaders = new RecentKeyUniqueSingleStorage();
        variables = new UniqueStorage();
        defaultItems = new DefaultKeyValueStorage();
    }

    public SingleStorage getDefaultCookies()
    {
        return defaultCookies;
    }

    public DuplicateStorage getDefaultParameters()
    {
        return defaultParameters;
    }

    public SingleStorage getDefaultStatics()
    {
        return defaultStatics;
    }

    public UniqueSingleStorage getDefaultHeaders()
    {
        return defaultHeaders;
    }

    public UniqueStorage getVariables()
    {
        return variables;
    }

    public DefaultKeyValueStorage getDefaultItems()
    {
        return defaultItems;
    }

}
