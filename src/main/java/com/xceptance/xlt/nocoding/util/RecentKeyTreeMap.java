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

import java.util.Comparator;
import java.util.TreeMap;

/**
 * The map is sorted by a Comparator provided at map creation time. If the comparator finds two equal elements in the
 * put method, it always uses the newest key, while the normal {@link TreeMap} uses the first added key. That means, if
 * the Comparator is {@link String#CASE_INSENSITIVE_ORDER} and K, V are {@link String}s, the newest capitalization of K
 * is stored and the old one is deleted.
 *
 * @author ckeiner
 */
public class RecentKeyTreeMap extends TreeMap<String, String>
{

    /**
     * Randomly generated UID
     */
    private static final long serialVersionUID = -7785650967625657832L;

    public RecentKeyTreeMap()
    {
        super(String.CASE_INSENSITIVE_ORDER);
    }

    public RecentKeyTreeMap(final Comparator<? super String> caseInsensitiveOrder)
    {
        super(caseInsensitiveOrder);
    }

    /**
     * Uses the most recent Key-Name as Key-Value
     */
    @Override
    public String put(final String arg0, final String arg1)
    {
        String output = null;
        if (containsKey(arg0))
        {
            output = this.remove(arg0);
            super.put(arg0, arg1);
        }
        else
        {
            output = super.put(arg0, arg1);
        }
        return output;
    };

}