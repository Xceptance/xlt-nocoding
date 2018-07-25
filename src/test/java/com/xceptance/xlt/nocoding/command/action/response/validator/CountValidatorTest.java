package com.xceptance.xlt.nocoding.command.action.response.validator;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.xceptance.xlt.nocoding.command.action.response.validator.AbstractValidator;
import com.xceptance.xlt.nocoding.command.action.response.validator.CountValidator;

/**
 * Tests {@link CountValidator}
 * 
 * @author ckeiner
 */
public class CountValidatorTest extends ValidationMethodTest
{

    /**
     * Verifies the {@link CountValidator} validates the correct amount of results
     * 
     * @throws Exception
     */
    @Test
    public void testCountValidator() throws Exception
    {
        final List<String> result = new ArrayList<>();
        result.add("test");
        AbstractValidator method = new CountValidator("1");
        method.setExpressionToValidate(result);
        method.execute(context);

        result.add("test_2");
        method = new CountValidator("2");
        method.setExpressionToValidate(result);
        method.execute(context);
    }

    /**
     * Verifies the {@link CountValidator} validates the correct amount of results. The expected amount of results is hidden
     * behind a variable
     * 
     * @throws Exception
     */
    @Test
    public void testCountValidatorWithVariables() throws Exception
    {
        final String variableName = "count";
        final String value = "1";
        context.getVariables().store(variableName, value);
        final List<String> result = new ArrayList<>();
        result.add("test");
        final AbstractValidator method = new CountValidator("${" + variableName + "}");
        method.setExpressionToValidate(result);
        method.execute(context);
    }

    /**
     * Verifies {@link CountValidator} throws an {@link AssertionError} if the wrong amount of items is specified
     * 
     * @throws Exception
     */
    @Test(expected = AssertionError.class)
    public void testCountValidatorWrongCount() throws Exception
    {
        final List<String> result = new ArrayList<>();
        result.add("test");
        final AbstractValidator method = new CountValidator("0");
        method.setExpressionToValidate(result);
        method.execute(context);
    }

    /**
     * Verifies {@link CountValidator} throws an error if it cannot convert to a number
     * 
     * @throws Exception
     */
    @Test(expected = NumberFormatException.class)
    public void testCountValidatorNotConvertibleToInt() throws Exception
    {
        final List<String> result = new ArrayList<>();
        result.add("test");
        final AbstractValidator method = new CountValidator("wrong");
        method.setExpressionToValidate(result);
        method.execute(context);
    }

    /**
     * Verifies {@link CountValidator} throws an error if there is no expression to validate
     * 
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void testCountValidatorNull() throws Exception
    {
        final AbstractValidator method = new CountValidator("1");
        method.setExpressionToValidate(null);
        method.execute(context);
    }

    /**
     * Verifies {@link CountValidator} can validate that there is no item in the result list
     * 
     * @throws Exception
     */
    @Test
    public void testCountValidatorEmptyList() throws Exception
    {
        final AbstractValidator method = new CountValidator("0");
        method.setExpressionToValidate(new ArrayList<String>());
        method.execute(context);
    }

}
