package com.xceptance.xlt.nocoding.scriptItem.action.response.validationMode;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class MatchesValidatorTest extends ValidationModeTest
{

    @Test
    public void testMatchesValidator() throws Exception
    {
        final List<String> result = new ArrayList<>();
        result.add("test");
        final AbstractValidationMode mode = new MatchesValidator("test");
        mode.setExpressionToValidate(result);
        mode.execute(context);
    }

    @Test
    public void testMatchesValidatorWithVariables() throws Exception
    {
        final String variableName = "matches";
        final String value = "test";
        context.storeVariable(variableName, value);
        final List<String> result = new ArrayList<>();
        result.add(value);
        final AbstractValidationMode mode = new MatchesValidator("${" + variableName + "}");
        mode.setExpressionToValidate(result);
        mode.execute(context);
    }

    @Test(expected = AssertionError.class)
    public void testMatchesValidatorWrongText() throws Exception
    {
        final List<String> result = new ArrayList<>();
        result.add("test");
        final AbstractValidationMode mode = new MatchesValidator("wrong");
        mode.setExpressionToValidate(result);
        mode.execute(context);
    }

    @Test(expected = AssertionError.class)
    public void testMatchesValidatorNull() throws Exception
    {
        final AbstractValidationMode mode = new MatchesValidator("test");
        mode.setExpressionToValidate(null);
        mode.execute(context);
    }

    @Test(expected = AssertionError.class)
    public void testMatchesValidatorEmptyList() throws Exception
    {
        final AbstractValidationMode mode = new MatchesValidator("test");
        mode.setExpressionToValidate(new ArrayList<String>());
        mode.execute(context);
    }

}
