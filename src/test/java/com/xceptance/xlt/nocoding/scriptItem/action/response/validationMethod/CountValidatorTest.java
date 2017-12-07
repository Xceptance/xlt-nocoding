package com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod.AbstractValidationMethod;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod.CountValidator;

public class CountValidatorTest extends ValidationMethodTest
{

    @Test
    public void testCountValidator() throws Exception
    {
        final List<String> result = new ArrayList<>();
        result.add("test");
        AbstractValidationMethod method = new CountValidator("1");
        method.setExpressionToValidate(result);
        method.execute(context);

        result.add("test_2");
        method = new CountValidator("2");
        method.setExpressionToValidate(result);
        method.execute(context);
    }

    @Test
    public void testCountValidatorWithVariables() throws Exception
    {
        final String variableName = "count";
        final String value = "1";
        context.getVariables().store(variableName, value);
        final List<String> result = new ArrayList<>();
        result.add("test");
        final AbstractValidationMethod method = new CountValidator("${" + variableName + "}");
        method.setExpressionToValidate(result);
        method.execute(context);
    }

    @Test(expected = AssertionError.class)
    public void testCountValidatorWrongCount() throws Exception
    {
        final List<String> result = new ArrayList<>();
        result.add("test");
        final AbstractValidationMethod method = new CountValidator("0");
        method.setExpressionToValidate(result);
        method.execute(context);
    }

    @Test(expected = NumberFormatException.class)
    public void testCountValidatorNotConvertibleToInt() throws Exception
    {
        final List<String> result = new ArrayList<>();
        result.add("test");
        final AbstractValidationMethod method = new CountValidator("wrong");
        method.setExpressionToValidate(result);
        method.execute(context);
    }

    @Test(expected = IllegalStateException.class)
    public void testCountValidatorNull() throws Exception
    {
        final AbstractValidationMethod method = new CountValidator("1");
        method.setExpressionToValidate(null);
        method.execute(context);
    }

    @Test
    public void testCountValidatorEmptyList() throws Exception
    {
        final AbstractValidationMethod method = new CountValidator("0");
        method.setExpressionToValidate(new ArrayList<String>());
        method.execute(context);
    }

}
