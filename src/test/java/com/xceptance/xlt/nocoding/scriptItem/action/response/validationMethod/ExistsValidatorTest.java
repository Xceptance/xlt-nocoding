package com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod.AbstractValidationMethod;
import com.xceptance.xlt.nocoding.scriptItem.action.response.validationMethod.ExistsValidator;

public class ExistsValidatorTest extends ValidationMethodTest
{

    @Test
    public void testExistsValidator() throws Exception
    {
        final List<String> result = new ArrayList<>();
        result.add("test");
        final AbstractValidationMethod method = new ExistsValidator();
        method.setExpressionToValidate(result);
        method.execute(context);
    }

    @Test(expected = AssertionError.class)
    public void testExistsValidatorNull() throws Exception
    {
        final AbstractValidationMethod method = new ExistsValidator();
        method.setExpressionToValidate(null);
        method.execute(context);
    }

    @Test(expected = AssertionError.class)
    public void testExistsValidatorEmptyList() throws Exception
    {
        final AbstractValidationMethod method = new ExistsValidator();
        method.setExpressionToValidate(new ArrayList<String>());
        method.execute(context);
    }

}
