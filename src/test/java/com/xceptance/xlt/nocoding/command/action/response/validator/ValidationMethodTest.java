package com.xceptance.xlt.nocoding.command.action.response.validator;

import com.xceptance.xlt.nocoding.command.AbstractContextTest;
import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Prepares {@link AbstractValidator} tests by creating a new context
 *
 * @author ckeiner
 */
public abstract class ValidationMethodTest extends AbstractContextTest
{
    public ValidationMethodTest(final Context<?> context)
    {
        super(context);
    }
}
