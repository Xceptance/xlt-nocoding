package com.xceptance.xlt.nocoding.scriptItem.action.response.validationMode;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TextValidatorTest extends ValidationModeTest
{

    @Test
    public void testTextValidator() throws Exception
    {
        final List<String> result = new ArrayList<>();
        result.add("test");
        final AbstractValidationMode mode = new TextValidator("test");
        mode.setExpressionToValidate(result);
        mode.execute(context);
    }

    @Test
    public void testTextValidatorWithVariables() throws Exception
    {
        final String variableName = "text";
        final String value = "test";
        context.getVariables().store(variableName, value);
        final List<String> result = new ArrayList<>();
        result.add(value);
        final AbstractValidationMode mode = new TextValidator("${" + variableName + "}");
        mode.setExpressionToValidate(result);
        mode.execute(context);
    }

    @Test(expected = AssertionError.class)
    public void testTextValidatorWrongText() throws Exception
    {
        final List<String> result = new ArrayList<>();
        result.add("test");
        final AbstractValidationMode mode = new TextValidator("wrong");
        mode.setExpressionToValidate(result);
        mode.execute(context);
    }

    @Test(expected = AssertionError.class)
    public void testTextValidatorNull() throws Exception
    {
        final AbstractValidationMode mode = new TextValidator("test");
        mode.setExpressionToValidate(null);
        mode.execute(context);
    }

    @Test(expected = AssertionError.class)
    public void testTextValidatorEmptyList() throws Exception
    {
        final AbstractValidationMode mode = new TextValidator("test");
        mode.setExpressionToValidate(new ArrayList<String>());
        mode.execute(context);
    }

}
