package com.xceptance.xlt.nocoding.command.action.response.validator;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.xceptance.xlt.nocoding.util.context.Context;

/**
 * Tests {@link MatchesValidator}
 *
 * @author ckeiner
 */
public class MatchesValidatorTest extends ValidationMethodTest
{

    public MatchesValidatorTest(final Context<?> context)
    {
        super(context);
    }

    /**
     * Verifies {@link MatchesValidator} matches the content of the first result with the expected content
     *
     * @throws Exception
     */
    @Test
    public void testMatchesValidator() throws Exception
    {
        final List<String> result = new ArrayList<>();
        result.add("test");
        final AbstractValidator method = new MatchesValidator("test");
        method.setExpressionToValidate(result);
        method.execute(context);
    }

    /**
     * Verifies {@link MatchesValidator} matches the content of the first result with the expected content, which is
     * hidden behind a variable.
     *
     * @throws Exception
     */
    @Test
    public void testMatchesValidatorWithVariables() throws Exception
    {
        final String variableName = "matches";
        final String value = "test";
        context.getVariables().store(variableName, value);
        final List<String> result = new ArrayList<>();
        result.add(value);
        final AbstractValidator method = new MatchesValidator("${" + variableName + "}");
        method.setExpressionToValidate(result);
        method.execute(context);
    }

    /**
     * Verifies {@link MatchesValidator} throws an {@link AssertionError} if the content and the expected content do not
     * match
     *
     * @throws Exception
     */
    @Test(expected = AssertionError.class)
    public void testMatchesValidatorWrongText() throws Exception
    {
        final List<String> result = new ArrayList<>();
        result.add("test");
        final AbstractValidator method = new MatchesValidator("wrong");
        method.setExpressionToValidate(result);
        method.execute(context);
    }

    /**
     * Verifies {@link MatchesValidator} throws an {@link AssertionError} when the result list is null
     *
     * @throws Exception
     */
    @Test(expected = AssertionError.class)
    public void testMatchesValidatorNull() throws Exception
    {
        final AbstractValidator method = new MatchesValidator("test");
        method.setExpressionToValidate(null);
        method.execute(context);
    }

    /**
     * Verifies {@link MatchesValidator} throws an {@link AssertionError} when the result list is empty
     *
     * @throws Exception
     */
    @Test(expected = AssertionError.class)
    public void testMatchesValidatorEmptyList() throws Exception
    {
        final AbstractValidator method = new MatchesValidator("test");
        method.setExpressionToValidate(new ArrayList<String>());
        method.execute(context);
    }

}
