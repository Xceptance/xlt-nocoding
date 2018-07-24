package com.xceptance.xlt.nocoding.scriptItem.action.response.validator;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.xceptance.xlt.nocoding.scriptItem.action.response.validator.AbstractValidator;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validator.ExistsValidator;

/**
 * Tests {@link ExistsValidator}
 * 
 * @author ckeiner
 */
public class ExistsValidatorTest extends ValidationMethodTest
{

    /**
     * Verifies {@link ExistsValidator} validates that the result list has an item in it
     * 
     * @throws Exception
     */
    @Test
    public void testExistsValidator() throws Exception
    {
        final List<String> result = new ArrayList<>();
        result.add("test");
        final AbstractValidator method = new ExistsValidator();
        method.setExpressionToValidate(result);
        method.execute(context);
    }

    /**
     * Verifies {@link ExistsValidator} throws an {@link AssertionError} if the result list is null
     * 
     * @throws Exception
     */
    @Test(expected = AssertionError.class)
    public void testExistsValidatorNull() throws Exception
    {
        final AbstractValidator method = new ExistsValidator();
        method.setExpressionToValidate(null);
        method.execute(context);
    }

    /**
     * Verifies {@link ExistsValidator} throws an {@link AssertionError} if the result list is empty
     * 
     * @throws Exception
     */
    @Test(expected = AssertionError.class)
    public void testExistsValidatorEmptyList() throws Exception
    {
        final AbstractValidator method = new ExistsValidator();
        method.setExpressionToValidate(new ArrayList<String>());
        method.execute(context);
    }

}
