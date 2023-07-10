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
package com.xceptance.xlt.nocoding.command.action.response.store;

import com.xceptance.xlt.nocoding.command.action.response.AbstractResponseSubItem;
import com.xceptance.xlt.nocoding.command.action.response.extractor.AbstractExtractor;

/**
 * The abstract class for every response item that interacts with the storage.
 *
 * @author ckeiner
 */
public abstract class AbstractResponseStore extends AbstractResponseSubItem
{
    /**
     * The name of the variable
     */
    protected final String variableName;

    /**
     * The selector for the value of the variable
     */
    protected final AbstractExtractor extractor;

    /**
     * Sets {@link #variableName} and {@link #extractor}.
     *
     * @param variableName
     *            The name of the variable
     * @param extractor
     *            The selector to use for the value
     */
    public AbstractResponseStore(final String variableName, final AbstractExtractor extractor)
    {
        this.variableName = variableName;
        this.extractor = extractor;
    }

    public String getVariableName()
    {
        return variableName;
    }

    public AbstractExtractor getExtractor()
    {
        return extractor;
    }

}
