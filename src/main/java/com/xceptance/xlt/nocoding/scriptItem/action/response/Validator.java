package com.xceptance.xlt.nocoding.scriptItem.action.response;

import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.AbstractExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.extractor.RegexpExtractor;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod.AbstractValidationMethod;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod.ExistsValidator;
import com.xceptance.xlt.nocoding.util.Context;

/**
 * Uses an {@link AbstractExtractor} and {@link AbstractValidationMethod} to validate the result of the extraction.
 * 
 * @author ckeiner
 */
public class Validator extends AbstractResponseItem
{

    /**
     * The name of the validation
     */
    private final String validationName;

    /**
     * The extractor to use
     */
    private final AbstractExtractor extractor;

    /**
     * The validation method
     */
    private AbstractValidationMethod method;

    /**
     * Creates an instance of {@link Validator} that sets the {@link #validationName}, {@link #extractor} and
     * {@link #method}.
     * 
     * @param validationName
     *            The name of the validation
     * @param extractor
     *            The extractor to use
     * @param method
     *            The validation method
     */
    public Validator(final String validationName, final AbstractExtractor extractor, final AbstractValidationMethod method)
    {
        this.validationName = validationName;
        this.extractor = extractor;
        this.method = method;
    }

    /**
     * Executes the validator by setting {@link #group} if it is specified and {@link #extractor} is a
     * {@link RegexpExtractor}. Then executes the {@link #extractor}. Then, if {@link #method} is null, it sets it to
     * {@link ExistsValidator}. Finally, it sets {@link AbstractValidationMethod#setExpressionToValidate(java.util.List)}
     * with {@link AbstractExtractor#getResult()} and executes the {@link #method}.
     */
    @Override
    public void execute(final Context context) throws Exception
    {
        // Execute the extractor
        extractor.execute(context);

        // If we don't have a validation method, then we simply want to confirm the existence of a solution
        if (method == null)
        {
            method = new ExistsValidator();
        }
        // Set the result of the execution as expressionToValidate in the ValidationMethod
        method.setExpressionToValidate(extractor.getResult());
        // Try to validate and catch any Exception and AssertionErrors so the validationName can be added to the
        // Exception/AssertionError
        try
        {
            // Validate the solution of the selector
            method.execute(context);
        }
        catch (final Exception e)
        {
            final String message = "\"" + validationName + "\" could not validate: " + e.getMessage();
            XltLogger.runTimeLogger.error(message);
            throw new Exception(message, e);
        }
        catch (final AssertionError e)
        {
            final String message = "\"" + validationName + "\" could not validate: " + e.getMessage();
            XltLogger.runTimeLogger.error(message);
            throw new AssertionError(message, e);
        }
    }

    public String getValidationName()
    {
        return validationName;
    }

    public AbstractExtractor getExtractor()
    {
        return extractor;
    }

    public AbstractValidationMethod getMethod()
    {
        return method;
    }

}
