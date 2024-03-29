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
package com.xceptance.xlt.nocoding.command.action.response.extractor.xpath;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.htmlunit.WebResponse;

import com.xceptance.xlt.nocoding.util.context.Context;
import com.xceptance.xlt.nocoding.util.resolver.VariableResolver;
import com.xceptance.xlt.nocoding.util.storage.DataStorage;

/**
 * The class that executes the actual extraction of the xpath expression
 *
 * @author ckeiner
 */
public abstract class XpathExtractorExecutor implements Serializable
{

    /**
     * The expression to use for the extraction
     */
    protected String extractionExpression;

    /**
     * The result of the extraction
     */
    protected final List<String> result;

    /**
     * Sets {@link #extractionExpression} and creates an {@link ArrayList} for {@link #result}.
     *
     * @param extractionExpression
     *            The {@link #extractionExpression} to use for extracting
     */
    public XpathExtractorExecutor(final String extractionExpression)
    {
        this.extractionExpression = extractionExpression;
        result = new ArrayList<>(1);
    }

    /**
     * Executes the extractor. Looks into the {@link WebResponse} located in {@link Context}. Then sets the extracted
     * expression via {@link #addResult(String)}.
     *
     * @param context
     *            The {@link Context} to use
     */
    public abstract void execute(Context<?> context);

    /**
     * Adds a string to the result list.
     *
     * @param result
     *            The String to be added to the result.
     */
    public void addResult(final String result)
    {
        getResult().add(result);
    }

    /**
     * Resolves {@link #extractionExpression}.
     *
     * @param context
     *            The {@link Context} with the {@link VariableResolver} and {@link DataStorage}.
     */
    protected void resolveValues(final Context<?> context)
    {
        extractionExpression = context.resolveString(extractionExpression);
    }

    public List<String> getResult()
    {
        return result;
    }

    public String getExtractionExpression()
    {
        return extractionExpression;
    }

    public void setExtractionExpression(final String extractionExpression)
    {
        this.extractionExpression = extractionExpression;
    }

}
