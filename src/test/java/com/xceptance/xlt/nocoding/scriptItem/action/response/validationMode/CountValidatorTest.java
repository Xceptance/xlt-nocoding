package com.xceptance.xlt.nocoding.scriptItem.action.response.validationMode;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class CountValidatorTest extends ValidationModeTest
{

    @Test
    public void testCountValidator() throws Exception
    {
        final List<String> result = new ArrayList<>();
        result.add("test");
        AbstractValidationMode mode = new CountValidator("1");
        mode.setExpressionToValidate(result);
        mode.execute(context);

        result.add("test_2");
        mode = new CountValidator("2");
        mode.setExpressionToValidate(result);
        mode.execute(context);
    }

    @Test
    public void testCountValidatorWithVariables() throws Exception
    {
        final String variableName = "count";
        final String value = "1";
        context.getVariables().store(variableName, value);
        final List<String> result = new ArrayList<>();
        result.add("test");
        final AbstractValidationMode mode = new CountValidator("${" + variableName + "}");
        mode.setExpressionToValidate(result);
        mode.execute(context);
    }

    @Test(expected = AssertionError.class)
    public void testCountValidatorWrongCount() throws Exception
    {
        final List<String> result = new ArrayList<>();
        result.add("test");
        final AbstractValidationMode mode = new CountValidator("0");
        mode.setExpressionToValidate(result);
        mode.execute(context);
    }

    @Test(expected = NumberFormatException.class)
    public void testCountValidatorNotConvertibleToInt() throws Exception
    {
        final List<String> result = new ArrayList<>();
        result.add("test");
        final AbstractValidationMode mode = new CountValidator("wrong");
        mode.setExpressionToValidate(result);
        mode.execute(context);
    }

    @Test(expected = IllegalStateException.class)
    public void testCountValidatorNull() throws Exception
    {
        final AbstractValidationMode mode = new CountValidator("1");
        mode.setExpressionToValidate(null);
        mode.execute(context);
    }

    @Test
    public void testCountValidatorEmptyList() throws Exception
    {
        final AbstractValidationMode mode = new CountValidator("0");
        mode.setExpressionToValidate(new ArrayList<String>());
        mode.execute(context);
    }

}
