package com.xceptance.xlt.nocoding.scriptItem.action.response.validationMode;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ExistsValidatorTest extends ValidationModeTest
{

    @Test
    public void testExistsValidator() throws Exception
    {
        final List<String> result = new ArrayList<>();
        result.add("test");
        final AbstractValidationMode mode = new ExistsValidator();
        mode.setExpressionToValidate(result);
        mode.execute(context);
    }

    @Test(expected = AssertionError.class)
    public void testExistsValidatorNull() throws Exception
    {
        final AbstractValidationMode mode = new ExistsValidator();
        mode.setExpressionToValidate(null);
        mode.execute(context);
    }

    @Test(expected = AssertionError.class)
    public void testExistsValidatorEmptyList() throws Exception
    {
        final AbstractValidationMode mode = new ExistsValidator();
        mode.setExpressionToValidate(new ArrayList<String>());
        mode.execute(context);
    }

}
