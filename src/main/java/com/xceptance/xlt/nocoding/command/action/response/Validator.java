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
package com.xceptance.xlt.nocoding.command.action.response;

import org.apache.commons.lang3.StringUtils;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.command.action.response.extractor.AbstractExtractor;
import com.xceptance.xlt.nocoding.command.action.response.validator.AbstractValidator;
import com.xceptance.xlt.nocoding.command.action.response.validator.ExistsValidator;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Uses an {@link AbstractExtractor} and {@link AbstractValidator} to validate the result of the extraction.
 *
 * @author ckeiner
 */
public class Validator extends AbstractResponseSubItem
{

    /**
     * The name of the validation
     */
    private String validationName;

    /**
     * The extractor to use
     */
    private AbstractExtractor extractor;

    /**
     * The validation method
     */
    private AbstractValidator method;

    /**
     * Creates an instance of {@link Validator} that sets the {@link #validationName}, {@link #extractor} and
     * {@link #method}.
     *
     * @param validationName
     *            The name of the validation
     * @param extractor
     *            The extractor to use
     * @param method
     *            The method for validating
     */
    public Validator(final String validationName, final AbstractExtractor extractor, final AbstractValidator method)
    {
        this.validationName = validationName;
        this.extractor = extractor;
        this.method = method;
    }

    /**
     * Executes the validator. First, it executes the {@link #extractor}. Then, if {@link #method} is null, it sets it
     * to {@link ExistsValidator}. Finally, it sets {@link AbstractValidator#setExpressionToValidate(java.util.List)}
     * with {@link AbstractExtractor#getResult()} and executes the {@link #method}.
     */
    @Override
    public void execute(final Context<?> context)
    {
        fillDefaultData(context);
        // Execute the extractor
        getExtractor().execute(context);

        // If we don't have a validation method, then we simply want to confirm the existence of a solution
        if (getMethod() == null)
        {
            setMethod(new ExistsValidator());
        }
        // Set the result of the execution as expressionToValidate in the ValidationMethod
        getMethod().setExpressionToValidate(getExtractor().getResult());
        // Try to validate and catch any AssertionErrors so the validationName can be added to the
        // Exception/AssertionError
        try
        {
            // Validate the solution of the selector
            getMethod().execute(context);
        }
        catch (final AssertionError e)
        {
            final String message = "Validation Step \"" + getValidationName() + "\" could not validate: " + e.getMessage();
            XltLogger.runTimeLogger.error(message);
            throw new AssertionError(message, e);
        }
    }

    protected void fillDefaultData(final Context<?> context)
    {
        if (StringUtils.isBlank(getValidationName()))
        {
            final String validationName = "Validate Action-" + context.getActionIndex();
            setValidationName(validationName);
        }
    }

    public String getValidationName()
    {
        return validationName;
    }

    public void setValidationName(final String validationName)
    {
        this.validationName = validationName;
    }

    public AbstractExtractor getExtractor()
    {
        return extractor;
    }

    public AbstractValidator getMethod()
    {
        return method;
    }

    public void setExtractor(final AbstractExtractor extractor)
    {
        this.extractor = extractor;
    }

    public void setMethod(final AbstractValidator method)
    {
        this.method = method;
    }

}
