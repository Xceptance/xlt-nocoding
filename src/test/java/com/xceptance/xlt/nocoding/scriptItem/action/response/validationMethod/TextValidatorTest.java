package com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod.AbstractValidationMethod;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod.TextValidator;

public class TextValidatorTest extends ValidationMethodTest
{

    @Test
    public void testTextValidator() throws Exception
    {
        final List<String> result = new ArrayList<>();
        result.add("test");
        final AbstractValidationMethod method = new TextValidator("test");
        method.setExpressionToValidate(result);
        method.execute(context);
    }

    @Test
    public void testTextValidatorWithVariables() throws Exception
    {
        final String variableName = "text";
        final String value = "test";
        context.getVariables().store(variableName, value);
        final List<String> result = new ArrayList<>();
        result.add(value);
        final AbstractValidationMethod method = new TextValidator("${" + variableName + "}");
        method.setExpressionToValidate(result);
        method.execute(context);
    }

    @Test(expected = AssertionError.class)
    public void testTextValidatorWrongText() throws Exception
    {
        final List<String> result = new ArrayList<>();
        result.add("test");
        final AbstractValidationMethod method = new TextValidator("wrong");
        method.setExpressionToValidate(result);
        method.execute(context);
    }

    @Test(expected = AssertionError.class)
    public void testTextValidatorNull() throws Exception
    {
        final AbstractValidationMethod method = new TextValidator("test");
        method.setExpressionToValidate(null);
        method.execute(context);
    }

    @Test(expected = AssertionError.class)
    public void testTextValidatorEmptyList() throws Exception
    {
        final AbstractValidationMethod method = new TextValidator("test");
        method.setExpressionToValidate(new ArrayList<String>());
        method.execute(context);
    }

}
