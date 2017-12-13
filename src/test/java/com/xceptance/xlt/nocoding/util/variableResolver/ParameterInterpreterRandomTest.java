package com.xceptance.xlt.nocoding.util.variableResolver;

import org.junit.Test;

/**
 * Tests {@link ParameterInterpreterRandom}
 * 
 * @author ckeiner
 */
public class ParameterInterpreterRandomTest
{

    /**
     * Tests the constructor
     */
    @Test
    public void testConstructor()
    {
        @SuppressWarnings("unused")
        final ParameterInterpreterRandom pir = new ParameterInterpreterRandom();
    }

    /**
     * Calls {@link ParameterInterpreterRandom#Email()}
     */
    @Test
    public void testEmail()
    {
        final ParameterInterpreterRandom pir = new ParameterInterpreterRandom();
        final String email = pir.Email();
        System.err.println(email);
    }
}
