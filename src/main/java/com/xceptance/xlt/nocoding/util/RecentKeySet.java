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
