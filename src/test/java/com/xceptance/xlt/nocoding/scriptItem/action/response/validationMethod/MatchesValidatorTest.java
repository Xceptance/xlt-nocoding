package com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod.AbstractValidationMethod;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod.MatchesValidator;

public class MatchesValidatorTest extends ValidationMethodTest
{

    @Test
    public void testMatchesValidator() throws Exception
    {
        final List<String> result = new ArrayList<>();
        result.add("test");
        final AbstractValidationMethod method = new MatchesValidator("test");
        method.setExpressionToValidate(result);
        method.execute(context);
    }

    @Test
    public void testMatchesValidatorWithVariables() throws Exception
    {
        final String variableName = "matches";
        final String value = "test";
        context.getVariables().store(variableName, value);
        final List<String> result = new ArrayList<>();
        result.add(value);
        final AbstractValidationMethod method = new MatchesValidator("${" + variableName + "}");
        method.setExpressionToValidate(result);
        method.execute(context);
    }

    @Test(expected = AssertionError.class)
    public void testMatchesValidatorWrongText() throws Exception
    {
        final List<String> result = new ArrayList<>();
        result.add("test");
        final AbstractValidationMethod method = new MatchesValidator("wrong");
        method.setExpressionToValidate(result);
        method.execute(context);
    }

    @Test(expected = AssertionError.class)
    public void testMatchesValidatorNull() throws Exception
    {
        final AbstractValidationMethod method = new MatchesValidator("test");
        method.setExpressionToValidate(null);
        method.execute(context);
    }

    @Test(expected = AssertionError.class)
    public void testMatchesValidatorEmptyList() throws Exception
    {
        final AbstractValidationMethod method = new MatchesValidator("test");
        method.setExpressionToValidate(new ArrayList<String>());
        method.execute(context);
    }

}
