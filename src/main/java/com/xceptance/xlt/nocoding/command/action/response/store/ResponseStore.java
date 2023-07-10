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

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.command.action.response.extractor.AbstractExtractor;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Takes an {@link AbstractExtractor} and stores the result in {@link Context#getVariables()}.
 *
 * @author ckeiner
 */
public class ResponseStore extends AbstractResponseStore
{

    /**
     * Creates an instance of {@link ResponseStore}, that stores the result from {@link AbstractExtractor} under the
     * name {@link #getVariableName()}.
     *
     * @param variableName
     *            The name of the variable
     * @param extractor
     *            The selector to use for the value
     */
    public ResponseStore(final String variableName, final AbstractExtractor extractor)
    {
        super(variableName, extractor);
    }

    /**
     * Resolves values, then stores the result in {@link Context#getVariables()}.
     *
     * @param context
     *            The {@link Context} to use
     */
    @Override
    public void execute(final Context<?> context)
    {
        // Execute the selector
        extractor.execute(context);
        // Store the solution
        context.getVariables().store(getVariableName(), extractor.getResult().get(0));
        XltLogger.runTimeLogger.info("Added Variable: " + variableName + " : " + extractor.getResult().get(0));
    }

}
